package com.monsoonblessing.kevinfaust.smartpark.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Kevin Faust on 12/10/2016.
 */

public class InternetConnectivityUtils {

    public static boolean isConnectedToInternet(Context context) {
        /*
        Returns true if we are connected to the internet
         */
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}
