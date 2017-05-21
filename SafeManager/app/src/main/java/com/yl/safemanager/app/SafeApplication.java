package com.yl.safemanager.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import io.rong.imkit.RongIM;

/**
 * Created by YL on 2017/2/24.
 */

public class SafeApplication extends Application {

    private static final String TAG = "SafeApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)  //减小对大图的装载
                .build();
        Fresco.initialize(this, config); //Fresco的初始化

        BmobConfig bmobConfig =new BmobConfig.Builder(this)
        .setApplicationId("ceef00eaf322eef098f0d8acae67d67c")
        .setConnectTimeout(5)
        .setUploadBlockSize(512*1024)
        .build();
        Bmob.initialize(bmobConfig); //Bmob的初始化

        RongIM.init(this); //融云初始化

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Log.e(TAG, "uncaughtException: " + throwable.toString());
            }
        });
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
