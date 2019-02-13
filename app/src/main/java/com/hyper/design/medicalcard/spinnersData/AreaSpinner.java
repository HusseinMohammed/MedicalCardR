package com.hyper.design.medicalcard.spinnersData;

/**
 * Created by Hyper Design Hussien on 12/21/2017.
 */

public class AreaSpinner {

    public int areaId;
    public String areaStringArabicName;
    public String areaStringEnglishName;

    public String areaKeyId = "id";
    public String areaKeyStringArabicName = "name_ar";
    public String areaKeyStringEnglishName = "name_en";

    public AreaSpinner() {
    }

    public AreaSpinner(int areaId, String areaStringArabicName, String areaStringEnglishName) {
        this.areaId = areaId;
        this.areaStringArabicName = areaStringArabicName;
        this.areaStringEnglishName = areaStringEnglishName;
    }

    public int getAreaId() {
        return areaId;
    }

    public String getAreaStringArabicName() {
        return areaStringArabicName;
    }

    public String getAreaStringEnglishName() {
        return areaStringEnglishName;
    }

    public String getAreaKeyId() {
        return areaKeyId;
    }

    public String getAreaKeyStringArabicName() {
        return areaKeyStringArabicName;
    }

    public String getAreaKeyStringEnglishName() {
        return areaKeyStringEnglishName;
    }
}
