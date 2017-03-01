package com.yl.safemanager.entities;

/**
 * Created by YL on 2017/3/1.
 *
 */

public class SafeFunctionItem {

    public static int TYPE_HEAD = 0; //头部展示
    public static int TYPE_FUNCTION = 1; //功能列表

    private int mCurrentType = 0;
    private Object mData;

    public SafeFunctionItem(int mCurrentType, Object mData) {
        this.mCurrentType = mCurrentType;
        this.mData = mData;
    }

    public int getCurrentType() {
        return mCurrentType;
    }

    public Object getData() {
        return mData;
    }
}
