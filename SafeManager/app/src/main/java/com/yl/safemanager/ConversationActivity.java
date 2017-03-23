package com.yl.safemanager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.widget.TextView;

import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.interfact.OnResultAttachedListener;
import com.yl.safemanager.utils.BmobUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by YL on 2017/3/19.
 */

public class ConversationActivity extends FragmentActivity implements RongIM.UserInfoProvider {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    private ArrayMap<String, UserInfo> mUserInfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        String title = getIntent().getData().getQueryParameter("title");
        mTitleView.setText(title);
        mUserInfos = new ArrayMap<>();
        RongIM.setUserInfoProvider(this, true);
    }

    @OnClick(R.id.btn_back)
    public void exitActivity() {
        finish();
        overridePendingTransition(R.anim.slide_toleft, R.anim.slide_toright);
    }

    @Override
    public UserInfo getUserInfo(final String s) {
        UserInfo currentUserInfo = mUserInfos.get(s);
        if (currentUserInfo != null) {
            return currentUserInfo;
        }
        BmobUtils.getUserInfo(s, new OnResultAttachedListener<SafeUser>() {
            @Override
            public void onResult(SafeUser safeUser) {
                if (safeUser != null) {
                    UserInfo userInfo = new UserInfo(safeUser.getUsername(), safeUser.getmNick(), Uri.parse(safeUser.getmPortrait()));
                    mUserInfos.put(s, userInfo); //设置用户信息
                }
            }
        });
        return null;
    }
}
