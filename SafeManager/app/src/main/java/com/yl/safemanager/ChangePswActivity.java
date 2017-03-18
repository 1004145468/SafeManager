package com.yl.safemanager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.EncryptUtils;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by YL on 2017/3/18.
 */

public class ChangePswActivity extends BaseTitleBackActivity {

    @BindView(R.id.chpsw_oldpsw)
    EditText mOldPswView;
    @BindView(R.id.chpsw_oldhint)
    TextView mOldPswHintView;
    @BindView(R.id.chpsw_newpsw)
    EditText mNewPswView;
    @BindView(R.id.chpsw_newhint)
    TextView mNewPswHintView;
    @BindView(R.id.chpsw_newpsw1)
    EditText mNewPswView1;
    @BindView(R.id.chpsw_newhint1)
    TextView mNewPswHintView1;
    @BindView(R.id.chpsw_btn)
    Button mChangePswBtn;

    @OnFocusChange(R.id.chpsw_oldpsw)
    public void onOldPswFocusChange() {
        if (mOldPswView.hasFocus()) {
            return;
        }
        String oldpsw = mOldPswView.getText().toString().trim();
        if (TextUtils.isEmpty(oldpsw)) {
            mOldPswHintView.setVisibility(View.VISIBLE);
        } else {
            mOldPswHintView.setVisibility(View.INVISIBLE);
        }
    }

    @OnFocusChange(R.id.chpsw_newpsw)
    public void onNewPswFocusChange() {
        if (mNewPswView.hasFocus()) {
            return;
        }
        String newpsw = mNewPswView.getText().toString().trim();
        if (TextUtils.isEmpty(newpsw)) {
            mNewPswHintView.setVisibility(View.VISIBLE);
        } else {
            mNewPswHintView.setVisibility(View.INVISIBLE);
        }
    }

    @OnFocusChange(R.id.chpsw_newpsw1)
    public void onNewPsw1FocusChange() {
        if (mNewPswView1.hasFocus()) {
            return;
        }
        String newpsw = mNewPswView.getText().toString().trim();
        String againPsw = mNewPswView1.getText().toString().trim();
        if (againPsw == null || !againPsw.equals(newpsw)) {
            mNewPswHintView1.setVisibility(View.VISIBLE);
        } else {
            mNewPswHintView1.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.chpsw_btn)
    public void changePsw() {  //点击修改密码
        String oldpsw = mOldPswView.getText().toString().trim();
        String newpsw = mNewPswView.getText().toString().trim();
        String againpsw = mNewPswView1.getText().toString().trim();
        if (TextUtils.isEmpty(oldpsw)) {
            mOldPswHintView.setVisibility(View.VISIBLE);
            return;
        } else {
            mOldPswHintView.setVisibility(View.INVISIBLE);
        }

        if (TextUtils.isEmpty(newpsw)) {
            mNewPswHintView.setVisibility(View.VISIBLE);
            return;
        } else {
            mNewPswHintView.setVisibility(View.INVISIBLE);
        }

        if (againpsw == null || !againpsw.equals(newpsw)) {
            mNewPswHintView1.setVisibility(View.VISIBLE);
            return;
        } else {
            mNewPswHintView1.setVisibility(View.INVISIBLE);
        }
        mChangePswBtn.setText(getString(R.string.changing_password));
        BmobUtils.changePsw(oldpsw, newpsw, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mChangePswBtn.setText(getString(R.string.chpsw_ok));
                if (e != null) {
                    if (e.getErrorCode() == 210) {
                        ToastUtils.showToast(ChangePswActivity.this, getString(R.string.oldpsw_error), Effects.thumbSlider, R.id.id_root);
                    } else {
                        ToastUtils.showToast(ChangePswActivity.this, getString(R.string.changepsw_fail), Effects.thumbSlider, R.id.id_root);
                    }
                } else {
                    finish();
                }
            }
        });
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_changepsw;
    }

    @Override
    public String getBarTitle() {
        return Constant.CHANGE_PSW;
    }
}
