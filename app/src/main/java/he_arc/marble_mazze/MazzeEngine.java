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


    public MazzeEngine(MazzeActivity mActivity) {
        this.mActivity = mActivity;
        mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


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
                                //mActivity.showDialog(MazzeActivity.DEFEAT_DIALOG);
                                //Log.i("MazzeEngine","Toucher Wall");
                                //ball.rebond(block);
                                Log.i("Balle","posX: "+ ball.getX() + " posY: " + ball.getY());
                                Log.i("Bloc", "centerX(): "+inter.centerY() + " centerY(): "+inter.centerY());
                                if(ball.getRayon() > inter.centerX()-ball.getX()){
                                    Log.i("Balle", "ChangementSpeedY");
                                    ball.changeYSpeed();
                                } else if(ball.getRayon() > inter.centerY()-ball.getY()) {
                                    Log.i("Balle", "ChangementSpeedX");
                                    ball.changeXSpeed();
                                }

                                break;
                            case HOLE:
                                ball.reset();
                                //ball.rebond(block);
                                //Log.i("MazzeEngine","Perdu");
                                break;
                            case START:
                                break;
                            case END:
                                //mActivity.showDialog(MazzeActivity.VICTORY_DIALOG);
                                Log.i("MazzeEngine","Gagne");
                                break;
                        }
                        break;
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