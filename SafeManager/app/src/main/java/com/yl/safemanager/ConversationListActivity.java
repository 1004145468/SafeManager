package com.yl.safemanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YL on 2017/3/19.
 */

public class ConversationListActivity extends FragmentActivity {

    @BindView(R.id.friend_id)
    EditText mFriendView;

    @BindView(R.id.chatto)
    ImageView mChatToView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversationlist);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.chatto)
    public void chatWithFriend() {

    }
}
