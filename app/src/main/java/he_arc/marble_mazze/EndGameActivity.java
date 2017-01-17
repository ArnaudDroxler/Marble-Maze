package he_arc.marble_mazze;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    private PowerManager.WakeLock w1;
    private TextView textEnd;
    private TextView textScore;
    private Button buttonRetry;
    private TextView buttonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        Intent intent = getIntent();
        boolean win = intent.getBooleanExtra("win",true);
        String winStr = win ? "Win" : "Lose";
        String score = intent.getStringExtra("score");
        String scoreStr = "Score : " + score;

        textEnd = (TextView) findViewById(R.id.textEnd);
        textEnd.setText(winStr);

        textScore = (TextView) findViewById(R.id.textScore);
        textScore.setText(scoreStr);

        buttonMenu = (Button) findViewById(R.id.buttonMenu);
        buttonMenu.setText("Menu");
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EndGameActivity.this, MazzeActivity.class);
                startActivity(myIntent);
            }
        });

        buttonRetry = (Button) findViewById(R.id.buttonRetry);
        buttonRetry.setText("Retry");
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EndGameActivity.this, MazzeActivity.class);
                startActivity(myIntent);
            }
        });
        
    }
}
