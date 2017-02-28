package com.yl.safemanager;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.SpUtils;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    SimpleDraweeView mHeadView;
    @BindView(R.id.et_user)
    EditText mUserView;
    @BindView(R.id.iv_userdelete)
    ImageView mUserDeleteView;
    @BindView(R.id.et_password)
    EditText mPasswordView;
    @BindView(R.id.iv_passwordeye)
    ImageView mPasswordEyeView;
    @BindView(R.id.tv_forgetpassword)
    TextView mForgetView;
    @BindView(R.id.tv_register)
    TextView mRegisterView;

    private boolean isVisiable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//初始化ButterKnife的綁定
        mForgetView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);//设置下划线
        mRegisterView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);//设置下划线
        mUserView.addTextChangedListener(new TextChangeHanlder(R.id.et_user));
        mPasswordView.addTextChangedListener(new TextChangeHanlder(R.id.et_password));
    }


    @OnClick(R.id.iv_userdelete)
    public void clearUserName() {
        mUserView.setText("");
    }

    @OnClick(R.id.iv_passwordeye)
    public void clearPassword() {
        isVisiable = !isVisiable;
        if (isVisiable) { //显示密码
            mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {          //隐藏密码
            mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.tv_forgetpassword)
    public void forgetPassword() {
        SFGT.gotoForgetPswActivity(this);
    }

    @OnClick(R.id.tv_register)
    public void onRegister() {
        SFGT.gotoRegisterActivity(this);
    }


    @OnClick(R.id.btn_login)
    public void login() {
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(username)) {
            ToastUtils.showToast(this, getString(R.string.username_empty), Effects.thumbSlider, R.id.id_root);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast(this, getString(R.string.password_empty), Effects.thumbSlider, R.id.id_root);
            return;
        }
        //用户登录
        DialogUtils.showIndeterminateDialog(this,getString(R.string.load_userinfo),false,null);
        BmobUtils.login(username, password, new SaveListener<SafeUser>() {
            @Override
            public void done(SafeUser safeUser, BmobException e) {
                DialogUtils.shutdownIndeterminateDialog();
                if(e != null){
                    ToastUtils.showToast(LoginActivity.this, getString(R.string.userinfo_empty), Effects.thumbSlider, R.id.id_root);
                }else{
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class TextChangeHanlder implements TextWatcher {
        private int mViewId;

        public TextChangeHanlder(int viewid) {
            mViewId = viewid;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (mViewId) {
                case R.id.et_user:
                    if (TextUtils.isEmpty(mUserView.getText().toString())) {
                        mUserDeleteView.setVisibility(View.INVISIBLE);
                    } else {
                        String headViewInfo = SpUtils.getString(getApplication(), mUserView.getText().toString()); //是否登陆过
                        mHeadView.setImageURI(headViewInfo);
                        mUserDeleteView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.et_password:
                    if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                        mPasswordEyeView.setVisibility(View.INVISIBLE);
                    } else {
                        mPasswordEyeView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
    }


}
