package he_arc.marble_mazze;

import android.app.Service;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

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

           if (ball != null) {
                RectF hitBox = ball.putXAndY(x,y);

                for (Block block : blocks) {

                    RectF inter = new RectF(block.getRectangle());
                    if (inter.intersect(hitBox)) {
                        switch (block.getType()) {
                            case WALL:
                                //mActivity.showDialog(MazzeActivity.DEFEAT_DIALOG);
                                Log.i("MazzeEngine","Perdu");
                                ball.reset();
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

    // Red�marre le capteur
    public void resume() {
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_GAME);
    }

    public List<Block> buildMazze() {
        blocks = new ArrayList<Block>();
        blocks.add(new Block(Block.Type.WALL, 0, 0));
        blocks.add(new Block(Block.Type.WALL, 0, 1));
        blocks.add(new Block(Block.Type.WALL, 0, 2));
        blocks.add(new Block(Block.Type.WALL, 0, 3));
        blocks.add(new Block(Block.Type.WALL, 0, 4));
        blocks.add(new Block(Block.Type.WALL, 0, 5));
        blocks.add(new Block(Block.Type.WALL, 0, 6));
        blocks.add(new Block(Block.Type.WALL, 0, 7));
        blocks.add(new Block(Block.Type.WALL, 0, 8));
        blocks.add(new Block(Block.Type.WALL, 0, 9));
        blocks.add(new Block(Block.Type.WALL, 0, 10));
        blocks.add(new Block(Block.Type.WALL, 0, 11));
        blocks.add(new Block(Block.Type.WALL, 0, 12));
        blocks.add(new Block(Block.Type.WALL, 0, 13));

        blocks.add(new Block(Block.Type.WALL, 1, 0));
        blocks.add(new Block(Block.Type.WALL, 1, 13));

        blocks.add(new Block(Block.Type.WALL, 2, 0));
        blocks.add(new Block(Block.Type.WALL, 2, 13));

        blocks.add(new Block(Block.Type.WALL, 3, 0));
        blocks.add(new Block(Block.Type.WALL, 3, 13));

        blocks.add(new Block(Block.Type.WALL, 4, 0));
        blocks.add(new Block(Block.Type.WALL, 4, 1));
        blocks.add(new Block(Block.Type.WALL, 4, 2));
        blocks.add(new Block(Block.Type.WALL, 4, 3));
        blocks.add(new Block(Block.Type.WALL, 4, 4));
        blocks.add(new Block(Block.Type.WALL, 4, 5));
        blocks.add(new Block(Block.Type.WALL, 4, 6));
        blocks.add(new Block(Block.Type.WALL, 4, 7));
        blocks.add(new Block(Block.Type.WALL, 4, 8));
        blocks.add(new Block(Block.Type.WALL, 4, 9));
        blocks.add(new Block(Block.Type.WALL, 4, 10));
        blocks.add(new Block(Block.Type.WALL, 4, 13));

        blocks.add(new Block(Block.Type.WALL, 5, 0));
        blocks.add(new Block(Block.Type.WALL, 5, 13));

        blocks.add(new Block(Block.Type.WALL, 6, 0));
        blocks.add(new Block(Block.Type.WALL, 6, 13));

        blocks.add(new Block(Block.Type.WALL, 7, 0));
        blocks.add(new Block(Block.Type.WALL, 7, 1));
        blocks.add(new Block(Block.Type.WALL, 7, 2));
        blocks.add(new Block(Block.Type.WALL, 7, 5));
        blocks.add(new Block(Block.Type.WALL, 7, 6));
        blocks.add(new Block(Block.Type.WALL, 7, 9));
        blocks.add(new Block(Block.Type.WALL, 7, 10));
        blocks.add(new Block(Block.Type.WALL, 7, 11));
        blocks.add(new Block(Block.Type.WALL, 7, 12));
        blocks.add(new Block(Block.Type.WALL, 7, 13));

        blocks.add(new Block(Block.Type.WALL, 8, 0));
        blocks.add(new Block(Block.Type.WALL, 8, 5));
        blocks.add(new Block(Block.Type.WALL, 8, 9));
        blocks.add(new Block(Block.Type.WALL, 8, 13));

        blocks.add(new Block(Block.Type.WALL, 9, 0));
        blocks.add(new Block(Block.Type.WALL, 9, 5));
        blocks.add(new Block(Block.Type.WALL, 9, 9));
        blocks.add(new Block(Block.Type.WALL, 9, 13));

        blocks.add(new Block(Block.Type.WALL, 10, 0));
        blocks.add(new Block(Block.Type.WALL, 10, 5));
        blocks.add(new Block(Block.Type.WALL, 10, 9));
        blocks.add(new Block(Block.Type.WALL, 10, 13));

        blocks.add(new Block(Block.Type.WALL, 11, 0));
        blocks.add(new Block(Block.Type.WALL, 11, 5));
        blocks.add(new Block(Block.Type.WALL, 11, 9));
        blocks.add(new Block(Block.Type.WALL, 11, 13));

        blocks.add(new Block(Block.Type.WALL, 12, 0));
        blocks.add(new Block(Block.Type.WALL, 12, 1));
        blocks.add(new Block(Block.Type.WALL, 12, 2));
        blocks.add(new Block(Block.Type.WALL, 12, 3));
        blocks.add(new Block(Block.Type.WALL, 12, 4));
        blocks.add(new Block(Block.Type.WALL, 12, 5));
        blocks.add(new Block(Block.Type.WALL, 12, 9));
        blocks.add(new Block(Block.Type.WALL, 12, 8));
        blocks.add(new Block(Block.Type.WALL, 12, 13));

        blocks.add(new Block(Block.Type.WALL, 13, 0));
        blocks.add(new Block(Block.Type.WALL, 13, 8));
        blocks.add(new Block(Block.Type.WALL, 13, 13));

        blocks.add(new Block(Block.Type.WALL, 14, 0));
        blocks.add(new Block(Block.Type.WALL, 14, 8));
        blocks.add(new Block(Block.Type.WALL, 14, 13));

        blocks.add(new Block(Block.Type.WALL, 15, 0));
        blocks.add(new Block(Block.Type.WALL, 15, 8));
        blocks.add(new Block(Block.Type.WALL, 15, 13));

        blocks.add(new Block(Block.Type.WALL, 16, 0));
        blocks.add(new Block(Block.Type.WALL, 16, 4));
        blocks.add(new Block(Block.Type.WALL, 16, 5));
        blocks.add(new Block(Block.Type.WALL, 16, 6));
        blocks.add(new Block(Block.Type.WALL, 16, 7));
        blocks.add(new Block(Block.Type.WALL, 16, 8));
        blocks.add(new Block(Block.Type.WALL, 16, 9));
        blocks.add(new Block(Block.Type.WALL, 16, 13));

        blocks.add(new Block(Block.Type.WALL, 17, 0));
        blocks.add(new Block(Block.Type.WALL, 17, 13));

        blocks.add(new Block(Block.Type.WALL, 18, 0));
        blocks.add(new Block(Block.Type.WALL, 18, 13));

        blocks.add(new Block(Block.Type.WALL, 19, 0));
        blocks.add(new Block(Block.Type.WALL, 19, 1));
        blocks.add(new Block(Block.Type.WALL, 19, 2));
        blocks.add(new Block(Block.Type.WALL, 19, 3));
        blocks.add(new Block(Block.Type.WALL, 19, 4));
        blocks.add(new Block(Block.Type.WALL, 19, 5));
        blocks.add(new Block(Block.Type.WALL, 19, 6));
        blocks.add(new Block(Block.Type.WALL, 19, 7));
        blocks.add(new Block(Block.Type.WALL, 19, 8));
        blocks.add(new Block(Block.Type.WALL, 19, 9));
        blocks.add(new Block(Block.Type.WALL, 19, 10));
        blocks.add(new Block(Block.Type.WALL, 19, 11));
        blocks.add(new Block(Block.Type.WALL, 19, 12));
        blocks.add(new Block(Block.Type.WALL, 19, 13));

        Block b = new Block(Block.Type.START, 2, 2);
        ball.setInitialRectangle(new RectF(b.getRectangle()));
        blocks.add(b);

        blocks.add(new Block(Block.Type.END, 8, 11));

        return blocks;
    }
}