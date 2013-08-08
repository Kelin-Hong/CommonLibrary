package com.me.commonlibrary.utils;

import com.me.commonlibrary.data.AppData;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CommonUtils {
  /**
   * 用UTF-8编码规则编码URL
   * @param param
   * @return
   */
    public static String encodeUrl(Map<String,String> param)
    {
        if(param==null) return "";
        StringBuilder sb=new StringBuilder();
        Set<Entry<String,String>> entries=param.entrySet();
        boolean first=true;
        for(Entry<String,String>  entry: entries)
        {
            String value=entry.getValue();
            String key=entry.getKey();
            if(!TextUtils.isEmpty(value))
            {
                if(first)
                {
                    first=false;
                }else
                {
                    sb.append("&");
                }
                try{
                    sb.append(URLEncoder.encode(key,"UTF-8")).append("=").append(URLEncoder.encode(value,"UTF-8"));
                }catch(UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
          
        }
        return sb.toString();
    }

    /**
     * 将 URL 转化为 Bundle
     * 
     * @param s
     * @return
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }
    public static void smoothScrollListViewToTop(ListView listView)
    {
        smoothScrollListView(listView,0,0,100);
    }
    
    public static void smoothScrollListView(ListView listView,int position,int offset,int duration)
    {
        if(VERSION.SDK_INT>7){ 
            listView.smoothScrollToPositionFromTop(position, offset, duration);
        }
    
    else{
        listView.setSelectionFromTop(position, offset);
       }
    }
    
    public static void installLauncherShortCut(Intent intent,String shortCutName,Bitmap icon,boolean duplicate){
       final Context context = null;
       Intent shortCutIntent=new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
       shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,shortCutName);
       shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON,icon);
       shortCutIntent.putExtra("duplicate",duplicate);
       intent.setAction(Intent.ACTION_MAIN);
       shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
       context.sendBroadcast(shortCutIntent);
    }
    
    public static int corvertDimentoPix(float dimen)
    {
        float density=AppData.getContext().getResources().getDisplayMetrics().density;
        return (int)(density*dimen+0.5f);
    }
    
    public static boolean isUrlValid(final String strUrl)
    {
        if(strUrl==null) return false;
        try{
            new URL(strUrl);
            return true;
        }catch(final MalformedURLException e)
            {
                return false;
            }
       
    }
    public static <Params,Progress,Result> void executeAsyncTask(AsyncTask<Params,Progress,Result> task,Params... params)
    {
        if(VERSION.SDK_INT>=11)
        {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }else
        {
            task.execute(params);
        }
    }
    public static enum SDCardState{
        AVAILABLE,UNAVAILABLE,FULL
    }
    
    public static SDCardState checkSDCardState()
    {
        SDCardState state=SDCardState.AVAILABLE;
        if(isSDCardUnavailable()||isSDCardBusy())
        {
            state=SDCardState.UNAVAILABLE;
        }else if(isSDCardFull())
        {
            state=SDCardState.FULL;
        }
        return state;
        
    }
    public static boolean isSDCardUnavailable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED);
    }
    
    public static boolean isSDCardBusy()
    {
        return !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 检查ＳＤ卡是否已满。如果ＳＤ卡的剩余空间小于１００ｋ，则认为ＳＤ卡已满。
     * 
     * @return
     */
    public static boolean isSDCardFull()
    {
        return getSDCardAvailableBytes()<=(100*1024);
    }
    
    public static boolean isSDCardUseful()
    {
        return (!isSDCardBusy()&&!isSDCardFull()&&!isSDCardUnavailable());
    }
    /**
     * 获取ＳＤ卡的剩余字节数。
     * 
     * @return
     */ 
    public static long getSDCardAvailableBytes()
    {
        if(isSDCardBusy()) return 0;
        final File path=Environment.getExternalStorageDirectory();
        final StatFs stat=new StatFs(path.getPath());
        final long blockSize=stat.getBlockSize();
        final long availableBlocks=stat.getAvailableBlocks();
        return blockSize*(availableBlocks-4);
    }

    /**
     * 在EditText 光标之后加入addString
     * 
     * @param editText
     * @param text
     */
    public static void insertTextToEditText(EditText editText,String text){
        int selectionIndex=editText.getSelectionStart();
        editText.getText().insert(selectionIndex, text);
        
    }
    
    public static boolean isIntentAvaiable(final Intent intent)
    {
        final PackageManager packageManager=AppData.getContext().getPackageManager();
        final List<ResolveInfo> list=packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size()>0;
    }
    
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
