package com.yl.safemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.yl.safemanager.adapter.LockFileAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.LockFileModel;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.DataBaseUtils;
import com.yl.safemanager.utils.FileConcealUtils;
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

import static com.yl.safemanager.utils.SFGT.REQUEST_FILE;

public class FileLockActivity extends BaseTitleBackActivity implements OnItemClickListener<LockFileModel> {

    private static final String TAG = "FileLockActivity";

    private static final String LockFileIndex = ".";

    private static final int ENCRYPTION_FAIL = 2;
    private static final int ENCRYPTION_SUCCESS = 3;
    private static final int DECRYPTION_FAIL = 4;
    private static final int DECRYPTION_SUCCESS = 5;

    private List<LockFileModel> mDatas;
    private LockFileAdapter lockFileAdapter;
    private SimpleDateFormat mTimerFormater;

    @BindView(R.id.filelock_list)
    RecyclerView mFileLockListView;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENCRYPTION_FAIL:
                    ToastUtils.showOriginToast(FileLockActivity.this, getString(R.string.encrypt_fail));
                    break;
                case ENCRYPTION_SUCCESS:
                    ToastUtils.showOriginToast(FileLockActivity.this, getString(R.string.encrypt_success));
                    LockFileModel lockFileModel = (LockFileModel) msg.obj;
                    DataBaseUtils.saveLockFileModel(lockFileModel); //加密记录存入数据库
                    mDatas.add(0, lockFileModel);
                    lockFileAdapter.notifyDataSetChanged();
                    break;
                case DECRYPTION_FAIL:
                    ToastUtils.showOriginToast(FileLockActivity.this, getString(R.string.decrypt_fail));
                    break;
                case DECRYPTION_SUCCESS:
                    ToastUtils.showOriginToast(FileLockActivity.this, getString(R.string.decrypt_success));
                    LockFileModel lockFileModel1 = (LockFileModel) msg.obj;
                    DataBaseUtils.deleteLockFileModel(lockFileModel1.getId());
                    mDatas.remove(lockFileModel1);
                    lockFileAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseUtils.initRealm(this);
        mTimerFormater = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        initViews();
        initDatas();
    }

    private void initDatas() {
        DataBaseUtils.getAllLockFileModels(new OnResultAttachedListener<List<LockFileModel>>() {
            @Override
            public void onResult(List<LockFileModel> lockFileModels) {
                mDatas.addAll(lockFileModels);
                lockFileAdapter.notifyDataSetChanged(); //刷新展示
            }
        });
    }

    private void initViews() {
        mFileLockListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFileLockListView.addItemDecoration(new SafeItemDecoration());
        mDatas = new ArrayList<>();
        lockFileAdapter = new LockFileAdapter(this, mDatas);
        mFileLockListView.setAdapter(lockFileAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseUtils.closeRealm();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filelock;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_FILELOCK;
    }

    @OnClick(R.id.filelock_add)
    public void addLockFile() { //添加加密文件
        SFGT.openFileChoose(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                String srcPath = UriUtils.queryFileUri(this, data.getData());
                if (TextUtils.isEmpty(srcPath)) {
                    return;
                }
                String desPath = srcPath.substring(0, srcPath.lastIndexOf("/") + 1) + LockFileIndex + srcPath.substring(srcPath.lastIndexOf("/") + 1);
                EncryptFile(srcPath, desPath); //文件加密
            }
        }
    }

    /**
     * 对文件进行加密
     *
     * @param filePath 源文件绝对路径
     * @param desPath  目标文件绝对路劲
     */
    private void EncryptFile(final String filePath, final String desPath) {
        new Thread() {
            @Override
            public void run() {
                Boolean result = FileConcealUtils.EncryptionFile(getApplicationContext(), filePath, desPath);
                if (result) { //成功就删除源文件
                    File srcFile = new File(filePath);
                    if (srcFile.exists() && srcFile.isFile()) {
                        srcFile.delete();
                    }
                    String originName = filePath.substring(filePath.lastIndexOf("/") + 1);
                    String desName = desPath.substring(desPath.lastIndexOf("/") + 1);
                    LockFileModel lockFileModel = new LockFileModel(
                            System.currentTimeMillis(), mTimerFormater.format(new Date()),
                            originName, desName, filePath, desPath);
                    mHandler.sendMessage(mHandler.obtainMessage(ENCRYPTION_SUCCESS, lockFileModel));
                } else {
                    File desFile = new File(desPath); //失败就删除错误文件
                    if (desFile.exists() && desFile.isFile()) {
                        desFile.delete();
                    }
                    mHandler.sendEmptyMessage(ENCRYPTION_FAIL);
                }
            }
        }.start();
    }

    /**
     * 对文件进行解密
     *
     * @param lockFileModel
     */
    public void onClick(final LockFileModel lockFileModel) {
        new Thread() {
            @Override
            public void run() {
                Boolean result = FileConcealUtils.DecryptionFile(getApplicationContext(), lockFileModel.getLockFilePath(), lockFileModel.getOriginFilePath());
                if (result) { //成功就删除源文件
                    File srcFile = new File(lockFileModel.getLockFilePath());
                    if (srcFile.exists() && srcFile.isFile()) {
                        srcFile.delete();
                    }
                    mHandler.sendMessage(mHandler.obtainMessage(DECRYPTION_SUCCESS, lockFileModel));
                } else {
                    File desFile = new File(lockFileModel.getOriginFilePath()); //失败就删除错误文件
                    if (desFile.exists() && desFile.isFile()) {
                        desFile.delete();
                    }
                    mHandler.sendEmptyMessage(DECRYPTION_FAIL);
                }
            }
        }.start();
    }

    @Override
    public void onLongClick(LockFileModel model) {
    }
}
