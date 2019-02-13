package com.hyper.design.medicalcard.PushNotification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyper.design.medicalcard.R;
import com.hyper.design.medicalcard.app.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Hyper Design Hussien on 1/7/2018.
 */

public class MyFirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIDService";
    public static final String token = FirebaseInstanceId.getInstance().getToken();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        storeRegIdInPref(refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.fcm_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.fcm_token), refreshedToken);
        editor.commit();

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        Log.d("FCM", "Instance ID: " + FirebaseInstanceId.getInstance().getToken());
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

}
