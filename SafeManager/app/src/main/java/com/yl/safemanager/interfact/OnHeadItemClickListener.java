package com.yl.safemanager.interfact;

/**
 * Created by YL on 2017/3/17.
 */

public interface OnHeadItemClickListener<T> {

    void onClickMail();

    void onClickInfo();

    void onClick(T model);

}
