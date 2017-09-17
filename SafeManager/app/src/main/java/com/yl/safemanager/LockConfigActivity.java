package com.yl.safemanager;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseActivity;
import com.yl.safemanager.utils.AppUtils;
import com.yl.safemanager.utils.DialogUtils;
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
        showTipDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();

    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !AppUtils.hasUsageStatsPermission(this)) {
            mAlertView.setVisibility(View.VISIBLE);
        } else {
            mAlertView.setVisibility(View.INVISIBLE);
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

        //保存
        SpUtils.saveString(this, SHORT_CODE, EncryptUtils.md5Encrypt(shortCode));
        finish();
        overridePendingTransition(0, R.anim.slide_tobottom);
    }

    private void showTipDialog() {
        String shortCode = SpUtils.getString(this, LockConfigActivity.SHORT_CODE);
        if(TextUtils.isEmpty(shortCode)){
            DialogUtils.showTipDialog(this, getString(R.string.open_floattingwindow_tip), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SFGT.gotoSetttingActivity(LockConfigActivity.this);
                }
            });
        }
    }
}
