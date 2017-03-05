package com.yl.safemanager.entities;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by YL on 2017/3/4.
 */

public class LockFileModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String mSaveTime; //文件存放时的时间
    private String mOriginFileName; //原始文件名
    private String mLockFileName; // 加锁文件名
    private String mOriginFilePath; //原始文件路径
    private String mLockFilePath; //加锁文件路径
    @Ignore
    private int mPosition; //显示的位置

    public LockFileModel() {
    }

    public LockFileModel(long id, String mSaveTime, String mOriginFileName, String mLockFileName, String mOriginFilePath, String mLockFilePath) {
        this.id = id;
        this.mSaveTime = mSaveTime;
        this.mOriginFileName = mOriginFileName;
        this.mLockFileName = mLockFileName;
        this.mOriginFilePath = mOriginFilePath;
        this.mLockFilePath = mLockFilePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public String getSaveTime() {
        return mSaveTime;
    }

    public void setSaveTime(String mSaveTime) {
        this.mSaveTime = mSaveTime;
    }

    public String getOriginFileName() {
        return mOriginFileName;
    }

    public void setOriginFileName(String mOriginFileName) {
        this.mOriginFileName = mOriginFileName;
    }

    public String getLockFileName() {
        return mLockFileName;
    }

    public void setLockFileName(String mLockFileName) {
        this.mLockFileName = mLockFileName;
    }

    public String getOriginFilePath() {
        return mOriginFilePath;
    }

    public void setOriginFilePath(String mOriginFilePath) {
        this.mOriginFilePath = mOriginFilePath;
    }

    public String getLockFilePath() {
        return mLockFilePath;
    }

    public void setLockFilePath(String mLockFilePath) {
        this.mLockFilePath = mLockFilePath;
    }
}
