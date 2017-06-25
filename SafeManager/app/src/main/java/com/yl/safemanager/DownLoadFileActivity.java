package com.yl.safemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.adapter.FileDownLoadAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeEmptyItemDecoration;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.LoadFileInfo;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.interfact.OnItemSwipeListener;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.ToastUtils;
import com.yl.safemanager.view.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by YL on 2017/3/17.
 */

public class DownLoadFileActivity extends BaseTitleBackActivity implements OnItemSwipeListener<LoadFileInfo>, OnItemClickListener<LoadFileInfo> {

    private static final String TAG = "DownLoadFileActivity";

    @BindView(R.id.download_listview)
    RecyclerView mDownLoadListView;

    private List<LoadFileInfo> mDatas;
    private FileDownLoadAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
    }

    private void initData() {
        DialogUtils.showIndeterminateDialog(this, getString(R.string.load_data), false, null);
        BmobUtils.getLoadFileInfos(new OnResultAttachedListener<List<LoadFileInfo>>() {
            @Override
            public void onResult(List<LoadFileInfo> loadFileInfos) {
                DialogUtils.shutdownIndeterminateDialog();
                if (loadFileInfos != null) {
                    mDatas.addAll(loadFileInfos);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * 初始化展示
     */
    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDownLoadListView.setLayoutManager(linearLayoutManager);
        mDatas = new ArrayList<>();
        mAdapter = new FileDownLoadAdapter(this, mDatas);
        mDownLoadListView.setAdapter(mAdapter);
        mDownLoadListView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_DATARECOVER;
    }

    @Override
    public void onItemDone(LoadFileInfo model, int viewid) {
        switch (viewid) {
            case R.id.file_share:  //分享
                ToastUtils.showOriginToast(this, "开始分享");
                break;
            case R.id.file_delete: // 删除
                deleteFile(model);
                break;
        }
    }

    @Override
    public void onClick(LoadFileInfo model) {
        //打开文件下载进度器
        final String downLoadString = getString(R.string.download_file);
        DialogUtils.showFileLoadDialog(this, downLoadString);
        //下载文件
        BmobUtils.downloadFile(model.getBmobFile(), new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                DialogUtils.closeFileLoadDialog();
                if (e != null) {
                    ToastUtils.showToast(DownLoadFileActivity.this, getString(R.string.download_filefail), Effects.flip, R.id.id_root);
                } else {
                    ToastUtils.showToast(DownLoadFileActivity.this, getString(R.string.download_filesuccess), Effects.flip, R.id.id_root);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {
                DialogUtils.updateFileLoadDialog(downLoadString + " ( " + l / 1024 + " k/s )", integer, integer + "%");
            }
        });
    }

    @Override
    public void onLongClick(LoadFileInfo model) {
    }

    private void deleteFile(final LoadFileInfo model) {
        final String objectId = model.getObjectId();
        DialogUtils.showMessageDialog(this, getString(R.string.dialog_deletemsg), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //删除文件
                final String downLoadString = getString(R.string.delete_file);
                DialogUtils.showIndeterminateDialog(DownLoadFileActivity.this, downLoadString, false, null);
                LoadFileInfo loadFileInfo = new LoadFileInfo();
                loadFileInfo.setObjectId(objectId);
                BmobUtils.deleteInfo(loadFileInfo, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        DialogUtils.closeFileLoadDialog();
                        if (e != null) {
                            ToastUtils.showToast(DownLoadFileActivity.this, getString(R.string.delete_fail), Effects.flip, R.id.id_root);
                        } else {
                            mDatas.remove(model);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
