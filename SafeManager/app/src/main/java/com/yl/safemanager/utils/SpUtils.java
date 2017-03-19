package com.yl.safemanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

/**
 * Created by YL on 2017/2/27.
 */

public class SpUtils {

    private static  String SP_NAME = "config";

    public static void saveString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key,value);
        edit.commit();
        //SharedPreferencesCompat.EditorCompat.getInstance().apply(edit); //异步存放
    }

    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }
}
