package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.LockFileModel;
import com.yl.safemanager.interfact.onEncryptItemOnclickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/4.
 */

public class LockFileAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private onEncryptItemOnclickListener listener;
    private LayoutInflater mLayoutInflater;
    private List<LockFileModel> mDatas;

    public LockFileAdapter(Context context, List<LockFileModel> mDatas) {
        listener = (onEncryptItemOnclickListener) context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_filelock, parent, false);
        return new LockFileHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        LockFileModel fileModel = mDatas.get(position);
        fileModel.setPosition(position);
        holder.setData(fileModel);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LockFileHolder extends BaseSfHolder {

        private final String FILE_TRANSLATION = "->";

        @BindView(R.id.tv_num)
        TextView numView;
        @BindView(R.id.tv_filename)
        TextView filenameView;
        @BindView(R.id.tv_time)
        TextView timeView;

        private LockFileModel model;

        public LockFileHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            model = (LockFileModel) data;
            if (model == null) {
                return;
            }
            numView.setText(model.getPosition());
            filenameView.setText(model.getOriginFileName() + FILE_TRANSLATION + model.getLockFileName());
            timeView.setText(model.getSaveTime());
        }

        @OnClick(R.id.id_root)
        public void encryptLockFile() {
            if (listener != null) {
                listener.DecryptFile(model.getLockFilePath(), model.getOriginFilePath(), model.getId(), model.getPosition());
            }
        }
    }
}
