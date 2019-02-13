package com.hyper.design.medicalcard.User;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hyper Design Hussien on 1/10/2018.
 */

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storedUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("username", user.username);
        spEditor.putInt("region", user.regionId);
        spEditor.putInt("area", user.areaId);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.putString("code", user.code);
        spEditor.commit();
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putInt("id", user.userId);
        spEditor.putString("username", user.username);
        spEditor.putString("email", user.email);
        spEditor.commit();
    }

    public User getLoggedInUser(){
        String username = userLocalDatabase.getString("username", "");
        int regionId = userLocalDatabase.getInt("region", 0);
        int areaId = userLocalDatabase.getInt("area", 0);
        String email = userLocalDatabase.getString("email", "");
        String password = userLocalDatabase.getString("password", "");
        String code = userLocalDatabase.getString("code", "");

        User storedUser = new User(username, regionId, areaId, email, password, code);
        return storedUser;
    }

    public User getUserLoggedInData(){
        int userId = userLocalDatabase.getInt("id", 0);
        String username = userLocalDatabase.getString("username", "");
        String email = userLocalDatabase.getString("email", "");

        User storedUser = new User(userId, username, email);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn",loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("LoggedIn", false) == true){
            return true;
        }else{
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

 }
