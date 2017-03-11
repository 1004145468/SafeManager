package com.yl.safemanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.EncryptUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SmsLockActivity extends BaseTitleBackActivity {

    @BindView(R.id.sms_content)
    EditText mContextView;

    @BindView(R.id.sms_send)
    Button mSendSmsView;

    @BindView(R.id.sms_read)
    Button mReadSmsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void lockAndsendSms() {
        //加密发送短信
        String content = mContextView.getText().toString();
        DialogUtils.openEnterPwsDialog(this, true, content, null);
    }

    @OnClick(R.id.sms_read)
    public void readSms() {
        //解密查看短信
        String content = mContextView.getText().toString();
        DialogUtils.openEnterPwsDialog(this, false, content, new OnResultAttachedListener<String>() {
            @Override
            public void onResult(String s) {
                mContextView.setText(s);
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_sms_lock;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_SMSLOCK;
    }
}
