package com.yl.safemanager.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by YL on 2017/3/9.
 */

public class WindowUtils {

    /**
     * 添加界面
     */
    public static void addFullScreenView(final Context context, View contentView) {
        if (contentView == null) {
            return;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = (Gravity.CENTER);
        windowManager.addView(contentView, params);
    }

    public static void removeFullScreenView(Context context, View contentView) {
        if (contentView == null) {
            return;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(contentView);
    }
}
