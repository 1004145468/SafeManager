package com.yl.safemanager.entities;

/**
 * Created by YL on 2017/3/1.
 */

public class SafeFunctionInfo {

    private String mFunctionName;
    private String mFunctionDescription;
    private int mFunctionImg;

    public SafeFunctionInfo(String mFunctionName, String mFunctionDescription, int mFunctionImg) {
        this.mFunctionName = mFunctionName;
        this.mFunctionDescription = mFunctionDescription;
        this.mFunctionImg = mFunctionImg;
    }

    public String getFunctionName() {
        return mFunctionName;
    }

    public void setFunctionName(String mFunctionName) {
        this.mFunctionName = mFunctionName;
    }

    public String getFunctionDescription() {
        return mFunctionDescription;
    }

    public void setFunctionDescription(String mFunctionDescription) {
        this.mFunctionDescription = mFunctionDescription;
    }

    public int getFunctionImg() {
        return mFunctionImg;
    }

    public void setFunctionImg(int mFunctionImg) {
        this.mFunctionImg = mFunctionImg;
    }
}
