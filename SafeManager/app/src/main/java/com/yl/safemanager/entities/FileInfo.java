package com.yl.safemanager.entities;

import cn.bmob.v3.BmobObject;

/**
 * Created by YL on 2017/3/16.
 */

public class FileInfo{

    private String mFileName;

    private String mFilePath;

    private String mFileSize;
    private int position;

    public FileInfo(String mFileName, String mFilePath, String mFileSize) {
        this.mFileName = mFileName;
        this.mFilePath = mFilePath;
        this.mFileSize = mFileSize;
    }

    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmFilePath() {
        return mFilePath;
    }

    public void setmFilePath(String mFilePath) {
        this.mFilePath = mFilePath;
    }

    public String getmFileSize() {
        return mFileSize;
    }

    public void setmFileSize(String mFileSize) {
        this.mFileSize = mFileSize;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
