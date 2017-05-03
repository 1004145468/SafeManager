package com.yl.safemanager;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

    public static final String SHORT_CODE = "shortcode";

    @BindView(R.id.config_alert)
    View mAlertView;

    @BindView(R.id.config_content)
    EditText configContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockconfig);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !AppUtils.hasUsageStatsPermission(this)) {
            mAlertView.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.config_alert)
    public void openConfigActivity() {
        SFGT.gotoPermisstion(this);
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

        ToastUtils.showToast(this, getString(R.string.functionisnavai), Effects.thumbSlider, R.id.id_root);
        //保存
        SpUtils.saveString(this, SHORT_CODE, EncryptUtils.md5Encrypt(shortCode));
        finish();
        overridePendingTransition(0, R.anim.slide_tobottom);
    }


}
