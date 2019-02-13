package com.hyper.design.medicalcard.User;

/**
 * Created by Hyper Design Hussien on 1/10/2018.
 */

public class User {
    public int userId;
    public String username, email, password, code;
    public int regionId, areaId;

    public User(){

    }

    public User(String username, int region, int area, String email, String password, String code) {
        this.username = username;
        this.regionId = region;
        this.areaId = area;
        this.email = email;
        this.password = password;
        this.code = code;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getAreaId() {
        return areaId;
    }
}
