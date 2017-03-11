package com.yl.safemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yl.safemanager.adapter.AppLockAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.AppInfo;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.services.LockService;
import com.yl.safemanager.utils.AppUtils;
import com.yl.safemanager.utils.DataBaseUtils;
import com.yl.safemanager.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AppLockActivity extends BaseTitleBackActivity {

    @BindView(R.id.applockrecyclerview)
    RecyclerView mAppLockRecyclerview;

    @BindView(R.id.button_stop)
    ImageView mStopButton;

    @BindView(R.id.button_playing)
    ProgressBar mPlayingButton;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            DialogUtils.shutdownIndeterminateDialog();
        }
    };

    private ArrayList<AppInfo> mDatas;
    private AppLockAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseUtils.initRealm(this);
        initViews();
        initDatas();
    }

    private void initViews() {
        mDatas = new ArrayList<>();
        mAppLockRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAppLockRecyclerview.addItemDecoration(new SafeItemDecoration());
        mAdapter = new AppLockAdapter(this, mDatas);
        mAppLockRecyclerview.setAdapter(mAdapter);
        //初始按钮
        boolean isRunning = AppUtils.isRunByServiceName(this, "services.LockService");
        mPlayingButton.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        mStopButton.setVisibility(isRunning ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化数据加载
     */
    private void initDatas() {
        DialogUtils.showIndeterminateDialog(this, getString(R.string.loading_appinfo), false, null);
        DataBaseUtils.getAllLockApps(new OnResultAttachedListener<List<AppInfo>>() {
            @Override
            public void onResult(final List<AppInfo> lockapps) {
                //获取曾经需要加密过得
                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<AppInfo> appInfos = AppUtils.loadAppInfos(AppLockActivity.this);
                        for (AppInfo lockapp : lockapps) {
                            for (AppInfo app : appInfos) {
                                if (lockapp.getPackageName().equals(app.getPackageName())) {
                                    app.setSelect(true);
                                    break;
                                }
                            }
                        }
                        mDatas.addAll(appInfos);
                        mHandler.sendEmptyMessage(0x110);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseUtils.closeRealm();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_app_lock;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_APPLOCK;
    }

    @OnClick(R.id.button_stop)
    public void startService() {
        mStopButton.setVisibility(View.GONE);
        mPlayingButton.setVisibility(View.VISIBLE);
        //开启加锁服务
        Intent intent = new Intent(this, LockService.class);
        startService(intent);
    }

    @OnClick(R.id.button_playing)
    public void stopService() {
        mStopButton.setVisibility(View.VISIBLE);
        mPlayingButton.setVisibility(View.GONE);
        //关闭加锁服务
        Intent intent = new Intent(this, LockService.class);
        stopService(intent);
    }
}
