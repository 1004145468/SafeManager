package com.yl.safemanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.safemanager.base.BaseTitleBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class InfoChangeActivity extends BaseTitleBackActivity {

    public static String CONTENY_TYPE = "content";
    public static int FIND_PSW = 0; // 找回密码
    private int mCurrentType = -1;

    @BindView(R.id.tv_modify_hint)
    TextView mModifyHintView;
    @BindView(R.id.et_modify_content)
    EditText mModifyContentView;
    @BindView(R.id.tv_motify_result)
    TextView mModifyResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCurrentType = getIntent().getIntExtra(CONTENY_TYPE, -1);
        super.onCreate(savedInstanceState);
        initmModifyHintView();
    }

    @Override
    public String getBarTitle() {
        switch (mCurrentType) {
            case 0:
                return getResources().getString(R.string.login_forgetpassword);
            default:
                return "";
        }
    }



    //配置功能提示
    private void initmModifyHintView() {
        switch (mCurrentType){
            case 0:
                mModifyHintView.setText(getResources().getString(R.string.user_hint));
                break;
        }
    }

    //配置修改结果显示
    private void initmModifyResultView(){
        switch (mCurrentType){
            case 0:
                mModifyResultView.setText(R.string.login_forgetpassword_mail);
                break;
        }
    }

    //修改用户信息
    @OnClick(R.id.btn_operation)
    public void startModity(){
        Toast.makeText(this, "开始修改用户信息", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_infochange;
    }
}
