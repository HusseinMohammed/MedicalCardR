package com.hyper.design.medicalcard.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.hyper.design.medicalcard.network_hus.MySingletonhus;
import com.hyper.design.medicalcard.spinnersData.AreaSpinner;
import com.hyper.design.medicalcard.spinnersData.CitySpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class BuyCard extends AppCompatActivity {

    //public static final int RESULT_LOAD_IMG = 111; //fragment result load 111
    public static final String SERVER_MEDICAL_URL = SERVER_URL;
    public static final String SERVER_CITY_URL = "/getregion";
    public static final String SERVER_AREA_URL = "/region";
    public static final String SERVER_BUY_CARD_URL = "/buycard";

    Spinner spinCities, spinAreas;
    ProgressBar pbBuyCard;
    LinearLayout llBuyCard;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterSpinCategories, adapterSpinSpecialists, adapterSpinCities, adapterSpinAreas;

    ArrayList<String> spinnerArrayCities, spinnerArrayAreas;

    public int idCity;
    String strCityArabic = null, strCityEnglish = null;
    public int idAreas;
    String strAreasArabic = null, strAreasEnglish = null;

    public static final String KEY_SPINCITY = "region"; // error "city" to "region"
    public static final String KEY_SPINAREA = "area";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_PROFILE = "profile";

    private ImageView ivProfile, ivId;
    private EditText etUsername, etEmail, etPhone, etAddress, etComment;
    private RadioGroup radioGroup;
    private RadioButton radioButtonIndividual, radioButtonFamily;

    private Button btnBuyCard;

    CitySpinner citySpinner;
    AreaSpinner areaSpinner;

    Map<String, Integer> integerStringCityMap;
    Map<String, Integer> integerStringAreasMap;

    SharedPreferences settings;

    //public String localLanguage;

    private SQLiteHandler db;
    private SessionManager session;

    public String sessionLanguage;

    private String profile, id;

    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG_PROF = 111, RESULT_LOAD_IMG_ID = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card);

        profile = new String();
        id = new String();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ivProfile = (ImageView) findViewById(R.id.iv_upload_profile);
        ivId = (ImageView) findViewById(R.id.iv_upload_id);
        llBuyCard = (LinearLayout) findViewById(R.id.buy_card_form);
        pbBuyCard = (ProgressBar) findViewById(R.id.pbBuyCard);


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG_PROF);
                //startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        ivId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG_ID);
                //startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });

        etUsername = (EditText) findViewById(R.id.et_name_buy_card);
        etEmail = (EditText) findViewById(R.id.et_email_buy_card);
        etPhone = (EditText) findViewById(R.id.et_phone_buy_card);
        spinCities = (Spinner) findViewById(R.id.spin_all_cities);
        spinAreas = (Spinner) findViewById(R.id.spin_all_areas);
        etAddress = (EditText) findViewById(R.id.et_address_buy_card);

        radioGroup = (RadioGroup) findViewById(R.id.radio_type_membership);


        radioButtonIndividual = (RadioButton) findViewById(R.id.radio_individual);
        radioButtonFamily = (RadioButton) findViewById(R.id.radio_family);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radio_individual:
                        Toast.makeText(getBaseContext(), radioButtonIndividual.getText(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radio_family:
                        Toast.makeText(getBaseContext(), radioButtonFamily.getText(), Toast.LENGTH_LONG).show();
                        break;
                    default:

                }
            }
        });

        etComment = (EditText) findViewById(R.id.et_comment);

        //NullPointerException
        spinnerArrayCities =  new ArrayList<String>();
        spinnerArrayAreas =  new ArrayList<String>();


        citySpinner = new CitySpinner();
        areaSpinner = new AreaSpinner();

        integerStringCityMap = new HashMap<String, Integer>();
        integerStringAreasMap = new HashMap<String, Integer>();

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguage", sessionLanguage);
        //localLanguage = settings.getString("locale","");
        //localLanguage = "en";

        editor.clear();
        editor.commit();

        getRegion();

        spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sendRegionId();

                if(spinnerArrayAreas.size() > 1) {
                    if (sessionLanguage.equals("en")) {
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

        /*spinAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                *//*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*//*
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        btnBuyCard = (Button) findViewById(R.id.btn_buy_card);
        btnBuyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                String stringNull = null;
                /*switch(stringNull){
                    case "Select City":
                        spinCities.getSelectedItem().equals("Select City");
                        Toast.makeText(getBaseContext(), "City is required!", Toast.LENGTH_LONG).show();
                        break;
                    case "Select Area":
                        spinCities.getSelectedItem().equals("Select Area");
                        Toast.makeText(getBaseContext(), "Area is required!", Toast.LENGTH_LONG).show();
                        break;
                    case "Name":
                        etUsername.getText().toString().trim().equals("");
                        etUsername.setError( "Name is required!" );
                        break;
                    case "Email":
                        etEmail.getText().toString().trim().equals("");
                        etEmail.setError( "Email is required!" );
                        break;
                    case "Phone":
                        etPhone.getText().toString().trim().equals("");
                        etPhone.setError( "Phone is required!" );
                        break;
                    case "Address":
                        etAddress.getText().toString().trim().equals("");
                        etAddress.setError( "Address is required!" );
                        break;
                }*/

                if(spinCities.getSelectedItem().equals("Select City")) {

                    Toast.makeText(getBaseContext(), "City is required!", Toast.LENGTH_LONG).show();

                } else if(spinAreas.getSelectedItem().equals("Select Area")){

                    Toast.makeText(getBaseContext(), "Area is required!", Toast.LENGTH_LONG).show();

                } else if( etUsername.getText().toString().trim().equals("")){
                    /**
                     *   You can Toast a message here that the Username is Empty
                     **/
                    etUsername.setError( "Name is required!" );

                } else if(etEmail.getText().toString().trim().equals("")) {

                    etEmail.setError( "Email is required!" );
                } else if (!isEmailValid(email)){
                    etEmail.setError(getString(R.string.error_invalid_email));
                }
                else if(etPhone.getText().toString().trim().equals("")) {
                    etPhone.setError( "Phone is required!" );

                } else if (!isPhoneValid(phone)){
                    etPhone.setError(getString(R.string.error_invalid_phone));

                } else if(etAddress.getText().toString().trim().equals("")) {

                    etAddress.setError( "Address is required!" );
                } /*else if (!isAddressValid(address)){
                    etAddress.setError(getString(R.string.error_invalid_address));
                }*/ else {
                    showProgress(true);
                    buyCard();
                    /*Intent intent = new Intent(BuyCard.this, MainActivity.class);
                    startActivity(intent);*/
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                /*if(R.id.iv_upload_profile == ){
                    ivProfile.setImageBitmap(selectedImage);
                } else {
                    ivId.setImageBitmap(selectedImage);
                }*/
                if(requestCode == RESULT_LOAD_IMG_PROF){
                    ivProfile.setImageBitmap(selectedImage);
                    profile = imageToString(selectedImage);
                } else {
                    ivId.setImageBitmap(selectedImage);
                    id = imageToString(selectedImage);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(BuyCard.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void buyCard() {
        Log.i(TAG, profile);
        Log.i(TAG, id);
        final String stringProfile = profile;
        final String stringId = id;
        final String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final int intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());
        final int intSpinArea = integerStringAreasMap.get(spinAreas.getSelectedItem());
        final String address = etAddress.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String comment = etComment.getText().toString().trim();
        final String radioChecked = getRadioChecked();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_BUY_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseBuyCard", response);
                        /*Toast.makeText(getApplicationContext(), "Success Connection", Toast.LENGTH_LONG).show();*/

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String successMessage = "تم أرسال طلبك بنجاح";
                            String success = jsonObject.getString("msg");
                            if(success.equals(successMessage)){

                                if (sessionLanguage.equals("en")) {
                                    showProgress(false);
                                    Toast.makeText(getBaseContext(), "Request has been sent successfully", Toast.LENGTH_LONG).show();

                                } else {
                                    showProgress(false);
                                    Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG).show();
                                }

                                ivProfile.setImageResource(R.drawable.user_profile);
                                ivId.setImageResource(R.drawable.badge);
                                etUsername.setText("");
                                etEmail.setText("");
                                spinCities.setSelection(0);
                                spinAreas.setSelection(0);
                                etPhone.setText("");
                                etAddress.setText("");
                                etComment.setText("");
                                radioButtonIndividual.isChecked();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Error Connection", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_TYPE, radioChecked);
                params.put(KEY_PROFILE, stringProfile);
                params.put(KEY_ID, stringId);
                params.put(KEY_NAME, username);
                params.put(KEY_EMAIL, email);
                params.put(KEY_SPINCITY, String.valueOf(intSpinCity));
                params.put(KEY_SPINAREA, String.valueOf(intSpinArea));
                params.put(KEY_ADDRESS, address);
                params.put(KEY_PHONE, phone);
                params.put(KEY_COMMENT, comment);
                return params;
            }
        };
        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String getRadioChecked(){
        String radioChecked = null;
        if(radioGroup.getCheckedRadioButtonId() == R.id.radio_individual){
            radioChecked = radioButtonIndividual.getText().toString();
            return radioChecked;
        } else {
            radioChecked = radioButtonFamily.getText().toString();
            return radioChecked;
        }
    }

    private void getRegion(){

        /*final RequestQueue requestQueue = Volley.newRequestQueue(BuyCard.this);*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("localLanguage", sessionLanguage);

                            Log.i("response", response);
                            JSONArray jsonArrayCity = new JSONArray(response); //response
                            JSONObject jsonObjectCity = null;
                            spinnerArrayCities =  new ArrayList<String>();

                            if(sessionLanguage.equals("en")) {
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
                                if(sessionLanguage.equals("en")){
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
                            spinCities = (Spinner) findViewById(R.id.spin_all_cities);
                            spinCities.setAdapter(adapter);
                            spinCities.setSelection(0);
                            spinCities.setDropDownWidth(525);

                            if(sessionLanguage.equals("en")){
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
        final int intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());

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
                                    if(sessionLanguage.equals("en")){
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
                                spinAreas = (Spinner) findViewById(R.id.spin_all_areas);
                                spinAreas.setAdapter(adapterSpinAreas);
                                spinAreas.setSelection(0);
                                spinAreas.setDropDownWidth(525);

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
        MySingletonhus.getInstance(BuyCard.this).addToRequestQueue(stringRequest);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            llBuyCard.setVisibility(show ? View.GONE : View.VISIBLE);
            llBuyCard.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    llBuyCard.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbBuyCard.setVisibility(show ? View.VISIBLE : View.GONE);
            pbBuyCard.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbBuyCard.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbBuyCard.setVisibility(show ? View.VISIBLE : View.GONE);
            llBuyCard.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return phone.matches("^[0-9]{11}$");
    }

    private boolean isAddressValid(String email) {
        //TODO: Replace this with your own logic
        return email.matches("\\\\d+\\\\s+([a-zA-Z]+|[a-zA-Z]+\\\\s[a-zA-Z]+)");
    }

    private boolean isNameValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("[A-Z][a-zA-Z]*");
    }

    private boolean isCommentValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() <= 50;
    }

}
