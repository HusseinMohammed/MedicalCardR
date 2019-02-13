package com.hyper.design.medicalcard.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.jsonParser;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;
import com.hyper.design.medicalcard.spinnersData.AreaSpinner;
import com.hyper.design.medicalcard.spinnersData.CategorySpinner;
import com.hyper.design.medicalcard.spinnersData.CitySpinner;
import com.hyper.design.medicalcard.spinnersData.SpecialistsSpinner;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class SuggestDoctor extends AppCompatActivity {

    public static final String SERVER_MEDICAL_URL = SERVER_URL; //local host = http://192.168.1.4/medicalcard/  //Server = http://hyper-design.com/medicalcard/
    public static final String PARAMETER_URL ="?token=";
    public static final String PARAMETER = FirebaseInstanceId.getInstance().getToken();
    public static final String SERVER_CATEGORY_URL= "/category";
    public static final String SERVER_CITY_URL= "/region";
    public static final String SERVER_SUGGEST_URL= "/suggest";

    public static final String KEY_SPIN_CATE = "category";
    public static final String KEY_SPIN_CITY = "region"; // error "city" to "region"
    public static final String KEY_SPIN_SPEC = "section";
    public static final String KEY_SPIN_AREA = "area";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";

    Spinner spinCategories, spinSpecialists, spinCities, spinAreas;
    ArrayAdapter<String> adapterSpinCategories, adapterSpinSpecialists, adapterSpinCities, adapterSpinAreas;

    EditText etName, etPhone, etAddress;
    Button btnSuggestDoctor;

    public int idCategory;
    public int idSpecialists;
    public int idCity;
    public int idAreas;

    DoctorContacts doctorContacts;
    public ArrayList<DoctorContacts> doctorContactsArrayList;

    public jsonParser jsonParser;
    public String stringResponse;

    public String dataStr = null;

    SharedPreferences sharedpreferences;

    public static final String MY_PREFS_NAME = "MyData";

    Context context;
    CategorySpinner categorySpinner;          CitySpinner citySpinner;
    SpecialistsSpinner specialistsSpinner;    AreaSpinner areaSpinner;

    ArrayList<String> spinnerArrayCategory, spinnerArraySpecialists, spinnerArrayCities, spinnerArrayAreas;

    Map<String, Integer> integerStringCategoryMap;
    Map<String, Integer> integerStringCityMap;
    Map<String, Integer> integerStringSpecialistsMap;
    Map<String, Integer> integerStringAreasMap;

    public static final String PREFS_NAME = "MyApp_Settings";

    SharedPreferences settings;

    private SQLiteHandler db;
    private SessionManager session;

    public String sessionLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_doctor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        spinCategories = (Spinner) findViewById(R.id.spin_all_categories);
        spinSpecialists = (Spinner) findViewById(R.id.spin_all_specialists);
        spinCities = (Spinner) findViewById(R.id.spin_all_cities);
        spinAreas = (Spinner) findViewById(R.id.spin_all_areas);
        etName =(EditText) findViewById(R.id.et_name);
        etPhone =(EditText) findViewById(R.id.et_phone);
        etAddress =(EditText) findViewById(R.id.et_address);
        btnSuggestDoctor = (Button) findViewById(R.id.button_suggest_doctor);

        spinnerArrayCategory =  new ArrayList<String>();
        spinnerArraySpecialists =  new ArrayList<String>();
        spinnerArrayCities =  new ArrayList<String>();
        spinnerArrayAreas =  new ArrayList<String>();

        doctorContacts = new DoctorContacts();
        doctorContactsArrayList = new ArrayList<>();

        categorySpinner = new CategorySpinner();
        citySpinner = new CitySpinner();
        specialistsSpinner = new SpecialistsSpinner();
        areaSpinner = new AreaSpinner();

        integerStringCategoryMap = new HashMap<String, Integer>();
        integerStringCityMap = new HashMap<String, Integer>();
        integerStringSpecialistsMap = new HashMap<String, Integer>();
        integerStringAreasMap = new HashMap<String, Integer>();

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguage", sessionLanguage);

        getRegionAndCategory();

        spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    /*if(spinCategories.getSelectedItem().equals(spinnerArrayCategory.get(i)))
                    {

                    }*/
                /*if(adapterView.getItemAtPosition(i).equals("Select Category")){

                }*/
                sendCategoryId();
                /*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*/
                if(spinnerArraySpecialists.size() > 1) {
                    Log.i("localLanguage", sessionLanguage);
                    System.out.println(sessionLanguage);
                    if(sessionLanguage.equals("en")){
                        spinSpecialists.setAdapter(null);
                        spinnerArraySpecialists.clear();
                        spinnerArraySpecialists.add("Choose an Specialist");
                    } else {
                        spinSpecialists.setAdapter(null);
                        spinnerArraySpecialists.clear();
                        spinnerArraySpecialists.add("اختر التخصص");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinSpecialists.setAdapter(null);
            }
        });



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

        btnSuggestDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(spinCategories.getSelectedItem().equals("Choose a Category") || spinCategories.getSelectedItem().equals("اختر الفئة")){
                    Toast.makeText(getBaseContext(), "Category is required!", Toast.LENGTH_LONG).show();
                } else if(spinSpecialists.getSelectedItem().equals("Choose a Specialist") || spinSpecialists.getSelectedItem().equals("اختر التخصص")){
                    Toast.makeText(getBaseContext(), "Specialist is required!", Toast.LENGTH_LONG).show();
                } else if(spinCities.getSelectedItem().equals("Choose a City") || spinCities.getSelectedItem().equals("اختر المدينة")) {
                    Toast.makeText(getBaseContext(), "City is required!", Toast.LENGTH_LONG).show();
                } else if(spinAreas.getSelectedItem().equals("Choose an Area") || spinAreas.getSelectedItem().equals("اختر المنطقة")){
                    Toast.makeText(getBaseContext(), "Area is required!", Toast.LENGTH_LONG).show();
                } else if( etName.getText().toString().trim().equals("")){
                    /**
                     *   You can Toast a message here that the Username is Empty
                     **/
                    etName.setError( "Name is required!" );
                } else if(etPhone.getText().toString().trim().equals("")) {
                    etPhone.setError( "Phone is required!" );
                } else if(etAddress.getText().toString().trim().equals("")) {
                    etAddress.setError( "Address is required!" );
                } else{
                    sendAllItems();
                    Intent intent = new Intent(SuggestDoctor.this, MainActivity.class);
                    startActivity(intent);
                }


            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(SuggestDoctor.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }


    private void getRegionAndCategory(){
        //Log.d("parameter", PARAMETER);
        System.out.println(PARAMETER);
        final RequestQueue requestQueue = Volley.newRequestQueue(SuggestDoctor.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_MEDICAL_URL + PARAMETER_URL + PARAMETER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.i("localLanguage", sessionLanguage);

                            Log.i("response", response);
                            /*Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();*/
                            JSONObject jsonObject = new JSONObject(response); //response
                            JSONArray jsonArrayCity = jsonObject.getJSONArray("region");
                            JSONObject jsonObjectCity = jsonArrayCity.getJSONObject(0);
                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("category");
                            JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(0);

                            integerStringCityMap.put(getResources().getString(R.string.Cities),0);
                            spinnerArrayCities.add(getResources().getString(R.string.Cities));

                            for(int i = 0; i < jsonArrayCity.length(); i++){

                                // read data from json array of region
                                jsonObjectCity = jsonArrayCity.getJSONObject(i);

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

                                /*// put data inside int and strings
                                citySpinner.cityId = jsonObjectCity.getInt(citySpinner.getCityKeyId());
                                citySpinner.cityStringArabicName = jsonObjectCity.getString(citySpinner.getCityKeyStringArabicName());
                                citySpinner.cityStringEnglishName = jsonObjectCity.getString(citySpinner.getCityKeyStringEnglishName());*/

                                /*// set data inside array list
                                integerStringCityMap.put(citySpinner.cityStringEnglishName, citySpinner.cityId);


                                spinnerArrayCities.add(citySpinner.cityStringEnglishName);*/

                            }

                            integerStringCategoryMap.put(getResources().getString(R.string.categories),0);
                            spinnerArrayCategory.add(getResources().getString(R.string.categories));

                            for(int i = 0; i < jsonArrayCategory.length(); i++){

                                // read data from json array of region
                                jsonObjectCategory = jsonArrayCategory.getJSONObject(i);

                                if(sessionLanguage.equals("en")){
                                    // put data inside int and strings
                                    categorySpinner.categoryId = jsonObjectCategory.getInt(categorySpinner.getCategoryKeyId());
                                    categorySpinner.categoryStringEnglishName = jsonObjectCategory.getString(categorySpinner.getCategoryKeyStringEnglishName());
                                    // set data inside array list
                                    integerStringCategoryMap.put(categorySpinner.categoryStringEnglishName, categorySpinner.categoryId);


                                    spinnerArrayCategory.add(categorySpinner.categoryStringEnglishName);

                                } else {
                                    // put data inside int and strings
                                    categorySpinner.categoryId = jsonObjectCategory.getInt(categorySpinner.getCategoryKeyId());
                                    categorySpinner.categoryStringArabicName = jsonObjectCategory.getString(categorySpinner.getCategoryKeyStringArabicName());
                                    // set data inside array list
                                    integerStringCategoryMap.put(categorySpinner.categoryStringArabicName, categorySpinner.categoryId);

                                    spinnerArrayCategory.add(categorySpinner.categoryStringArabicName);

                                }

                                /*// put data inside int and strings
                                categorySpinner.categoryId = jsonObjectCategory.getInt(categorySpinner.getCategoryKeyId());
                                categorySpinner.categoryStringArabicName = jsonObjectCategory.getString(categorySpinner.getCategoryKeyStringArabicName());
                                categorySpinner.categoryStringEnglishName = jsonObjectCategory.getString(categorySpinner.getCategoryKeyStringEnglishName());*/

                                /*idCategory = categorySpinner.categoryId;*/

                                // set data inside array list
                                /*integerStringCategoryMap.put(categorySpinner.categoryStringEnglishName, categorySpinner.categoryId);


                                spinnerArrayCategory.add(categorySpinner.categoryStringEnglishName);*/

                            }


                            for(Map.Entry m:integerStringCategoryMap.entrySet()){
                                System.out.println(m.getKey()+" "+m.getValue());
                            }


                            // TODO 1 Spinner Category
                            adapterSpinCategories = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, spinnerArrayCategory);

                            adapterSpinCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCategories = (Spinner) findViewById(R.id.spin_all_categories);
                            spinCategories.setAdapter(adapterSpinCategories);
                            spinCategories.setDropDownWidth(525);

                            Log.e("ArrayCategories", spinnerArrayCategory.toString());

                            // TODO 2 Spinner Cities
                            adapterSpinCities = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, spinnerArrayCities);

                            adapterSpinCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCities = (Spinner) findViewById(R.id.spin_all_cities);
                            spinCities.setAdapter(adapterSpinCities);
                            spinCities.setSelection(0);
                            spinCities.setDropDownWidth(525);
                            /*if (!compareValue.equals(null)) {
                                int spinnerPosition = adapter.getPosition(compareValue);
                                mSpinner.setSelection(spinnerPosition);
                            }*/

                            integerStringSpecialistsMap.put(getResources().getString(R.string.Specialists),0);
                            spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));

                            integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                            spinnerArrayAreas.add(getResources().getString(R.string.Areas));

                            Log.e("ArrayCities",spinnerArrayCities.toString());

                            Log.d("idCategory", "idCategory = "+idCategory);
                            Log.d("idCity", "idCity = "+idCity);

                            integerStringSpecialistsMap.put(getResources().getString(R.string.Specialists),0);
                            spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));

                            integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                            spinnerArrayAreas.add(getResources().getString(R.string.Areas));


                            requestQueue.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        };

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                requestQueue.stop();

            }
        });

        MySingletonhus.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void sendCategoryId() {

        final int intSpinCategory = integerStringCategoryMap.get(spinCategories.getSelectedItem());

        final RequestQueue requestQueue = Volley.newRequestQueue(SuggestDoctor.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_CATEGORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("categoryResponse",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            try{
                                /*integerStringSpecialistsMap.put("Select Specialist",0);
                                spinnerArraySpecialists.add("Select Specialist");*/

                                for(int i = 0; i < jsonArray.length(); i++){

                                    // read data from json array of region
                                    JSONObject jsonObjectSpecialists = jsonArray.getJSONObject(i);

                                    if(sessionLanguage.equals("en")){
                                        // put data inside int and strings
                                        specialistsSpinner.specialistId = jsonObjectSpecialists.getInt( specialistsSpinner.getSpecialistKeyId());
                                        specialistsSpinner.specialistStringEnglishName = jsonObjectSpecialists.getString( specialistsSpinner.getSpecialistKeyStringEnglishName());

                                        // set data inside array list
                                        integerStringSpecialistsMap.put(specialistsSpinner.specialistStringEnglishName, specialistsSpinner.specialistId);

                                        spinnerArraySpecialists.add(specialistsSpinner.specialistStringEnglishName);

                                    } else {
                                        // put data inside int and strings
                                        specialistsSpinner.specialistId = jsonObjectSpecialists.getInt( specialistsSpinner.getSpecialistKeyId());
                                        specialistsSpinner.specialistStringArabicName = jsonObjectSpecialists.getString( specialistsSpinner.getSpecialistKeyStringArabicName());

                                        // set data inside array list
                                        integerStringSpecialistsMap.put(specialistsSpinner.specialistStringArabicName, specialistsSpinner.specialistId);

                                        spinnerArraySpecialists.add(specialistsSpinner.specialistStringArabicName);
                                    }

                                    /*// put data inside int and strings
                                    specialistsSpinner.specialistId = jsonObjectSpecialists.getInt( specialistsSpinner.getSpecialistKeyId());
                                    specialistsSpinner.specialistStringArabicName = jsonObjectSpecialists.getString( specialistsSpinner.getSpecialistKeyStringArabicName());
                                    specialistsSpinner.specialistStringEnglishName = jsonObjectSpecialists.getString( specialistsSpinner.getSpecialistKeyStringEnglishName());

                                    // set data inside array list
                                    integerStringSpecialistsMap.put(specialistsSpinner.specialistStringEnglishName, specialistsSpinner.specialistId);

                                    spinnerArraySpecialists.add(specialistsSpinner.specialistStringEnglishName);*/

                                }

                                // TODO 1 Spinner Specialist
                                adapterSpinSpecialists = new ArrayAdapter<String>(getBaseContext(),
                                        android.R.layout.simple_spinner_item, spinnerArraySpecialists);

                                adapterSpinSpecialists.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinSpecialists = (Spinner) findViewById(R.id.spin_all_specialists);
                                spinSpecialists.setAdapter(adapterSpinSpecialists);
                                spinSpecialists.setSelection(0);
                                spinSpecialists.setDropDownWidth(525);

                                Log.e("ArraySpecialists",spinnerArraySpecialists.toString());

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                            Log.d("idSpecialist", "id Specialist= "+idSpecialists);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error Category", error+"");
                requestQueue.stop();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_SPIN_CATE, intSpinCategory + ""); //intSpinCategory +
                return params;
            }
        };
        MySingletonhus.getInstance(SuggestDoctor.this).addToRequestQueue(stringRequest);
    }

    private void sendRegionId(){

        final int intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CitySpinner Response",response);
                        //Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();
                        /*Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();*/
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            try{
                                // Loop through the array elements


                                for(int i = 0; i < jsonArray.length(); i++){

                                    // read data from json array of region
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

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

                                    /*// put data inside int and strings
                                    areaSpinner.areaId = jsonObject.getInt(areaSpinner.getAreaKeyId());
                                    areaSpinner.areaStringArabicName = jsonObject.getString( areaSpinner.getAreaKeyStringArabicName());
                                    areaSpinner.areaStringEnglishName = jsonObject.getString( areaSpinner.getAreaKeyStringEnglishName());

                                    // set data inside array list
                                    integerStringAreasMap.put(areaSpinner.areaStringEnglishName, areaSpinner.areaId);


                                    spinnerArrayAreas.add(areaSpinner.areaStringEnglishName);*/

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
                params.put(KEY_SPIN_CITY, intSpinCity+"");
                return params;
            }
        };
        MySingletonhus.getInstance(SuggestDoctor.this).addToRequestQueue(stringRequest);

    }

    private void sendAllItems(){

        final int intSpinCategory = integerStringCategoryMap.get(spinCategories.getSelectedItem());
        final int intSpinSpecialist = integerStringSpecialistsMap.get(spinSpecialists.getSelectedItem());
        final int intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());
        final int intSpinArea = integerStringAreasMap.get(spinAreas.getSelectedItem());
        final String strName = etName.getText().toString().trim();
        final String strPhone = etPhone.getText().toString().trim();
        final String strAddress = etAddress.getText().toString().trim();




        final RequestQueue requestQueue = Volley.newRequestQueue(SuggestDoctor.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_SUGGEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String successMessage = "تم أرسال طلبك ";
                            String success = jsonObject.getString("msg");
                            if (sessionLanguage.equals("en")) {
                                Toast.makeText(getBaseContext(), "Thanks for your Suggestion", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(), success, Toast.LENGTH_LONG).show();
                            }

                            /*if(successMessage == success){
                            } else {

                            }*/

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
                Log.i("intSpinCategory",intSpinCategory+""); //1
                Log.i("intSpinCity",intSpinCity+"");//2
                Log.i("intSpinSpecialist",intSpinSpecialist+"");//0
                Log.i("intSpinArea",intSpinArea+"");//0

                params.put(KEY_SPIN_CATE, String.valueOf(intSpinCategory)); //"1"
                params.put(KEY_SPIN_CITY, String.valueOf(intSpinCity)); //
                params.put(KEY_SPIN_SPEC, String.valueOf(intSpinSpecialist)); // +
                params.put(KEY_SPIN_AREA, String.valueOf(intSpinArea)); //
                params.put(KEY_NAME, strName);
                params.put(KEY_PHONE, strPhone);
                params.put(KEY_ADDRESS, strAddress);

                return params;
            }
        };

        MySingletonhus.getInstance(SuggestDoctor.this).addToRequestQueue(stringRequest);

    }

}
