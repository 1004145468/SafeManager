package com.yl.safemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.yl.safemanager.adapter.LockFileAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.LockFileModel;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.interfact.onEncryptItemOnclickListener;
import com.yl.safemanager.utils.DataBaseUtils;
import com.yl.safemanager.utils.FileConcealUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

public class FileLockActivity extends BaseTitleBackActivity implements onEncryptItemOnclickListener {

    private static final String TAG = "FileLockActivity";
    private static final int REQUEST_FILE = 1;

    private static final int ENCRYPTION_FAIL = 2;
    private static final int ENCRYPTION_SUCCESS = 3;
    private static final int DECRYPTION_FAIL = 4;
    private static final int DECRYPTION_SUCCESS = 5;

    private List<LockFileModel> mDatas;
    private LockFileAdapter lockFileAdapter;

    @BindView(R.id.filelock_list)
    RecyclerView mFileLockListView;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENCRYPTION_FAIL:
                    Toast.makeText(FileLockActivity.this, "加密失败", Toast.LENGTH_SHORT).show();
                    break;
                case ENCRYPTION_SUCCESS:
                    Toast.makeText(FileLockActivity.this, "加密失败", Toast.LENGTH_SHORT).show();
                    break;
                case DECRYPTION_FAIL:
                    Toast.makeText(FileLockActivity.this, "解密失败", Toast.LENGTH_SHORT).show();
                    break;
                case DECRYPTION_SUCCESS:
                    Toast.makeText(FileLockActivity.this, "解密成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBaseUtils.initRealm(this);
        initViews();
        initDatas();
    }

    private void initDatas() {
        DataBaseUtils.getAllLockFileModels(new OnResultAttachedListener<RealmResults<LockFileModel>>() {
            @Override
            public void onResult(RealmResults<LockFileModel> lockFileModels) {
                int length = lockFileModels.size();
                for (int i = 0; i < length; i++) {
                    mDatas.add(lockFileModels.get(i));
                }
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILE) {
                Uri uri = data.getData();
                String srcPath = uri.getPath();
                String desPath = srcPath.substring(0, srcPath.lastIndexOf("/") + 1) + "LOCK" + srcPath.substring(srcPath.lastIndexOf("/") + 1);
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
                    DataBaseUtils.saveLockFileModel(originName, desName, filePath, desPath, new OnResultAttachedListener<LockFileModel>() {
                        @Override
                        public void onResult(LockFileModel lockFileModel) {
                            mDatas.add(0, lockFileModel);
                            lockFileAdapter.notifyItemInserted(0);
                        }
                    });
                    mHandler.sendEmptyMessage(ENCRYPTION_SUCCESS);
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
     * @param filePath 源文件绝对路径
     * @param desPath  目标文件绝对路劲
     */
    public void DecryptFile(final String filePath, final String desPath, final long id, final int position) {
        new Thread() {
            @Override
            public void run() {
                Boolean result = FileConcealUtils.DecryptionFile(getApplicationContext(), filePath, desPath);
                if (result) { //成功就删除源文件
                    File srcFile = new File(filePath);
                    if (srcFile.exists() && srcFile.isFile()) {
                        srcFile.delete();
                    }
                    //TODO 删除数据库一条数据
                    DataBaseUtils.deleteLockFileModel(id);
                    //TODO recyclerview的數據刷新
                    mDatas.remove(position);
                    lockFileAdapter.notifyItemRemoved(position);
                    mHandler.sendEmptyMessage(DECRYPTION_SUCCESS);
                } else {
                    File desFile = new File(desPath); //失败就删除错误文件
                    if (desFile.exists() && desFile.isFile()) {
                        desFile.delete();
                    }
                    mHandler.sendEmptyMessage(DECRYPTION_FAIL);
                }
            }
        }.start();
    }
}
