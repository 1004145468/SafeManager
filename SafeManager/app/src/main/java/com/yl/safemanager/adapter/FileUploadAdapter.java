package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.UploadFileActivity;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.FileInfo;
import com.yl.safemanager.interfact.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;

/**
 * Created by YL on 2017/3/16.
 */

public class FileUploadAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private LayoutInflater mLayoutInflater;
    private List<FileInfo> mDatas;
    private OnItemClickListener<FileInfo> mListener;

    public FileUploadAdapter(Context context, List<FileInfo> mDatas) {
        mListener = (OnItemClickListener<FileInfo>) context;
        mLayoutInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_uploadfile, parent, false);
        return new FileUploadHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        holder.setData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class FileUploadHolder extends BaseSfHolder {

        private FileInfo fileInfo;

        @BindView(R.id.file_name)
        TextView fileNameView;
        @BindView(R.id.file_path)
        TextView filePathView;
        @BindView(R.id.file_size)
        TextView fileSizeView;

        public FileUploadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            if (data == null) {
                return;
            }
            fileInfo = (FileInfo) data;
            fileNameView.setText(fileInfo.getmFileName());
            filePathView.setText(fileInfo.getmFilePath());
            fileSizeView.setText(fileInfo.getmFileSize());
        }

        @OnClick(R.id.file_delete)
        public void onItemClick() {
            if (mListener != null) {
                mListener.onClick(fileInfo);
            }
        }
    }

}
