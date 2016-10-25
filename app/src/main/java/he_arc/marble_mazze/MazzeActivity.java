package he_arc.marble_mazze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MazzeActivity extends AppCompatActivity {

    private MazzeView mView = null;
    private MazzeEngine mEngine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
