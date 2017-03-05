package com.yl.safemanager.utils;

import android.content.Context;

import com.yl.safemanager.entities.LockFileModel;
import com.yl.safemanager.interfact.OnResultAttachedListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by YL on 2017/3/4.
 */

public class DataBaseUtils {

    private static Realm mRealm;
    private static SimpleDateFormat timeFormat;

    public static void initRealm(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        mRealm = Realm.getInstance(realmConfiguration);
        timeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
    }

    public static void closeRealm() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
        if (timeFormat != null) {
            timeFormat = null;
        }
    }


    /**
     * 对文件进行加锁时 调用(添加一条数据进入数据库)
     *
     * @param originName
     * @param lockName
     * @param originPath
     * @param lockPath
     */
    public static void saveLockFileModel(final String originName, final String lockName, final String originPath, final String lockPath,
                                         final OnResultAttachedListener<LockFileModel> listener) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LockFileModel lockFileModel = realm.createObject(LockFileModel.class);
                lockFileModel.setId(System.currentTimeMillis());
                lockFileModel.setSaveTime(timeFormat.format(new Date()));  //当前存入的时间
                lockFileModel.setOriginFileName(originName);
                lockFileModel.setLockFileName(lockName);
                lockFileModel.setOriginFilePath(originPath);
                lockFileModel.setLockFilePath(lockPath);
                if(listener != null){
                    listener.onResult(lockFileModel);
                }
            }
        });
    }

    /***
     * 对文件进行解锁时候 调用 （删除一条数据从数据库）
     *
     * @param id
     */
    public static void deleteLockFileModel(final long id) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LockFileModel fileModel = realm.where(LockFileModel.class).equalTo("mSaveTime", id).findFirst();
                if (fileModel != null) {
                    fileModel.deleteFromRealm();
                }
            }
        });
    }


    /**
     * 获取所有已经加锁的文件信息
     */
    public static void getAllLockFileModels(final OnResultAttachedListener<RealmResults<LockFileModel>> listener) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LockFileModel> mFileModels = realm.where(LockFileModel.class).findAllSortedAsync("id", Sort.DESCENDING);
                if (listener != null) {
                    listener.onResult(mFileModels);
                }
            }
        });
    }
}
