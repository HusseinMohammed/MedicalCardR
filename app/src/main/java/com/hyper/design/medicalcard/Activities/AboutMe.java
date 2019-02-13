package com.hyper.design.medicalcard.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class AboutMe extends AppCompatActivity {

    public static final String SERVER_MEDICAL_URL = SERVER_URL; // http://192.168.1.4/medicalcard/
    public static final String SERVER_CONTACT_US_URL= "/aboutus";

    TextView aboutUs, aboutUsData;
    private SQLiteHandler db;
    private SessionManager session;

    public String sessionLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        aboutUs = (TextView) findViewById(R.id.tv_about_us);
        aboutUsData = (TextView) findViewById(R.id.tv_about_us_data);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguage", sessionLanguage);

        aboutUs();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(AboutMe.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void aboutUs() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_MEDICAL_URL + SERVER_CONTACT_US_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseRegister", response);


                        try {
                            aboutUsData = (TextView) findViewById(R.id.tv_about_us_data);
                            JSONObject jsonObject = new JSONObject(response);
                            String aboutUs ="";
                            if(sessionLanguage.equals("en")){
                                aboutUs = jsonObject.getString("about_en");
                            } else {
                                aboutUs = jsonObject.getString("about_ar");
                            }
                            if(aboutUs.equals(null)){
                                aboutUs = "";
                                aboutUsData.setText(aboutUs);
                            } else {
                                aboutUsData.setText(aboutUs);
                            }

                            //Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();




                            //Log.d("responseRegister", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error Connection", Toast.LENGTH_LONG).show();
            }
        });
        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
