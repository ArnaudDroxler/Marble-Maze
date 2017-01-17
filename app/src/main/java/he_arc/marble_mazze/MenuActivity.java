package he_arc.marble_mazze;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

public class MenuActivity extends AppCompatActivity {

    private PowerManager.WakeLock w1;
    private TextView textMenu;
    private TextView textNomNiveau1;
    private TextView textScoreNiveau1;
    private Button buttonStart1;
    private Context ctx;
    private String content ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        textNomNiveau1 = (TextView) findViewById(R.id.textNomNiveau1);
        textNomNiveau1.setText("Niveau1");

        textScoreNiveau1 = (TextView) findViewById(R.id.textScoreNiveau1);
        textNomNiveau1.setText("0");
        ctx = getApplicationContext();
        //Récupération du fichier de sauvegarde
        try {
            FileInputStream fin = ctx.openFileInput("MM_save");
            int c;
            while( (c = fin.read()) != -1){
                content += Character.toString((char)c);
            }
            //string temp contains all the data of the file.
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Séparation de la string: 1 token par niveau
        StringTokenizer tokensNiveaux = new StringTokenizer(content, "|");
        content="";

        while(tokensNiveaux.hasMoreTokens()) {
            String thisTokenNiveau = tokensNiveaux.nextToken();
            //Pour chaque niveau, on va séparer le nom du score
            StringTokenizer tokensDonneesNiveauActuel = new StringTokenizer(thisTokenNiveau, "/");
            String thisNomNiveau = tokensDonneesNiveauActuel.nextToken();
            String thisScoreNiveau = tokensDonneesNiveauActuel.nextToken();
            //Si on analyse le score de ce niveau là
            if(thisNomNiveau.equals(textNomNiveau1.getText())) {
                textScoreNiveau1.setText("Score : "+ thisScoreNiveau);
            }
        }
        
        buttonStart1 = (Button) findViewById(R.id.buttonStart1);;
        buttonStart1.setText("Start");

        buttonStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, MazzeActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
