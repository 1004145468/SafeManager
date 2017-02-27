package com.yl.safemanager.utils;

import android.app.Activity;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.yl.safemanager.R;

/**
 * Created by YL on 2017/2/27.
 */

public class ToastUtils {

    //Toast展示
    public static void showToast(Activity context, String msg, Effects effect, int layoutid) {
        NiftyNotificationView.build(context, msg, effect, layoutid)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }
}
