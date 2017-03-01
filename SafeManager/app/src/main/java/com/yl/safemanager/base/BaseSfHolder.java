package com.yl.safemanager.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by YL on 2017/3/1.
 */

public abstract class BaseSfHolder extends RecyclerView.ViewHolder {


    public BaseSfHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(Object data);
}
