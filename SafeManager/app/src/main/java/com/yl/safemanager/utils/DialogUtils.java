package com.yl.safemanager.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.SMLockActivity;
import com.yl.safemanager.interfact.OnResultAttachedListener;

/**
 * Created by YL on 2017/2/25.
 */

public class DialogUtils {

    private static Dialog dialog;
    private static TextView noteView;

    //展示一个不确定进度Dialog
    public static void showIndeterminateDialog(Context context, String note, boolean canCancel, DialogInterface.OnCancelListener listener) {
        View dialogView = View.inflate(context, R.layout.dialog_indeterminate, null);
        noteView = (TextView) dialogView.findViewById(R.id.dialog_note);
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
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

    public static void shutdownIndeterminateDialog() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
        if (noteView != null) {
            noteView = null;
        }
    }

    /**
     * 打开一个输入密码对话框
     *
     * @param context
     * @param isEncrption
     */
    public static void openEnterPwsDialog(final Context context, final boolean isEncrption, final int type, final String content, final OnResultAttachedListener<String> listener) {
        View dialogView = View.inflate(context, R.layout.dialog_enterpsw, null);
        noteView = (TextView) dialogView.findViewById(R.id.dialog_note);
        final EditText contentView = (EditText) dialogView.findViewById(R.id.dialog_content);
        final Button okButton = (Button) dialogView.findViewById(R.id.dialog_button);
        contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    okButton.setEnabled(true);
                } else {
                    okButton.setEnabled(false);
                }
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = contentView.getText().toString();
                if (isEncrption) {
                    //加密信息进行跳转
                    String encryptContent = EncryptUtils.encrypt(key, content);
                    if (type == SMLockActivity.SMS_TYPE) {
                        //打开收信箱 填入内容
                        SFGT.openSmsBox(context, encryptContent);
                    } else {
                        //打开邮件,填入内容
                        SFGT.openMailBox(context, encryptContent);
                    }

                } else {
                    if (listener != null) {
                        String decryptContent = EncryptUtils.decrypt(key, content);
                        listener.onResult(decryptContent);
                    }
                }
                shutdownIndeterminateDialog();
            }
        });
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(dialogView);
        noteView.setText(isEncrption ? context.getText(R.string.sms_encryptionkey) : context.getString(R.string.sms_decryptionKey));
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                shutdownIndeterminateDialog();// 取消之后释放资源
            }
        });
        dialog.show();
        Window dialogWindow = dialog.getWindow(); //Dialog的承载体,设置Dialog的显示效果
        WindowManager.LayoutParams attributes = dialogWindow.getAttributes();
        attributes.width = DensityUtils.dip2px(context, 320);
        attributes.height = DensityUtils.dip2px(context, 200);
        dialogWindow.setAttributes(attributes);
    }

    public static void showMessageDialog(Context context, String msg,
                                         DialogInterface.OnClickListener positiveOnclick
    ) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setIcon(null)
                .setPositiveButton(R.string.dialog_positive, positiveOnclick)
                .setNegativeButton(R.string.dialog_cancel, null)
                .setCancelable(false)
                .show();
    }


}
