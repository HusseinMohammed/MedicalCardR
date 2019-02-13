package com.hyper.design.medicalcard.network;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Hyper Design Hussien on 1/22/2018.
 */

public class CheckNetwork {

    private static final String TAG = CheckNetwork.class.getSimpleName();
    private static AlertDialog.Builder alertDialogBuilder;


    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            alertDialogBuilder = new AlertDialog.Builder(
                    context);
            // set dialog message
            alertDialogBuilder
                    .setMessage("No Network Connection")
                    .setCancelable(false)
                    .setPositiveButton("ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //enable wifi
                            //wifiMan.setWifiEnabled(true);
                        }
                    });
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}
