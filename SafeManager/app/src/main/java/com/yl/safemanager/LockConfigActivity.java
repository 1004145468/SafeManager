package com.yl.safemanager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.utils.AppUtils;
import com.yl.safemanager.utils.EncryptUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.SpUtils;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/18.
 */

public class LockConfigActivity extends BaseActivity {

    public static final String BACKTOLOCKACTIVITY = "backtoLockAc";

    private boolean isBackToLockActivity = false;

    public static final String SHORT_CODE = "shortcode";

    @BindView(R.id.config_stepone)
    View configInfoView;

    @BindView(R.id.config_steptwo)
    View configFloattingView;

    @BindView(R.id.config_content)
    EditText configContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockconfig);
        ButterKnife.bind(this);
        isBackToLockActivity = getIntent().getBooleanExtra(BACKTOLOCKACTIVITY, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();

    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !AppUtils.hasUsageStatsPermission(this)) {
            configInfoView.setVisibility(View.VISIBLE);
            return;
        } else {
            configInfoView.setVisibility(View.INVISIBLE);
        }
        configContentView.setEnabled(true);
        configFloattingView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.config_stepone)
    public void openConfigActivity() {
        SFGT.gotoPermisstion(this);
    }

    @OnClick(R.id.config_steptwo)
    public void openSettingActivity() {
        SFGT.gotoSetttingActivity(this);
    }

    @OnClick(R.id.config_btn)
    public void saveShortCode() {
        String shortCode = configContentView.getText().toString().trim();
        if (TextUtils.isEmpty(shortCode)) {
            ToastUtils.showToast(this, getString(R.string.shortcodeisemp), Effects.thumbSlider, R.id.id_root);
            return;
        }

        if (shortCode.length() != 6) {
            ToastUtils.showToast(this, getString(R.string.shortcodeisshort), Effects.thumbSlider, R.id.id_root);
            return;
        }

        //保存
        SpUtils.saveString(this, SHORT_CODE, EncryptUtils.md5Encrypt(shortCode));
        if (isBackToLockActivity) {
            SFGT.gotoAppLockActivity(this);
        }
        finish();
        overridePendingTransition(0, R.anim.slide_tobottom);
    }
}
