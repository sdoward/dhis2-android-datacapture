package org.dhis2.ehealthMobile;

import org.dhis2.ehealthMobile.network.NetworkUtils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

public class EHealthAfricaApplication extends Application{

    private static NetworkUtils networkUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        networkUtils = new NetworkUtils((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    public static NetworkUtils getNetworkUtils() {
        return networkUtils;
    }
}
