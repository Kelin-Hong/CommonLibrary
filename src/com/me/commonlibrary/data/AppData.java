package com.me.commonlibrary.data;
import android.content.Context;
import android.content.res.Resources;
public class AppData {
     private static Context sContext;
     static void init(Context context)
     {
         sContext=context;
     }
     public static Context getContext()
     {
         return sContext;
     }
     public static Resources getResources()
     {
         return sContext.getResources();
     }
     
     public static int getColor(int resId)
     {
         return sContext.getResources().getColor(resId);
     }
}
