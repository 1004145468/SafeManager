package com.yl.safemanager.interfact;

/**
 * Created by YL on 2017/3/5.
 */

public interface OnItemClickListener<T> {
    void onClick(T model);

    void onLongClick(T model);
}
