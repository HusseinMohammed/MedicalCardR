package com.hyper.design.medicalcard;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hyper.design.medicalcard.Activities.AboutMe;
import com.hyper.design.medicalcard.Activities.BlogActivity;
import com.hyper.design.medicalcard.Activities.Settings;
import com.hyper.design.medicalcard.Activities.BuyCard;
import com.hyper.design.medicalcard.Activities.ContactUs;
import com.hyper.design.medicalcard.Activities.SuggestDoctor;
import com.hyper.design.medicalcard.AlertDialogManager.AlertDialogManager;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.Helper.SQLiteHandler;
import com.hyper.design.medicalcard.User.User;
import com.hyper.design.medicalcard.User.UserLocalStore;
import com.hyper.design.medicalcard.checkNetwork.ConnectivityReceiver;
import com.hyper.design.medicalcard.checkNetwork.MyApplication;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.Activities.LoginActivity;
import com.hyper.design.medicalcard.Activities.RecyclerViewOfDoctors;
import com.hyper.design.medicalcard.dataProcess.jsonParser;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;
import com.hyper.design.medicalcard.spinnersData.AreaSpinner;
import com.hyper.design.medicalcard.spinnersData.CategorySpinner;
import com.hyper.design.medicalcard.spinnersData.CitySpinner;
import com.hyper.design.medicalcard.spinnersData.SpecialistsSpinner;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private Spinner category_Spinner;
    private Spinner specialist_Spinner;

    private ArrayAdapter<Category> categoriesArrayAdapter;
    private ArrayAdapter<Specialist> specialistArrayAdapter;

    private ArrayList<Category> categories;
    private ArrayList<Specialist> specialists;

    public static final String SERVER_MEDICAL_URL = SERVER_URL; //local host = http://192.168.1.4/medicalcard/  //Server = http://hyper-design.com/newmedicalcard/api
    public static final String PARAMETER_URL ="?token=";
    public static final String PARAMETER = FirebaseInstanceId.getInstance().getToken();
    public static final String SERVER_CATEGORY_URL= "/category";
    public static final String SERVER_CITY_URL= "/region";
    public static final String SERVER_SEARCH_URL= "/search";

    public static final String KEY_SPINCATEG = "category";
    public static final String KEY_SPINCITY = "region"; // error "city" to "region"
    public static final String KEY_SPINSPEC = "section";
    public static final String KEY_SPINAREA = "area";
    public static final String KEY_DOCTORNAME = "name";
    private static final String ACCESS_TOKEN = "";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    //private com.google.api.services.gmail.Gmail mService = null;
    private Exception mLastError = null;


    Spinner spinCategories, spinSpecialists, spinCities, spinAreas;
    ArrayAdapter<String> adapterSpinCategories, adapterSpinSpecialists, adapterSpinCities, adapterSpinAreas;

    EditText etDoctorName;
    Button btnSearchForDoctor;


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

    //Context context = this.getBaseContext();

    CategorySpinner categorySpinner;          CitySpinner citySpinner;
    SpecialistsSpinner specialistsSpinner;    AreaSpinner areaSpinner;

    ArrayList<String> spinnerArrayCategory, spinnerArraySpecialists, spinnerArrayCities, spinnerArrayAreas;

    Map<String, Integer> integerStringCategoryMap;
    Map<String, Integer> integerStringCityMap;
    Map<String, Integer> integerStringSpecialistsMap;
    Map<String, Integer> integerStringAreasMap;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Session Manager Class
    //SessionManager session;

    // Button Logout
    private Button btnLogout;

    NavigationView navigationView;

    //Implement
    UserLocalStore userLocalStore;

    SharedPreferences settings;

    public String localLanguage;


    public static final String PREFS_NAME = "MyApp_Settings";

    ImageView ivUser;
    TextView tvUserName, tvUserEmail;

    public String lang;

    private View mProgressView;
    private View mLoginFormView;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    public int i = 0;

    ImageView imageView;

    Boolean existProgress = false;
    AlertDialog.Builder alertDialogBuilder;
    Context context = getBaseContext();

    private TextView txtName;
    private TextView txtEmail;
    //private Button btnLogout;


    private SQLiteHandler db;
    private SessionManager session;

    public boolean successOrFail = false;

    BroadcastReceiver broadcastReceiver;
    String name = "", email = "";
    Intent intent;

    public String sessionLanguage;

    AppConstant appConstant;

    private String empty = null;

    AssetManager am;
    Typeface typeface;

    @Override
    protected void onStart() {
        super.onStart();
        //displayUserDetails();
        /*if (authenticate() == true){
            //hideLoginVisibleLogoutItem();
            //visibleLoginHideLogoutItem();
            //displayUserDetails();
        } else {
            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }*/

        checkConnection();

        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguage", sessionLanguage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*falsssssssssssssssse setContentView(R.layout.nav_header_main);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appConstant = new AppConstant();

        //Add Font
        am = getApplicationContext().getAssets(); //Error not call context.getApplicationContext().getAssets()

        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Cairo-Black.ttf"));

        //setTypeface(typeface);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        /*alertDialogBuilder = new AlertDialog.Builder(
                context);*/

        // Manually checking internet connection
        //checkConnection();




        // Displaying the user details on the screen
        //txtName.setText(name);
        //txtEmail.setText(email);



        //heartBeat();
        /*intent = getIntent();
        intent.getStringExtra("email");
        intent.getStringExtra("name");*/

        imageView = (ImageView) findViewById(R.id.iv_medical_card_logo);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        ivUser = (ImageView) findViewById(R.id.iv_user_image);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserEmail = (TextView) findViewById(R.id.tv_user_email);


        spinCategories = (Spinner) findViewById(R.id.spin_all_categories);
        spinSpecialists = (Spinner) findViewById(R.id.spin_all_specialists);
        spinCities = (Spinner) findViewById(R.id.spin_all_cities);
        spinAreas = (Spinner) findViewById(R.id.spin_all_areas);
        etDoctorName =(EditText) findViewById(R.id.et_doctor_name);
        btnSearchForDoctor = (Button) findViewById(R.id.btn_search_for_doctor);
        userLocalStore = new UserLocalStore(this);
        jsonParser = new jsonParser(this);

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

        ivUser = (ImageView) header.findViewById(R.id.iv_user_image);
        txtName = (TextView) header.findViewById(R.id.tv_user_name);
        txtEmail = (TextView) header.findViewById(R.id.tv_user_email);
        //btnLogout = (Button) findViewById(R.id.btnLogout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_sign_out); //.setVisible(true)



        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        /*if (!session.isLoggedIn()) {

        }*/

        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguage", sessionLanguage);

        /*if(sessionLanguage == "ar"){
            *//*HashMap<String, String> language = db.getLanguageDetails();
            sessionLanguage = language.get("languageName");*//*
        }*/


        Log.e("sessionLanguageDB", sessionLanguage);

        Log.e("sessionLogin", String.valueOf(session.isLoggedIn()));
        if(session.isLoggedIn()){
            //logoutUser();
            hideLoginVisibleLogoutItem();
            Log.e("authenticate", "Success authenticate");
            //nav_Menu.findItem(R.id.nav_sign_out).setVisible(true); //.setVisible(true)
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();

            String name = user.get("name");
            String email = user.get("email");

            User userData = userLocalStore.getUserLoggedInData();

            int userId = userData.userId;
            Log.d("USER ID", userId + "");
            // Displaying the user details on the screen
            txtName.setText(name);
            txtEmail.setText(email);
        }



        /*else if(!session.isLoggedIn()){
            Log.e("isLoggedIn", "Success isLoggedIn false");
            logoutUser();
        } else {

        }*/

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        localLanguage = settings.getString("locale","");
        //localLanguage = "en";
        //Log.i("localLanguage",localLanguage);
        System.out.println("localLanguage" + localLanguage);

        /*lang = getPersistedData(this, Locale.getDefault().getLanguage());
        setLocale(this, lang);

        getLanguage(this);


        LocaleHelper.onCreate(this, "en");*/

        //change language
        /*if (settings.getString("locale",null).equals("en")) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            settings.edit().putString("locale", "en").commit();
        }*/


        mProgressBar= (ProgressBar) findViewById(R.id.main_progress);


        //btnSearchForDoctor.setEnabled(true);
        btnSearchForDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Log.i("intCategory",);
                if(spinCategories.getSelectedItem().equals("Choose a Category") || spinCategories.getSelectedItem().equals("اختر الفئة")){
                    if(sessionLanguage.equals("en")){
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.category_required), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.category_required), Toast.LENGTH_LONG).show();
                    }
                } else {
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginFormView.setVisibility(View.INVISIBLE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*/
                    Log.d("sss", sendAllItems());
                    sendAllItems();
                    Log.d("sss", sendAllItems());
                    /*if(sendAllItems().equals("empty")){
                        Toast.makeText(getBaseContext(),  "Not Results Found", Toast.LENGTH_LONG).show();
                    } else {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getApplicationContext(),RecyclerViewOfDoctors.class);
                        //intent.putExtra("arg", getText());
                        startActivity(intent);
                        setContentView(R.layout.activity_recylerview_of_doctors);
                    }*/
                }

            }
        });



        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        //initializeUI();

        mLoginFormView = findViewById(R.id.main_form);
        mProgressView = findViewById(R.id.main_progress);


    }

    private void logoutUser() {
        session.setLogin(false);


        db.deleteUsers();

        visibleLoginHideLogoutItem();

        ivUser.setImageResource(R.drawable.logo);
        txtName.setText(getResources().getString(R.string.medical_card_account_name));
        txtEmail.setText(getResources().getString(R.string.medical_card_account_email));
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //checkConnection();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);

        if(ConnectivityReceiver.checkWifiAndData() == false){
            showProgress(true);
        } else if(ConnectivityReceiver.isConnected() == false){
            //checkConnection();
            showProgress(true);
        } else {
            showProgress(false);
            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // session manager
            session = new SessionManager(getApplicationContext());

            sessionLanguage = session.isLanguageIn();
            Log.e("sessionLanguage", sessionLanguage);

            getRegionAndCategory();

            spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int id, long l) {
                    /*if(spinCategories.getSelectedItem().equals(spinnerArrayCategory.get(i)))
                    {

                    }*/
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginFormView.setVisibility(View.INVISIBLE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }
                                });
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*/


                    //mCountDownTimer.start();


                    sendCategoryId();
                    //btnSearchForDoctor.setEnabled(false);
                    /*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*/
                    //remove the selected item from other adapter
                    Log.i("localLanguage", localLanguage);
                    //Log.i("lang", lang);
                    if (spinnerArraySpecialists.size() > 1) {
                        /*spinSpecialists.setAdapter(null);
                        //spinnerArraySpecialists.clear();
                        spinnerArraySpecialists.subList(5, spinnerArraySpecialists.size());
                        //spinnerArraySpecialists.add("Choose a Specialist");*/
                        if (sessionLanguage.equals("en")) {
                            spinSpecialists.setAdapter(null);
                            spinnerArraySpecialists.clear();
                            spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));
                        } else {
                            spinSpecialists.setAdapter(null);
                            spinnerArraySpecialists.clear();
                            spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));
                        }

                    }

                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //spinSpecialists.setAdapter(null);
                }
            });

            spinCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    System.out.println(existProgress);
                    /*if (existProgress == true) {
                        System.out.println(existProgress);
                        showProgress(true);
                    }*/
                    sendRegionId();
                    //existProgress = true;
                    //btnSearchForDoctor.setEnabled(false);
                /*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*/
                    if (spinnerArrayAreas.size() > 1) {

                        if (sessionLanguage.equals("en")) {
                            spinAreas.setAdapter(null);
                            spinnerArrayAreas.clear();
                            spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                        } else {
                            spinAreas.setAdapter(null);
                            spinnerArrayAreas.clear();
                            spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                        }

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnSearchForDoctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                Log.i("intCategory",);
                    if (spinCategories.getSelectedItem().equals("Choose a Category") || spinCategories.getSelectedItem().equals("اختر الفئة")) {
                        if(sessionLanguage.equals("en")){
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.category_required), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.category_required), Toast.LENGTH_LONG).show();
                        }
                    } else {
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginFormView.setVisibility(View.INVISIBLE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*/
                        sendAllItems();
                        // TODO Auto-generated method stub
                        /*Intent intent = new Intent(getApplicationContext(), RecyclerViewOfDoctors.class);
                        //intent.putExtra("arg", getText());
                        startActivity(intent);
                        setContentView(R.layout.activity_recylerview_of_doctors);*/
                    }

                }
            });
        }
        /*spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinCategories.getSelectedItem().equals(spinnerArrayCategory.get(i)))
                {

                }
                sendCategoryId();
                //btnSearchForDoctor.setEnabled(false);
                Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();
                //remove the selected item from other adapter
                if(spinnerArraySpecialists.size() > 1) {
                    spinSpecialists.setAdapter(null);
                    spinnerArraySpecialists.clear();
                    spinnerArraySpecialists.add("Choose a Specialist");
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
                //btnSearchForDoctor.setEnabled(false);
                Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();
                if(spinnerArrayAreas.size() > 1) {
                    spinAreas.setAdapter(null);
                    spinnerArrayAreas.clear();
                    spinnerArrayAreas.add("Choose a Area");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


        /*btnSearchForDoctor.setEnabled(true);
        btnSearchForDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                Log.i("intCategory",);
                if(spinCategories.getSelectedItem().equals("Choose a Category")){
                    Toast.makeText(getBaseContext(), "Category is required!", Toast.LENGTH_LONG).show();
                }else {
                    sendAllItems();
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getApplicationContext(),RecyclerViewOfDoctors.class);
                    //intent.putExtra("arg", getText());
                    startActivity(intent);
                    setContentView(R.layout.activity_recylerview_of_doctors);
                }

            }
        });*/
        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //heartBeat();

        /*if (settings.getString("locale",null).equals("en")) {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            settings.edit().putString("locale", "en").commit();
        }*/
    }

    public void heartBeat(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        //scaleDown.setDuration(BASE_DURATION * 4);


        scaleDown.start();
    }

    private  boolean authenticate(){
        Intent intent = getIntent();
        Boolean userLoggedIn = intent.getBooleanExtra("userLoggedIn", false);
        return userLoggedIn; //userLocalStore.getUserLoggedIn()
    }

    public void displayUserDetails(){
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserEmail = (TextView) findViewById(R.id.tv_user_email);
        //intent = getIntent();
        //        User user = userLocalStore.getLoggedInUser();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        /*tvUserName.setText(user.username);
        tvUserEmail.setText(user.email);*/
        tvUserName.setText(name);
        tvUserEmail.setText(email);
    }

    private void hideLoginVisibleLogoutItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_sign_in).setVisible(false);

        Menu nav_Menu1 = navigationView.getMenu();
        nav_Menu1.findItem(R.id.nav_sign_out).setVisible(true);
    }

    private void visibleLoginHideLogoutItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu1 = navigationView.getMenu();
        nav_Menu1.findItem(R.id.nav_sign_out).setVisible(false);

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_sign_in).setVisible(true);
    }

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

    //Calls Apis
    private void getRegionAndCategory(){
        showProgress(true);

        //Log.d("parameter", PARAMETER);
        System.out.println(PARAMETER);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_MEDICAL_URL + PARAMETER_URL + PARAMETER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.i("localLanguage", localLanguage);

                            Log.i("response", response);
                            /*Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();*/
                            JSONObject jsonObject = new JSONObject(response); //response
                            JSONArray jsonArrayCity = jsonObject.getJSONArray("region");
                            JSONObject jsonObjectCity = jsonArrayCity.getJSONObject(0);
                            JSONArray jsonArrayCategory = jsonObject.getJSONArray("category");
                            JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(0);

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

                            if(sessionLanguage.equals("en")){
                                integerStringCategoryMap.put(getResources().getString(R.string.categories),0);
                                spinnerArrayCategory.add(getResources().getString(R.string.categories));
                            } else {
                                integerStringCategoryMap.put(getResources().getString(R.string.categories),0);
                                spinnerArrayCategory.add(getResources().getString(R.string.categories));
                            }



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
                            spinCategories.setDropDownWidth(1020);

                            Log.e("ArrayCategories", spinnerArrayCategory.toString());

                            // TODO 2 Spinner Cities
                            adapterSpinCities = new ArrayAdapter<String>(getBaseContext(),
                                    android.R.layout.simple_spinner_item, spinnerArrayCities);

                            adapterSpinCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinCities = (Spinner) findViewById(R.id.spin_all_cities);
                            spinCities.setAdapter(adapterSpinCities);
                            spinCities.setSelection(0);
                            spinCities.setDropDownWidth(1020);
                            /*if (!compareValue.equals(null)) {
                                int spinnerPosition = adapter.getPosition(compareValue);
                                mSpinner.setSelection(spinnerPosition);
                            }*/
                            if(sessionLanguage.equals("en")){
                                integerStringSpecialistsMap.put(getResources().getString(R.string.Specialists),0);
                                spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));
                            } else {
                                integerStringSpecialistsMap.put(getResources().getString(R.string.Specialists),0);
                                spinnerArraySpecialists.add(getResources().getString(R.string.Specialists));
                            }

                            if(sessionLanguage.equals("en")){
                                integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                                spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                            } else {
                                integerStringAreasMap.put(getResources().getString(R.string.Areas), 0);
                                spinnerArrayAreas.add(getResources().getString(R.string.Areas));
                            }




                            Log.e("ArrayCities",spinnerArrayCities.toString());

                            Log.d("idCategory", "idCategory = "+idCategory);
                            Log.d("idCity", "idCity = "+idCity);
                            showProgress(false);


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
        showProgress(true);
        final int intSpinCategory = integerStringCategoryMap.get(spinCategories.getSelectedItem());

        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

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
                                spinSpecialists.setDropDownWidth(1020);


                                Log.e("ArraySpecialists",spinnerArraySpecialists.toString());

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                            Log.d("idSpecialist", "id Specialist= "+idSpecialists);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showProgress(false);
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
                params.put(KEY_SPINCATEG, intSpinCategory + ""); //intSpinCategory +
                return params;
            }
        };
        MySingletonhus.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
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
                                spinAreas.setDropDownWidth(1020);


                                Log.e("ArrayAreas",spinnerArrayAreas.toString());

                            }catch (JSONException e){
                                e.printStackTrace();
                            }


                            Log.d("id Area", "id = "+idAreas);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //requestQueue.stop();
                        showProgress(false);

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
                params.put(KEY_SPINCITY, intSpinCity+"");
                return params;
            }
        };
        MySingletonhus.getInstance(MainActivity.this).addToRequestQueue(stringRequest);

    }

    private String sendAllItems(){

        final int intSpinCategory = integerStringCategoryMap.get(spinCategories.getSelectedItem());
        if(integerStringSpecialistsMap.get(spinSpecialists.getSelectedItem()).equals(getResources().getString(R.string.Specialists))
                || integerStringAreasMap.get(spinAreas.getSelectedItem()).equals(getResources().getString(R.string.Areas))){
            spinSpecialists.setSelection(0);
            spinAreas.setSelection(0);
        }

        final int intSpinSpecialist = integerStringSpecialistsMap.get(spinSpecialists.getSelectedItem());
        final int intSpinCity = integerStringCityMap.get(spinCities.getSelectedItem());
        final int intSpinArea = integerStringAreasMap.get(spinAreas.getSelectedItem());
        final String strDoctorName = etDoctorName.getText().toString().trim();
        final String strSpinCategory = spinCategories.getSelectedItem().toString();
        final String strSpinSpecialist = spinSpecialists.getSelectedItem().toString();


        /*final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL + SERVER_SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("CitySpinner Response",response.toString());
                        Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();
                        stringResponse = response;
                        Log.i("data",stringResponse);


                        jsonParser = new jsonParser();

                        //doctorContactsArrayList = jsonParser.jsonProcess(stringResponse);

                        for(int i = 0; i < doctorContactsArrayList.size(); i++){
                            System.out.println(doctorContactsArrayList.get(i).getDoctorName());
                        }

                        dataStr = new Gson().toJson(doctorContactsArrayList);
                        *//*SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("dataStr", dataStr);
                        editor.commit();*//*

                        Intent intent = new Intent(MainActivity.this, RecyclerViewOfDoctors.class);
                        intent.putExtra("dataStr", dataStr);
                        Log.e("dataStr",dataStr);
                        startActivity(intent);

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

                params.put(KEY_SPINCATEG, String.valueOf(intSpinCategory)); //"1"
                params.put(KEY_SPINCITY, String.valueOf(intSpinCity)); //
                params.put(KEY_SPINSPEC, String.valueOf(intSpinSpecialist)); // +
                params.put(KEY_SPINAREA, String.valueOf(intSpinArea)); //
                params.put(KEY_DOCTORNAME, strDoctorName);
                return params;
            }
        };

        MySingletonhus.getInstance(MainActivity.this).addToRequestQueue(stringRequest);*/

        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_SPINCATEG, intSpinCategory);
            jsonObject.put(KEY_SPINCITY, intSpinCity);
            jsonObject.put(KEY_SPINSPEC, intSpinSpecialist);
            jsonObject.put(KEY_SPINAREA, intSpinArea);
            jsonObject.put(KEY_DOCTORNAME, strDoctorName);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Json Array", jsonArray.toString());
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                SERVER_MEDICAL_URL + SERVER_SEARCH_URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("CitySpinner Response",response.toString());
                        //Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();
                        //stringResponse = response;
                        Log.i("data",response.toString());


                        //jsonParser = new jsonParser(context);

                        doctorContactsArrayList = jsonParser.jsonProcess(response);


                        if (doctorContactsArrayList.isEmpty()){ //doctorContactsArrayList.size() == 0 ||
                            Log.d("empty", doctorContactsArrayList.isEmpty() + "");
                            Toast.makeText(getBaseContext(),  "Not Results Found", Toast.LENGTH_LONG).show();
                            empty = "empty";
                        } else {
                            for(int i = 0; i < doctorContactsArrayList.size(); i++){
                                System.out.println(doctorContactsArrayList.get(i).getDoctorName());
                            }
                            dataStr = new Gson().toJson(doctorContactsArrayList);
                            /*SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("dataStr", dataStr);
                            editor.commit();*/

                            Intent intent = new Intent(MainActivity.this, RecyclerViewOfDoctors.class);
                            intent.putExtra("dataStr", dataStr);
                            intent.putExtra("spinCategory", strSpinCategory);
                            intent.putExtra("spinSpecialist", strSpinSpecialist);

                            Log.e("dataStr",dataStr);
                            Log.e("spinCategory",strSpinCategory + strSpinSpecialist);


                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error CitySpinner", error+"");
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        //requestQueue1.add(jsonArrayRequest);
        MySingletonhus.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

        return null;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Would you like to exist this app?");
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
            session.setLanguage("en");


            //db.deleteLanguage();

            SharedPreferences.Editor spEditor = settings.edit();
            spEditor.clear();
            spEditor.commit();
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*
                getMenuInflater().inflate(R.menu.main, menu);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_search) {
            // Handle the camera action
            /*fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,
                            new SearchForDoctor()).commit();*/
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_blog) {
            // Handle the camera action
            /*fragmentManager.beginTransaction()
                    .replace(R.id.rvBlog,
                            new BlogFragment()).commit();*/
            showProgress(true);
            Intent intent = new Intent(MainActivity.this, BlogActivity.class);
            startActivity(intent);
            finish();
            showProgress(false);
        } else if (id == R.id.nav_buy_card) {
            Intent intent = new Intent(MainActivity.this, BuyCard.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_suggest_doctor) {
            Intent intent = new Intent(MainActivity.this, SuggestDoctor.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(MainActivity.this, AboutMe.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_sign_in) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_sign_out) {
            //visibleLoginHideLogoutItem();
            logoutUser();

            /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();*/
            /*userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);

            //session.logoutUser();
            logoutUser();*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        private void logoutUser() {
            session.setLogin(false);

            db.deleteUsers();

            // Launching the login activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /*public void addTask(Task t) {
        if (null == currentTasks) {
            currentTasks = new ArrayList<task>();
        }
        currentTasks.add(t);

        // save the task list to preference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString(TASKS, ObjectSerializer.serialize(currentTasks));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }*/

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected){
            showSnack(isConnected);
        }
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message = "";
        int color = Color.WHITE;
        if (isConnected) {
            message = getResources().getString(R.string.successCheckNetwork);
            color = Color.WHITE;
        } else {
            message = getResources().getString(R.string.checkNetwork);
            color = Color.WHITE;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);


        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    /*private boolean checkConnection(){

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        //For 3G check
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        //For WiFi Check
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();

        System.out.println(is3g + " net " + isWifi);

        *//*switch (successOrFail)
        {
            case successOrFail:

        }*//*
        if(!isWifi && !is3g) {
            showAlertDialog();
            return isWifi;
        }
        return isWifi;

        *//*if ( && )
        {
            *//**//*try {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError)
                            .getConnectionStatusCode());
                }
            } catch (IOException e){
                e.printStackTrace();
            }*//**//*
            //Toast.makeText(getApplicationContext(),"Please make sure your Network Connection is ON ",Toast.LENGTH_LONG).show();



        }
        else
        {
            //" Your method what you want to do "
            //onCreate();
        }


        *//**//*if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        MainActivity.REQUEST_AUTHORIZATION);
            } else {
                mOutputText.setText("The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            mOutputText.setText("Request cancelled.");
        }*//*
    }*/


    public void showAlertDialog(){
        alertDialogBuilder = new AlertDialog.Builder(
                context);



        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.alert_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.exit_message),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        finish();
                        /*startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));*/
                        //enable wifi
                        //wifiMan.setWifiEnabled(true);
                    }
                })
                /*.setNegativeButton("cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        //disable wifi
                        //wifiMan.setWifiEnabled(false);
                    }
                })*/;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        /*// set title
        alertDialogBuilder.setTitle("Network Settings");*/

        /*// set dialog message
        alertDialogBuilder
                .setMessage("Please Check Network Connection")
                .setCancelable(false)
                .setPositiveButton("Open Wi-Fi",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                        //enable wifi
                        //wifiMan.setWifiEnabled(true);
                    }
                })
                .setPositiveButton("Open Mobile Data",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        *//*Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                        startActivity(intent);*//*
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setComponent(new ComponentName("com.android.settings",
                                "com.android.settings.Settings$DataUsageSummaryActivity"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //disable wifi
                        //wifiMan.setWifiEnabled(false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();*/
    }

    public void showAlertDialogNetwork(){
        alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Network Settings");

        // set dialog message
        alertDialogBuilder
                .setMessage("Please make sure your Network Connection is ON")
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        //enable wifi
                        //wifiMan.setWifiEnabled(true);
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //disable wifi
                        //wifiMan.setWifiEnabled(false);
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void installListener() {

        if (broadcastReceiver == null) {

            broadcastReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();

                    NetworkInfo info = (NetworkInfo) extras
                            .getParcelable("networkInfo");

                    NetworkInfo.State state = info.getState();
                    //Log.d("InternalBroadcastReceiver", info.toString() + " "
                    //        + state.toString());

                    if (state == NetworkInfo.State.CONNECTED) {

                        //onNetworkUp();

                    } else {

                        //onNetworkDown();

                    }

                }
            };

            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    private void initializeUI() {
        category_Spinner = (Spinner) findViewById(R.id.spin_all_categories);
        specialist_Spinner = (Spinner) findViewById(R.id.spin_all_specialists);
        //city_Spinner = (Spinner) findViewById(R.id.SpinnerCountryActivity_city_spinner);

        categories = new ArrayList<>();
        specialists = new ArrayList<>();
        //cities = new ArrayList<>();

        createLists();

        categoriesArrayAdapter = new ArrayAdapter<Category>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categoriesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_Spinner.setAdapter(categoriesArrayAdapter);

        specialistArrayAdapter = new ArrayAdapter<Specialist>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, specialists);
        specialistArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialist_Spinner.setAdapter(specialistArrayAdapter);

        /*cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, cities);
        cityArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        city_Spinner.setAdapter(cityArrayAdapter);*/

        category_Spinner.setOnItemSelectedListener(country_listener);
        specialist_Spinner.setOnItemSelectedListener(state_listener);
//        city_Spinner.setOnItemSelectedListener(city_listener);

    }

    private AdapterView.OnItemSelectedListener country_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Category category = (Category) category_Spinner.getItemAtPosition(position);
                //category.countryName = (String) category_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: country: "+category.getCountryID());
                ArrayList<Specialist> tempSpecialists = new ArrayList<>();

                tempSpecialists.add(new Specialist(0, new Category(0, "Choose a Category"), "Choose a Specialist"));

                for (Specialist singleState : specialists) {
                    if (singleState.getCountry().getCountryID() == category.getCountryID()) {
                        tempSpecialists.add(singleState);
                    }
                }

                specialistArrayAdapter = new ArrayAdapter<Specialist>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tempSpecialists);
                specialistArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                specialist_Spinner.setAdapter(specialistArrayAdapter);
            }

            /*cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, new ArrayList<City>());
            cityArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            city_Spinner.setAdapter(cityArrayAdapter);*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener state_listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                final Specialist state = (Specialist) specialist_Spinner.getItemAtPosition(position);
                Log.d("SpinnerCountry", "onItemSelected: state: "+state.getStateID());
                //ArrayList<City> tempCities = new ArrayList<>();

                Category country = new Category(0, "Choose a Category");
                Specialist firstState = new Specialist(0, country, "Choose a Specialist");
                //tempCities.add(new City(0, country, firstState, "Choose a City"));

                /*for (City singleCity : cities) {
                    if (singleCity.getState().getStateID() == state.getStateID()) {
                        tempCities.add(singleCity);
                    }
                }

                cityArrayAdapter = new ArrayAdapter<City>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, tempCities);
                cityArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                city_Spinner.setAdapter(cityArrayAdapter);*/
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void createLists() {
        Category category0 = new Category(0, "Choose a Category");
        Category category1 = new Category(1, "Category1");
        Category category2 = new Category(2, "Category2");

        categories.add(new Category(0, "Choose a Category"));
        categories.add(new Category(1, "Category1"));
        categories.add(new Category(2, "Category2"));

        Specialist specialist0 = new Specialist(0, category0, "Choose a Specialist");
        Specialist specialist1 = new Specialist(1, category1, "Specialist1");
        Specialist specialist2 = new Specialist(2, category1, "Specialist2");
        Specialist specialist3 = new Specialist(3, category2, "Specialist3");
        Specialist specialist4 = new Specialist(4, category2, "Specialist4");

        specialists.add(specialist0);
        specialists.add(specialist1);
        specialists.add(specialist2);
        specialists.add(specialist3);
        specialists.add(specialist4);

        /*cities.add(new City(0, country0, state0, "Choose a City"));
        cities.add(new City(1, country1, state1, "City1"));
        cities.add(new City(2, country1, state1, "City2"));
        cities.add(new City(3, country1, state2, "City3"));
        cities.add(new City(4, country2, state2, "City4"));
        cities.add(new City(5, country2, state3, "City5"));
        cities.add(new City(6, country2, state3, "City6"));
        cities.add(new City(7, country2, state4, "City7"));
        cities.add(new City(8, country1, state4, "City8"));*/
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private class Category implements Comparable<Category> {

        private int countryID;
        private String countryName;


        public Category(int countryID, String countryName) {
            this.countryID = countryID;
            this.countryName = countryName;
        }

        public int getCountryID() {
            return countryID;
        }

        public String getCountryName() {
            return countryName;
        }

        @Override
        public String toString() {
            return countryName;
        }


        @Override
        public int compareTo(Category another) {
            return this.getCountryID() - another.getCountryID();//ascending order
//            return another.getCountryID()-this.getCountryID();//descending  order
        }
    }

    private class Specialist implements Comparable<Specialist> {

        private int stateID;
        private Category category;
        private String stateName;

        public Specialist(int stateID, Category category, String stateName) {
            this.stateID = stateID;
            this.category = category;
            this.stateName = stateName;
        }

        public int getStateID() {
            return stateID;
        }

        public Category getCountry() {
            return category;
        }

        public String getStateName() {
            return stateName;
        }

        @Override
        public String toString() {
            return stateName;
        }

        @Override
        public int compareTo(Specialist another) {
            return this.getStateID() - another.getStateID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Stop", "on Stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Pause", "on Pause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Restart", "on Restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Destroy", "onDestroy");
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("PostResume", "onPostResume");
    }

}


//mProgressBar.setProgress(i);
/*
        mCountDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                i++;
                mProgressBar.setProgress((int)i*100/(5000/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                i++;
                mProgressBar.setProgress(100);
            }
        };
*/

        /*getRegionAndCategory();

        spinCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int id, long l) {
                    *//*if(spinCategories.getSelectedItem().equals(spinnerArrayCategory.get(i)))
                    {

                    }*//*
                    *//*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoginFormView.setVisibility(View.INVISIBLE);
                                        mProgressBar.setVisibility(View.VISIBLE);
                                    }
                                });
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*//*


                    //mCountDownTimer.start();

                    showProgress(true);
                    sendCategoryId();
                    //btnSearchForDoctor.setEnabled(false);
                    *//*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*//*
                    //remove the selected item from other adapter
                    Log.i("localLanguage", localLanguage);
                    //Log.i("lang", lang);
                    if(spinnerArraySpecialists.size() > 1) {
                        *//*spinSpecialists.setAdapter(null);
                        //spinnerArraySpecialists.clear();
                        spinnerArraySpecialists.subList(5, spinnerArraySpecialists.size());
                        //spinnerArraySpecialists.add("Choose a Specialist");*//*
                        if(localLanguage.equals("en")){
                            spinSpecialists.setAdapter(null);
                            spinnerArraySpecialists.clear();
                            spinnerArraySpecialists.add("Choose an Specialist");
                        } else {
                            spinSpecialists.setAdapter(null);
                            spinnerArraySpecialists.clear();
                            spinnerArraySpecialists.add("اختر التخصص");
                        }

                    }

                    *//*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(mProgressStatus < 100){
                                mProgressStatus +=10;
                                android.os.SystemClock.sleep(50);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(mProgressStatus);
                                    }
                                });
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.GONE);
                                        mLoginFormView.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }
                    }).start();*//*

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
                //btnSearchForDoctor.setEnabled(false);
                *//*Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) +" Selected", Toast.LENGTH_LONG).show();*//*
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/