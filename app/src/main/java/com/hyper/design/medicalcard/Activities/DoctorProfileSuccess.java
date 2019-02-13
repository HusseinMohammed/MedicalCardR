package com.hyper.design.medicalcard.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.dataProcess.DoctorContacts;
import com.hyper.design.medicalcard.dataProcess.KeyDoctorContacts;

public class DoctorProfileSuccess extends AppCompatActivity {

    ImageView ivDoctorProfile;
    RatingBar ratingBar;
    TextView tvDoctorName, tvDoctorProfession, tv_discount1, tvDiscount,
            tvDoctorSpecialist, tvFees, tvDoctorLocation, tvDoctorPhone, tvReviews, tv_doctor_profile_feedback1, textView;
    TabHost tabHost;
    TabHost.TabSpec tabSpecProfile;
    TabHost.TabSpec tabSpecReview;

    DoctorContacts doctorContacts;
    private String imageUrlStatic = "uploads/item/resize200/"
                 , imageUrlVariable = "http://hyper-design.com/medicalcard/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_doctor_profile);

        ivDoctorProfile = (ImageView) findViewById(R.id.iv_doctor_profile_image1);
        ratingBar = (RatingBar) findViewById(R.id.rating1);
        tvDoctorName = (TextView) findViewById(R.id.tv_doctor_name1);
        tvDoctorProfession = (TextView) findViewById(R.id.tv_doctor_job1);
        tvDiscount = (TextView) findViewById(R.id.tv_discount_percentage1);
        tv_discount1 = (TextView) findViewById(R.id.tv_discount1);
        tvDoctorSpecialist = (TextView) findViewById(R.id.tv_doctor_profile_specialist1);
        tvFees = (TextView) findViewById(R.id.tv_discount);
        tvDoctorLocation = (TextView) findViewById(R.id.tv_doctor_profile_location1);
        tvDoctorPhone = (TextView) findViewById(R.id.tv_doctor_profile_phone1);
        tv_doctor_profile_feedback1 = (TextView) findViewById(R.id.tv_doctor_profile_feedback1);

        textView = (TextView) findViewById(R.id.textView2);

        /*tvReviews = (TextView) findViewById(R.id.tv_reviews);*/

        /*tabHost = (TabHost) findViewById(R.id.th_profile_reviews);
        tabHost.setup();

        tabSpecProfile = tabHost.newTabSpec("Profile");
        tabSpecProfile.setContent(R.id.Profile);
        tabSpecProfile.setIndicator("Profile");

        tabSpecReview = tabHost.newTabSpec("Reviews");
        tabSpecReview.setContent(R.id.Reviews);
        tabSpecReview.setIndicator("Reviews");

        tabHost.addTab(tabSpecProfile);
        tabHost.addTab(tabSpecReview);*/



        Intent intent = getIntent();

        textView.setText(intent.getStringExtra(KeyDoctorContacts.nameAraKey));
       /* Picasso.with(getApplicationContext()).load(imageUrlVariable + imageUrlStatic + intent.getStringExtra(KeyDoctorContacts.imageKey)).into(ivDoctorProfile);
        ratingBar.setNumStars(3);

        tvDoctorName.setText(intent.getStringExtra(KeyDoctorContacts.nameAraKey));
        tvDoctorProfession.setText(intent.getStringExtra(KeyDoctorContacts.profKey));
        tv_discount1.setText("Discount :");
        tvDiscount.setText(intent.getStringExtra(KeyDoctorContacts.discKey));
        tvDoctorSpecialist.setText(intent.getStringExtra(KeyDoctorContacts.specKey));
        tvFees.setText(intent.getStringExtra(KeyDoctorContacts.feesKey));
        tvDoctorName.setText(intent.getStringExtra(KeyDoctorContacts.locKey));
        tvDoctorLocation.setText(intent.getStringExtra(KeyDoctorContacts.phoneKey));
        tv_doctor_profile_feedback1.setText(R.string.doctor_about);*/

    }
}
