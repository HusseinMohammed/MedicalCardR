/*package com.hyper.design.medicalcard.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;

*//**
 * Created by Hyper Design Hussien on 1/18/2018.
 *//*

public class HeartBeat extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MusicHelper.loadAll(this);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {Thread.sleep(1000);} catch (InterruptedException e) {}

                finish();
                startActivity(new Intent().setClass(getBaseContext(), MainActivity.class));
            }
        }).start();
    }

}*/
