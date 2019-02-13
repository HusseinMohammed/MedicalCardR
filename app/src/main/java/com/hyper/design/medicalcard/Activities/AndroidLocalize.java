package com.hyper.design.medicalcard.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.hyper.design.medicalcard.R;

import java.util.Locale;

public class AndroidLocalize extends AppCompatActivity {
    Spinner spinnerctrl;
    Button btn;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_localize);


    }
}
