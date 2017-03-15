package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.interfact.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by YL on 2017/3/12.
 */

public class SMDataAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private OnItemClickListener listener;
    private LayoutInflater mLayoutInflater;
    private List<SmDataModel> mDatas;

    public SMDataAdapter(Context context, List<SmDataModel> mDatas) {
        listener = (OnItemClickListener) context;
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
        holder.setData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class SMDataHolder extends BaseSfHolder {

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

        @OnClick(R.id.smdata_root)
        public void onClick() {
            if (listener != null) {
                listener.onClick(smDataModel);
            }
        }

        @OnLongClick(R.id.smdata_root)
        public boolean onLongClick() {
            if (listener != null){
                listener.onLongClick(smDataModel);
            }
            return true;
        }
    }


}
