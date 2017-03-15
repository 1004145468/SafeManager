package com.yl.safemanager.utils;


import android.content.Context;
import android.net.Uri;

import com.yl.safemanager.R;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.interfact.OnResultAttachedListener;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
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
                    signUp(context, username, password, e == null ? fileUrl : "", listener);
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
        String filePath = UriUtils.queryUri(context, uri);
        File file = new File(filePath);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                listener.onResult(e, bmobFile.getFileUrl());
            }
        });
    }

    public static void login(String username, String password, SaveListener<SafeUser> listener) {
        SafeUser safeUser = new SafeUser();
        safeUser.setUsername(username);
        safeUser.setPassword(EncryptUtils.md5Encrypt(password));
        safeUser.login(listener);
    }

    public static SafeUser getCurrentUser() {
        return BmobUser.getCurrentUser(SafeUser.class);
    }

    public static void synchroInfo(BmobObject obj, SaveListener<String> listener) {
        obj.save(listener);
    }

    public static void updateInfo(BmobObject bmobObject, UpdateListener listener) {
        bmobObject.update(bmobObject.getObjectId(), listener);
    }

    public static void deleteInfo(BmobObject bmobObject, UpdateListener listener) {
        bmobObject.delete(listener);
    }

    /**
     * 获取私密数据记录
     *
     * @param listener
     */
    public static void getSmDateModels(final OnResultAttachedListener<List<SmDataModel>> listener) {
        BmobQuery<SmDataModel> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("useid", getCurrentUser().getUsername());
        bmobQuery.setLimit(50);
        bmobQuery.findObjects(new FindListener<SmDataModel>() {
            @Override
            public void done(List<SmDataModel> list, BmobException e) {
                if (listener != null) {
                    listener.onResult(list);
                }
            }
        });
    }

    /**
     * 文件下载成功回调
     */
    public interface onUploadFileResult {
        void onResult(BmobException e, String fileUrl);
    }
}
