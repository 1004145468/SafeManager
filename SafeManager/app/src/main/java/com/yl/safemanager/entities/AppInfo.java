package com.yl.safemanager.entities;

import android.graphics.drawable.Drawable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by YL on 2017/3/8.
 */

public class AppInfo extends RealmObject {

    private String mPackageName;
    private String mAppName;
    @Ignore
    private Drawable mIcon;
    @Ignore
    private boolean isSelect = false;

    public AppInfo() {
    }

    public AppInfo(String mPackageName, String mAppName, Drawable mIcon) {
        this.mPackageName = mPackageName;
        this.mAppName = mAppName;
        this.mIcon = mIcon;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
