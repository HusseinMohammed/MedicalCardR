package com.hyper.design.medicalcard.spinnersData;

/**
 * Created by Hyper Design Hussien on 12/21/2017.
 */

public class CategorySpinner {

    public int categoryId;
    public String categoryStringArabicName;
    public String categoryStringEnglishName;

    public String categoryKeyId ="id";
    public String categoryKeyStringArabicName = "name_ar";
    public String categoryKeyStringEnglishName = "name_en";

    public CategorySpinner() {
    }

    public CategorySpinner(int categoryId, String categoryStringArabicName, String categoryStringEnglishName) {
        this.categoryId = categoryId;
        this.categoryStringArabicName = categoryStringArabicName;
        this.categoryStringEnglishName = categoryStringEnglishName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryStringArabicName() {
        return categoryStringArabicName;
    }

    public String getCategoryStringEnglishName() {
        return categoryStringEnglishName;
    }

    public String getCategoryKeyId() {
        return categoryKeyId;
    }

    public String getCategoryKeyStringArabicName() {
        return categoryKeyStringArabicName;
    }

    public String getCategoryKeyStringEnglishName() {
        return categoryKeyStringEnglishName;
    }
}
