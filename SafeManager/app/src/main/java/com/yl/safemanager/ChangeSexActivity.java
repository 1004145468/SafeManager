package com.yl.safemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.yl.safemanager.entities.SafeUser.MAN;
import static com.yl.safemanager.entities.SafeUser.WOMAN;

/**
 * Created by YL on 2017/3/17.
 */

public class ChangeSexActivity extends BaseTitleBackActivity {

    @BindView(R.id.sex_man)
    ImageView mManView;
    @BindView(R.id.sex_woman)
    ImageView mWoManView;
    @BindView(R.id.sex_update)
    ProgressBar mUpdateBar;

    private SafeUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = BmobUtils.getCurrentUser();
        initViews();
    }

    private void initViews() {
        int sexIndex = currentUser.getmSex();
        mManView.setImageResource(sexIndex == MAN ? R.drawable.pop_see_male_on : R.drawable.pop_see_male_off);
        mManView.setEnabled(sexIndex == MAN ? false : true);
        mWoManView.setImageResource(sexIndex == MAN ? R.drawable.pop_see_female_off : R.drawable.pop_see_female_on);
        mWoManView.setEnabled(sexIndex == MAN ? true : false);
    }

    @OnClick(R.id.sex_man)
    public void onClickMan() {
        mManView.setEnabled(false); //鎖住
        mUpdateBar.setVisibility(View.VISIBLE);
        currentUser.setmSex(MAN);
        BmobUtils.updateInfo(currentUser, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mUpdateBar.setVisibility(View.GONE);
                if (e == null){
                    mManView.setImageResource(R.drawable.pop_see_male_on);
                    mWoManView.setImageResource(R.drawable.pop_see_female_off);
                    Intent intent = new Intent();
                    intent.putExtra("data", getString(R.string.man));
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    @OnClick(R.id.sex_woman)
    public void onClickWoMan() {
        mWoManView.setEnabled(false);
        mUpdateBar.setVisibility(View.VISIBLE);
        currentUser.setmSex(WOMAN);
        BmobUtils.updateInfo(currentUser, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mUpdateBar.setVisibility(View.GONE);
                if (e == null) {
                    mManView.setImageResource(R.drawable.pop_see_male_off);
                    mWoManView.setImageResource(R.drawable.pop_see_female_on);
                    Intent intent = new Intent();
                    intent.putExtra("data", getString(R.string.woman));
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_changesex;
    }

    @Override
    public String getBarTitle() {
        return Constant.CHANGE_SEX;
    }
}
