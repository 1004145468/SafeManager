package com.yl.safemanager.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.safemanager.R;
import com.yl.safemanager.base.BaseSfHolder;
import com.yl.safemanager.entities.SafeFunctionInfo;
import com.yl.safemanager.entities.SafeFunctionItem;
import com.yl.safemanager.entities.SafeUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/1.
 */

public class FunctionAdapter extends RecyclerView.Adapter<BaseSfHolder> {

    private static final String TAG = "FunctionAdapter";

    private LayoutInflater mLayoutInflater;
    private ArrayList<SafeFunctionItem> mDatas;

    public FunctionAdapter(Context context, ArrayList<SafeFunctionItem> datas) {
        mLayoutInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getCurrentType();
    }

    @Override
    public BaseSfHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = null;
        BaseSfHolder baseSfHolder = null;
        if (viewType == SafeFunctionItem.TYPE_HEAD) {
            rootView = mLayoutInflater.inflate(R.layout.view_headinfo, parent, false);
            baseSfHolder = new HeadViewHolder(rootView);
        } else if (viewType == SafeFunctionItem.TYPE_FUNCTION) {
            rootView = mLayoutInflater.inflate(R.layout.view_functionitem, parent, false);
            baseSfHolder = new FunctionViewHolder(rootView);
        }
        return baseSfHolder;
    }

    @Override
    public void onBindViewHolder(BaseSfHolder holder, int position) {
        holder.setData(mDatas.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * Holder 相关
     */
    class HeadViewHolder extends BaseSfHolder {

        @BindView(R.id.head_bg)
        SimpleDraweeView bgView;
        @BindView(R.id.head_nick)
        TextView nickView;
        @BindView(R.id.head_sex)
        ImageView sexView;
        @BindView(R.id.head_msg)
        TextView msgView;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            SafeUser safeUser = (SafeUser) data;
            //绑定数据
            nickView.setText(safeUser.getmNick());
            sexView.setImageResource(safeUser.getmSex() == SafeUser.MAN ? R.drawable.global_icon_male : R.drawable.global_icon_female);
            msgView.setText(safeUser.getmNote());
            bgView.setImageURI(Uri.parse(safeUser.getmPortrait()));
        }

        @OnClick(R.id.head_edit)
        public void changePeopleInfo() {
            Log.d(TAG, "changePeopleInfo: ========================================");
            //TODO
        }

        @OnClick(R.id.head_mail)
        public void gotoConversionListActivity() {
            Log.d(TAG, "gotoConversionListActivity: ====================================");
            //TODO
        }


    }

    class FunctionViewHolder extends BaseSfHolder {

        @BindView(R.id.function_img)
        ImageView functionImg;
        @BindView(R.id.function_name)
        TextView functionName;
        @BindView(R.id.function_description)
        TextView functionDescription;

        public FunctionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Object data) {
            SafeFunctionInfo info = (SafeFunctionInfo) data;
            functionImg.setImageResource(info.getFunctionImg());
            functionName.setText(info.getFunctionName());
            functionDescription.setText(info.getFunctionDescription());
        }
    }
}
