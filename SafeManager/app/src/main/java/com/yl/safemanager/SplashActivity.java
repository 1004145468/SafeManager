package com.yl.safemanager;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.utils.SFGT;

/**
 * Created by YL on 2017/3/19.
 */

public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //TODO 获取TOken
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SFGT.gotoLoginActivity(SplashActivity.this);
                finish();
            }
        }, 3000);
    }
}
