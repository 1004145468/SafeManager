package com.yl.safemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.yl.safemanager.utils.SFGT.IMAGEPICK_REQUEST_CODE;

/**
 * Created by YL on 2017/3/17.
 */

public class UserInfoMotifyActivity extends BaseTitleBackActivity {

    public static final int CODE_CHANGE_NICK = 1;
    public static final int CODE_CHANGE_MSG = 2;
    public static final int CODE_CHANGE_SEX = 3;

    @BindView(R.id.me_head)
    SimpleDraweeView mHeadView;
    @BindView(R.id.me_nick)
    TextView mNickView;
    @BindView(R.id.me_id)
    TextView mIdView;
    @BindView(R.id.me_sex)
    TextView mSexView;
    @BindView(R.id.me_msg)
    TextView mMsgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        SafeUser currentUser = BmobUtils.getCurrentUser();
        if (currentUser != null) {
            mHeadView.setImageURI(currentUser.getmPortrait());
            mNickView.setText(currentUser.getmNick());
            mIdView.setText(currentUser.getUsername());
            mSexView.setText(currentUser.getmSex() == SafeUser.MAN ? getString(R.string.man) : getString(R.string.woman));
            mMsgView.setText(currentUser.getmNote());
        }
    }

    @OnClick(R.id.me_head)
    public void updateHead() {
        SFGT.gotoImagePick(this);
    }

    @OnClick(R.id.container_nick)
    public void changeNick() {
        SFGT.gotoChangeInfoActivity(this, CODE_CHANGE_NICK);
    }

    @OnClick(R.id.container_sex)
    public void changeSex() {
        //修改性别
        SFGT.gotoChangeSexActivity(this);
    }

    @OnClick(R.id.container_msg)
    public void changeMsg() {
        SFGT.gotoChangeInfoActivity(this, CODE_CHANGE_MSG);
    }

    @OnClick(R.id.container_changepsw)
    public void changePsw() { //更换密码
        SFGT.gotoChangePswActivity(this);
    }

    @OnClick(R.id.me_exit)  //退出登录
    public void exitUser() {
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String changeContent = data.getStringExtra("data");
        switch (requestCode) {
            case CODE_CHANGE_NICK: //修改昵称
                mNickView.setText(changeContent);
                break;
            case CODE_CHANGE_MSG:  //修改签名
                mMsgView.setText(changeContent);
                break;
            case CODE_CHANGE_SEX: //修改性别
                mSexView.setText(changeContent);
                break;
            case IMAGEPICK_REQUEST_CODE: //修改头像
                updateHeadInfo(data.getData());
                break;
        }
    }

    //更新头像信息
    private void updateHeadInfo(final Uri imageUri) {
        BmobUtils.uploadFile(this, imageUri, new BmobUtils.onUploadFileResult() {
            @Override
            public void onResult(BmobException e, final String fileUrl) {
                if (e != null) {
                    ToastUtils.showToast(UserInfoMotifyActivity.this, getString(R.string.upload_filefail), Effects.thumbSlider, R.id.id_root);
                } else {
                    SafeUser currentUser = BmobUtils.getCurrentUser();
                    currentUser.setmPortrait(fileUrl);
                    BmobUtils.updateInfo(currentUser, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                ToastUtils.showToast(UserInfoMotifyActivity.this, getString(R.string.headupdate_fail), Effects.thumbSlider, R.id.id_root);
                            } else {
                                mHeadView.setImageURI(fileUrl); //设置新图片
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_motifyuserinfo;
    }

    @Override
    public String getBarTitle() {
        return Constant.MOTIFY_USERINFO;
    }
}
