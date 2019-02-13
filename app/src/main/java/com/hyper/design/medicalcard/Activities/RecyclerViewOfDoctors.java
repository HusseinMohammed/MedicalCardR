package com.hyper.design.medicalcard.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.hyper.design.medicalcard.AdaptersRecyclerView.RecyclerViewOfDoctorsAdapter;
import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.RetroFit.ApiInterface;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.jsonParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewOfDoctors extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewOfDoctorsAdapter recyclerViewOfDoctorsAdapter;
    public ArrayList<DoctorContacts> doctorContactsArrayList;
    private ApiInterface apiInterface;

    public static final String MY_PREFS_NAME = "MyData";
    public static final String DEFAULT = "N/A";
    public SharedPreferences sharedPreferences;
    public String stringResponse;

    DoctorContacts doctorContacts;
    public List<DoctorContacts> doctorContactsList;

    private String doctorIntId;

    private String doctorStringImage;

    private String doctorName;
    private String doctorProfession;
    private String discountPercentage;

    private String doctorSpecialist;
    private int discountPercentage1;
    private String doctorLocation;
    private String doctorPhone, doctorAddress;
    String strSpinCategory, strSpinSpecialist;

    Context context;

    public jsonParser jsonParser;

    private String imageUrlStatic = "uploads/item/resize200/"
            , imageUrlVariable = "http://hyper-design.com/newmedicalcard/";

    public static final String PREFS_NAME = "MyApp_Settings";
    String str = "";//you need to retrieve this string from shared preferences.
    public String dataStr;
    ArrayList<DoctorContacts> restoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recylerview_of_doctors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.doctors_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        doctorContacts = new DoctorContacts();

        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            return;
        }

        dataStr = bundle.getString("dataStr");
        //Log.e("dataStr444",dataStr);
        doctorContacts.setDataStr(dataStr);

        strSpinCategory = bundle.getString("spinCategory");
        //Log.e("spinCategory",strSpinCategory);
        strSpinSpecialist = bundle.getString("spinSpecialist");
        //Log.e("spinSpecialist",strSpinSpecialist);
        Type type = new TypeToken<ArrayList<DoctorContacts>>() {}.getType();

        restoreData = new Gson().fromJson(dataStr, type);
        //Log.e("sssss",restoreData.toString());
        System.out.println(restoreData.get(0).getDoctorName());

        recyclerMain();

        //setRecyclerViewOfDoctor();
        //recyclerMain();




        /*Log.e("doctor",doctorContactsArrayList.toString());*/
        /*recyclerViewOfDoctorsAdapter = new RecyclerViewOfDoctorsAdapter(doctorContactsArrayList);
        recyclerView.setAdapter(recyclerViewOfDoctorsAdapter);
        recyclerViewOfDoctorsAdapter.notifyDataSetChanged();*/

        /*if (restoredText == null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            Log.e("name", name);
        }*/
        //Log.d("data", data);
        // Now he make Request Interface Instance of Retrofit
        //apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        /*Call<List<DoctorContacts>> call = apiInterface.getContacts();

        call.enqueue(new Callback<List<DoctorContacts>>() {
            @Override
            public void onResponse(Call<List<DoctorContacts>> call, Response<List<DoctorContacts>> response) {
                doctorContacts = (ArrayList<DoctorContacts>) response.body();
                recyclerViewOfDoctorsAdapter = new RecyclerViewOfDoctorsAdapter(doctorContacts);
                recyclerView.setAdapter(recyclerViewOfDoctorsAdapter);
            }

            @Override
            public void onFailure(Call<List<DoctorContacts>> call, Throwable t) {

            }
        });*/




    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            Intent intent = new Intent(RecyclerViewOfDoctors.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        /*Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);*/
        return super.onOptionsItemSelected(item);//true
    }

    private void setRecyclerViewOfDoctor(){
        /*ArrayList<DoctorContacts> doctorContactsArrayList = new ArrayList<>();*/

        doctorContactsArrayList = new ArrayList<>();
        JSONArray jsonArray = null; //response
        JSONObject jsonObjectI = null;
        JSONObject jsonObjectRegion = null;
        JSONObject jsonObjectArea = null;
        Log.i("stringSecond",stringResponse);
        try {
            jsonArray = new JSONArray(stringResponse);


            for(int i =0; i <jsonArray.length(); i++){

                try {
                // Read Json Doctors Data
                jsonObjectI = jsonArray.getJSONObject(i);

                //Set data inside json data in strings
                doctorStringImage = jsonObjectI.getString("image");

                doctorName = jsonObjectI.getString("name_en");
                doctorProfession = jsonObjectI.getString("specialty_en");
                discountPercentage = jsonObjectI.getString("commision");

                doctorSpecialist = jsonObjectI.getString("specialty_en");
                discountPercentage1 = jsonObjectI.getInt("price");
                doctorPhone = jsonObjectI.getString("phone");
                doctorAddress = jsonObjectI.getString("address_en");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                jsonObjectRegion = jsonObjectI.getJSONObject("region");
                    try{
                    jsonObjectArea = jsonObjectI.getJSONObject("area");

                    doctorLocation = jsonObjectRegion.getString("name_en") + jsonObjectArea.getString("name_en");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Set Doctor Data in Constructor
                doctorContacts = new DoctorContacts(doctorIntId, doctorStringImage,doctorName,doctorProfession,discountPercentage,
                        doctorSpecialist/*,discountPercentage1*/,doctorLocation ,doctorPhone, doctorAddress); //, doctorAddress
                /*stringList.add(doctorName);*/

                /*doctorContactsList.setDoctorStringImage("https://www.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_284x96dp.png");
                doctorContactsList.setDoctorName(doctorName);
                doctorContactsList.setDoctorProfession(doctorProfession);
                doctorContactsList.setDiscountPercentage(discountPercentage+"");
                doctorContactsList.setDoctorSpecialist(doctorSpecialist);
                doctorContactsList.setDiscountPercentage1(discountPercentage1+"");
                doctorContactsList.setDoctorLocation(doctorLocation);
                doctorContactsList.setDoctorPhone(doctorPhone);*/

                //set all data inside list
                doctorContactsArrayList.add(doctorContacts);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerViewOfDoctorsAdapter = new RecyclerViewOfDoctorsAdapter(doctorContactsArrayList, getApplicationContext(), this);
        recyclerView.setAdapter(recyclerViewOfDoctorsAdapter);
        recyclerViewOfDoctorsAdapter.notifyDataSetChanged();


        /*DoctorContacts doctorContacts1 = new DoctorContacts();
        doctorContacts1.setDoctorStringImage("https://www.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_284x96dp.png");
        doctorContacts1.setDoctorName("DocName");
        doctorContacts1.setDoctorProfession("DocProf");
        doctorContacts1.setDiscountPercentage("Discount");
        doctorContacts1.setDoctorSpecialist("DocSpec");
        doctorContacts1.setDoctorLocation("DocLoc");
        doctorContacts1.setDiscountPercentage1("Discount1");
        doctorContacts1.setDoctorPhone("DocPhone");
        doctorContacts.add(doctorContacts1);

        DoctorContacts doctorContacts2 = new DoctorContacts();
        doctorContacts1.setDoctorStringImage("https://www.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_284x96dp.png");
        doctorContacts1.setDoctorName("DocName");
        doctorContacts1.setDoctorProfession("DocProf");
        doctorContacts1.setDiscountPercentage("Discount");
        doctorContacts1.setDoctorSpecialist("DocSpec");
        doctorContacts1.setDoctorLocation("DocLoc");
        doctorContacts1.setDiscountPercentage1("Discount1");
        doctorContacts1.setDoctorPhone("DocPhone");
        doctorContacts.add(doctorContacts2);

        DoctorContacts doctorContacts3 = new DoctorContacts();
        doctorContacts1.setDoctorStringImage("https://www.gstatic.com/images/branding/googlelogo/2x/googlelogo_color_284x96dp.png");
        doctorContacts1.setDoctorName("DocName");
        doctorContacts1.setDoctorProfession("DocProf");
        doctorContacts1.setDiscountPercentage("Discount");
        doctorContacts1.setDoctorSpecialist("DocSpec");
        doctorContacts1.setDoctorLocation("DocLoc");
        doctorContacts1.setDiscountPercentage1("Discount1");
        doctorContacts1.setDoctorPhone("DocPhone");
        doctorContacts.add(doctorContacts3);

        recyclerViewOfDoctorsAdapter = new RecyclerViewOfDoctorsAdapter(doctorContacts);
        recyclerView.setAdapter(recyclerViewOfDoctorsAdapter);
        recyclerViewOfDoctorsAdapter.notifyDataSetChanged();*/

    }

    private void selectData(){

        SharedPreferences prefs = getSharedPreferences("AllData", MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void recyclerMain(){

        recyclerView = (RecyclerView) findViewById(R.id.doctors_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        //System.out.println(jsonParser.getDoctorContactsArrayList().get(0).getDoctorName());
        recyclerViewOfDoctorsAdapter = new RecyclerViewOfDoctorsAdapter(restoreData, getApplicationContext(), this);
        recyclerView.setAdapter(recyclerViewOfDoctorsAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewOfDoctorsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RecyclerViewOfDoctors.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // call this to finish the current activity
    }

}
