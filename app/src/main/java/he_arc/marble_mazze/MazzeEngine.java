package he_arc.marble_mazze;

import android.app.Application;
import android.app.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Arnaud on 25.10.2016.
 */

public class MazzeEngine {

    private Ball ball = null;
    private MazzeActivity mActivity = null;
    private SensorManager mManager = null;
    private Sensor mAccelerometre = null;
    private List<Block> blocks = null;
    private Vibrator vibrator;
    private Context ctx;
    private long time;
    private int score;

    //On enregistre la réussite du niveau et le nombre de vie restante
    private String content ="";
    private String nomNiveau;



    public MazzeEngine(MazzeActivity mActivity,String levelName) {
        this.mActivity = mActivity;

        //Recupere le service pour avoir l accelerometre
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        time = System.currentTimeMillis();
        score = 0;
        nomNiveau = levelName;

        //Recupere le service pour la vibration
        vibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
        ctx = mActivity.getApplicationContext();
    }


    SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent pEvent) {
            float x = pEvent.values[0];
            float y = pEvent.values[1];
            float z = pEvent.values[2];

           if (ball != null) {
               //update la position de la ball
                RectF hitBox = ball.putXAndY(x,y);

               //Pour chaque block on test l intersection avec la ball
                for (Block block : blocks) {
                    RectF inter = new RectF(block.getRectangle());
                    if (inter.intersect(hitBox)) {
                        //On fonction du type on realise des traitement different
                        switch (block.getType()) {
                            case WALL:
                                //Vibration de 50 ms
                                vibrator.vibrate(50);
                                if((inter.right-inter.left)<(inter.bottom-inter.top)){
                                    ball.rebondY();
                                    //On replace la balle au bord du mur
                                    if(ball.getRectangle().left==inter.left)
                                    {
                                        ball.replaceLeft(inter.right);
                                    }
                                    else
                                    {
                                        ball.replaceRight(inter.left);
                                    }
                                } else if((inter.right-inter.left)>(inter.bottom-inter.top)){
                                    ball.rebondX();
                                    //On replace la balle au bord du mur (A modifier)
                                    if(inter.bottom == ball.getRectangle().bottom)
                                    {
                                        ball.replaceBottom(inter.top);
                                    }
                                    else
                                    {
                                        ball.replaceTop(inter.bottom);
                                    }
                                }
                                else {
                                    ball.rebondX();
                                    ball.rebondY();
                                    //On replace la balle au bord du mur (A modifier)
                                    if(inter.left == ball.getRectangle().left && inter.top == ball.getRectangle().top)
                                    {

                                        ball.replaceLeft(inter.right);

                                        ball.replaceTop(inter.bottom);
                                    }
                                    else if(inter.left == ball.getRectangle().left && inter.bottom == ball.getRectangle().bottom)
                                    {
                                        ball.replaceLeft(inter.right);
                                        ball.replaceBottom(inter.top);
                                    }
                                    else if(inter.right == ball.getRectangle().right && inter.top == ball.getRectangle().top)
                                    {
                                        ball.replaceRight(inter.left);
                                        ball.replaceTop(inter.bottom);
                                    }
                                    else if(inter.right == ball.getRectangle().right && inter.bottom == ball.getRectangle().bottom)
                                    {
                                        ball.replaceRight(inter.left);
                                        ball.replaceBottom(inter.top);
                                    }

                                }
                                break;
                            case HOLE:
                                if((inter.right-inter.left)<(inter.bottom-inter.top)){
                                   //Tape côté
                                    if(inter.right-inter.left >= 2*(ball.getRayon()/3))
                                    {
                                        //Si la balle a plus de la moitié dans la zone, on perd une vie et reset la pos
                                        ball.vie--;
                                        ball.reset();
                                    }
                                } else if((inter.right-inter.left)>(inter.bottom-inter.top)){
                                  //Tape dessus/dessous
                                    if(inter.bottom-inter.top >= 2*(ball.getRayon()/3))
                                    {
                                        //Si la balle a plus de la moitié dans la zone, on perd une vie et reset la pos
                                        ball.vie--;
                                        ball.reset();
                                    }
                                }
                                else {
                                    //Arrive depuis le coin
                                    if(Math.sqrt(Math.pow(inter.right-inter.left,2)+Math.pow(inter.bottom-inter.top,2)) >= 2*(Math.sqrt(2* Math.pow((ball.getRayon()),2))/3)){
                                        //Si la balle a plus de la moitié dans la zone, on perd une vie et reset la pos
                                        ball.vie--;
                                        ball.reset();
                                    }
                                }
                                break;
                            case START:
                                break;
                            case END:
                                Log.i("MazzeEngine","Gagne");

                                //Calcule du temps et du score
                                time = System.currentTimeMillis() - time;
                                score = ball.vie * (int)(-time + 100000);

                                //Récupération du fichier de sauvegarde
                                try {
                                    FileInputStream fin = ctx.openFileInput("MM_save");
                                    int c;
                                    while( (c = fin.read()) != -1){
                                        content += Character.toString((char)c);
                                    }
                                    //string temp contains all the data of the file.
                                    fin.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //Séparation de la string: 1 token par niveau
                                StringTokenizer tokensNiveaux = new StringTokenizer(content, "|");
                                content="";

                                boolean modif = false;
                                while(tokensNiveaux.hasMoreTokens())
                                {
                                    String thisTokenNiveau = tokensNiveaux.nextToken();
                                    //Pour chaque niveau, on va séparer le nom du score
                                    StringTokenizer tokensDonneesNiveauActuel = new StringTokenizer(thisTokenNiveau, "/");
                                    String thisNomNiveau = tokensDonneesNiveauActuel.nextToken();
                                    String thisScoreNiveau = tokensDonneesNiveauActuel.nextToken();
                                    //Si on analyse le score de ce niveau là
                                    if(thisNomNiveau.equals(nomNiveau)) {
                                        //On signale qu'on a trouvé le niveau
                                        modif = true;
                                        //Si le score précédent est moins bon
                                        if (Integer.parseInt(thisScoreNiveau) < score) {
                                            //On remplace le résultat par le nouveau score
                                            thisScoreNiveau = score + "";

                                        }
                                    }
                                    //On replace les tokens dans le fichier
                                    content += thisNomNiveau+"/"+thisScoreNiveau+"|";
                                }

                                if(modif == false)
                                {
                                    //Si aucune valeur trouvée, on crée une nouvelle entrée
                                    content += (nomNiveau+"/"+score+"|");
                                }

                                //Ajout de la string au reste du fichier

                                //Ecriture de la sauvegarde
                                try {
                                    FileOutputStream fos = ctx.openFileOutput("MM_save", Context.MODE_PRIVATE);
                                    fos.write(content.getBytes());
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                content ="";

                                //Pour debug
                                /*scoreNiveau = (Integer.parseInt(scoreNiveau)+1)+"";
                                if(scoreNiveau.equals("3")){
                                    setNomNiveau("niveau2");
                                }*/

                                
                                mActivity.EndGame(true,score);
                                ball.reset();
                                break;
                        }
                        break;
                    }
                }
               if(ball.vie == 0){
                   Log.i("MazzeEngine","Perdu");
                   score = 0;
                   mActivity.EndGame(false,score);
               }
           }
        }

        @Override
        public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {

        }
    };

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    //Suppression du SensorListener
    public void stop() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    //Ajout du SensorListener au Manager
    public void resume() {
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    /*
    Fonction qui parse une image qui utilise la couleur du pixel pour definir si il s agit d une trou/mur/depart/arriver
    Elle retourne une liste de Block qui contienne la position de chaque Block sur l ecrans
    Parametre :
           Bitmap imageToParse :  image a parse
           int offset : offset defini a partir du ratio calculer dans MazzeActtivity, il permet de definir la position des block dans la sur l ecrans en respectant le ratio
     */
    public List<Block> buildMazze(Bitmap imageToParse, int offset) {
        blocks = new ArrayList<Block>();

        for (int y = 0; y < imageToParse.getHeight(); y++) {
            for (int x = 0; x < imageToParse.getWidth(); x++) {
                int rgb = imageToParse.getPixel(x, y);
                if (rgb == Color.BLACK) {
                    blocks.add(new Block(Block.Type.WALL, x+offset,y));
                }
                else if(rgb == Color.RED){
                    blocks.add(new Block(Block.Type.HOLE, x+offset,y));
                }
                else if(rgb == Color.BLUE){
                    blocks.add(new Block(Block.Type.END, x+offset,y));
                }
                else if(rgb == Color.GREEN){
                    Block b = new Block(Block.Type.START, x+offset,y);
                    ball.setInitialRectangle(new RectF(b.getRectangle()));
                    blocks.add(b);
                }
            }
        }

        return blocks;
    }

}