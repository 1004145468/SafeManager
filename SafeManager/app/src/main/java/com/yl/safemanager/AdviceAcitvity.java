package com.yl.safemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.yl.safemanager.base.BaseTitleBackActivity;
import com.yl.safemanager.constant.Constant;
import com.yl.safemanager.entities.AdviceModel;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.DialogUtils;
import com.yl.safemanager.utils.SFGT;
import com.yl.safemanager.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AdviceAcitvity extends BaseTitleBackActivity {

    private static final String TAG = "AdviceAcitvity";

    @BindView(R.id.advice_msg)
    EditText mContentView;

    @BindView(R.id.advice_pic)
    ImageView mPicView;

    @BindView(R.id.advice_btn)
    Button mSubmitView;

    private Uri mPicUri; //选择图片的路径
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnTextChanged(R.id.advice_msg)
    public void onTextChanged() {
        content = mContentView.getText().toString();
        if (TextUtils.isEmpty(content)) {
            mSubmitView.setEnabled(false);
        } else {
            mSubmitView.setEnabled(true);
        }
    }

    @OnClick(R.id.advice_pic)
    public void selectPic() {
        //选择图片
        SFGT.gotoImagePick(this);
    }


    @OnClick(R.id.advice_btn)
    public void submitAdview() {
        //提交意见
        DialogUtils.showIndeterminateDialog(this, getString(R.string.advice_submitting), false, null);
        if (mPicUri == null) {
            //只提交意见
            AdviceModel adviceModel = new AdviceModel(BmobUtils.getCurrentUser().getUsername(), content, "");
            BmobUtils.synchroInfo(adviceModel, new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    DialogUtils.shutdownIndeterminateDialog();
                    ToastUtils.showToast(AdviceAcitvity.this,
                            e == null ? getString(R.string.advice_success) : getString(R.string.advice_fail),
                            Effects.slideIn, R.id.id_root);
                }
            });
        } else {
            //带附件提交意见
            BmobUtils.uploadFile(this, mPicUri, new BmobUtils.onUploadFileResult() {
                @Override
                public void onResult(BmobException e, String fileUrl) {
                    if (e != null) {
                        DialogUtils.shutdownIndeterminateDialog();
                        ToastUtils.showToast(AdviceAcitvity.this, getString(R.string.advice_fileupload_fail), Effects.slideIn, R.id.id_root);
                    } else {
                        AdviceModel adviceModel = new AdviceModel(BmobUtils.getCurrentUser().getUsername(), content, fileUrl);
                        BmobUtils.synchroInfo(adviceModel, new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                DialogUtils.shutdownIndeterminateDialog();
                                ToastUtils.showToast(AdviceAcitvity.this,
                                        e == null ? getString(R.string.advice_success) : getString(R.string.advice_fail),
                                        Effects.slideIn, R.id.id_root);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SFGT.IMAGEPICK_REQUEST_CODE) {
                mPicUri = data.getData();
                mPicView.setImageURI(mPicUri);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_advice_acitvity;
    }

    @Override
    public String getBarTitle() {
        return Constant.FUNCTION_IDEA;
    }
}
