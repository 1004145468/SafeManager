package com.yl.safemanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.BmobUtils;
import com.yl.safemanager.utils.SFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static com.xiaomi.push.service.aa.f;

/**
 * Created by YL on 2017/3/19.
 */

public class ConversationListActivity extends FragmentActivity implements RongIM.UserInfoProvider {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.friend_id)
    EditText mFriendView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversationlist);
        RongIM.setUserInfoProvider(this, true);
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

    @Override
    public UserInfo getUserInfo(String s) {
        BmobUtils.getUserInfo(s, new OnResultAttachedListener<SafeUser>() {
            @Override
            public void onResult(SafeUser safeUser) {
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(safeUser.getUsername(), safeUser.getmNick(), Uri.parse(safeUser.getmPortrait())));
            }
        });
        return null;
    }
}
