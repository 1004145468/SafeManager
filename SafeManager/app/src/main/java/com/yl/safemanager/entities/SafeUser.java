package com.yl.safemanager.entities;

import cn.bmob.v3.BmobUser;

/**
 * Created by YL on 2017/2/25.
 */

public class SafeUser extends BmobUser {

    public static int MAN = 0;
    public static int WOMAN = 1;

    private String mPortrait;//用户头像

    private String mNick; //用户昵称

    private int mSex;  //

    private String mNote; //个性签名

    public SafeUser() {
    }

    public SafeUser(String mPortrait, String mNick, int mSex, String mNote) {
        this.mPortrait = mPortrait;
        this.mNick = mNick;
        this.mSex = mSex;
        this.mNote = mNote;
    }

    public String getmPortrait() {
        return mPortrait;
    }

    public void setmPortrait(String mPortrait) {
        this.mPortrait = mPortrait;
    }

    public String getmNick() {
        return mNick;
    }

    public void setmNick(String mNick) {
        this.mNick = mNick;
    }

    public int getmSex() {
        return mSex;
    }

    public void setmSex(int mSex) {
        this.mSex = mSex;
    }

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }
}
