package com.yl.safemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.utils.FileConcealUtils;

import java.io.File;

import butterknife.OnClick;

import static com.yl.safemanager.utils.FileConcealUtils.EncryptionFile;

public class FileLockActivity extends BaseTitleBackActivity {

    private static final String TAG = "FileLockActivity";
    private static final int REQUEST_FILE = 1;

    private static final int ENCRYPTION_FAIL = 2;
    private static final int ENCRYPTION_SUCCESS = 3;
    private static final int DECRYPTION_FAIL = 4;
    private static final int DECRYPTION_SUCCESS = 5;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ENCRYPTION_FAIL:
                    Toast.makeText(FileLockActivity.this, "加密失败", Toast.LENGTH_SHORT).show();
                    break;
                case ENCRYPTION_SUCCESS:
                    Toast.makeText(FileLockActivity.this, "加密成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                EncryptFile(srcPath,desPath); //文件加密
                //TODO 存入数据库
                //TODO recyclerview的數據刷新
            }
        }
    }

    /**
     * 对文件进行解密
     * @param filePath 源文件绝对路径
     * @param desPath 目标文件绝对路劲
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
}
