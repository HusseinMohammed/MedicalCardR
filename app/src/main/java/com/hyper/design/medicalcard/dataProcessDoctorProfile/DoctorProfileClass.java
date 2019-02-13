package com.hyper.design.medicalcard.dataProcessDoctorProfile;

import com.hyper.design.medicalcard.ContentReviews;

import java.util.List;

/**
 * Created by dell on 31/12/2017.
 */

public class DoctorProfileClass {
    //    @SerializedName("")
    public String doctorProfileStringImage;

    //    @SerializedName("")
    public String doctorProfileName;
    //    @SerializedName("")
    public String doctorProfileCategorySection;
    //    @SerializedName("")
    public String doctorProfileRate;

    public int doctorRate;

    //    @SerializedName("")
    public String doctorProfileSpecialist;
    //    @SerializedName("")
    public String doctorProfileLocation;
    //    @SerializedName("")
    public String doctorProfilePhone;

    public String doctorProfileSchedule;

    public String doctorProfileService;

    public List<ContentReviews> listDoctorProfileReviews;

    public DoctorProfileClass() {
    }

    public DoctorProfileClass(String doctorProfileStringImage) {
        this.doctorProfileStringImage = doctorProfileStringImage;
    }

    public DoctorProfileClass(String doctorProfileStringImage, String doctorProfileName, String doctorProfileCategorySection, String doctorProfileRate, String doctorProfileSpecialist, String doctorProfileLocation, String doctorProfilePhone, String doctorProfileSchedule, String doctorProfileService, List<ContentReviews> listDoctorProfileReviews) {
        this.doctorProfileStringImage = doctorProfileStringImage;
        this.doctorProfileName = doctorProfileName;
        this.doctorProfileCategorySection = doctorProfileCategorySection;
        this.doctorProfileRate = doctorProfileRate;
        this.doctorProfileSpecialist = doctorProfileSpecialist;
        this.doctorProfileLocation = doctorProfileLocation;
        this.doctorProfilePhone = doctorProfilePhone;
        this.doctorProfileSchedule = doctorProfileSchedule;
        this.doctorProfileService = doctorProfileService;
        this.listDoctorProfileReviews = listDoctorProfileReviews;
    }

    public DoctorProfileClass(String doctorProfileName, String doctorProfileSpecialist, String doctorProfileLocation, String doctorProfilePhone, String doctorProfileService) {
        this.doctorProfileName = doctorProfileName;
        this.doctorProfileSpecialist = doctorProfileSpecialist;
        this.doctorProfileLocation = doctorProfileLocation;
        this.doctorProfilePhone = doctorProfilePhone;
        this.doctorProfileService = doctorProfileService;
    }


    public void setDoctorProfileStringImage(String doctorProfileStringImage) {
        this.doctorProfileStringImage = doctorProfileStringImage;
    }

    public void setDoctorProfileName(String doctorProfileName) {
        this.doctorProfileName = doctorProfileName;
    }

    public void setDoctorProfileCategorySection(String doctorProfileCategorySection) {
        this.doctorProfileCategorySection = doctorProfileCategorySection;
    }

    public void setDoctorProfileRate(String doctorProfileRate) {
        this.doctorProfileRate = doctorProfileRate;
    }

    public void setDoctorProfileSpecialist(String doctorProfileSpecialist) {
        this.doctorProfileSpecialist = doctorProfileSpecialist;
    }

    public void setDoctorProfileLocation(String doctorProfileLocation) {
        this.doctorProfileLocation = doctorProfileLocation;
    }

    public void setDoctorProfilePhone(String doctorProfilePhone) {
        this.doctorProfilePhone = doctorProfilePhone;
    }

    public void setDoctorProfileSchedule(String doctorProfileSchedule) {
        this.doctorProfileSchedule = doctorProfileSchedule;
    }

    public void setDoctorProfileService(String doctorProfileService) {
        this.doctorProfileService = doctorProfileService;
    }

    public void setListDoctorProfileReviews(List<ContentReviews> listDoctorProfileReviews) {
        this.listDoctorProfileReviews = listDoctorProfileReviews;
    }

    public void setDoctorRate(int doctorRate) {
        this.doctorRate = doctorRate;
    }

    public String getDoctorProfileStringImage() {
        return doctorProfileStringImage;
    }

    public String getDoctorProfileName() {
        return doctorProfileName;
    }

    public String getDoctorProfileCategorySection() {
        return doctorProfileCategorySection;
    }

    public String getDoctorProfileRate() {
        return doctorProfileRate;
    }

    public String getDoctorProfileSpecialist() {
        return doctorProfileSpecialist;
    }

    public String getDoctorProfileLocation() {
        return doctorProfileLocation;
    }

    public String getDoctorProfilePhone() {
        return doctorProfilePhone;
    }

    public String getDoctorProfileSchedule() {
        return doctorProfileSchedule;
    }

    public String getDoctorProfileService() {
        return doctorProfileService;
    }

    public List<ContentReviews> getListDoctorProfileReviews() {
        return listDoctorProfileReviews;
    }

    public int getDoctorRate() {
        return doctorRate;
    }

}
