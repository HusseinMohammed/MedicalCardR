package com.hyper.design.medicalcard.spinnersData;

/**
 * Created by Hyper Design Hussien on 12/21/2017.
 */

public class SpecialistsSpinner {

    public int specialistId;
    public String specialistStringArabicName;
    public String specialistStringEnglishName;

    public String specialistKeyId = "id";
    public String specialistKeyStringArabicName = "name_ar";
    public String specialistKeyStringEnglishName = "name_en";

    public SpecialistsSpinner() {
    }

    public SpecialistsSpinner(int specialistId, String specialistStringArabicName, String specialistStringEnglishName) {
        this.specialistId = specialistId;
        this.specialistStringArabicName = specialistStringArabicName;
        this.specialistStringEnglishName = specialistStringEnglishName;
    }

    public int getSpecialistId() {
        return specialistId;
    }

    public String getSpecialistStringArabicName() {
        return specialistStringArabicName;
    }

    public String getSpecialistStringEnglishName() {
        return specialistStringEnglishName;
    }

    public String getSpecialistKeyId() {
        return specialistKeyId;
    }

    public String getSpecialistKeyStringArabicName() {
        return specialistKeyStringArabicName;
    }

    public String getSpecialistKeyStringEnglishName() {
        return specialistKeyStringEnglishName;
    }
}
