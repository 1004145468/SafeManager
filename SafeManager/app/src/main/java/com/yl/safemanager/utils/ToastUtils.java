package com.yl.safemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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

    public static void showOriginToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
