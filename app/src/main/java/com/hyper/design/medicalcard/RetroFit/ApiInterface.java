package com.hyper.design.medicalcard.RetroFit;

import com.hyper.design.medicalcard.dataProcess.DoctorContacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by dell on 17/12/2017.
 */

public interface ApiInterface {

    @POST("readcontacts.php")
    Call<List<DoctorContacts>> getContacts();
}
