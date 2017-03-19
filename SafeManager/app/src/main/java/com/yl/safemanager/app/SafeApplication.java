package com.yl.safemanager.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;

/**
 * Created by YL on 2017/2/24.
 */

public class SafeApplication extends Application {

    private static final String TAG = "SafeApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this); //Fresco的初始化
        Bmob.initialize(this, "ceef00eaf322eef098f0d8acae67d67c"); //Bmob的初始化
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Log.e(TAG, "uncaughtException: " + throwable.toString());  //防止Bmob网络中断情况 应用崩溃
            }
        });*/
    }
}
