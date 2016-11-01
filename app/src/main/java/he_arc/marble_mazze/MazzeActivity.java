package he_arc.marble_mazze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MazzeActivity extends AppCompatActivity {

    private MazzeView mView = null;
    private MazzeEngine mEngine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
    }

    @Override
    protected void onPause() {
        super.onStop();
        mEngine.stop();
    }

}
