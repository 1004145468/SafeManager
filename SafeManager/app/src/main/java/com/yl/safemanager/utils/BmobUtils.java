package com.yl.safemanager.utils;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.yl.safemanager.R;
import com.yl.safemanager.entities.SafeUser;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by YL on 2017/2/27.
 */

public class BmobUtils {

    /**
     * 用戶注册
     *
     * @param username 用户名
     * @param password 密码
     * @param uri      头像地址
     * @param listener 回调接口
     */
    public static void signUpWithFile(final Context context, final String username, final String password, Uri uri, final SaveListener<SafeUser> listener) {
        if (uri == null) {
            signUp(context, username, password, "", listener);
        } else {
            uploadFile(context, uri, new onUploadFileResult() {
                @Override
                public void onResult(BmobException e, String fileUrl) {
                    signUp(context, username, password, e == null ? fileUrl : "" , listener);
                }
            });
        }
    }

    private static void signUp(final Context context, final String username, final String password, String fileUrl, final SaveListener<SafeUser> listener) {
        SafeUser safeUser = new SafeUser();
        safeUser.setUsername(username);
        safeUser.setPassword(EncryptUtils.md5Encrypt(password));
        safeUser.setmPortrait(fileUrl);
        safeUser.setmNick(context.getResources().getString(R.string.default_nick));
        safeUser.setmSex(SafeUser.MAN);
        safeUser.setmNote(context.getResources().getString(R.string.default_msg));
        safeUser.signUp(listener);
    }

    /**
     * 图片文件的上传
     *
     * @param uri      图片Uri
     * @param listener 图片上传回调
     */
    public static void uploadFile(Context context, Uri uri, final onUploadFileResult listener) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
        cursor.close();
        File file = new File(filePath);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                listener.onResult(e, bmobFile.getFileUrl());
            }
        });
    }

    public static void login(String username,String password,SaveListener<SafeUser> listener){
        SafeUser safeUser = new SafeUser();
        safeUser.setUsername(username);
        safeUser.setPassword(EncryptUtils.md5Encrypt(password));
        safeUser.login(listener);
    }

    /**
     * 文件下载成功回调
     */
    public interface onUploadFileResult {
        void onResult(BmobException e, String fileUrl);
    }
}
