package he_arc.marble_mazze;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

        Log.i("Ac","" + offset);
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

}
