package com.yl.safemanager.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.R;
import com.yl.safemanager.entities.AppInfo;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.DataBaseUtils;
import com.yl.safemanager.utils.EncryptUtils;
import com.yl.safemanager.utils.SpUtils;
import com.yl.safemanager.utils.ToastUtils;
import com.yl.safemanager.utils.WindowUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LockService extends Service {

    private static final String TAG = "LockService";

    private String mConfigPassword = null;
    private View mContentView;
    private List<AppInfo> mDatas;
    private Timer mTimer;
    private Handler mHander = new Handler();

    public LockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //查询所有加锁信息
        initDatas();
        //初始化展示面板
        initViews();
        //开启定时任务
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initDatas() {
        DataBaseUtils.initRealm(this);
        DataBaseUtils.getAllLockApps(new OnResultAttachedListener<List<AppInfo>>() {
            @Override
            public void onResult(List<AppInfo> appInfos) {
                mDatas = appInfos;
            }
        });
        DataBaseUtils.closeRealm();
        mConfigPassword = SpUtils.getString(this, "shortpassword");
    }

    private void initViews() {
        mContentView = View.inflate(this, R.layout.view_lock, null);
        final EditText passwordView = (EditText) mContentView.findViewById(R.id.et_password);
        final TextView hintView = (TextView) mContentView.findViewById(R.id.password_hint);
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                hintView.setVisibility(View.GONE);
                if (length == 6) {
                    String content = EncryptUtils.md5Encrypt(editable.toString());
                    if (content.equals(mConfigPassword)) {
                        //移除枷鎖面板
                        WindowUtils.removeFullScreenView(LockService.this, mContentView);
                    } else {
                        //提示密碼錯誤
                        passwordView.setText(""); //清除密碼
                        hintView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 开始监控
     */
    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new AppDog(), 0, 200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭timer对象
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
        if (mHander != null) {
            mHander.removeCallbacksAndMessages(null);
            mHander = null;
        }
    }

    /**
     * 定时任务
     */
    class AppDog extends TimerTask {

        private boolean isShowLockView = false;
        private boolean containerApp = false;
        private ActivityManager mActivityManager;
        private String topAppPackageName = null;

        public AppDog() {
            mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }

        @Override
        public void run() {
            topAppPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName(); //顶层应用的包名
            containerApp = false;
            for (AppInfo appInfo : mDatas) {
                if (topAppPackageName.equals(appInfo.getPackageName())) {
                    containerApp = true;
                    if (!isShowLockView) {//在加锁列表中并且没有添加解锁界面
                        isShowLockView = true;
                        mHander.post(new Runnable() {
                            @Override
                            public void run() {
                                WindowUtils.addFullScreenView(LockService.this, mContentView); //添加解鎖面板
                            }
                        });
                    }
                    break;
                }
            }

            if (isShowLockView && !containerApp) {  //添加解锁界面 并且么有在加锁列表中
                isShowLockView = false;
                mHander.post(new Runnable() {
                    @Override
                    public void run() {
                        WindowUtils.removeFullScreenView(LockService.this, mContentView);
                    }
                });
            }
        }
    }
}
