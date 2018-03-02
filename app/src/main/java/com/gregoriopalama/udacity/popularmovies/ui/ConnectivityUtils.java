package com.gregoriopalama.udacity.popularmovies.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class used to check if there is an available data connection
 *
 * @author Gregorio Palamà
 */
public class ConnectivityUtils {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork;
        try {
            activeNetwork = cm.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            return false;
        }

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
