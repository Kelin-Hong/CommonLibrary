package com.me.commonlibrary.data;
import android.app.Application;
public class CommonApplication extends Application {
  
    @Override
    public void onCreate()
    {
        super.onCreate();
        AppData.init(getApplicationContext());
    }
}
