package com.yl.safemanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditSmDataActivity extends BaseTitleBackActivity {

    @BindView(R.id.smdata_title)
    EditText mTitleView;
    @BindView(R.id.smdata_content)
    EditText mContentView;
    @BindView(R.id.smdata_time)
    TextView mTimeView;
    private String objectId;
    private String title;
    private String content;

    private SimpleDateFormat mDateFormater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        objectId = getIntent().getStringExtra("objectid");
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        String savetime = getIntent().getStringExtra("savetime");
        initViews(title, content, savetime);
    }

    private void initViews(String title, String content, String savetime) {
        mTitleView.setText(title);
        mContentView.setText(content);
        mTimeView.setText(savetime);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_sm_data;
    }

    @Override
    public String getBarTitle() {
        return Constant.EDIT_RECORD;
    }

    @OnClick(R.id.smdata_btn)
    public void updateSmdata() {
        String currentTitle = mTitleView.getText().toString();
        String currentContent = mContentView.getText().toString();
        if (currentTitle.equals(title) && currentContent.equals(content)) {
            return;
        }

        SmDataModel smDataModel = new SmDataModel(mDateFormater.format(new Date()), currentTitle, currentContent);
        smDataModel.setObjectId(objectId);
        BmobUtils.updateInfo(smDataModel, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) {
                    ToastUtils.showOriginToast(EditSmDataActivity.this,"更新失败");
                }else{
                    ToastUtils.showOriginToast(EditSmDataActivity.this,"更新成功");
                }
            }
        });
    }
}
