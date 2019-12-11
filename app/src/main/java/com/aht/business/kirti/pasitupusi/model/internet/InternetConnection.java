package com.aht.business.kirti.pasitupusi.model.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class InternetConnection {

    private InternetConnection() {

    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;

            /*try {
                URL url = new URL("http://www.google.co.in/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                } else {
                    System.out.println(urlc.getResponseCode());
                    return false;
                }
            } catch (Exception e) {
                //Log.i("warning", "Error checking internet connection", e);
                System.out.println(e.getMessage());
                return false;
            }*/
        } else {

            System.out.println(activeNetwork);
        }

        return false;

    }

}
