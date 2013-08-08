package com.me.commonlibrary.utils;

import com.me.commonlibrary.data.AppData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public static boolean hasNetwork()
    {
        boolean result=true;
        final ConnectivityManager cm=(ConnectivityManager) AppData.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo =cm.getActiveNetworkInfo();
        if(networkInfo==null||!networkInfo.isConnectedOrConnecting())
        {
            result=false;
        }
      return result;   
    }
    
    public static int getActiveNetworkType()
    {
        int defaultValue=-1;
        ConnectivityManager cm=(ConnectivityManager)AppData.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm==null) return defaultValue;
        NetworkInfo info=cm.getActiveNetworkInfo();
        if(info==null) return defaultValue;
        return info.getType();
    }
}
