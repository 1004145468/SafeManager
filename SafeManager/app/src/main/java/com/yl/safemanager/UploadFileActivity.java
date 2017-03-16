package com.yl.safemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.widget.Button;

import com.yl.safemanager.adapter.FileUploadAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.FileInfo;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yl.safemanager.utils.SFGT.REQUEST_FILE;

/**
 * Created by YL on 2017/3/16.
 */

public class UploadFileActivity extends BaseTitleBackActivity implements OnItemClickListener<FileInfo> {

    @BindView(R.id.upload_dobtn)
    Button mUploadBtn;
    @BindView(R.id.upload_listview)
    RecyclerView mFileRecyclerView;

    private List<FileInfo> mDatas;
    private FileUploadAdapter mFileUploadAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFileRecyclerView.setLayoutManager(linearLayoutManager);
        mFileRecyclerView.addItemDecoration(new SafeItemDecoration());
        mDatas = new ArrayList<>();
        mFileUploadAdapter = new FileUploadAdapter(this, mDatas);
        mFileRecyclerView.setAdapter(mFileUploadAdapter);
    }

    @OnClick(R.id.upload_addbtn)
    public void addFile() {
        SFGT.openFileChoose(this); //打开文件选择器
    }

    @OnClick(R.id.upload_dobtn)
    public void uploadFile() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                Uri fileUri = data.getData();
                String filePath = fileUri.getPath();  //文件大小
                File file = new File(filePath);
                String fileName = file.getName();  //文件名
                String fileSize = Formatter.formatFileSize(this,file.length()); //文件大小
                mDatas.add(0,new FileInfo(fileName,filePath,fileSize));
                mFileUploadAdapter.notifyItemInserted(0);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.acitivty_upload;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_DATABACKUP;
    }

    @Override
    public void onClick(FileInfo model) {
        int position = model.getPosition();
        mDatas.remove(position);
        mFileUploadAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onLongClick(FileInfo model) {
    }
}
