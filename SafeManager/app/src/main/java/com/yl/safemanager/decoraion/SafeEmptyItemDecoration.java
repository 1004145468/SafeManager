package com.yl.safemanager.decoraion;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yl.safemanager.utils.DensityUtils;

/**
 * Created by YL on 2017/3/1.
 */

public class SafeEmptyItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, DensityUtils.dip2px(view.getContext(), 15));
    }
}
