package com.yl.safemanager.services;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yl.safemanager.LockConfigActivity;
import com.yl.safemanager.R;
import com.yl.safemanager.utils.AppUtils;
import com.yl.safemanager.utils.EncryptUtils;
import com.yl.safemanager.utils.SpUtils;
import com.yl.safemanager.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LockService extends Service {

    private static final String TAG = "LockService";

    private String mConfigPassword = null;
    private View mContentView;
    private List<String> mDatas;
    private Timer mTimer;
    private Handler mHander = new Handler();

    private boolean isShowLockView = false;
    private boolean canShow = true;
    private String currentLockPackageName = "";

    public LockService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //查询所有加锁信息
        if (initDatas(intent)) {
            //初始化展示面板
            initViews();
            //开启定时任务
            startTimer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean initDatas(Intent intent) {
        boolean shouldTimer = false;
        ArrayList<String> appInfos = intent.getStringArrayListExtra("lockapps");
        if (appInfos != null && appInfos.size() > 0) {
            mDatas = new ArrayList<>();
            mDatas.addAll(appInfos);
            shouldTimer = true;
        }
        mConfigPassword = SpUtils.getString(this, LockConfigActivity.SHORT_CODE);
        return shouldTimer;
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
                    if (mConfigPassword != null && content.equals(mConfigPassword)) {
                        //移除枷鎖面板
                        passwordView.setText(""); //清除密碼
                        WindowUtils.removeFullScreenView(LockService.this, mContentView);
                        isShowLockView = false;
                        canShow = false;
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

        private boolean containerApp = false;
        private String topAppPackageName = null;
        private ActivityManager mActivityManager; //5.0以下
        private UsageStatsManager usageStatsManager;  //5.0 及以上

        public AppDog() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //大于5.0
                usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            } else {
                mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            }
        }

        @Override
        public void run() {
            topAppPackageName = AppUtils.getTopAppPackageName(mActivityManager, usageStatsManager);//顶层应用的包名
            if (topAppPackageName == null) {
                return;
            }
            containerApp = false;
            for (String  packageName : mDatas) {
                if (topAppPackageName.equals(packageName)) {
                    Log.d(TAG, "run: 当前应用在加锁列表中=========" + topAppPackageName);
                    containerApp = true;
                    if (!isShowLockView && canShow) {//在加锁列表中并且没有添加解锁界面
                        currentLockPackageName = topAppPackageName;
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

            if (!canShow && !topAppPackageName.equals(currentLockPackageName)) {
                canShow = true;
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
