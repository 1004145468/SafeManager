package com.yl.safemanager;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.yl.safemanager.adapter.FunctionAdapter;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.SafeFunctionInfo;
import com.yl.safemanager.entities.SafeFunctionItem;
import com.yl.safemanager.utils.BmobUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static junit.runner.Version.id;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.rv_function)
    RecyclerView mFunctionViews;

    @BindArray(R.array.functionnames)
    String[] mFunctionName;

    @BindArray(R.array.functiondescrips)
    String[] mFunctionDesc;

    //功能图标
    private int[] mFuntionImg = {R.drawable.filestory, R.drawable.sms, R.drawable.app,
                                R.drawable.backup, R.drawable.recovery, R.drawable.record,
                                R.drawable.mail, R.drawable.idea};

    private LinearLayoutManager linearLayoutManager;

    private View mHeadView;
    private int mHeadViewHeight = 0;
    private double mStartY = 0;
    private boolean isScale = false;
    private ValueAnimator valueAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFunctionViews.setLayoutManager(linearLayoutManager);
        mFunctionViews.addItemDecoration(new SafeItemDecoration());
        initConfig();
        initData();
        initListener();
    }

    private void initConfig() {
        valueAnimator = ValueAnimator.ofFloat(1);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(300);
    }

    //构造数据体
    private void initData() {
        ArrayList<SafeFunctionItem> datas = new ArrayList<>();
        SafeFunctionItem item = new SafeFunctionItem(SafeFunctionItem.TYPE_HEAD, BmobUtils.getCurrentUser());
        datas.add(item);
        SafeFunctionInfo functionInfo = null;
        int length = mFunctionName.length;
        for (int i = 0; i < length; i++) {
            functionInfo = new SafeFunctionInfo(mFunctionName[i], mFunctionDesc[i], mFuntionImg[i]);
            item = new SafeFunctionItem(SafeFunctionItem.TYPE_FUNCTION, functionInfo);
            datas.add(item);
        }
        mFunctionViews.setAdapter(new FunctionAdapter(this, datas));
    }

    private void initListener() {

        mFunctionViews.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeadView = mFunctionViews.getChildAt(0);
                mHeadViewHeight = mHeadView.getMeasuredHeight();
                mFunctionViews.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });

        //为Recyclerview设置滑动监听
        mFunctionViews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        double tmpY = motionEvent.getY();
                        double dy = tmpY - mStartY;
                        if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && dy > 0) {
                            ViewGroup.LayoutParams layoutParams = mHeadView.getLayoutParams();
                            layoutParams.height = (int) (layoutParams.height + dy * 0.6f);
                            mHeadView.requestLayout();
                            mStartY = tmpY;
                            isScale = true;
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isScale) {
                            linearLayoutManager.scrollToPosition(0);
                            overshootSmoothToPosition();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void overshootSmoothToPosition() {
        final ViewGroup.LayoutParams layoutParams = mHeadView.getLayoutParams();
        final int height = layoutParams.height;
        final int tmpHeight = height - mHeadViewHeight;
        valueAnimator.removeAllUpdateListeners();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                layoutParams.height = (int) (height - tmpHeight * animatedValue);
                mHeadView.requestLayout();
                isScale = false;
            }
        });
        valueAnimator.start();
    }
}
