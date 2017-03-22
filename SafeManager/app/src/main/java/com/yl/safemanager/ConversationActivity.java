package com.yl.safemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/19.
 */

public class ConversationActivity extends FragmentActivity {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        String title = getIntent().getData().getQueryParameter("title");
        mTitleView.setText(title);
    }

    @OnClick(R.id.btn_back)
    public void exitActivity() {
        finish();
        overridePendingTransition(R.anim.slide_toleft, R.anim.slide_toright);
    }
}
