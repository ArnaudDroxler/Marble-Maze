package he_arc.marble_mazze;

import android.app.Service;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;


import java.util.ArrayList;
import java.util.List;

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
    private int vie;


    public MazzeEngine(MazzeActivity mActivity) {
        this.mActivity = mActivity;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        vibrator = (Vibrator) mActivity.getSystemService(mActivity.VIBRATOR_SERVICE);
        vie = 3;
    }


    SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent pEvent) {
            float x = pEvent.values[0];
            float y = pEvent.values[1];
            float z = pEvent.values[2];

           if (ball != null) {
                RectF hitBox = ball.putXAndY(x,y);

                for (Block block : blocks) {

                    RectF inter = new RectF(block.getRectangle());
                    if (inter.intersect(hitBox)) {
                        switch (block.getType()) {
                            case WALL:
                                vibrator.vibrate(50);
                                if((inter.right-inter.left)<(inter.bottom-inter.top)){
                                    ball.rebondY();
                                    //On replace la balle au bord du mur (A modifier car transforme le rect au lieu de déplacer)
                                    if(ball.getRectangle().left==inter.left)
                                    {
                                        //ball.getRectangle().left = inter.right;
                                        ball.replaceLeft(inter.right);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                    else
                                    {
                                        //ball.getRectangle().right = inter.left;
                                        ball.replaceRight(inter.left);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                } else if((inter.right-inter.left)>(inter.bottom-inter.top)){
                                    ball.rebondX();
                                    //On replace la balle au bord du mur (A modifier)
                                    if(inter.bottom == ball.getRectangle().bottom)
                                    {
                                        //ball.getRectangle().bottom =inter.top;
                                        ball.replaceBottom(inter.top);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                    else
                                    {
                                        //ball.getRectangle().top = inter.bottom;
                                        ball.replaceTop(inter.bottom);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                }
                                else {
                                    ball.rebondX();
                                    ball.rebondY();
                                    //On replace la balle au bord du mur (A modifier)
                                    if(inter.left == ball.getRectangle().left && inter.top == ball.getRectangle().top)
                                    {
                                        //ball.getRectangle().left = inter.right;
                                        ball.replaceLeft(inter.right);
                                        //ball.getRectangle().top = inter.bottom;
                                        ball.replaceTop(inter.bottom);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                    else if(inter.left == ball.getRectangle().left && inter.bottom == ball.getRectangle().bottom)
                                    {
                                        //ball.getRectangle().left = inter.right;
                                        ball.replaceLeft(inter.right);
                                        //ball.getRectangle().bottom = inter.top;
                                        ball.replaceBottom(inter.top);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                    else if(inter.right == ball.getRectangle().right && inter.top == ball.getRectangle().top)
                                    {
                                        //ball.getRectangle().right = inter.left;
                                        ball.replaceRight(inter.left);
                                        //ball.getRectangle().top = inter.bottom;
                                        ball.replaceTop(inter.bottom);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }
                                    else if(inter.right == ball.getRectangle().right && inter.bottom == ball.getRectangle().bottom)
                                    {
                                        //ball.getRectangle().right = inter.left;
                                        ball.replaceRight(inter.left);
                                        //ball.getRectangle().bottom = inter.top;
                                        ball.replaceBottom(inter.top);
                                        Log.i("Balle", "Left: " + ball.getRectangle().left + " Right: "+ ball.getRectangle().right + " Bottom: "+ ball.getRectangle().bottom + " Top: " + ball.getRectangle().top);
                                    }

                                }
                                break;
                            case HOLE:
                                vie--;
                                ball.reset();
                                break;
                            case START:
                                break;
                            case END:
                                Log.i("MazzeEngine","Gagne");
                                break;
                        }
                        if(vie == 0){
                            Log.i("MazzeEngine","Perdu");
                        }
                    }
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

    public void stop() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);
    }

    public void resume() {
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

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