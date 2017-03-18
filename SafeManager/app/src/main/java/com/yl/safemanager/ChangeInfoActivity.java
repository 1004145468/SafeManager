package com.yl.safemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.utils.BmobUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.yl.safemanager.UserInfoMotifyActivity.CODE_CHANGE_MSG;
import static com.yl.safemanager.UserInfoMotifyActivity.CODE_CHANGE_NICK;

/**
 * Created by YL on 2017/3/17.
 */

public class ChangeInfoActivity extends BaseTitleBackActivity {

    private static final String TAG = "ChangeNickActivity";

    private int currentType = 0;

    @BindView(R.id.nick_content)
    EditText mContentView;

    @BindView(R.id.nick_textwatcher)
    TextView mTextWatcher;

    @BindView(R.id.info_update)
    ProgressBar mUpdateBar;

    private SafeUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentType = getIntent().getIntExtra("type", CODE_CHANGE_NICK);
        super.onCreate(savedInstanceState);
        currentUser = BmobUtils.getCurrentUser();
        initViews();
    }

    private void initViews() {
        if (currentType == CODE_CHANGE_NICK) {
            mContentView.setText(currentUser.getmNick());
            mContentView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});  //设置最大字数
        } else if (currentType == CODE_CHANGE_MSG) {
            mContentView.setText(currentUser.getmNote());
            mContentView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        }
    }

    @OnTextChanged(R.id.nick_content)
    public void onTextChange() {
        int contentSize = mContentView.getText().toString().length();
        mTextWatcher.setText(contentSize + "");
    }

    @OnClick(R.id.nick_save)
    public void saveInfo() {
        final String newInfo = mContentView.getText().toString();
        if (currentType == CODE_CHANGE_NICK) {
            if (newInfo.equals(currentUser.getmNick())) {
                finish(); //没有做修改
                return;
            } else {
                currentUser.setmNick(newInfo);
            }
        } else if (currentType == CODE_CHANGE_MSG) {
            if (newInfo.equals(currentUser.getmNote())) {
                finish();
                return;
            } else {
                currentUser.setmNote(newInfo);
            }
        }
        mUpdateBar.setVisibility(View.VISIBLE);
        BmobUtils.updateInfo(currentUser, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mUpdateBar.setVisibility(View.GONE);
                if(e == null){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("data", newInfo);
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_changeinfo;
    }

    @Override
    public String getBarTitle() {
        if (currentType == CODE_CHANGE_NICK) {
            return Constant.CHANGE_NICK;
        } else if (currentType == CODE_CHANGE_MSG) {
            return Constant.CHANGE_MSG;
        } else {
            return Constant.CHANGE_UNKNOW;
        }
    }
}
