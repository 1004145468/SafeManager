package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.interfact.OnItemSwipeListener;
import com.yl.safemanager.view.SwipeItemLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/12.
 */

public class SMDataAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private OnItemSwipeListener<SmDataModel> mOnItemSwipeListener;
    private OnItemClickListener<SmDataModel> mOnItemClickListener;
    private LayoutInflater mLayoutInflater;
    private List<SmDataModel> mDatas;

    public SMDataAdapter(Context context, List<SmDataModel> mDatas) {
        mOnItemSwipeListener = (OnItemSwipeListener<SmDataModel>) context;
        mOnItemClickListener = (OnItemClickListener<SmDataModel>) context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_smdata, parent, false);
        return new SMDataHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        SmDataModel smDataModel = mDatas.get(position);
        smDataModel.setPosition(position);
        holder.setData(smDataModel);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class SMDataHolder extends BaseSfHolder {

        @BindView(R.id.smdata_root)
        SwipeItemLayout swipeItemLayout;
        @BindView(R.id.smdata_time)
        TextView timeView;
        @BindView(R.id.smdata_title)
        TextView titleView;

        private SmDataModel smDataModel;

        public SMDataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            if (data == null) {
                return;
            }
            smDataModel = (SmDataModel) data;
            timeView.setText(smDataModel.getSavetime());
            titleView.setText(smDataModel.getTitle());
        }

        @OnClick({R.id.smdata_content, R.id.smdata_copy, R.id.smdata_delete})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.smdata_content:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(smDataModel);
                    }
                    break;
                case R.id.smdata_copy:
                case R.id.smdata_delete:
                    swipeItemLayout.close();
                    if (mOnItemSwipeListener != null) {
                        mOnItemSwipeListener.onItemDone(smDataModel, view.getId());
                    }
                    break;
            }
        }
    }


}
