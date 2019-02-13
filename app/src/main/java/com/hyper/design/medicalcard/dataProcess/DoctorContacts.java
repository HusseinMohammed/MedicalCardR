package com.hyper.design.medicalcard.dataProcess;

import java.util.ArrayList;

/**
 * Created by dell on 17/12/2017.
 */

public class DoctorContacts {

    public String id;

//    @SerializedName("")
    public String doctorStringImage;

//    @SerializedName("")
    public String doctorName;
//    @SerializedName("")
    public String doctorProfession;
//    @SerializedName("")
    public String discountPercentage;

//    @SerializedName("")
    public String doctorSpecialist;
//    @SerializedName("")
    /*public int discountPercentage1;*/
//    @SerializedName("")
    public String doctorLocation;
//    @SerializedName("")
    public String doctorPhone;

    public String dataStr;

    public int doctorRate;

    public String doctorCategory;

    public String doctorAddress;

    public ArrayList<String> itemImages;

    public String itemImage;

    public DoctorContacts(){

    }

    public DoctorContacts(int doctorRate){
        this.doctorRate = doctorRate;
    }

    public DoctorContacts(String id, String doctorStringImage, String doctorName, String doctorProfession, String discountPercentage,
                          String doctorSpecialist, String doctorLocation, String doctorPhone, String doctorCategory) {
        this.id = id;
        this.doctorStringImage = doctorStringImage;
        this.doctorName = doctorName;
        this.doctorProfession = doctorProfession;
        this.discountPercentage = discountPercentage;
        this.doctorSpecialist = doctorSpecialist;
        this.doctorLocation = doctorLocation;
        this.doctorPhone = doctorPhone;
        this.doctorCategory = doctorCategory;
    }

    public DoctorContacts(ArrayList<String> itemImages) {
        this.itemImages = itemImages;
    }

    public void setDoctorStringImage(String doctorStringImage) {
        this.doctorStringImage = doctorStringImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public void setDoctorCategory(String doctorCategory) {
        this.doctorCategory = doctorCategory;
    }

    public void setDoctorRate(int doctorRate) {
        this.doctorRate = doctorRate;
    }


    public String getId() {
        return id;
    }

    public String getDoctorStringImage() {
        return doctorStringImage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorProfession() {
        return doctorProfession;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public String getDoctorSpecialist() {
        return doctorSpecialist;
    }

/*
    public int getDiscountPercentage1() {
        return discountPercentage1;
    }
*/

    public String getDoctorLocation() {
        return doctorLocation;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public String getDataStr() {
        return dataStr;
    }

    public int getDoctorRate() {
        return doctorRate;
    }

    public String getDoctorCategory() {
        return doctorCategory;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public ArrayList<String> getItemImages() {
        return itemImages;
    }

}
