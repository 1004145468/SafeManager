package com.yl.safemanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.yl.safemanager.adapter.FunctionAdapter;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.SafeFunctionInfo;
import com.yl.safemanager.entities.SafeFunctionItem;
import com.yl.safemanager.entities.TokenResult;
import com.yl.safemanager.interfact.OnHeadItemClickListener;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.ChatUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;

import static com.yl.safemanager.constant.Constant.FUNCTION_APPLOCK;
import static com.yl.safemanager.constant.Constant.FUNCTION_DATABACKUP;
import static com.yl.safemanager.constant.Constant.FUNCTION_DATARECORD;
import static com.yl.safemanager.constant.Constant.FUNCTION_DATARECOVER;
import static com.yl.safemanager.constant.Constant.FUNCTION_FILELOCK;
import static com.yl.safemanager.constant.Constant.FUNCTION_IDEA;
import static com.yl.safemanager.constant.Constant.FUNCTION_MAILLOCK;
import static com.yl.safemanager.constant.Constant.FUNCTION_SMSLOCK;

public class MainActivity extends BaseActivity implements OnHeadItemClickListener<SafeFunctionInfo> {

    private static final String TAG = "MainActivity";

    public static final int EXIT_CODE = 1;

    @BindView(R.id.tv_chathint)
    TextView mChatView;

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
    private FunctionAdapter functionAdapter;
    private ArrayList<SafeFunctionItem> datas;
    private ObjectAnimator mChatViewAnim;

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

    @Override
    protected void onResume() {
        super.onResume();
        if (datas.get(0).getCurrentType() == SafeFunctionItem.TYPE_HEAD) {
            datas.remove(0);
        }
        datas.add(0, new SafeFunctionItem(SafeFunctionItem.TYPE_HEAD, BmobUtils.getCurrentUser()));
        functionAdapter.notifyDataSetChanged();
    }

    private void initConfig() {
        mChatView.measure(0, 0);
        mChatView.setPivotX(0);
        mChatView.setPivotY(mChatView.getMeasuredHeight() / 2);
        mChatViewAnim = ObjectAnimator.ofFloat(mChatView, View.SCALE_X, 0, 1);
        mChatViewAnim.setDuration(500);
        mChatViewAnim.setInterpolator(new OvershootInterpolator());
        mChatViewAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mChatView.setVisibility(View.VISIBLE);
            }
        });

        valueAnimator = ValueAnimator.ofFloat(1);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(300);
    }

    //构造数据体
    private void initData() {
        datas = new ArrayList<>();
        SafeFunctionItem item = null;
        SafeFunctionInfo functionInfo = null;
        int length = mFunctionName.length;
        for (int i = 0; i < length; i++) {
            functionInfo = new SafeFunctionInfo(mFunctionName[i], mFunctionDesc[i], mFuntionImg[i]);
            item = new SafeFunctionItem(SafeFunctionItem.TYPE_FUNCTION, functionInfo);
            datas.add(item);
        }
        functionAdapter = new FunctionAdapter(this, datas);
        mFunctionViews.setAdapter(functionAdapter);
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

    @Override
    public void onClickMail() {
        enterChat();
    }

    @Override
    public void onClickInfo() {
        SFGT.gotoUserInfoActivity(this);
    }

    @Override
    public void onClick(SafeFunctionInfo info) {
        switch (info.getFunctionName()) {
            case FUNCTION_FILELOCK:
                SFGT.gotoFileLockActivity(this);
                break;
            case FUNCTION_SMSLOCK:
                SFGT.gotoSmsLockActivity(this);
                break;
            case FUNCTION_APPLOCK:
                SFGT.gotoAppLockActivity(this);
                break;
            case FUNCTION_DATABACKUP:
                SFGT.gotoBackupActivity(this);
                break;
            case FUNCTION_DATARECOVER:
                SFGT.gotoDownLoadFileActivity(this);
                break;
            case FUNCTION_DATARECORD:
                SFGT.gotoNoteRecordActivity(this);
                break;
            case FUNCTION_MAILLOCK:
                SFGT.gotoMailLockActivity(this);
                break;
            case FUNCTION_IDEA:
                SFGT.gotoAdviceActivity(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EXIT_CODE) {
                //进入登录界面
                SFGT.gotoLoginActivity(this);
                finish();
            }
        }
    }

    /**
     * 重写后退按钮功能
     */
    @Override
    public void onBackPressed() {
        DialogUtils.showMessageDialog(this, getString(R.string.exitapp), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void enterChat() {
        mChatViewAnim.start(); //开始动画
        ChatUtils.getTokenByPost(new OnResultAttachedListener<TokenResult>() {
            @Override
            public void onResult(TokenResult tokenResult) {
                if (tokenResult == null || TextUtils.isEmpty(tokenResult.getToken())) {
                    mChatView.setVisibility(View.GONE);
                    ToastUtils.showOriginToast(MainActivity.this, "服务器繁忙！");
                } else {
                    ChatUtils.connnect(MainActivity.this, tokenResult.getToken(), new RongIMClient.ConnectCallback() {
                        @Override
                        public void onTokenIncorrect() {
                            mChatView.setVisibility(View.GONE);
                            ToastUtils.showOriginToast(MainActivity.this, "连接失败，请重新尝试！");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            mChatView.setVisibility(View.GONE);
                            ToastUtils.showOriginToast(MainActivity.this, "连接失败，请重新尝试！");
                        }

                        @Override
                        public void onSuccess(String s) {
                            mChatView.setVisibility(View.GONE);
                            //打开聊天界面
                            SFGT.gotoConversationListActivity(MainActivity.this);
                        }
                    });
                }
            }
        });
    }
}
