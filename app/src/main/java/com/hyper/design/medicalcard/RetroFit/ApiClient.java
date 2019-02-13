package com.hyper.design.medicalcard.RetroFit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dell on 17/12/2017.
 */

public class ApiClient {

    public static final String BASE_URL= "http://192.168.1.4/medicalcard/api";
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient(){

        if(retrofit == null){

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
