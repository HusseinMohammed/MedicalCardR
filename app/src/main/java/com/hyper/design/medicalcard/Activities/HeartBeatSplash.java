package com.hyper.design.medicalcard.Activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.hyper.design.medicalcard.MainActivity;
import com.hyper.design.medicalcard.R;

public class HeartBeatSplash extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_beat_splash);

        imageView = (ImageView) findViewById(R.id.ivHeartBeat);

        /*runOnUiThread(new Runnable() {
            public void run() {
                // your code to update the UI thread here
                heartBeat();
                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        heartBeat();
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*heartBeat();
        Intent intent = new Intent(HeartBeatSplash.this, MainActivity.class);
        startActivity(intent);
        finish();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*heartBeat();
        Intent intent = new Intent(HeartBeatSplash.this, MainActivity.class);
        startActivity(intent);
        finish();*/

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        /*heartBeat();
        Intent intent = new Intent(HeartBeatSplash.this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }

    public void heartBeat(){
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                imageView,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(310);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        //scaleDown.setDuration(BASE_DURATION * 4);

        scaleDown.start();
    }

}
