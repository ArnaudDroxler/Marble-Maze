package he_arc.marble_mazze;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MazzeActivity extends AppCompatActivity {

    private MazzeView mView = null;
    private MazzeEngine mEngine = null;

    private int screenWidth;
    private int screenHeight;
    private int mapWidth;
    private int mapHeight;
    private int offset;
    private int ratio;

    //désactiver mise en veille
    private PowerManager.WakeLock w1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        try {
            //essaye d'ouvrir le fichier de sauvegarde du programme
            FileInputStream fin = getApplicationContext().openFileInput("MM_save");
            fin.close();
        } catch (FileNotFoundException e) {
            try {
                //S'il n'existe pas, on le créé
                FileOutputStream fos = getApplicationContext().openFileOutput("MM_save", Context.MODE_PRIVATE);
                fos.write("".getBytes());
                fos.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        //Désactiver le mode veille
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        w1 = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, this.getClass().getName());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        Bitmap imageToParse = BitmapFactory.decodeResource(getResources(),R.drawable.map,opts);

        mapWidth =  imageToParse.getWidth();
        mapHeight = imageToParse.getHeight();

        ratio = (screenHeight/mapHeight);
        offset = ( Math.abs(screenWidth - mapWidth*ratio)/2)/ratio;

        mView = new MazzeView(this);
        setContentView(mView);

        mEngine = new MazzeEngine(this);

        Ball ball = new Ball(ratio/2);

        mView.setBall(ball);
        mEngine.setBall(ball);

        List<Block> mList = mEngine.buildMazze(imageToParse,offset);
        mView.setBlocks(mList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEngine.resume();
        if (w1 != null) {
            w1.acquire();
        }
    }

    @Override
    protected void onPause() {
        if (w1 != null) {
            w1.release();
        }
        super.onStop();
        mEngine.stop();
    }


    public void EndGame(Boolean win){
        Intent myIntent = new Intent(this, EndGameActivity.class);
        startActivityForResult(myIntent, 0);
        myIntent.putExtra("win", win);
        startActivity(myIntent);
    }
}
