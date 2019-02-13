package com.hyper.design.medicalcard.spinnersData;

/**
 * Created by Hyper Design Hussien on 12/21/2017.
 */

public class CitySpinner {

    public int cityId;
    public String cityStringArabicName;
    public String cityStringEnglishName;

    public String cityKeyId = "id";
    public String cityKeyStringArabicName = "name_ar";
    public String cityKeyStringEnglishName = "name_en";

    public CitySpinner() {
    }

    public CitySpinner(int cityId, String cityStringArabicName, String cityStringEnglishName) {
        this.cityId = cityId;
        this.cityStringArabicName = cityStringArabicName;
        this.cityStringEnglishName = cityStringEnglishName;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityStringArabicName() {
        return cityStringArabicName;
    }

    public String getCityStringEnglishName() {
        return cityStringEnglishName;
    }

    public String getCityKeyId() {
        return cityKeyId;
    }

    public String getCityKeyStringArabicName() {
        return cityKeyStringArabicName;
    }

    public String getCityKeyStringEnglishName() {
        return cityKeyStringEnglishName;
    }
}
