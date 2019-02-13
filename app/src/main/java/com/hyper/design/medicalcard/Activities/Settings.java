package com.hyper.design.medicalcard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;

import java.util.Locale;

public class Settings extends AppCompatActivity implements View.OnClickListener{

    TextView tvLang;
    RadioButton rbEng, rbArab;
    RadioGroup rgChangeLanguage;
    SharedPreferences settings;
    Locale myLocale;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rgChangeLanguage = (RadioGroup) findViewById(R.id.rg_change_language);


        tvLang = (TextView) findViewById(R.id.tv_change_lang);
        rbEng = (RadioButton) findViewById(R.id.rb_english);
        rbArab = (RadioButton) findViewById(R.id.rb_arabic);



        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());


        /*Spinner spinner = (Spinner) findViewById(R.id.pioedittxt5);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.travel_reasons, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    public void onClickRadio(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_arabic:
                setLocaleLanguage("ar");
                //setLocale("ar");
                break;
                /*if (checked)
                // Pirates are the best*/
            case R.id.rb_english:
                setLocaleLanguage("en");
                //setLocale("en");
                break;/*if (checked)
                // Ninjas rule*/
            default:
                setLocaleLanguage("en");
                //setLocale("en");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        /*onRadioButtonClicked(view);*/
        /*switch (view.getId()) {
            case R.id.rb_arabic:
                if(che)
                setLocale("ar");
                break;
            case R.id.rb_english:
                setLocale("en");
                break;
        }*/
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        getBaseContext().getResources().updateConfiguration(conf,
                getBaseContext().getResources().getDisplayMetrics());
        session.setLanguage(lang);

        db.addLanguage(lang);

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().putString("locale", lang).commit();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    public void setLocaleLanguage(String lang){
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        session.setLanguage(lang);

        //db.addLanguage(lang);

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().putString("locale", lang).commit();
        this.finish();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

    public Locale getMyLocale() {
        return myLocale;
    }

    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    private void clearLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
