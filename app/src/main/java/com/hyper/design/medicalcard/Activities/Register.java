package com.hyper.design.medicalcard.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.User.User;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;
import com.hyper.design.medicalcard.spinnersData.AreaSpinner;
import com.hyper.design.medicalcard.spinnersData.CitySpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class Register extends AppCompatActivity implements View.OnClickListener{

    public static final String SERVER_MEDICAL_URL = SERVER_URL;  //local host = http://192.168.1.4/medicalcard/api  //Server = http://hyper-design.com/newmedicalcard/api
    public static final String SERVER_CITY_URL= "/getregion";
    public static final String SERVER_AREA_URL= "/region";
    public static final String SERVER_REGISTER_URL= "/register";


    Spinner spinCities, spinAreas;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterSpinCategories, adapterSpinSpecialists, adapterSpinCities, adapterSpinAreas;


    List<String> spinnerArrayCities, spinnerArrayAreas;

    public int idCity;
    String strCityArabic = null, strCityEnglish = null;
    public int idAreas;
    String strAreasArabic = null, strAreasEnglish = null;

    public static final String KEY_SPINCITY = "region"; // error "city" to "region"
    public static final String KEY_SPINAREA = "area";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_CODE = "code";

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etCodeNumber;

    //private Button btnRegister;
    //String Name, Password, Email, Address;

    CitySpinner citySpinner;
    AreaSpinner areaSpinner;

    Map<String, Integer> integerStringCityMap;
    Map<String, Integer> integerStringAreasMap;

    public int intSpinCity, intSuccess = 0;
    public String strMessage = null;

    SharedPreferences settings;

    public String localLanguage;


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private static final String TAG = Register.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        inputFullName = (EditText) findViewById(R.id.etUsername);
        spinCities = (Spinner) findViewById(R.id.spin_all_cities_register);
        spinAreas = (Spinner) findViewById(R.id.spin_all_areas_register);
        inputEmail = (EditText) findViewById(R.id.ac_tv_email);
        inputPassword = (EditText) findViewById(R.id.et_password);
        etCodeNumber = (EditText) findViewById(R.id.etCodeNumber);
        btnRegister = (Button) findViewById(R.id.btn_register);
        //btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }*/



        etUsername = (EditText) findViewById(R.id.etUsername);
        spinCities = (Spinner) findViewById(R.id.spin_all_cities_register);
        spinAreas = (Spinner) findViewById(R.id.spin_all_areas_register);
        etEmail = (EditText) findViewById(R.id.ac_tv_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etCodeNumber = (EditText) findViewById(R.id.etCodeNumber);
        btnRegister = (Button) findViewById(R.id.btn_register);


        //NullPointerException
        spinnerArrayCities =  new ArrayList<String>();
        spinnerArrayAreas =  new ArrayList<String>();

        citySpinner = new CitySpinner();
        areaSpinner = new AreaSpinner();


        integerStringCityMap = new HashMap<String, Integer>();
        integerStringAreasMap = new HashMap<String, Integer>();

        // Set up the login form.
        mLoginFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);



        //populateAutoComplete();



        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        localLanguage = settings.getString("locale","");
        //localLanguage = "en";


        editor.clear();
        editor.commit();

        getRegion();

        spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                sendRegionId();

                if(spinnerArrayAreas.size() > 1) {
                    if (localLanguage.equals("en")) {
                        spinAreas.setAdapter(null);
                        spinnerArrayAreas.clear();
                        spinnerArrayAreas.add("Choose an Area");
                    } else {
                        spinAreas.setAdapter(null);
                        spinnerArrayAreas.clear();
                        spinnerArrayAreas.add("اختر المنطقة");
                    }
                }
                /*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Store values at the time of the login attempt.
                String username = etUsername.getText().toString();
                String region = spinCities.getSelectedItem().toString(); //
                String area = spinAreas.getSelectedItem().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String code = etCodeNumber.getText().toString();

                switch (view.getId()){
                    case R.id.btn_register:
                        if(username.equals("")) {
                            etUsername.setError(getString(R.string.error_field_required));
                        } else if( region.equals("Choose a Region")){
                            /**
                             *   You can Toast a message here that the Username is Empty
                             */
                            Toast.makeText(getBaseContext(), "Region is required!", Toast.LENGTH_LONG).show();

                        } else if(area.equals("Choose an Area")){

                            Toast.makeText(getBaseContext(), "Area is required!", Toast.LENGTH_LONG).show();
                        }  else if(email.equals("")) {
                            etEmail.setError( getString(R.string.error_field_required));
                        }  else if(!isEmailValid(email)) {
                            etEmail.setError(getString(R.string.error_invalid_email));
                        }  else if(!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                            etPassword.setError(getString(R.string.error_invalid_password));
                        }  else if(code.equals("")) {
                            etCodeNumber.setError(getString(R.string.error_field_required));
                        }  else {

                            username = etUsername.getText().toString();;
                            email = etEmail.getText().toString();
                            password = etPassword.getText().toString();
                            code = etCodeNumber.getText().toString().trim();
                            final int idFinalCity = integerStringCityMap.get(spinCities.getSelectedItem());
                            final int idFinalArea = integerStringAreasMap.get(spinAreas.getSelectedItem());
                            //int age = Integer.parseInt(etAge.getText().toString);

                            User registerUserData = new User(username, idFinalCity, idFinalArea, email, password, code);
                            showProgress(true);
                            registerUser();
                        }
                }
            }
        });


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(Register.this, LoginActivity.class);
            startActivity(intent);
            //onDestroy();
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void registerUser() {

        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String code = etCodeNumber.getText().toString().trim();
        final int idFinalCity = integerStringCityMap.get(spinCities.getSelectedItem());
        final int idFinalArea = integerStringAreasMap.get(spinAreas.getSelectedItem());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseRegister", response);
                        //                        Toast.makeText(getApplicationContext(), "Success Connection", Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            strMessage = jsonObject.getString("msg");
                            intSuccess = jsonObject.getInt("done");


                            if(intSuccess == 1 && strMessage.equals("sucess register")){
                                JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                                String userName = jsonObjectUser.getString("name");
                                int clientId = jsonObjectUser.getInt("client_id");
                                String userEmail = jsonObjectUser.getString("email");
                                int userId = jsonObjectUser.getInt("id");

                                Intent intent = new Intent(Register.this, LoginActivity.class);
                                intent.putExtra("email", userEmail);
                                intent.putExtra("id", userId);
                                intent.putExtra("name", userName);

                                finish();
                                onDestroy();

                                startActivity(intent);
                            } else if (strMessage.equals("email token") ){
                                Toast.makeText(getBaseContext(), "This Email is already exist", Toast.LENGTH_LONG).show();
                                showProgress(false);
                                /*final AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage(strMessage);
                                builder.setCancelable(true);
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();*/
                            } else if (strMessage.equals("code error")){
                                Toast.makeText(getBaseContext(), "Invalid Code", Toast.LENGTH_LONG).show();
                                showProgress(false);
                            } else {

                            }

                            Log.d("responseRegister", jsonObject.toString());

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
                params.put(KEY_SPINCITY, idFinalCity+"");
                params.put(KEY_SPINAREA, idFinalArea+"");
                params.put(KEY_PASSWORD, password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_CODE, code);
                return params;
            }
        };
        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getRegion(){

        /*final RequestQueue requestQueue = Volley.newRequestQueue(BuyCard.this);*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.i("localLanguage", localLanguage);
                            Log.i("response", response);
                            JSONArray jsonArrayCity = new JSONArray(response); //response
                            JSONObject jsonObjectCity = null;
                            spinnerArrayCities =  new ArrayList<String>();

                            if(localLanguage.equals("en")) {
                                integerStringCityMap.put(getResources().getString(R.string.Cities), 0);
                                spinnerArrayCities.add(getResources().getString(R.string.Cities));
                            } else {
                                integerStringCityMap.put(getResources().getString(R.string.Cities), 0);
                                spinnerArrayCities.add(getResources().getString(R.string.Cities));
                            }

                            for(int i = 0; i < jsonArrayCity.length(); i++){

                                // read data from json array of region
                                jsonObjectCity = jsonArrayCity.getJSONObject(i);

                                // put data inside int and strings
                                if(localLanguage.equals("en")){
                                    citySpinner.cityId = jsonObjectCity.getInt(citySpinner.getCityKeyId());
                                    citySpinner.cityStringEnglishName = jsonObjectCity.getString(citySpinner.getCityKeyStringEnglishName());

                                    // set data inside array list
                                    integerStringCityMap.put(citySpinner.cityStringEnglishName, citySpinner.cityId);

                                    spinnerArrayCities.add(citySpinner.cityStringEnglishName);

                                } else {
                                    citySpinner.cityId = jsonObjectCity.getInt(citySpinner.getCityKeyId());
                                    citySpinner.cityStringArabicName = jsonObjectCity.getString(citySpinner.getCityKeyStringArabicName());

                                    // set data inside array list
                                    integerStringCityMap.put(citySpinner.cityStringArabicName, citySpinner.cityId);

                                    spinnerArrayCities.add(citySpinner.cityStringArabicName);
                                }

                            }

                            Log.i("response", response);
                            // TODO 2 Spinner Cities
                            adapter = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, spinnerArrayCities);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCities = (Spinner) findViewById(R.id.spin_all_cities_register);
                            spinCities.setAdapter(adapter);
                            spinCities.setDropDownWidth(524);


                            if(localLanguage.equals("en")){
                                integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                                spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                            } else {
                                integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                                spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                            }

                            //requestQueue.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        };

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //requestQueue.stop();
            }
        }) ;

        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    //update on this Method
    private void sendRegionId(){

        final String strSpinCategory = spinCities.getSelectedItem().toString().trim();

        Log.i("id CitySpinner",""+idCity);
        intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());



        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_AREA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CitySpinner Response",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            try{
                                // Loop through the array elements

                                for(int i = 0; i < jsonArray.length(); i++){

                                    // read data from json array of region
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    // put data inside int and strings
                                    if(localLanguage.equals("en")){
                                        // put data inside int and strings
                                        areaSpinner.areaId = jsonObject.getInt(areaSpinner.getAreaKeyId());
                                        areaSpinner.areaStringEnglishName = jsonObject.getString( areaSpinner.getAreaKeyStringEnglishName());

                                        // set data inside array list
                                        integerStringAreasMap.put(areaSpinner.areaStringEnglishName, areaSpinner.areaId);


                                        spinnerArrayAreas.add(areaSpinner.areaStringEnglishName);

                                    } else {
                                        // put data inside int and strings
                                        areaSpinner.areaId = jsonObject.getInt(areaSpinner.getAreaKeyId());
                                        areaSpinner.areaStringArabicName = jsonObject.getString( areaSpinner.getAreaKeyStringArabicName());

                                        // set data inside array list
                                        integerStringAreasMap.put(areaSpinner.areaStringArabicName, areaSpinner.areaId);


                                        spinnerArrayAreas.add(areaSpinner.areaStringArabicName);
                                    }

                                }


                                // TODO 1 Spinner Category
                                adapterSpinAreas = new ArrayAdapter<String>(getBaseContext(),
                                        android.R.layout.simple_spinner_item, spinnerArrayAreas);

                                adapterSpinAreas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinAreas = (Spinner) findViewById(R.id.spin_all_areas_register);
                                spinAreas.setAdapter(adapterSpinAreas);
                                spinAreas.setSelection(0);
                                spinAreas.setDropDownWidth(524);

                                Log.e("ArrayAreas",spinnerArrayAreas.toString());

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                            Log.d("id Area", "id = "+idAreas);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error CitySpinner", error+"");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_SPINCITY, String.valueOf(intSpinCity));
                return params;
            }
        };
        MySingletonhus.getInstance(Register.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {

    }

    /*
           private boolean isEmailValid(String email) {
                //TODO: Replace this with your own logic
                return email.contains("@");
            }

            private boolean isPasswordValid(String password) {
                //TODO: Replace this with your own logic
                return password.length() > 4;
            }
            */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@gmail.com") ||
                email.contains("@hotmail.com") ||
                email.contains("@yahoo.com") ||
                email.contains("@github.com") ||
                email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
