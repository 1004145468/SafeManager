package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.AppInfo;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.utils.DataBaseUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/6.
 */

public class AppLockAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private LayoutInflater mLayoutInflater;
    private OnItemClickListener<AppInfo> mListener;
    private ArrayList<AppInfo> mDatas;

    public AppLockAdapter(Context context, ArrayList<AppInfo> mDatas) {
        mLayoutInflater = LayoutInflater.from(context);
        mListener = (OnItemClickListener<AppInfo>) context;
        this.mDatas = mDatas;
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_lockapp, parent, false);
        return new AppLockHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        holder.setData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class AppLockHolder extends BaseSfHolder {

        private AppInfo appInfo;

        @BindView(R.id.app_icon)
        ImageView iconView;
        @BindView(R.id.app_name)
        TextView nameView;
        @BindView(R.id.app_packagename)
        TextView packageView;
        @BindView(R.id.app_select)
        ImageView selectView;

        public AppLockHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            if (data == null) {
                return;
            }
            appInfo = (AppInfo) data;
            iconView.setImageDrawable(appInfo.getIcon());
            nameView.setText(appInfo.getAppName());
            packageView.setText(appInfo.getPackageName());
            selectView.setImageResource(appInfo.isSelect() ? R.drawable.button_choose_on : R.drawable.button_choose_none);
        }

        @OnClick(R.id.app_select)
        public void handleApp() {
            appInfo.setSelect(!appInfo.isSelect());
            selectView.setImageResource(appInfo.isSelect() ? R.drawable.button_choose_on : R.drawable.button_choose_none);
            if (mListener != null) {
                mListener.onClick(appInfo);
            }
        }
    }


}
