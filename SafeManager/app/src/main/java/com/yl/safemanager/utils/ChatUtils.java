package com.yl.safemanager.utils;

import android.content.Context;
import android.os.Handler;

import com.yl.safemanager.app.SafeApplication;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.entities.TokenResult;
import com.yl.safemanager.interfact.OnResultAttachedListener;

import java.io.IOException;

import cn.bmob.v3.helper.GsonUtil;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by YL on 2017/3/19.
 */

public class ChatUtils {

    /**
     * 获取Token
     *
     * @param listener
     */
    public static void getTokenByPost(final OnResultAttachedListener<TokenResult> listener) {
        SafeUser currentUser = BmobUtils.getCurrentUser();
        if (currentUser == null) {
            if (listener != null) {
                listener.onResult(null);
                return;
            }
        }
        final Handler handler = new Handler();
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userId", currentUser.getUsername())
                .add("name", currentUser.getmNick())
                .add("portraitUri", currentUser.getmPortrait())
                .build();
        Request request = new Request.Builder()
                .url("http://123.207.237.185:8080/SafeManager/user/getToken")
                .post(formBody)
                .build();
        Call tokenCall = okHttpClient.newCall(request);
        tokenCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onResult(null);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final TokenResult tokenBean = (TokenResult) GsonUtil.toObject(response.body().string(), TokenResult.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onResult(tokenBean);
                        }
                    }
                });
            }
        });
    }

    /**
     * 进行连接时使用
     *
     * @param context
     * @param token
     * @param listener
     */
    public static void connnect(Context context, String token, RongIMClient.ConnectCallback listener) {
        if (context.getApplicationInfo().packageName.equals(SafeApplication.getCurProcessName(getApplicationContext()))) {
            RongIM.getInstance().setMessageAttachedUserInfo(true);
            RongIM.connect(token, listener);

        }
    }
}
