package he_arc.marble_mazze;

import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        int score = intent.getIntExtra("score",0);
        String scoreStr = "Score : " + score;

        textEnd = (TextView) findViewById(R.id.textMenu);
        textEnd.setText(winStr);

        textScore = (TextView) findViewById(R.id.textNomNiveau);
        textScore.setText(scoreStr);

        buttonMenu = (Button) findViewById(R.id.buttonStart1);
        buttonMenu.setText("Menu");
        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EndGameActivity.this, MenuActivity.class);
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
