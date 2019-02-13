package com.hyper.design.medicalcard;

/**
 * Created by Hyper Design Hussien on 1/28/2018.
 */

public class AppConstant {

    //Search
    public static final String SERVER_URL = "http://www.medicalcardeg.com/api"; //local host = http://192.168.1.4/medicalcard/  //Server = http://hyper-design.com/newmedicalcard/api
    public static final String PARAMETER_URL ="?token=";
    public static final String SERVER_CATEGORY_URL = "/category";
    public static final String SERVER_CITY_URL = "/region";
    public static final String SERVER_SEARCH_URL = "/search";

    //Contact us
    public static final String SERVER_CONTACT_US_URL = "/contactus";

    //Buy Card
    public static final String SERVER_CITY_BUY_URL = "/getregion";
    public static final String SERVER_AREA_URL = "/region";
    public static final String SERVER_BUY_CARD_URL = "/buycard";

    //Buy Card
    public static final String SERVER_URL_BLOG= "/blogcategory";

    private String IMAGE_URL_STATIC = "uploads/blogitem/source/";

    public static final String IMAGE_URL_VARIABLE = "http://hyper-design.com/newmedicalcard/";
}
