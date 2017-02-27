package com.yl.safemanager;

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

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.SFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.iv_head)
    ImageView mHeadView;
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
    public void clearUserName(){
        mUserView.setText("");
    }

    @OnClick(R.id.iv_passwordeye)
    public void clearPassword(){
        isVisiable = !isVisiable;
        if(isVisiable){ //显示密码
            mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{          //隐藏密码
            mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @OnClick(R.id.tv_forgetpassword)
    public void forgetPassword(){
       SFGT.gotoForgetPswActivity(this);
    }

    @OnClick(R.id.tv_register)
    public void onRegister(){
        SFGT.gotoRegisterActivity(this);
    }


    @OnClick(R.id.btn_login)
    public void login(){
        //TODO 用户的登录

    }

    class TextChangeHanlder implements TextWatcher{
        private  int mViewId;

        public TextChangeHanlder(int viewid){
            mViewId = viewid;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (mViewId){
                case R.id.et_user:
                    if(TextUtils.isEmpty(mUserView.getText().toString())){
                        mUserDeleteView.setVisibility(View.INVISIBLE);
                    }else{
                        mUserDeleteView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.et_password:
                    if(TextUtils.isEmpty(mPasswordView.getText().toString())){
                        mPasswordEyeView.setVisibility(View.INVISIBLE);
                    }else{
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
