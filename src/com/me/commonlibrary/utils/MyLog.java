package com.me.commonlibrary.utils;

import com.me.commonlibrary.BuildConfig;

import android.text.TextUtils;
import android.util.Log;

public class MyLog {
   private static boolean debug=BuildConfig.DEBUG;
   private String mTag;
   
   public <T> MyLog(Class<T> class1)
   {
       mTag=class1.getCanonicalName();
   }
   public void w(String msg)
   {
       if(!debug || TextUtils.isEmpty(msg))
           return;
       Log.w(mTag,msg);
   }
   public void i(String msg) {
       if (!debug || TextUtils.isEmpty(msg)) {
           return;
       }

       Log.i(mTag, msg);
   }

   public void d(String msg) {
       if (!debug || TextUtils.isEmpty(msg)) {
           return;
       }

       Log.d(mTag, msg);
   }

   public void e(String msg) {
       if (!debug || TextUtils.isEmpty(msg)) {
           return;
       }

       Log.e(mTag, msg);
   }

   public void e(String msg, Throwable t) {
       if (!debug || TextUtils.isEmpty(msg)) {
           return;
       }
       Log.e(mTag, msg + t.getMessage());
   }
}
