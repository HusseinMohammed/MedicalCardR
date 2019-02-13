package com.hyper.design.medicalcard.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyper.design.medicalcard.ContentReviews;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.PushNotification.MySingleton;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.TabHost.ViewPagerAdapter;
import com.hyper.design.medicalcard.User.User;
import com.hyper.design.medicalcard.User.UserLocalStore;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.KeyDoctorContacts;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.DoctorProfileClass;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.jsonParserDoctorProfile;
import com.hyper.design.medicalcard.network_hus.MySingletonhus;
import com.hyper.design.medicalcard.tabbed.CustomAdapter;
import com.hyper.design.medicalcard.tabbed.Profile;
import com.hyper.design.medicalcard.tabbed.Review;
import com.hyper.design.medicalcard.tabbed.RowItems;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyper.design.medicalcard.AppConstant.IMAGE_URL_VARIABLE;
import static com.hyper.design.medicalcard.AppConstant.SERVER_URL;

public class DoctorProfile extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;

    Toolbar toolbar;
    //TabLayout tabLayout;
    //ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;


    ImageView ivDoctorProfile;
    RatingBar ratingBar;
    TextView  tvDoctorProfession, tvDiscountPercentage, //tvDoctorName,
              tvFees, tvDoctorLocation;
    //Update 31/12
    TextView tvDoctorName, tvDoctorCategorySection;
    TextView tvDoctorSpecialist, tvDiscount, tvLocation,
            tvDoctorPhone, tvDoctorSchedule, tvDoctorService;
    ListView listViewReviews;
    ProgressBar mProgressBar;

    TabHost tabHost;
    TabHost.TabSpec tabSpecProfile;
    TabHost.TabSpec tabSpecReview;

    DoctorContacts doctorContacts;
    public ArrayList<DoctorProfileClass> doctorProfileClasses;
    public ArrayList<DoctorProfile> doctorProfiles;
    String strSpinCategory, strSpinSpecialist;

    private Context context = null;

    private String imageUrlStatic = "uploads/item/resize200/"
            , imageUrlVariable = IMAGE_URL_VARIABLE; //http://192.168.1.4/medicalcard/

    //Update 31/12 send id of item and receive doctor profile data
    public static final String SERVER_MEDICAL_URL = SERVER_URL; //local host = http://192.168.1.4/medicalcard/  //Server = http://hyper-design.com/newmedicalcard/
    public static final String SERVER_ITEM_URL= "/item";
    public static final String SERVER_REVIEW_URL= "/review";
    public static final String PUT_NOTIFICATION_URL= "/token";

    public static final String KEY_ITEM_ID = "id";
    /*jsonParserDoctorProfile jsonParserDoctorProfile;*/
    public static int id;
    public String idToken, restoreData;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    jsonParserDoctorProfile jsonParserDoctorProfile;

    DoctorProfileClass doctorProfileClass;

    String[] accountNames, accountComments;
    List<RowItems> rowItems;
    ListView listView;
    RowItems rowItemsClass;

    List<ContentReviews> list;
    public int idCheck;

    public String dataStr;

    //Define Language Variables
    SharedPreferences settings;

    public String localLanguage;

    private SessionManager session;

    public String sessionLanguage;

    UserLocalStore userLocalStore;

    View layoutAddReview;

    private RatingBar addRate;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private EditText addReview;
    private Button addRateReview;


    private String review, itemId, userId, numOfStars;

    // Shared Preferences
    SharedPreferences pref;

    public DoctorProfile(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile);

        mLoginFormView = findViewById(R.id.doctor_profile_form);
        mProgressView = findViewById(R.id.main_progress);

        doctorContacts = new DoctorContacts();
        doctorProfiles = new ArrayList<>();

        settings = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        localLanguage = settings.getString("locale","");

        //localLanguage = "en";
        //Log.i("localLanguage",localLanguage);
        System.out.println("localLanguage" + localLanguage);

        // session manager
        session = new SessionManager(getApplicationContext());

        sessionLanguage = session.isLanguageIn();
        Log.e("sessionLanguagemmm", sessionLanguage);

        userLocalStore = new UserLocalStore(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        jsonParserDoctorProfile = new jsonParserDoctorProfile();
        doctorProfileClass = new DoctorProfileClass();

        listView = (ListView) findViewById(R.id.lv_reviews);
        list = new ArrayList<ContentReviews>();
        rowItemsClass = new RowItems();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.th_profile_reviews);

        //mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            return;
        }

        dataStr = bundle.getString("dataStr");
        //Log.i("dataStr", dataStr);

        Intent intent = getIntent();
        strSpinCategory = intent.getStringExtra("spinCategory");
        strSpinSpecialist = intent.getStringExtra("spinSpecialist");
        //Log.i("dataStr", strSpinSpecialist);
        //Log.i("dataStr", strSpinCategory);


        //Update 31/12 Doctor Profile
        ivDoctorProfile = (ImageView) findViewById(R.id.iv_doctor_profile_image_image);
        tvDoctorName = (TextView) findViewById(R.id.tv_doctor_name);
        tvDoctorCategorySection = (TextView) findViewById(R.id.tv_doctor_category_section);
        ratingBar = (RatingBar) findViewById(R.id.rating);

        //Update 31/12 Tab Profile
        tvDoctorSpecialist = (TextView) findViewById(R.id.tv_specialist);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvDoctorPhone = (TextView) findViewById(R.id.tv_doctor_phone);
        tvDoctorSchedule = (TextView) findViewById(R.id.tv_doctor_schedule);
        tvDoctorService = (TextView) findViewById(R.id.tv_doctor_service);

        //getDoctorProfileToken();
        idToken = intent.getStringExtra(KeyDoctorContacts.idKey);
        Log.i("id item",id + "");

        idToken = intent.getStringExtra("id");
        Log.i("idToken", idToken);
        getDoctorProfileData();

        /*Bundle bundleFragment = new Bundle();
        bundleFragment.putString("prof", prof);
        bundleFragment.putString("disc", disc);
        bundleFragment.putString("spec", spec);
        bundleFragment.putString("loc", loc);
        bundleFragment.putString("phone", phone);*/

        /*bundleFragment.putString("service", spec);
        bundleFragment.putString("loc", loc);*/
        // set Fragment class(Profile) Arguments
        /*Profile profile = new Profile();
        profile.setArguments(bundleFragment);*/

        tabHost = (TabHost) findViewById(R.id.th_profile_reviews);
        tabHost.setup();

        tabSpecProfile = tabHost.newTabSpec("Profile");
        tabSpecProfile.setContent(R.id.Profile);
        tabSpecProfile.setIndicator("Profile");

        tabSpecReview = tabHost.newTabSpec("Reviews");
        tabSpecReview.setContent(R.id.Reviews);
        tabSpecReview.setIndicator("Reviews");

        tabHost.addTab(tabSpecProfile);
        tabHost.addTab(tabSpecReview);

        /*String url = intent.getStringExtra(KeyDoctorContacts.imageKey);

        if(url.equals("")){
            ivDoctorProfile.setImageResource(R.drawable.doctor_image);
        } else {
            Picasso.with(context).load(imageUrlVariable + imageUrlStatic + url).into(ivDoctorProfile);//
        }*/


        tvDoctorName.setText(intent.getStringExtra(KeyDoctorContacts.nameEngKey));
        tvDoctorSpecialist.setText(intent.getStringExtra(KeyDoctorContacts.specKey));
        tvDiscount.setText(intent.getStringExtra(KeyDoctorContacts.discKey));
        tvLocation.setText(intent.getStringExtra(KeyDoctorContacts.locKeyRegion));
        tvDoctorPhone.setText(intent.getStringExtra(KeyDoctorContacts.phoneKey));
        tvDoctorCategorySection.setText(intent.getStringExtra(KeyDoctorContacts.categoryKey));
        Log.d("rateInt", intent.getIntExtra(KeyDoctorContacts.rateKey, 0) + "");
        ratingBar.setRating(intent.getIntExtra(KeyDoctorContacts.rateKey, 0));


        itemId = intent.getStringExtra(KeyDoctorContacts.idKey);

        layoutAddReview = (View) findViewById(R.id.layout_add_view);

        //addRate = (RatingBar) findViewById(R.id.addRate);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.rb1:
                        numOfStars = rb1.getText().toString();
                        Log.i("1", rb1.getText().toString());
                        break;
                    case R.id.rb2:
                        numOfStars = rb2.getText().toString();
                        Log.i("1", rb2.getText().toString());
                        break;
                    case R.id.rb3:
                        numOfStars = rb3.getText().toString();
                        Log.i("1", rb3.getText().toString());
                        break;
                    case R.id.rb4:
                        numOfStars = rb4.getText().toString();
                        Log.i("1", rb4.getText().toString());
                        break;
                    case R.id.rb5:
                        numOfStars = rb5.getText().toString();
                        Log.i("1", rb5.getText().toString());
                        break;
                    default:
                }
            }
        });

        addReview = (EditText) findViewById(R.id.addReview);
        addRateReview = (Button) findViewById(R.id.add);

        addRateReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /*addRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfStars = ratingBar.getNumStars() + "";
                Log.i("Stars", numOfStars);
            }
        });
        addRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // TODO perform your action here
                    numOfStars = ratingBar.getNumStars() + "";
                    Log.i("Stars", numOfStars);
                }
                return false;
            }
        });

        addRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                numOfStars = ratingBar.getNumStars() + "";
                Log.i("Stars", numOfStars);
            }
        });*/




        if(session.isLoggedIn()){
            layoutAddReview.setVisibility(View.VISIBLE);
            getUserData();
            //addRateAndReview();
            addRateReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addRateAndReview();
                }
            });
        }

    }

    private void getUserData(){
        User userData = userLocalStore.getUserLoggedInData();

        userId = userData.userId + "";
        Log.d("USER ID Doctor", userId + "");
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

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(DoctorProfile.this, RecyclerViewOfDoctors.class);
            intent.putExtra("dataStr",dataStr);
            intent.putExtra("spinCategory",strSpinCategory);
            intent.putExtra("spinSpecialist",strSpinSpecialist);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Profile profile = new Profile();
                    return profile;
                case 1:
                    Review review = new Review();
                    return review;
            }
            /*// getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);*/
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    public void getDoctorProfileToken(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_URL + PUT_NOTIFICATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.mGetInstance(DoctorProfile.this).addToRequestQueue(stringRequest);
    }

    public void getDoctorProfileData(){
        showProgress(true);

        /*JSONObject jObject=new JSONObject();
        try {
            jObject.put(KEY_ITEM_ID, id); //"1"
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        final String intSpinItem = idToken;

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_ITEM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String stringResponse = response;

                        Log.e("CitySpinner Response",response);
                        //Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(stringResponse);

                            if(doctorProfileClasses == null){

                            }

                            jsonParserDoctorProfile = new jsonParserDoctorProfile();

                            doctorProfileClasses = jsonParserDoctorProfile.jsonProcess(jsonObject);

                            for(int i = 0; i < doctorProfileClasses.size(); i++){
                                System.out.println(doctorProfileClasses.get(i).getDoctorProfileService());
                            }

                            String urlImage = jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileStringImage();
                            System.out.println("URL " +  urlImage);
                            //Log.i("string_url",jsonParserDoctorProfile.getDoctorContacts().getDoctorStringImage() + "");

                            StringToBitMap(imageUrlVariable + imageUrlStatic + jsonParserDoctorProfile.getDoctorContacts().getDoctorStringImage());

                            if(urlImage.equals("")) {
                                ivDoctorProfile.setImageResource(R.drawable.doctor_image);
                            } else {
                                Picasso.with(context).load(imageUrlVariable + imageUrlStatic + urlImage).into(ivDoctorProfile);
                            }

                            /*if(jsonParserDoctorProfile.getDoctorContacts().getDoctorName() == ""){

                            }*/

                            System.out.println("doctor name " + jsonParserDoctorProfile.getDoctorContacts().getDoctorName());

                            /*tvDoctorName.setText(jsonParserDoctorProfile.getDoctorContacts().getDoctorName());
                            tvDoctorSpecialist.setText(jsonParserDoctorProfile.getDoctorContacts().getDoctorSpecialist());
                            tvDiscount.setText(jsonParserDoctorProfile.getDoctorContacts().getDiscountPercentage());
                            tvLocation.setText(jsonParserDoctorProfile.getDoctorContacts().getDoctorLocation());
                            tvDoctorPhone.setText(jsonParserDoctorProfile.getDoctorContacts().getDoctorPhone());*/

                            /*tvDoctorName.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileName());
                            tvDoctorSpecialist.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileSpecialist());
                            tvDiscount.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileService());
                            tvLocation.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileLocation());
                            tvDoctorPhone.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfilePhone());*/



                            tvDoctorSchedule.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileSchedule());
                            tvDoctorService.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileService());


                            rowItems = new ArrayList<RowItems>();

                            /*accountNames = getResources().getStringArray(R.array.account_name);

                            accountComments = getResources().getStringArray(R.array.comments);

                            for (int i = 0; i < accountNames.length; i++){
                                RowItems items = new RowItems(accountNames[i], accountComments[i]);
                                rowItems.add(items);
                            }*/

                            listView = (ListView) findViewById(R.id.lv_reviews);

                            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), jsonParserDoctorProfile.contentReviewsList());
                            listView.setAdapter(customAdapter);
                            showProgress(false);

                            /*listView.setOnItemClickListener(this);*/

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
                Log.i("intSpinCategory",intSpinItem+""); //1
                params.put(KEY_ITEM_ID, intSpinItem); //"1"
                return params;
            }
        };

        MySingletonhus.getInstance(DoctorProfile.this).addToRequestQueue(stringRequest);

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SERVER_URL + SERVER_ITEM_URL, jObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("CitySpinner Response",jsonObject.toString());
                        Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();
                        *//*stringResponse = response;*//*


                        jsonParserDoctorProfile = new jsonParserDoctorProfile();
                        jsonParserDoctorProfile.jsonProcess(jsonObject);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        //queue.add(jsonArrayRequest);
        MySingletonhus.getInstance(DoctorProfile.this).addToRequestQueue(jsonObjectRequest);*/
    }

    private void addRateAndReview(){
        final String itemIds = itemId;
        final String userIds = userId;
        final String userRate = numOfStars;
        final String userReview = addReview.getText().toString();
        Log.d("rate",itemIds + userIds );

        RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVER_MEDICAL_URL + SERVER_REVIEW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String stringResponse = response;

                        Log.e("CitySpinner Response",response);
                        //Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(stringResponse);

                            String message = jsonObject.getString("msg");
                            Log.i("msg", message);
                            if(message.equals("تم أرسال تقييمك بنجاح")){
                                rb1.setChecked(true);
                                addReview.setText("");
                            }

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
                params.put("item_id", itemIds); //"1"
                params.put("user_id", userIds); //"1"
                params.put("comment", userReview); //"1"
                params.put("rate", userRate); //"1"
                return params;
            }
        };

        MySingletonhus.getInstance(DoctorProfile.this).addToRequestQueue(stringRequest);
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}

/*//Update 31/12 Tab Review
        listViewReviews = (ListView) findViewById(R.id.lv_reviews);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_list_reviews);*/

       /* Bundle bundle = recyclerViewOfDoctors.getIntent().getExtras();

        strSpinCategory = bundle.getString("spinCategory");
        //System.out.println(strSpinCategory);
        strSpinSpecialist = bundle.getString("spinSpecialist");

        tvDoctorCategorySection.setText(strSpinCategory + " - " + strSpinSpecialist);*/


        /*String url = intent.getStringExtra(KeyDoctorContacts.imageKey);
        Log.i("url", url);
        if(url == ""){
            ivDoctorProfile.setImageResource(R.drawable.doctor_profile);
        } else {
            Picasso.with(getApplicationContext()).load(imageUrlVariable + imageUrlStatic + url).into(ivDoctorProfile);
        }
        //Picasso.with(getApplicationContext()).load(imageUrlVariable + imageUrlStatic + intent.getStringExtra(KeyDoctorContacts.imageKey)).into(ivDoctorProfile);
        */


        /*Log.i("id item",id + "");
        String name = intent.getStringExtra(KeyDoctorContacts.nameEngKey);
        tvDoctorName.setText(name);
        String prof = intent.getStringExtra(KeyDoctorContacts.profKey);
        //tvDoctorProfession.setText(prof);
        String disc = intent.getStringExtra(KeyDoctorContacts.discKey);
        //tvDiscountPercentage.setText(disc);
        String spec = intent.getStringExtra(KeyDoctorContacts.specKey);
        //tvDoctorSpecialist.setText(spec);
        tvFees.setText("100");
        String loc = intent.getStringExtra(KeyDoctorContacts.locKeyRegion)
                    +intent.getStringExtra(KeyDoctorContacts.addressEngKey);
        //tvDoctorLocation.setText(loc);
        String phone = intent.getStringExtra(KeyDoctorContacts.phoneKey);
        //tvDoctorPhone.setText(phone);*/

/*tvDoctorProfession.setText("Dentist");
        tvDiscount.setText("Discount :");
        tvDiscountPercentage.setText("25%");*/
        /*tvDoctorSpecialist.setText("Dentist Whightnen");
        tvFees.setText("50EGP");
        tvDoctorLocation.setText("Gamal Abd El Nasser");
        tvDoctorPhone.setText("01028203313");*/




        /*//Lost and Found Tab Host
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        *//**
 *Set an Adapter for the View Pager
 *//*

        viewPager.setAdapter(new TabHostAdapter(getSupportFragmentManager())); //getChildFragmentManager()

        *//**
 * Now , this is a workaround ,
 * The setupWithViewPager dose't works without the runnable .
 * Maybe a Support Library Bug .
 *//*

        tabLayout.post(new Runnable() {
                           @Override
                           public void run() {
                               tabLayout.setupWithViewPager(viewPager);
                           }
                       }
        );*/

/*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    TabDoctorProfile tabDoctorProfile = new TabDoctorProfile();
                    return tabDoctorProfile;
                case 1:
                    TabReviews tabReviews = new TabReviews();
                    return tabReviews;
                default:
                    return null;
            }
            */
/*//*
/ getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);*//*

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "Profile";
                case 1:
                    return "Reviews";
            }
            return null;
        }
    }
*/


/*tvDoctorSpecialist.setText(spec);
        tvDiscount.setText(disc);
        tvLocation.setText(loc);
        tvDoctorPhone.setText(phone);*/

        /*tvDoctorSchedule.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileSchedule());
        tvDoctorService.setText(jsonParserDoctorProfile.getDoctorContactsArrayList().getDoctorProfileService());*/

        /*tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#1D976C"));
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#1D976C"));*/

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new TabDoctorProfile(), "Profile");
        viewPagerAdapter.addFragments(new TabReviews(), "Reviews");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);*/

// Create the adapter that will return a fragment for each of the three
// primary sections of the activity.
        /*mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));*/