package com.yl.safemanager.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.TextView;

import com.yl.safemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseTitleBackActivity extends Activity implements Animation.AnimationListener {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mTitleView.setText(getBarTitle());
    }

    public abstract int getLayoutId();

    public abstract String getBarTitle();

    @OnClick(R.id.btn_back)
    public void exitActivity(){
        finish();
        overridePendingTransition(R.anim.slide_toleft,R.anim.slide_toright);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
