package cn.sharesdk.onekeyshare.themes.classic;

import com.yl.safemanager.R;
import com.yl.safemanager.constant.Constant;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by YangLang on 2017/6/27.
 * 分享参数的组装类
 */

public class ShareContentCallback implements ShareContentCustomizeCallback {

    private String title;
    private String titleUrl;
    private String content;
    private boolean shareTextbyWx;

    public ShareContentCallback(String title, String titleUrl, String content, boolean shareTextbyWx) {
        this.title = title;
        this.titleUrl = titleUrl;
        this.content = content;
        this.shareTextbyWx = shareTextbyWx;
    }

    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        paramsToShare.setTitle(title);
        paramsToShare.setTitleUrl(titleUrl);
        paramsToShare.setText(content);
        paramsToShare.setImageUrl("hhttp://api.open.qq.com/tfs/show_img.php?appid=1106113100&uuid=app1106113100_40_40%7C1048576%7C1492665031.6646");
        paramsToShare.setUrl(Constant.DEFAULT_LINK);
        String currentPlatform = platform.getName();
        if (currentPlatform.equals("QZone")) {
            paramsToShare.setSite(Constant.APP_NAME);
            paramsToShare.setSiteUrl(Constant.DEFAULT_LINK);
        } else if (currentPlatform.equals("Wechat") || currentPlatform.equals("WechatMoments")) {
            if (shareTextbyWx) {  //只分享分本
                paramsToShare.setShareType(Platform.SHARE_TEXT);
            } else {
                paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
            }
        }
    }
}
