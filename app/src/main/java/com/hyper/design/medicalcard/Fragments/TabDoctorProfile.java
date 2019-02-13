package com.hyper.design.medicalcard.Fragments;

/**
 * Created by Hyper Design Hussien on 12/26/2017.
 */
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyper.design.medicalcard.R;

public class TabDoctorProfile extends Fragment {

    public TextView tvDoctorSpecialist, tvFees, tvDoctorLocation, tvDoctorPhone;

    public TabDoctorProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvDoctorSpecialist = (TextView) getActivity().findViewById(R.id.tv_specialist_tab);
        tvFees = (TextView) getActivity().findViewById(R.id.tv_discount_tab);
        tvDoctorLocation = (TextView) getActivity().findViewById(R.id.tv_location_tab);
        tvDoctorPhone = (TextView) getActivity().findViewById(R.id.tv_doctor_phone_tab);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_doctor_tab, container, false);

        tvDoctorSpecialist = (TextView) getActivity().findViewById(R.id.tv_specialist_tab);
        tvFees = (TextView) getActivity().findViewById(R.id.tv_discount_tab);
        tvDoctorLocation = (TextView) getActivity().findViewById(R.id.tv_location_tab);
        tvDoctorPhone = (TextView) getActivity().findViewById(R.id.tv_doctor_phone_tab);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDoctorSpecialist = (TextView) getActivity().findViewById(R.id.tv_specialist_tab);
        tvFees = (TextView) getActivity().findViewById(R.id.tv_discount_tab);
        tvDoctorLocation = (TextView) getActivity().findViewById(R.id.tv_location_tab);
        tvDoctorPhone = (TextView) getActivity().findViewById(R.id.tv_doctor_phone_tab);

        tvDoctorSpecialist.setText("Dentist Whightnen");
        tvFees.setText("50EGP");
        tvDoctorLocation.setText("Gamal Abd El Nasser");
        tvDoctorPhone.setText("01028203313");

    }
}
