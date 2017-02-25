package com.yl.safemanager.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by YL on 2017/2/24.
 */

public class SafeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this); //Fresco的初始化
    }
}
