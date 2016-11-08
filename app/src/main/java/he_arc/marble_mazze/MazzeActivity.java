package he_arc.marble_mazze;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MazzeActivity extends AppCompatActivity {

    private MazzeView mView = null;
    private MazzeEngine mEngine = null;

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


        mView = new MazzeView(this);
        setContentView(mView);

        mEngine = new MazzeEngine(this);

        Ball ball = new Ball();

        mView.setBall(ball);
        mEngine.setBall(ball);

        List<Block> mList = mEngine.buildMazze();
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
