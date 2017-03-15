package com.yl.safemanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SMLockActivity extends BaseTitleBackActivity {

    public static final int SMS_TYPE = 1;
    public static final int MAIL_TYPE = 2;

    private int mCurrentType = 0;

    @BindView(R.id.sms_content)
    EditText mContextView;

    @BindView(R.id.sms_send)
    Button mSendSmsView;

    @BindView(R.id.sms_read)
    Button mReadSmsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCurrentType = getIntent().getIntExtra("type", SMS_TYPE);
        super.onCreate(savedInstanceState);
    }

    @OnTextChanged(R.id.sms_content)
    public void onTextChange() {
        if (!TextUtils.isEmpty(mContextView.getText().toString())) {
            mSendSmsView.setEnabled(true);
            mReadSmsView.setEnabled(true);
        } else {
            mSendSmsView.setEnabled(false);
            mReadSmsView.setEnabled(false);
        }
    }

    @OnClick(R.id.sms_send)
    public void lockAndsendSM() {
        //加密发送短信或者邮件
        String content = mContextView.getText().toString();
        DialogUtils.openEnterPwsDialog(this, true, mCurrentType, content, null);
    }

    @OnClick(R.id.sms_read)
    public void readSM() {
        //解密查看短信或者邮件
        String content = mContextView.getText().toString();
        DialogUtils.openEnterPwsDialog(this, false, mCurrentType, content, new OnResultAttachedListener<String>() {
            @Override
            public void onResult(String s) {
                mContextView.setText(s);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_sm_lock;
    }

    @Override
    public String getBarTitle() {
        if (mCurrentType == SMS_TYPE) {
            return Constant.FUNCTION_SMSLOCK;
        } else {
            return Constant.FUNCTION_MAILLOCK;
        }
    }
}
