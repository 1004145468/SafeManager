package com.yl.safemanager.entities;

/**
 * Created by YL on 2017/3/19.
 */

public class TokenResult {

    private int code;
    private String token;
    private String userId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
