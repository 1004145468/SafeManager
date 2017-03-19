package com.yl.safemanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yl.safemanager.utils.SFGT;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AppLockActivity extends BaseTitleBackActivity {

    @BindView(R.id.applockrecyclerview)
    RecyclerView mAppLockRecyclerview;

    @BindView(R.id.button_playing)
    ImageView mPlayingButton;

    @BindView(R.id.button_lockconfig)
    ImageView mLockConfigButton;

    @BindView(R.id.function_open)
    TextView mStartBtn;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            DialogUtils.shutdownIndeterminateDialog();
        }
    };

    private ArrayList<AppInfo> mDatas;
    private AppLockAdapter mAdapter;
    private AnimatorSet mStartBtnAnim;
    private AnimatorSet mPlayingBtnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseUtils.initRealm(this);
        initAnim();
        initViews();
        initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initShows();
    }

    private void initShows() {
        //初始按钮
        boolean isRunning = AppUtils.isRunByServiceName(this, ".services.LockService");
        mPlayingButton.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        mLockConfigButton.setVisibility(isRunning ? View.VISIBLE : View.GONE);
        mStartBtn.setVisibility(isRunning ? View.GONE : View.VISIBLE);
    }

    private void initAnim() {
        int withHeight = getResources().getDisplayMetrics().heightPixels;
        mStartBtnAnim = new AnimatorSet();
        ObjectAnimator StartBtnAnimX = ObjectAnimator.ofFloat(mStartBtn, View.SCALE_X, 0, 1);
        ObjectAnimator StartBtnAnimY = ObjectAnimator.ofFloat(mStartBtn, View.SCALE_Y, 0, 1);
        ObjectAnimator StartBtnupAnim = ObjectAnimator.ofFloat(mStartBtn, View.TRANSLATION_Y, 100, 0);
        StartBtnAnimX.setDuration(800);
        StartBtnAnimY.setDuration(800);
        StartBtnupAnim.setDuration(800);
        StartBtnupAnim.setInterpolator(new OvershootInterpolator());
        mStartBtnAnim.play(StartBtnAnimX).with(StartBtnAnimY).with(StartBtnupAnim);
        mStartBtnAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mStartBtn.setVisibility(View.VISIBLE);
                mLockConfigButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(AppLockActivity.this, LockService.class);
                stopService(intent);
            }
        });

        mPlayingBtnAnim = new AnimatorSet();
        ObjectAnimator PlayingBtnAnimX = ObjectAnimator.ofFloat(mPlayingButton, View.SCALE_X, 0, 1);
        ObjectAnimator PlayingBtnAnimY = ObjectAnimator.ofFloat(mPlayingButton, View.SCALE_Y, 0, 1);
        ObjectAnimator PlayingBtnDownAnim = ObjectAnimator.ofFloat(mPlayingButton, View.TRANSLATION_Y, -withHeight, 0);
        PlayingBtnAnimX.setDuration(800);
        PlayingBtnAnimY.setDuration(800);
        PlayingBtnDownAnim.setDuration(800);
        mPlayingBtnAnim.play(PlayingBtnAnimX).with(PlayingBtnAnimY).with(PlayingBtnDownAnim);
        mPlayingBtnAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mPlayingButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLockConfigButton.setVisibility(View.VISIBLE);
                Intent intent = new Intent(AppLockActivity.this, LockService.class);
                startService(intent);
            }
        });
    }

    private void initViews() {
        mDatas = new ArrayList<>();
        mAppLockRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAppLockRecyclerview.addItemDecoration(new SafeItemDecoration());
        mAdapter = new AppLockAdapter(this, mDatas);
        mAppLockRecyclerview.setAdapter(mAdapter);
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

    @OnClick(R.id.function_open) //开启加锁服务
    public void startService() {
        mStartBtn.setVisibility(View.GONE);
        mPlayingBtnAnim.start();
    }

    @OnClick(R.id.button_playing)  //关闭加锁服务
    public void stopService() {
        mPlayingButton.setVisibility(View.GONE);
        mStartBtnAnim.start();
    }

    @OnClick(R.id.button_lockconfig)  //设置解锁信息
    public void setLockConfig() {
        Intent intent = new Intent(AppLockActivity.this, LockService.class);
        stopService(intent);
        SFGT.gotoLockConfigActivity(this);
    }
}
