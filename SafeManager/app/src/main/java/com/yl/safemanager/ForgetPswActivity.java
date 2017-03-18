package com.yl.safemanager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.RegexUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPswActivity extends BaseTitleBackActivity {

    @BindView(R.id.et_modify_content)
    EditText mModifyContentView;
    @BindView(R.id.tv_motify_result)
    TextView mModifyResultView;
    @BindView(R.id.btn_operation)
    Button operatonBtn;

    @Override
    public String getBarTitle() {
        return getResources().getString(R.string.login_forgetpassword);
    }

    //重置密码
    @OnClick(R.id.btn_operation)
    public void startModity() {
        mModifyResultView.setText("");
        String email = mModifyContentView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            return;
        }
        if (!RegexUtils.isEmail(email)) {
            return;
        }
        operatonBtn.setText(getString(R.string.reset_forgetpassword));
        BmobUtils.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                operatonBtn.setText(getString(R.string.ok));
                if (e == null) {
                    mModifyResultView.setText(getString(R.string.login_forgetpassword_mail));
                } else {
                    mModifyResultView.setText(getString(R.string.login_forgetpassword_mailfail));
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgetpsw;
    }
}
