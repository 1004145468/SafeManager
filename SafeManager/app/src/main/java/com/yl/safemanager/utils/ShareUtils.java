package com.yl.safemanager.utils;

import android.content.Context;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.themes.classic.ShareContentCallback;

/**
 * Created by YangLang on 2017/6/27.
 */

public class ShareUtils {

    private static final String TAG = "ShareUtils";

    public static void share(final Context context, String title, String titleUrl, String content,boolean shareTextbyWx) {
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCallback(title,titleUrl,content,shareTextbyWx));
        oks.show(context);
    }
}
