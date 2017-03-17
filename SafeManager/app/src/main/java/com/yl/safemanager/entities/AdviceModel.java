package com.yl.safemanager.entities;

import com.yl.safemanager.utils.BmobUtils;

import cn.bmob.v3.BmobObject;

import static cn.bmob.v3.BmobRole.tableName;

/**
 * Created by YL on 2017/3/11.
 */

public class AdviceModel extends BmobObject {

    private String userId;
    private String advice;
    private String filepath;


    public AdviceModel(String advice, String filepath) {
        setTableName(AdviceModel.class.getSimpleName());
        userId = BmobUtils.getCurrentUser().getUsername();
        this.advice = advice;
        this.filepath = filepath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
