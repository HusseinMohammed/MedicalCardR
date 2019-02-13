package com.hyper.design.medicalcard.dataProcess;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.DoctorProfileClass;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.KeyDoctorProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.hyper.design.medicalcard.DbHelper.TAG;

/**
 * Created by Hyper Design Hussien on 12/20/2017.
 */

public class jsonParser {

    ArrayList<DoctorContacts> doctorContactsArrayList;
    DoctorContacts doctorContacts;
    ArrayList<DoctorProfileClass> doctorProfileClasses;
    DoctorProfileClass doctorProfileClass;
    SharedPreferences settings;
    public String localLanguage;
    private SessionManager session;
    public String sessionLanguage;
    private String categoryName;
    List<String> rateList;
    JSONObject jsonObjectRate;
    List<String> itemId;

    public jsonParser(Context context) {
        settings = context.getSharedPreferences("CommonPrefs", 0);
        localLanguage = settings.getString("locale","");
        session = new SessionManager(context);
        sessionLanguage = session.isLanguageIn();
        Log.i("locallllll", localLanguage);
    }

    public ArrayList<DoctorContacts> jsonProcess(JSONObject jsonObjectFile){

        doctorContactsArrayList = new ArrayList<>();
        doctorContacts = new DoctorContacts();
        doctorProfileClasses = new ArrayList<>();
        doctorProfileClass = new DoctorProfileClass();

        try {
            JSONArray jsonRating = jsonObjectFile.getJSONArray(KeyDoctorProfile.ratingKey);

            //you don't able to put rate null value
            jsonObjectRate  = jsonRating.getJSONObject(0);
            /*//String rateId[] = new String[508];
            List<String> rateId = new ArrayList<>(508);
                            *//*for (int i = 0; i < 510; i++){
                                //rateId.add(String.valueOf(i));
                                rateId.add(i + "");
                            }
                            Log.i(TAG, rateId.toString());
                            Log.i(TAG, rateId.get(0));
                            Log.i(TAG, jsonObjectRate.length() + ":");*//*

            int j = 0;
            for (int x = 0; x < jsonObjectRate.length(); x++){
                //rateId.add(String.valueOf(i));
                rateId.add(jsonObjectRate.getInt(x+"") + "");
            }*/
            int j = 0;
            /*Iterator<String> iterator = jsonObjectRate.keys();
            rateList = new ArrayList<>();
            while(iterator.hasNext()) {
                String currentKey = iterator.next();
                if(currentKey.equals(doctorContacts.getId())){
                    Log.i("keys", currentKey);
                    rateList.add(currentKey);
                    doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                    Log.d("idRate", doctorContacts.getId());
                } else {
                    doctorContacts.setDoctorRate(0);
                    Log.d("idNoRate", doctorContacts.getId());
                }

            }
            Log.i("keys", rateList.size() + "");*/

            Iterator<String> iterator = jsonObjectRate.keys();
            rateList = new ArrayList<>();

            /*while(iterator.hasNext()) {
                String currentKey = iterator.next();
                Log.i("keys", currentKey);
                if(currentKey.equals(doctorContacts.getId())){
                    Log.i("keys", currentKey);
                    rateList.add(currentKey);
                    doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                    Log.d("idRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                } else {
                    doctorContacts.setDoctorRate(0);
                    Log.d("idNoRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                }

            }
            Log.i("keys", rateList.size() + "");*/

            //rateList.size();

            /*Iterator<String> keys = jsonObjectRate.keys();
            if( keys.hasNext() ){
                String key = (String)keys.next(); // First key in your json object
                Log.i("keys", key);
            }*/

            while (j < jsonObjectRate.length()) {

                //if(jsonObjectRate.getInt(j + ""))


                //Log.i("keys", jsonObjectRate.keys().toString());
                /*if(jsonObjectRate.keys().equals(doctorContacts.getId()))
                {
                    //Log.i(TAG, jsonObjectRate.getInt(rateId.get(j)+"") + "");
                } else {

                }
*/

                                /*jsonObjectRate.getString(jsonObjectRate.getString("1"));
                                //Save Doctor Rate in String
                                String rate = jsonObjectRate.getString(rateId.get(j));

                                Double rateDouble = Double.valueOf(rate);

                                int rateInt = rateDouble.intValue();

                                Log.i("Rate", rateInt + "");*/

                //doctorProfileClass.setDoctorProfileRate();

                /*//Save Doctor Rate in int
                doctorContacts.setDoctorRate(jsonObjectRate.getInt(j + ""));
                doctorProfileClass.setDoctorProfileRate(jsonObjectRate.getString(rateId.get(j)));
                Log.i("Length", jsonObjectRate.getInt(rateId.get(j)) + "");*/
                j++;
            }

            Log.i(TAG, String.valueOf(doctorProfileClass.getDoctorProfileRate()));

                            /*JSONArray jsonArray = jsonObjectFile.getJSONArray("rating");
                            if(jsonArray.length() == 0)
                            {

                            }else {

                            *//*for (int i = 0; i < jsonObject.length(); i++){
                                Map<String,String> stringMap = new HashMap<>();
                                stringMap.put("1",jsonObject.getString("1"));
                            }*//*
                                //doctorContacts.setDoctorRate(jsonObject.getString("1"));
                            }
                            System.out.println(jsonArray);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArrayCategory = jsonObjectFile.getJSONArray("category");
            JSONObject jsonObject = new JSONObject();
            if(jsonArrayCategory.length() == 0)
            {

            }
            else {

                jsonObject = jsonArrayCategory.getJSONObject(0);
                if (sessionLanguage.equals("en")) {
                    doctorContacts.setDoctorCategory(jsonObject.getString("name_en"));
                    /*categoryName = jsonObject.getString("name_en");
                    doctorContacts.getDoctorCategory();*/
                    Log.i("categoryname", jsonObject.getString("name_en"));
                } else {
                    doctorContacts.setDoctorCategory(jsonObject.getString("name_ar"));
                }
            }
            System.out.println(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = jsonObjectFile.getJSONArray("searchData");
            Log.i(TAG,jsonArray.length() + " " + jsonArray.toString());
            if(jsonArray.length() != 0) {

                JSONArray jsonArray1 = jsonArray.getJSONArray(0);
                itemId = new ArrayList<>();


                /*JSONArray jsonArray2 = jsonArray.getJSONArray(0);

                JSONArray jsonArray1 = jsonArray2.getJSONArray(0);*/

                Iterator<String> iterator = jsonObjectRate.keys();
                rateList = new ArrayList<>();

                while(iterator.hasNext()) {
                    String currentKey = iterator.next();
                    Log.i("keysOuter", currentKey);

                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject = jsonArray1.getJSONObject(i);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("region");

                        JSONObject jsonObject2 = jsonObject.getJSONObject("area");


                        if (sessionLanguage.equals("en")) {
                            doctorContacts = new DoctorContacts(jsonObject.getString(KeyDoctorContacts.idKey), jsonObject.getString(KeyDoctorContacts.imageKey), jsonObject.getString(KeyDoctorContacts.nameAraKey),
                                    jsonObject.getString(KeyDoctorContacts.profKey), jsonObject.getString(KeyDoctorContacts.discKey),
                                    jsonObject.getString(KeyDoctorContacts.specKey), /*jsonObject.getInt(KeyDoctorContacts.feesKey),*/
                                    jsonObject1.getString(KeyDoctorContacts.locKey1) + "-" + jsonObject2.getString(KeyDoctorContacts.locKey1),
                                    jsonObject.getString(KeyDoctorContacts.phoneKey), doctorContacts.getDoctorCategory()); //, jsonObject.getString(KeyDoctorProfile.addressEngKey)

                            Log.d("doctorId", doctorContacts.getId());
                            doctorContacts.getId();

                            itemId.add(doctorContacts.getId());

                            Log.d("idDoctor", doctorContacts.getId());
                            if (doctorContacts.getId().equals(currentKey)) {
                                doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                                //doctorContacts = new DoctorContacts(jsonObjectRate.getInt(currentKey));
                                Log.d("keysExist", doctorContacts.getDoctorRate() + " " + doctorContacts.getDoctorName());
                            } else {
                                doctorContacts.setDoctorRate(0);
                                //doctorContacts = new DoctorContacts(1);
                                Log.d("keysNotExist", doctorContacts.getDoctorRate() + " " + doctorContacts.getDoctorName());
                            }
                        /*if(doctorContacts.getId().equals(rateList.get(i))){
                            doctorContacts.setDoctorRate(jsonObjectRate.getInt(rateList.get(i)));
                        } else {
                            doctorContacts.setDoctorRate(0);
                        }*/

                        /*Iterator<String> iterator = jsonObjectRate.keys();
                        rateList = new ArrayList<>();

                        while(iterator.hasNext()) {
                            String currentKey = iterator.next();
                            Log.i("keysOuter", currentKey);

                            if(doctorContacts.getId() == currentKey){
                                Log.i("keysInner", currentKey);
                                rateList.add(currentKey);
                                doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                                Log.d("idRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                            } else {
                                doctorContacts.setDoctorRate(0);
                                Log.d("idNoRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                            }

                        }
                        Log.i("keys", rateList.size() + "");*/

                            doctorContactsArrayList.add(doctorContacts);
                        } else {
                            doctorContacts = new DoctorContacts(jsonObject.getString(KeyDoctorContacts.idKey), jsonObject.getString(KeyDoctorContacts.imageKey), jsonObject.getString(KeyDoctorContacts.nameAraKey),
                                    jsonObject.getString(KeyDoctorContacts.profKeyAra), jsonObject.getString(KeyDoctorContacts.discKey),
                                    jsonObject.getString(KeyDoctorContacts.specKeyAr), /*jsonObject.getInt(KeyDoctorContacts.feesKey),*/
                                    jsonObject1.getString(KeyDoctorContacts.locKeyAra1) + "-" + jsonObject2.getString(KeyDoctorContacts.locKeyAra1),
                                    jsonObject.getString(KeyDoctorContacts.phoneKey), doctorContacts.getDoctorCategory()); //, jsonObject.getString(K)

                            Log.d("doctorId", doctorContacts.getId());
                            doctorContacts.getId();

                            itemId.add(doctorContacts.getId());

                            Log.d("idDoctor", doctorContacts.getId());
                            if (doctorContacts.getId().equals(currentKey)) {
                                doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                                //doctorContacts = new DoctorContacts(jsonObjectRate.getInt(currentKey));
                                Log.d("keysExist", doctorContacts.getDoctorRate() + " " + doctorContacts.getDoctorName());
                            } else {
                                doctorContacts.setDoctorRate(0);
                                //doctorContacts = new DoctorContacts(1);
                                Log.d("keysNotExist", doctorContacts.getDoctorRate() + " " + doctorContacts.getDoctorName());
                            }
                            doctorContactsArrayList.add(doctorContacts);
                        }
                /*doctorContacts = new DoctorContacts(jsonObject.getString(KeyDoctorContacts.idKey), jsonObject.getString(KeyDoctorContacts.imageKey), jsonObject.getString(KeyDoctorContacts.nameAraKey),
                        jsonObject.getString(KeyDoctorContacts.profKey), jsonObject.getString(KeyDoctorContacts.discKey),
                        jsonObject.getString(KeyDoctorContacts.specKey), *//*jsonObject.getInt(KeyDoctorContacts.feesKey),*//*
                        jsonObject1.getString(KeyDoctorContacts.locKey1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKey1),
                        jsonObject.getString(KeyDoctorContacts.phoneKey));

                doctorContactsArrayList.add(doctorContacts);*/

                    }

                }

                /*Log.i("itemId", itemId.size() + "");
                Log.i("itemId", itemId.get(0) + "");


                //                HashMap<String, >
                Iterator<String> iterator = jsonObjectRate.keys();
                rateList = new ArrayList<>();

                while(iterator.hasNext()) {
                    String currentKey = iterator.next();
                    Log.i("keysOuter", currentKey);
                    for(int x = 0; x < itemId.size(); x++){
                        if(currentKey.equals(itemId.get(x))){
                            Log.i("keysInner", currentKey);
                            rateList.add(currentKey);
                            Log.i("keysInner", jsonObjectRate.getInt(currentKey) + "");
                            doctorContacts.setDoctorRate(jsonObjectRate.getInt(currentKey));
                            Log.d("idRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                        } else {
                            doctorContacts.setDoctorRate(0);
                            Log.d("idNoRate", doctorContacts.getId() + doctorContacts.getDoctorName());
                        }
                    }

                }
                //Log.i("keysInner", doctorContacts.getDoctorRate());
                Log.i("keys", rateList.size() + "");*/

            } else {
                //doctorContactsArrayList.add(null);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return doctorContactsArrayList;
    }

    public ArrayList<DoctorContacts> getDoctorContactsArrayList(){

        return doctorContactsArrayList;
    }

}
    /*JSONArray array=new JSONArray();


    JSONObject jObject=new JSONObject();
            try {
                    jObject.put(KEY_SPINCATEG, String.valueOf(intSpinCategory)); //"1"
                    jObject.put(KEY_SPINCITY, String.valueOf(intSpinCity)); //
                    jObject.put(KEY_SPINSPEC, String.valueOf(intSpinSpecialist)); // +
                    jObject.put(KEY_SPINAREA, String.valueOf(intSpinArea)); //
                    jObject.put(KEY_DOCTORNAME, strDoctorName);
                    } catch (JSONException e) {
                    e.printStackTrace();
                    }
                    array.put(jObject);

                    RequestQueue queue = Volley.newRequestQueue(this);
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, SERVER_URL + SERVER_SEARCH_URL, array,
                    new Response.Listener<JSONArray>() {
@Override
public void onResponse(JSONArray jsonArray) {
        Log.e("CitySpinner Response",jsonArray.toString());
        Toast.makeText(getApplicationContext(), "Success CitySpinner Connection", Toast.LENGTH_LONG).show();
                        *//*stringResponse = response;*//*
        Log.i("data",stringResponse);


        jsonParser = new jsonParser();

        doctorContactsArrayList = jsonParser.jsonProcess(stringResponse);

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
        },
        new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError volleyError) {

        }
        });

        //queue.add(jsonArrayRequest);
        MySingletonhus.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);*/