package com.me.commonlibrary.utils;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PackageChangeReceiver extends BroadcastReceiver {
    private static MyLog mMyLog=new MyLog(PackageChangeReceiver.class);
    public static final int TYPE_ADD = 1;

    public static final int TYPE_REMOVE = 2;

// public static final int TYPE_REPLACED = 3;

    /**
     * These methods will be called in UI thread.
     */
    public interface PackageListener {
        /**
         * Will be called in UI thread
         */
        void onPackageChanged(Context cxt, String pkgName, int uid, int type);
    }

    private static List<WeakReference<PackageListener>> sListeners =
            new ArrayList<WeakReference<PackageListener>>();

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        mMyLog.i( "Received: " + intent.getAction()
                    + ", data: " + intent.getDataString() + ", replacing: " + replacing);

        notifyPackageChange(context, intent);
    }

    private void notifyPackageChange(Context cxt, Intent intent) {
        String action = intent.getAction();
        boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);
        int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
        String pkgName = URI.create(intent.getDataString()).getSchemeSpecificPart();

        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            // New app installed
            if (!replacing) {
                notifyPackageChange(cxt, pkgName, uid, TYPE_ADD);
               // do something when application replace
            }
        } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
            notifyPackageChange(cxt, pkgName, uid, TYPE_REMOVE);
        }
    }

    public static void registerListener(PackageListener l) {
        if (l == null) {
            mMyLog.w("null listener not allowed");
            return;
        }
        synchronized (sListeners) {
            for (WeakReference<PackageListener> listenerHolder : sListeners) {
                if (listenerHolder.get() == l) return;
            }
            sListeners.add(new WeakReference<PackageListener>(l));
        }
    }

    public static void unregisterListener(PackageListener l) {
        if (l == null) {
            mMyLog.w( "null listener not allowed");
            return;
        }
        synchronized (sListeners) {
            final int N = sListeners.size();
            for (int i = 0; i < N; i++) {
                if (sListeners.get(i).get() == l) {
                    sListeners.remove(i);
                    return;
                }
            }
        }
    }

    private static void notifyPackageChange(Context cxt, String pkgName, int uid, int type) {
        int curSize;
        synchronized (sListeners) {
            curSize = sListeners.size();
            for (int i = 0; i < sListeners.size();) {
                PackageListener l = sListeners.get(i).get();
                if (l == null) {
                    sListeners.remove(i);
                } else {
                    l.onPackageChanged(cxt, pkgName, uid, type);
                    i++;
                }
            }
        }
       mMyLog.d("notify change: " + curSize);
    }

}
