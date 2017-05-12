package com.yl.safemanager.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.yl.safemanager.R;
import com.yl.safemanager.entities.LoadFileInfo;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.interfact.OnResultAttachedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.v3.BmobRealTimeData.TAG;

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
        safeUser.setPassword(password);
        safeUser.setmPortrait(fileUrl);
        safeUser.setmNick(context.getResources().getString(R.string.default_nick));
        safeUser.setmSex(SafeUser.MAN);
        safeUser.setmNote(context.getResources().getString(R.string.default_msg));
        safeUser.setEmail(username);
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
        if (filePath == null) {
            return;
        }
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String originFilePath = params[0];  // 获取源文件的路径
                String fileHeadPath = originFilePath.substring(0, originFilePath.lastIndexOf(File.separator) + 1);
                String fileName = originFilePath.substring(originFilePath.lastIndexOf(File.separator) + 1); //文件名
                StringBuilder compressPath = new StringBuilder();
                compressPath.append(fileHeadPath).append("compress_").append(fileName);
                Bitmap originBitmap = BitmapFactory.decodeFile(originFilePath);
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(compressPath.toString());
                    boolean compressResult = originBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                    originBitmap.recycle(); //回收图片
                    if (compressResult) {
                        Log.d(TAG, "doInBackground: ====================== 文件压缩成功");
                        return compressPath.toString();
                    }
                    return originFilePath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return originFilePath;
                } finally {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onPostExecute(String filePath) {
                File file = new File(filePath);
                final BmobFile bmobFile = new BmobFile(file);
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (listener != null) {
                            listener.onResult(e, bmobFile.getFileUrl());
                        }
                    }
                });
            }

        }.execute(filePath);
    }

    /**
     * 对文件进行下载
     *
     * @param bmobFile 下载的文件
     * @param listener 下载监听器
     */
    public static void downloadFile(BmobFile bmobFile, DownloadFileListener listener) {
        File downFile = FileUtils.createFile("SfDown", bmobFile.getFilename());
        bmobFile.download(downFile, listener);
    }


    /**
     * 批量文件上传
     *
     * @param filePaths
     * @param listener
     */
    public static void batchUploadFile(List<String> filePaths, UploadBatchListener listener) {
        int filePathSize = filePaths.size();
        String[] filePathArray = filePaths.toArray(new String[filePathSize]);
        BmobFile.uploadBatch(filePathArray, listener);
    }

    public static void login(String username, String password, SaveListener<SafeUser> listener) {
        SafeUser safeUser = new SafeUser();
        safeUser.setUsername(username);
        safeUser.setPassword(password);
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
        bmobQuery.addWhereEqualTo("userId", getCurrentUser().getUsername());
        bmobQuery.setLimit(50);
        bmobQuery.order("-createdAt"); //时间降序
        bmobQuery.findObjects(new FindListener<SmDataModel>() {
            @Override
            public void done(List<SmDataModel> list, BmobException e) {
                if (listener != null) {
                    listener.onResult(list);
                }
            }
        });
    }

    public static void getLoadFileInfos(final OnResultAttachedListener<List<LoadFileInfo>> listener) {
        BmobQuery<LoadFileInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userId", getCurrentUser().getUsername());
        bmobQuery.setLimit(50);
        bmobQuery.order("-createdAt"); //时间降序
        bmobQuery.findObjects(new FindListener<LoadFileInfo>() {
            @Override
            public void done(List<LoadFileInfo> list, BmobException e) {
                if (listener != null) {
                    listener.onResult(list);
                }
            }
        });
    }

    /**
     * 通过邮箱重置密码
     *
     * @param email
     * @param listener
     */
    public static void resetPasswordByEmail(String email, UpdateListener listener) {
        BmobUser.resetPasswordByEmail(email, listener);
    }

    /**
     * 修改密码
     *
     * @param oldpsw   旧密码
     * @param newpsw   新密码
     * @param listener 回调
     */
    public static void changePsw(String oldpsw, String newpsw, UpdateListener listener) {
        BmobUser.updateCurrentUserPassword(oldpsw, newpsw, listener);
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userid
     */
    public static void getUserInfo(String userid, final OnResultAttachedListener<SafeUser> listener) {
        BmobQuery<SafeUser> safeUserBmobQuery = new BmobQuery<>();
        safeUserBmobQuery.addWhereEqualTo("username", userid);
        safeUserBmobQuery.setLimit(1);
        safeUserBmobQuery.findObjects(new FindListener<SafeUser>() {
            @Override
            public void done(List<SafeUser> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    if (listener != null) {
                        listener.onResult(list.get(0));
                    }
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
