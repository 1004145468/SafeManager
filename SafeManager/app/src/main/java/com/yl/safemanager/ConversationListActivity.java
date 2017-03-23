package com.yl.safemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.yl.safemanager.utils.SFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;

/**
 * Created by YL on 2017/3/19.
 */

public class ConversationListActivity extends FragmentActivity{

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.friend_id)
    EditText mFriendView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversationlist);
        ButterKnife.bind(this);
        mTitleView.setText(getString(R.string.chathistory));
    }

    @OnClick(R.id.chatto)
    public void chatWithFriend() {
        String frientId = mFriendView.getText().toString().trim();
        if (TextUtils.isEmpty(frientId)) {
            return;
        }
        SFGT.gotoConversionActivity(this, frientId);

    }

    @OnClick(R.id.btn_back)
    public void exitActivity() {
        finish();
        overridePendingTransition(R.anim.slide_toleft, R.anim.slide_toright);
    }
}
