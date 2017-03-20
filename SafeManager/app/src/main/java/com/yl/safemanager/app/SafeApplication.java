package com.yl.safemanager.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;

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
        RongIM.init(this); //融云初始化
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Log.e(TAG, "uncaughtException: " + throwable.toString());  //防止Bmob网络中断情况 应用崩溃
            }
        });*/
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
