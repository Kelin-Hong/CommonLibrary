package com.me.commonlibrary.utils;

import com.me.commonlibrary.data.AppData;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtils {
       public static void showKeyBoard(final EditText editText)
       {
           if(editText==null)
           {
               return;
           }
           editText.requestFocus();
           editText.post(new Runnable(){

            public void run() {

             InputMethodManager imm=(InputMethodManager) AppData.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
             imm.showSoftInput(editText,InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
               
           });
       }
       
       public static void hideSoftInput(EditText editText){
           if(editText==null){
               return;
           }
           InputMethodManager inputMethodManager = (InputMethodManager) AppData.getContext()
                   .getSystemService(Context.INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
       }
}
