package com.hyper.design.medicalcard.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.KeyDoctorContacts;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DoctorProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DoctorProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView ivDoctorProfile;
    RatingBar ratingBar;
    TextView tvDoctorName, tvDoctorProfession, tvDiscount,
            tvDoctorSpecialist, tvFees, tvDoctorLocation, tvDoctorPhone, tvReviews;
    TabHost tabHost;
    TabHost.TabSpec tabSpecProfile;
    TabHost.TabSpec tabSpecReview;

    DoctorContacts doctorContacts;
    private String imageUrlStatic = "uploads/item/resize200/"
            , imageUrlVariable = "http://hyper-design.com/medicalcard/";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorProfileFragment newInstance(String param1, String param2) {
        DoctorProfileFragment fragment = new DoctorProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        ivDoctorProfile = (ImageView) getActivity().findViewById(R.id.iv_doctor_profile_image1);
        ratingBar = (RatingBar) getActivity().findViewById(R.id.rating1);
        tvDoctorName = (TextView) getActivity().findViewById(R.id.tv_doctor_name1);
        tvDoctorProfession = (TextView) getActivity().findViewById(R.id.tv_doctor_job1);
        tvDiscount = (TextView) getActivity().findViewById(R.id.tv_discount_percentage1);
        tvDoctorSpecialist = (TextView) getActivity().findViewById(R.id.tv_doctor_profile_specialist1);
        /*tvFees = (TextView) findViewById(R.id.tv_fees);*/
        tvDoctorLocation = (TextView) getActivity().findViewById(R.id.tv_doctor_profile_location1);
        tvDoctorPhone = (TextView) getActivity().findViewById(R.id.tv_doctor_profile_phone1);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();

        Picasso.with(getActivity().getApplicationContext()).load(intent.getStringExtra(imageUrlVariable+imageUrlStatic+ KeyDoctorContacts.imageKey)).into(ivDoctorProfile);

        tvDoctorName.setText(getArguments().getString(KeyDoctorContacts.nameAraKey));
        tvDoctorProfession.setText(getArguments().getString(KeyDoctorContacts.profKey));
        tvDiscount.setText(getArguments().getString(KeyDoctorContacts.discKey));
        tvDoctorSpecialist.setText(getArguments().getString(KeyDoctorContacts.specKey));
        tvFees.setText(getArguments().getString(KeyDoctorContacts.feesKey));
        tvDoctorLocation.setText(getArguments().getString(KeyDoctorContacts.locKeyRegion));
        tvDoctorPhone.setText(getArguments().getString(KeyDoctorContacts.phoneKey));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
