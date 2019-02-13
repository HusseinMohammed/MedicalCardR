package com.hyper.design.medicalcard.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {

    public static final String SERVER_MEDICAL_URL = SERVER_URL; // http://192.168.1.4/medicalcard/
    public static final String SERVER_CONTACT_US_URL= "/contactus";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_MESSAGE = "message";


    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etTitle;
    private EditText etMessage;

    private Button btnSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etMessage = (EditText) findViewById(R.id.et_message);
        btnSendMessage = (Button) findViewById(R.id.btn_send_message);


        /*btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs();
            }
        });*/
        btnSendMessage.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(ContactUs.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void contactUs() {

        final String username = etUsername.getText().toString();
        final String email = etEmail.getText().toString();
        final String phone = etPhone.getText().toString();
        //final String title = etTitle.getText().toString().trim();
        final String message = etMessage.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_CONTACT_US_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseRegister", response);


                          try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.getString("done");
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(ContactUs.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            //Log.d("responseRegister", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PHONE, phone);
                params.put(KEY_MESSAGE, message);
                return params;
            }
        };
        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@gmail.com") ||
                email.contains("@hotmail.com") ||
                email.contains("@yahoo.com") ||
                email.contains("@github.com");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_message:
                // Store values at the time of the login attempt.
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                //String titleOfMessage = etTitle.getText().toString();
                String message = etMessage.getText().toString();

                if(username.equals("")) {
                    etUsername.setError(getString(R.string.error_field_required));
                }   else if(email.equals("")) {
                    etEmail.setError( getString(R.string.error_field_required));
                }  else if(!isEmailValid(email)) {
                    etEmail.setError(getString(R.string.error_invalid_email));
                }  else if(phone.equals("")) {
                    etPhone.setError(getString(R.string.error_field_required));
                } else if (!isPhoneValid(phone)){
                    etPhone.setError(getString(R.string.error_invalid_phone));

                } else if(message.equals("")) {   /*else if(message.equals("")) {etMessage.setError(getString(R.string.error_field_required));}*/
                    etMessage.setError(getString(R.string.error_field_required));
                } else {
                    contactUs();
                }
                break;
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^[0-9]{11}$");
    }

    private boolean isMessageValid(String message) {
        //TODO: Replace this with your own logic
        return message.length() <= 50;
    }
}
