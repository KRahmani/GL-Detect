package com.example.gldetect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static Locale myLocale;
    private static Button start_btn;
    private static TextView txtAccueil,txtDiagnostic,txtAproposGL,txtAproposApp;

    //Shared Preferences Variables
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Locale_Preference, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
         start_btn= (Button) findViewById(R.id.start_btn);
         txtAccueil = (TextView) findViewById(R.id.TVAccueil);
         txtDiagnostic = (TextView) findViewById(R.id.TVdiagnostic);
         txtAproposGL = (TextView) findViewById(R.id.TVAproposGL);
         txtAproposApp = (TextView) findViewById(R.id.TVAproposApp);
        final Context context = this;
        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context,Start.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_langue,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String lang = "fr";
        switch (item.getItemId()){
            case R.id.français :
                lang = "fr";
                changeLocale(lang);
                return true;
            case R.id.anglais :
                lang = "en";
                changeLocale(lang);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Change Locale
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);//Set Selected Locale
        saveLocale(lang);//Save the selected locale
        Locale.setDefault(myLocale);//set new locale as default
        Configuration config = new Configuration();//get Configuration
        config.locale = myLocale;//set config locale as selected locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        updateTexts();//Update texts of current page (main_activity)
    }
    //Save locale method in preferences
    public void saveLocale(String lang) {
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
    }

    //Get locale method in preferences
    public void loadLocale() {
        String language = sharedPreferences.getString(Locale_KeyValue, "");
        changeLocale(language);
    }

    //Update text methods
    private void updateTexts() {
        start_btn.setText(R.string.btCommencer);
        txtAccueil.setText(R.string.textAccueil1);
        txtDiagnostic.setText(R.string.textAccueil2);
        txtAproposGL.setText(R.string.btAproposApp);
        txtAproposApp.setText(R.string.btAproposGL);

    }

}
