package com.hyper.design.medicalcard.dataProcessDoctorProfile;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyper.design.medicalcard.ContentReviews;
import com.hyper.design.medicalcard.Helper.SessionManager;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.KeyDoctorContacts;
import com.hyper.design.medicalcard.tabbed.RowItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 31/12/2017.
 */

public class jsonParserDoctorProfile {

    public DoctorProfileClass doctorProfileClass;
    public ArrayList<DoctorProfileClass> doctorProfileClasses;
    ContentReviews contentReviews;
    List<RowItems> list;
    RowItems rowItems;
    DoctorContacts doctorContacts;
    SharedPreferences settings;
    public String localLanguage;
    private SessionManager session;
    public String sessionLanguage;

    /*public jsonParserDoctorProfile(Context context) {
        settings = context.getSharedPreferences("CommonPrefs", 0);
        localLanguage = settings.getString("locale","");
        session = new SessionManager(context);
        sessionLanguage = session.isLanguageIn();
        Log.i("locallllll", localLanguage);
    }*/

    public ArrayList<DoctorProfileClass> jsonProcess(JSONObject jsonObject){

        doctorProfileClass = new DoctorProfileClass();
        doctorProfileClasses = new ArrayList<>();
        contentReviews = new ContentReviews();
        doctorContacts = new DoctorContacts();
        rowItems = new RowItems();

        list = new ArrayList<RowItems>();

        try {
            JSONArray jsonArrayItem = jsonObject.getJSONArray("item");
            //Log.i(TAG, localLanguage);
            if(jsonArrayItem.length() != 0)
            {
                JSONObject jsonObjectItem = jsonArrayItem.getJSONObject(0);

                JSONObject jsonObject1 = jsonObjectItem.getJSONObject("region");

                JSONObject jsonObject2 = jsonObjectItem.getJSONObject("area");

                //Log.i(TAG, localLanguage);


                doctorContacts = new DoctorContacts(jsonObjectItem.getString(KeyDoctorContacts.idKey), jsonObjectItem.getString(KeyDoctorContacts.imageKey), jsonObjectItem.getString(KeyDoctorContacts.nameEngKey),
                        jsonObjectItem.getString(KeyDoctorContacts.profKey), jsonObjectItem.getString(KeyDoctorContacts.discKey),
                        jsonObjectItem.getString(KeyDoctorContacts.specKey), /*jsonObject.getInt(KeyDoctorContacts.feesKey),*/
                        jsonObject1.getString(KeyDoctorContacts.locKey1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKey1),
                        jsonObjectItem.getString(KeyDoctorContacts.phoneKey),  doctorContacts.getDoctorCategory()); //, jsonObjectItem.getString(KeyDoctorContacts.addressEngKey)

                Log.i(TAG, doctorContacts.getDoctorName());

                /*if(sessionLanguage.equals("en")){
                    Log.e(TAG, sessionLanguage);
                    doctorContacts = new DoctorContacts(jsonObjectItem.getString(KeyDoctorContacts.idKey), jsonObjectItem.getString(KeyDoctorContacts.imageKey), jsonObjectItem.getString(KeyDoctorContacts.nameEngKey),
                            jsonObjectItem.getString(KeyDoctorContacts.profKey), jsonObjectItem.getString(KeyDoctorContacts.discKey),
                            jsonObjectItem.getString(KeyDoctorContacts.specKey), *//*jsonObject.getInt(KeyDoctorContacts.feesKey),*//*
                            jsonObject1.getString(KeyDoctorContacts.locKey1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKey1),
                            jsonObjectItem.getString(KeyDoctorContacts.phoneKey), jsonObjectItem.getString(KeyDoctorContacts.addressEngKey));

                    Log.e(TAG, doctorContacts.getDoctorName() + "");

                    doctorProfileClass = new DoctorProfileClass(jsonObjectItem.getString(KeyDoctorContacts.nameEngKey), jsonObjectItem.getString(KeyDoctorContacts.specKey),
                            jsonObject1.getString(KeyDoctorContacts.locKey1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKey1), jsonObjectItem.getString(KeyDoctorContacts.phoneKey)
                            ,jsonObjectItem.getString(KeyDoctorContacts.discKey));

                    doctorProfileClasses.add(doctorProfileClass);
                } else {
                    doctorContacts = new DoctorContacts(jsonObjectItem.getString(KeyDoctorContacts.idKey), jsonObjectItem.getString(KeyDoctorContacts.imageKey), jsonObjectItem.getString(KeyDoctorContacts.nameAraKey),
                            jsonObjectItem.getString(KeyDoctorContacts.profKey), jsonObjectItem.getString(KeyDoctorContacts.discKey),
                            jsonObjectItem.getString(KeyDoctorContacts.specKey), *//*jsonObject.getInt(KeyDoctorContacts.feesKey),*//*
                            jsonObject1.getString(KeyDoctorContacts.locKeyAra1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKeyAra1),
                            jsonObjectItem.getString(KeyDoctorContacts.phoneKey), jsonObjectItem.getString(KeyDoctorContacts.addressArabKey));

                    doctorProfileClass = new DoctorProfileClass(jsonObjectItem.getString(KeyDoctorContacts.nameAraKey), jsonObjectItem.getString(KeyDoctorContacts.specKeyAr),
                            jsonObject1.getString(KeyDoctorContacts.locKeyAra1) + "-" +jsonObject2.getString(KeyDoctorContacts.locKeyAra1), jsonObjectItem.getString(KeyDoctorContacts.phoneKey)
                            ,jsonObjectItem.getString(KeyDoctorContacts.discKey));

                    doctorProfileClasses.add(doctorProfileClass);
                }*/

                //doctorContacts.setDoctorStringImage(jsonObjectItem.getString(KeyDoctorContacts.imageKey));

                Log.i(TAG, jsonObjectItem.getString(KeyDoctorContacts.imageKey));

                if(jsonObjectItem.getString(KeyDoctorContacts.imageKey).equals(null))
                    doctorProfileClass.setDoctorProfileStringImage("");
                else
                    doctorProfileClass = new DoctorProfileClass(jsonObjectItem.getString(KeyDoctorContacts.imageKey));

                Log.i(TAG, doctorContacts.getDoctorStringImage());


            } else {
                /*doctorContacts = new DoctorContacts(null, null, null, null,
                        null,null,null,null);*/
                Log.i(TAG, "MMMMMMMMMMMMM");
                doctorContacts = new DoctorContacts("", "", "", "",
                        "", "", "", "", ""); //, ""
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArrayService = jsonObject.getJSONArray(KeyDoctorProfile.serviceKey);
            JSONObject jsonObjectService = null;

            if(jsonArrayService.length() == 0){
                doctorProfileClass.setDoctorProfileService("");
            } else {
                jsonObjectService = jsonArrayService.getJSONObject(0);

                doctorProfileClass.setDoctorProfileService(jsonObjectService.getString(KeyDoctorProfile.serviceEngKey));
                /*if(localLanguage.equals("en")){
                    doctorProfileClass.setDoctorProfileService(jsonObjectService.getString(KeyDoctorProfile.serviceEngKey));
                } else {
                    doctorProfileClass.setDoctorProfileService(jsonObjectService.getString(KeyDoctorProfile.serviceAraKey));
                }*/

            }

            JSONArray jsonArrayItemImage = jsonObject.getJSONArray(KeyDoctorProfile.itemImageKey);
            JSONObject jsonObjectItemImage = new JSONObject();
            ArrayList<String> itemImages = new ArrayList<>();

            System.out.println(jsonArrayItemImage.length());
            if(jsonArrayItemImage.length() != 0){

                for (int i = 0; i < jsonObjectItemImage.length(); i++){
                    doctorContacts.setItemImage(jsonObjectItemImage.getString(KeyDoctorProfile.imageItemKey));

                    itemImages.add(doctorContacts.getItemImage());
                }
                doctorContacts = new DoctorContacts(itemImages);
            } else {
                doctorContacts.setItemImage("");
                itemImages.add(doctorContacts.getItemImage());
                doctorContacts = new DoctorContacts(itemImages);
            }

            JSONArray jsonArrayItemComment = jsonObject.getJSONArray(KeyDoctorProfile.itemCommentKey);
            Log.d("comments", jsonArrayItemComment.toString());
            if(jsonArrayItemComment.length() != 0){
                Log.d("comments", jsonArrayItemComment.toString());
                for(int i = 0; i < jsonArrayItemComment.length(); i++){
                    JSONObject jsonObjectItemComments = jsonArrayItemComment.getJSONObject(i);

                    /*if(localLanguage.equals("en")){

                    } else {

                    }*/
                    rowItems = new RowItems(jsonObjectItemComments.getString("id"),
                            jsonObjectItemComments.getString("name"), jsonObjectItemComments.getString("comment"));

                    /*rowItems.reviewId = jsonObjectItemComments.getString("id");
                    rowItems.reviewName = jsonObjectItemComments.getString("name");
                    rowItems.reviewComment = jsonObjectItemComments.getString("comment");*/



                    //if(rowItems.reviewComment != null && rowItems.reviewName)
                    list.add(rowItems);

                    for(int x = 0; x < list.size(); x++){
                        Log.d("list", list.get(x).reviewId);
                        Log.d("list", list.get(x).reviewName);
                        Log.d("list", list.get(x).reviewComment);
                    }
                }
                for(int i = 0; i < list.size(); i++){
                    Log.d("list", list.get(i).reviewId);
                    Log.d("list", list.get(i).reviewName);
                    Log.d("list", list.get(i).reviewComment);
                }
            } else {
                rowItems.reviewComment = "";
                rowItems.reviewName = "";
            }


            JSONArray jsonArrayItemParentComment = jsonObject.getJSONArray(KeyDoctorProfile.itemParnetCommentKey);

            if(jsonArrayItemParentComment.length() != 0){

                for(int i = 0; i < jsonArrayItemParentComment.length(); i++){
                    JSONObject jsonObjectItemComments = jsonArrayItemComment.getJSONObject(i);

                    /*if(localLanguage.equals("en")){

                    } else {

                    }*/
                    rowItems.reviewComment = jsonObjectItemComments.getString("comment");
                    rowItems.reviewName = jsonObjectItemComments.getString("user_id");
                    //if(rowItems.reviewComment != null && rowItems.reviewName)
                    list.add(rowItems);
                }

            } else {

            }


            JSONArray jsonArraySchedule = jsonObject.getJSONArray(KeyDoctorProfile.itemScheduleKey);
            if(jsonArraySchedule.length() == 0){
                doctorProfileClass.setDoctorProfileSchedule("");
            } else {
                JSONObject jsonObjectSchedule = jsonArraySchedule.getJSONObject(0);

                doctorProfileClass.setDoctorProfileSchedule(jsonObjectSchedule.getString(KeyDoctorProfile.dayKey)
                        + " - " +jsonObjectSchedule.getString(KeyDoctorProfile.timeKey));
            }

            //you don't able to put rate null value
            JSONObject jsonObjectRate  = new JSONObject();
            jsonObjectRate = jsonObject.getJSONObject(KeyDoctorProfile.ratingKey);

            if (jsonObjectRate.length() != 0){
                doctorProfileClass.setDoctorProfileRate(jsonObjectRate.getString(KeyDoctorProfile.finalRateKey));
            } else {
                doctorProfileClass.setDoctorProfileRate("0");
            }


            doctorProfileClasses.add(doctorProfileClass);


        } catch (JSONException e){
            e.printStackTrace();
        }
        return doctorProfileClasses;
    }

    public DoctorProfileClass getDoctorContactsArrayList(){

        return doctorProfileClass;
    }

    public DoctorContacts getDoctorContacts(){

        return doctorContacts;
    }

    public List<RowItems> contentReviewsList(){
        return list;
    }

}
