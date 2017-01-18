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
    private TextView textNomNiveau2;
    private TextView textScoreNiveau2;
    private Button buttonStart2;
    private TextView textNomNiveau3;
    private TextView textScoreNiveau3;
    private Button buttonStart3;
    private Context ctx;
    private String content ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        textMenu = (TextView) findViewById(R.id.textMenu);
        textMenu.setText("Choix du niveau");

        textNomNiveau1 = (TextView) findViewById(R.id.textNomNiveau1);
        textNomNiveau1.setText("Niveau 1");

        textScoreNiveau1 = (TextView) findViewById(R.id.textScoreNiveau1);
        textScoreNiveau1.setText("Score : 0");

        textNomNiveau2 = (TextView) findViewById(R.id.textNomNiveau2);
        textNomNiveau2.setText("Niveau 2");

        textScoreNiveau2 = (TextView) findViewById(R.id.textScoreNiveau2);
        textScoreNiveau2.setText("Score : 0");

        textNomNiveau3 = (TextView) findViewById(R.id.textNomNiveau3);
        textNomNiveau3.setText("Niveau 3");

        textScoreNiveau3 = (TextView) findViewById(R.id.textScoreNiveau3);
        textScoreNiveau3.setText("Score : 0");

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
            if(thisNomNiveau.equals("niveau_1")) {
                textScoreNiveau1.setText("Score : "+ thisScoreNiveau);
            }
            if(thisNomNiveau.equals("niveau_2")) {
                textScoreNiveau2.setText("Score : "+ thisScoreNiveau);
            }
            if(thisNomNiveau.equals("niveau_3")) {
                textScoreNiveau3.setText("Score : "+ thisScoreNiveau);
            }
        }

        buttonStart1 = (Button) findViewById(R.id.buttonStart1);;
        buttonStart1.setText("Start");

        buttonStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, MazzeActivity.class);
                myIntent.putExtra("level", R.drawable.niveau_1);
                startActivity(myIntent);
            }
        });

        buttonStart2 = (Button) findViewById(R.id.buttonStart2);;
        buttonStart2.setText("Start");

        buttonStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, MazzeActivity.class);
                myIntent.putExtra("level", R.drawable.niveau_2);
                startActivity(myIntent);
            }
        });

        buttonStart3 = (Button) findViewById(R.id.buttonStart3);
        buttonStart3.setText("Start");

        buttonStart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MenuActivity.this, MazzeActivity.class);
                myIntent.putExtra("level", R.drawable.niveau_3);
                startActivity(myIntent);
            }
        });


    }
}
