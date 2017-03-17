package com.yl.safemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.LoadFileInfo;
import com.yl.safemanager.interfact.OnItemClickListener;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by YL on 2017/3/17.
 */

public class FileDownLoadAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private LayoutInflater mLayoutInflater;
    private OnItemClickListener<LoadFileInfo> listener;
    private List<LoadFileInfo> mDatas;

    public FileDownLoadAdapter(Context mContext, List<LoadFileInfo> mDatas) {
        mLayoutInflater = LayoutInflater.from(mContext);
        listener = (OnItemClickListener<LoadFileInfo>) mContext;
        this.mDatas = mDatas;
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.item_downloadfile, parent, false);
        return new FileDownLoadHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        LoadFileInfo loadFileInfo = mDatas.get(position);
        loadFileInfo.setPosition(position);
        holder.setData(loadFileInfo);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class FileDownLoadHolder extends BaseSfHolder {

        private LoadFileInfo loadFileInfo;
        @BindView(R.id.file_name)
        TextView fileNameView;
        @BindView(R.id.file_size)
        TextView fileSizeView;
        @BindView(R.id.file_time)
        TextView fileTimeView;

        public FileDownLoadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            if (data == null) {
                return;
            }
            loadFileInfo = (LoadFileInfo) data;
            fileNameView.setText(loadFileInfo.getFileName());
            fileSizeView.setText(loadFileInfo.getFileSize());
            fileTimeView.setText(loadFileInfo.getUploadTime());
        }

        @OnClick(R.id.file_download)
        public void onDownLoadFile() {
            if (listener != null) {
                listener.onClick(loadFileInfo);
            }
        }

        @OnLongClick(R.id.file_root)
        public boolean deleteLoadFile() {
            if (listener != null) {
                listener.onLongClick(loadFileInfo);
            }
            return true;
        }
    }
}
