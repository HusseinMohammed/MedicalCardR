package com.hyper.design.medicalcard.tabbed;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.DoctorProfileClass;
import com.hyper.design.medicalcard.dataProcessDoctorProfile.jsonParserDoctorProfile;

/**
 * Created by Hyper Design Hussien on 12/31/2017.
 */

public class Profile extends Fragment {
    public TextView tvDoctorSpecialist, tvDiscount, tvDoctorLocation, tvDoctorPhone, tvSchedule, tvService;
    DoctorProfileClass doctorProfileClass;
    jsonParserDoctorProfile jsonParserDoctorProfile;
    Context context = getContext();

    public Profile() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        jsonParserDoctorProfile = new jsonParserDoctorProfile();

        tvDoctorSpecialist = (TextView) getActivity().findViewById(R.id.tv_specialist);
        tvDiscount = (TextView) getActivity().findViewById(R.id.tv_discount);
        tvDoctorLocation = (TextView) getActivity().findViewById(R.id.tv_location);
        tvDoctorPhone = (TextView) getActivity().findViewById(R.id.tv_doctor_phone);
        tvSchedule = (TextView) getActivity().findViewById(R.id.tv_specialist_tab1);
        tvService = (TextView) getActivity().findViewById(R.id.tv_discount_tab1);
        String prof = getArguments().getString("prof");
        String disc = getArguments().getString("disc");
        String spec = getArguments().getString("spec");
        String loc = getArguments().getString("loc");
        String phone = getArguments().getString("phone");
        tvDoctorSpecialist.setText(prof);
        tvDiscount.setText(disc + spec);
        tvDoctorLocation.setText(loc);
        tvDoctorPhone.setText(disc);
        tvDoctorSpecialist.setText(phone);
        //tvDiscount.setText(disc);

        return inflater.inflate(R.layout.tab_profile, container, false);
        //super.onCreateView(inflater, container, savedInstanceState)
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        jsonParserDoctorProfile = new jsonParserDoctorProfile();
        doctorProfileClass = new DoctorProfileClass();
        String schedule = jsonParserDoctorProfile.getDoctorContactsArrayList().doctorProfileSchedule;
        String service = jsonParserDoctorProfile.getDoctorContactsArrayList().doctorProfileService;
        tvSchedule.setText(schedule);
        tvService.setText(service);

    }
}
