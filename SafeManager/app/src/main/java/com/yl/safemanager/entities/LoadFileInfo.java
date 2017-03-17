package com.yl.safemanager.entities;

import com.yl.safemanager.utils.BmobUtils;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by YL on 2017/3/17.
 */

public class LoadFileInfo extends BmobObject {

    private String fileName;
    private String fileSize;
    private String uploadTime;
    private BmobFile bmobFile;
    private String userId;
    private int position;

    public LoadFileInfo() {
    }

    public LoadFileInfo(String fileName, String uploadTime, String fileSize, BmobFile bmobFile) {
        super(LoadFileInfo.class.getSimpleName());
        userId = BmobUtils.getCurrentUser().getUsername(); //设置文件的所属
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadTime = uploadTime;
        this.bmobFile = bmobFile;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }
}
