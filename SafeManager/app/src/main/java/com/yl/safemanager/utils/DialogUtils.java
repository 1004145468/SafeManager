package com.yl.safemanager.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yl.safemanager.R;

/**
 * Created by YL on 2017/2/25.
 */

public class DialogUtils {

    private static Dialog dialog;
    private static TextView noteView;

    //展示一个不确定进度Dialog
    public static void showIndeterminateDialog(Context context, String note, boolean canCancel, DialogInterface.OnCancelListener listener) {
        if(dialog == null){
            View dialogView = View.inflate(context, R.layout.dialog_indeterminate, null);
            noteView = (TextView) dialogView.findViewById(R.id.dialog_note);
            dialog = new Dialog(context, R.style.dialog);
            dialog.setContentView(dialogView);
        }
        noteView.setText(note);
        dialog.setCancelable(canCancel);
        dialog.setOnCancelListener(listener); //设置取消监听
        dialog.show();
        Window dialogWindow = dialog.getWindow(); //Dialog的承载体,设置Dialog的显示效果
        WindowManager.LayoutParams attributes = dialogWindow.getAttributes();
        attributes.width = DensityUtils.dip2px(context, 240);
        attributes.height = DensityUtils.dip2px(context, 120);
        dialogWindow.setAttributes(attributes);
    }

    public static void shutdownIndeterminateDialog(){
        if(dialog != null){
            dialog.cancel();
        }
    }



}
