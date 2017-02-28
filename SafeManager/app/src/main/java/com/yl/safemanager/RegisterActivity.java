package com.yl.safemanager;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.RegexUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.SpUtils;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends BaseTitleBackActivity {

    private static final String TAG = "RegisterActivity";

    @BindView(R.id.iv_head)
    SimpleDraweeView mHeadView;
    @BindView(R.id.et_username)
    EditText mUserNameView;
    @BindView(R.id.et_password)
    EditText mPasswordView;
    @BindView(R.id.et_password_again)
    EditText mPassword2View;

    private Uri ImageUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public String getBarTitle() {
        return getResources().getString(R.string.register);
    }


    @OnClick(R.id.iv_head)
    public void changePortrait() {
        //打开图库
        SFGT.gotoImagePick(this);
    }

    @OnClick(R.id.btn_regeist)
    public void onRegeist() {
        //开始注册
        final String username = mUserNameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        String password1 = mPassword2View.getText().toString();
        //非空判断
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password1)) {
            ToastUtils.showToast(this, getResources().getString(R.string.infoisnocomplete), Effects.slideIn, R.id.id_root);
            return;
        }
        //邮箱验证
        if (!RegexUtils.isEmail(username)) {
            ToastUtils.showToast(this, getResources().getString(R.string.isnotmail), Effects.slideIn, R.id.id_root);
            return;
        }
        //两次密码输入不一致
        if (!password1.equals(password)) {
            ToastUtils.showToast(this, getResources().getString(R.string.passwordnotequal), Effects.slideIn, R.id.id_root);
            return;
        }
        DialogUtils.showIndeterminateDialog(this, getResources().getString(R.string.onregister), false, null);
        BmobUtils.signUpWithFile(this, username, password, ImageUri, new SaveListener<SafeUser>() {
            @Override
            public void done(SafeUser safeUser, BmobException e) {
                DialogUtils.shutdownIndeterminateDialog();
                String errorMsg = null;
                if (e != null) {
                    errorMsg = getResources().getString(R.string.default_error);
                    if (e.getErrorCode() == 202) {
                        errorMsg = getResources().getString(R.string.username_exist);
                    } else if (e.getErrorCode() == 9015) {
                        errorMsg = getResources().getString(R.string.default_error);
                    }
                } else {
                    errorMsg = getResources().getString(R.string.register_success);
                    SpUtils.saveString(getApplicationContext(), username, ImageUri == null ? "" : ImageUri.toString());//保存头像信息
                }
                ToastUtils.showToast(RegisterActivity.this, errorMsg, Effects.standard, R.id.id_root);
            }
        });
    }

    @OnClick(R.id.btn_login)
    public void gotoLogin() {
        //开始登录
        finish();//下滑退出
        overridePendingTransition(R.anim.slide_toleft, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SFGT.IMAGEPICK_REQUEST_CODE) {
                //获取图片信息
                ImageUri = data.getData();
                mHeadView.setImageURI(ImageUri);
            }
        }
    }
}
