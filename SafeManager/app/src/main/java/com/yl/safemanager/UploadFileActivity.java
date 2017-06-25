package com.yl.safemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Button;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.adapter.FileUploadAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeEmptyItemDecoration;
import com.yl.safemanager.entities.FileInfo;
import com.yl.safemanager.entities.LoadFileInfo;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.ToastUtils;
import com.yl.safemanager.utils.UriUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import static com.yl.safemanager.utils.SFGT.REQUEST_FILE;

/**
 * Created by YL on 2017/3/16.
 */

public class UploadFileActivity extends BaseTitleBackActivity implements OnItemClickListener<FileInfo> {

    private static final String TAG = "UploadFileActivity";

    @BindView(R.id.upload_listview)
    RecyclerView mFileRecyclerView;

    private List<String> mFilePaths;
    private List<FileInfo> mDatas;
    private FileUploadAdapter mFileUploadAdapter;
    private int mCurUploadFileNum = 0;
    private int mCurRemoveIndex = 0;
    private SimpleDateFormat mDateFormater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        mDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFileRecyclerView.setLayoutManager(linearLayoutManager);
        //mFileRecyclerView.addItemDecoration(new SafeEmptyItemDecoration());
        mDatas = new ArrayList<>();
        mFilePaths = new ArrayList<>();
        mFileUploadAdapter = new FileUploadAdapter(this, mDatas);
        mFileRecyclerView.setAdapter(mFileUploadAdapter);
    }

    @OnClick(R.id.upload_add)
    public void addFile() {
        SFGT.openFileChoose(this); //打开文件选择器
    }

    @OnClick(R.id.upload_send)
    public void uploadFile() {
        final int totalSize = mFilePaths.size();
        if (totalSize == 0) {
            return; //没有指定文件 不能上传
        }
        DialogUtils.showFileLoadDialog(this, getString(R.string.upload_file));
        mCurUploadFileNum = 0; //重置当前上传文件index
        mCurRemoveIndex = 0;    //重置当前需要移除的index
        Log.d(TAG, "uploadFile: 总上传文件数" + totalSize);
        BmobUtils.batchUploadFile(mFilePaths, new UploadBatchListener() {
            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                //更新文件加载器
                DialogUtils.updateFileLoadDialog(getString(R.string.upload_file) + "(" + i + "/" + i2 + ")", i1, i1 + "%");
            }

            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                //文件上传成功后,入数据库
                final FileInfo fileInfo = mDatas.get(mCurRemoveIndex);
                final LoadFileInfo loadFileInfo = new LoadFileInfo(fileInfo.getmFileName(),
                        mDateFormater.format(new Date()), fileInfo.getmFileSize(), list.get(list.size() - 1));
                BmobUtils.synchroInfo(loadFileInfo, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null) {
                            ToastUtils.showToast(UploadFileActivity.this, fileInfo.getmFileName() + getString(R.string.upload_filesavafail),
                                    Effects.flip, R.id.id_root);
                            loadFileInfo.delete(); //删除上传的记录
                            mCurRemoveIndex++;
                        } else {
                            mDatas.remove(mCurRemoveIndex); //成功就移除文件
                            mFilePaths.remove(mCurRemoveIndex);
                            mFileUploadAdapter.notifyItemRemoved(mCurRemoveIndex);
                        }
                        mCurUploadFileNum++;
                        if (mCurUploadFileNum == totalSize) {
                            DialogUtils.closeFileLoadDialog();//关闭文件加载器
                        }
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                DialogUtils.closeFileLoadDialog();//关闭文件加载器
                //展示Toast
                ToastUtils.showToast(UploadFileActivity.this, mDatas.get(mCurRemoveIndex).getmFileName() + getString(R.string.upload_filefail),
                        Effects.flip, R.id.id_root);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                String fileUriPath = UriUtils.queryFileUri(this, data.getData());
                if (TextUtils.isEmpty(fileUriPath)) {
                    return;
                }
                File file = new File(fileUriPath);
                String filePath = file.getAbsolutePath();//文件路径
                String fileName = file.getName();  //文件名
                String fileSize = Formatter.formatFileSize(this, file.length()); //文件大小
                mFilePaths.add(0, filePath);
                mDatas.add(0, new FileInfo(fileName, filePath, fileSize));
                mFileUploadAdapter.notifyDataSetChanged();
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
        mFilePaths.remove(model.getmFilePath());
        mDatas.remove(model);
        mFileUploadAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(FileInfo model) {
    }
}
