package com.yl.safemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.adapter.SMDataAdapter;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.decoraion.SafeItemDecoration;
import com.yl.safemanager.entities.SmDataModel;
import com.yl.safemanager.interfact.OnItemClickListener;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class SMDataActivity extends BaseTitleBackActivity implements OnItemClickListener<SmDataModel> {

    private static final String TAG = "SMDataActivity";

    @BindView(R.id.note_listview)
    RecyclerView mRecyclerView;

    private SimpleDateFormat mDateFormater;

    private List<SmDataModel> mDatas;
    private SMDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SafeItemDecoration());
        mDatas = new ArrayList<>();
        mAdapter = new SMDataAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
    }

    //加载数据
    private void initDatas() {
        if (mDatas != null && mDatas.size() > 0) {
            mDatas.clear(); //清楚缓存
        }
        mDateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        DialogUtils.showIndeterminateDialog(this, getString(R.string.load_data), false, null);
        BmobUtils.getSmDateModels(new OnResultAttachedListener<List<SmDataModel>>() {
            @Override
            public void onResult(List<SmDataModel> smDataModels) {
                DialogUtils.shutdownIndeterminateDialog(); //隐藏加载进度条
                if (smDataModels != null) {
                    mDatas.addAll(smDataModels);
                    mAdapter.notifyDataSetChanged();// 刷新数据展示
                }
            }
        });
    }

    @OnClick(R.id.note_add)
    public void addNote() {
        //添加一个记录
        DialogUtils.showIndeterminateDialog(this, getString(R.string.new_dataing), false, null);
        final SmDataModel smDataModel = new SmDataModel(BmobUtils.getCurrentUser().getUsername(),
                mDateFormater.format(new Date()), getString(R.string.new_data), "");
        BmobUtils.synchroInfo(smDataModel, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                DialogUtils.shutdownIndeterminateDialog();
                if (e != null) {
                    Log.d(TAG, "done: " + e.toString());
                    ToastUtils.showToast(SMDataActivity.this, getString(R.string.new_data_fail), Effects.flip, R.id.id_root);
                } else {
                    mDatas.add(0, smDataModel);
                    mAdapter.notifyItemInserted(0);
                }
            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_smdata;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_DATARECORD;
    }

    @Override
    public void onClick(SmDataModel model) {
        //跳转到SmData数据的编辑界面
        Intent intent = new Intent(this, EditSmDataActivity.class);
        intent.putExtra("objectid", model.getObjectId());
        intent.putExtra("title", model.getTitle());
        intent.putExtra("content", model.getContent());
        intent.putExtra("savetime", model.getSavetime());
        startActivity(intent);
    }

    @Override
    public void onLongClick(final SmDataModel model) {
        DialogUtils.showMessageDialog(this, getString(R.string.dialog_deletemsg), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DialogUtils.showIndeterminateDialog(SMDataActivity.this, getString(R.string.load_data), false, null);  //打开进度
                SmDataModel smDataModel = new SmDataModel();
                smDataModel.setObjectId(model.getObjectId());
                BmobUtils.deleteInfo(smDataModel, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        DialogUtils.shutdownIndeterminateDialog(); //隐藏
                        if (e != null) {
                            ToastUtils.showToast(SMDataActivity.this, getString(R.string.delete_fail), Effects.flip, R.id.id_root);
                        } else {
                            int position = model.getPosition();
                            mDatas.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ToastUtils.showToast(SMDataActivity.this, getString(R.string.data_update_success), Effects.flip, R.id.id_root);
                        }
                    }
                });
            }
        });
    }
}
