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
import android.widget.LinearLayout;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    private PowerManager.WakeLock w1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        Intent intent = getIntent();
        boolean win = intent.getBooleanExtra("win",true);


        LinearLayout ll = new LinearLayout(this);

        String str = win ? "win" : "lose";
        TextView tx= new TextView(this);
        tx.setText(str);
        ll.addView(tx);

        setContentView(ll);

    }
}
