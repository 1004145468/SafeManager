package com.yl.safemanager.utils;

import android.content.Context;
import android.util.Log;

import com.yl.safemanager.entities.AppInfo;
import com.yl.safemanager.entities.LockFileModel;
import com.yl.safemanager.interfact.OnResultAttachedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.id;

/**
 * Created by YL on 2017/3/4.
 */

public class DataBaseUtils {

    private static final String TAG = "DataBaseUtils";

    private static Realm mRealm;

    public static void initRealm(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        mRealm = Realm.getInstance(realmConfiguration);
    }

    public static void closeRealm() {
        if (mRealm != null) {
            mRealm.close();
            mRealm = null;
        }
    }

    /**
     * 对文件进行加锁时 调用(添加一条数据进入数据库)
     *
     * @param lockFileModel
     */
    public static void saveLockFileModel(final LockFileModel lockFileModel) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LockFileModel realmObject = realm.createObject(LockFileModel.class);
                realmObject.setId(lockFileModel.getId());
                realmObject.setSaveTime(lockFileModel.getSaveTime());
                realmObject.setOriginFileName(lockFileModel.getOriginFileName());
                realmObject.setLockFileName(lockFileModel.getLockFileName());
                realmObject.setOriginFilePath(lockFileModel.getOriginFilePath());
                realmObject.setLockFilePath(lockFileModel.getLockFilePath());
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
                LockFileModel fileModel = realm.where(LockFileModel.class).equalTo("id", id).findFirst();
                if (fileModel != null) {
                    fileModel.deleteFromRealm();
                }
            }
        });
    }

    /**
     * 获取所有已经加锁的文件信息
     */
    public static void getAllLockFileModels(final OnResultAttachedListener<List<LockFileModel>> listener) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LockFileModel> mFileModels = realm.where(LockFileModel.class).findAllSorted("id", Sort.DESCENDING);
                if (listener != null) {
                    listener.onResult(realm.copyFromRealm(mFileModels));
                }
            }
        });
    }

    /**
     * 存入加锁应用信息
     *
     * @param appInfos
     */
    public static void saveLockApp(final List<AppInfo> appInfos) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AppInfo> all = realm.where(AppInfo.class).findAll();
                all.deleteAllFromRealm(); //删除并更新存储记录
                for (AppInfo appInfo : appInfos) {
                    realm.copyToRealm(appInfo);
                }
            }
        });

    }

    public static void getAllLockApps(final OnResultAttachedListener<List<AppInfo>> listener) {
        List<AppInfo> mDatas = new ArrayList<>();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AppInfo> results = realm.where(AppInfo.class).findAll();
                if(listener != null){
                   listener.onResult(realm.copyFromRealm(results));
                }
            }
        });
    }
}
