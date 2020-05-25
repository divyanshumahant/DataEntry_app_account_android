package com.calculator.dataentry.common;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMob = cm.getNetworkInfo(cm.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(cm.TYPE_WIFI);
        if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
            return true;
        }

        if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()) {

            return true;
        }
        return false;

    }

    public boolean isConnectingToInternetOffline(){
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //new MainActivity().callService();

            // CustomLogger.getInsatance(_context).putLog("Network Connected");
            // _context.startService(new Intent(_context, SendOfflineDataService.class));

            //SendOfflineCall();
        }
        return false;

    }
}
