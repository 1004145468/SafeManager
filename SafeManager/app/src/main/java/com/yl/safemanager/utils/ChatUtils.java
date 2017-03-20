package com.yl.safemanager.utils;

import android.content.Context;

import com.yl.safemanager.app.SafeApplication;
import com.yl.safemanager.entities.SafeUser;
import com.yl.safemanager.entities.TokenResult;
import com.yl.safemanager.networkinterface.SafeNetInterface;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

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
    public static void getToken(Callback<TokenResult> listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.cn.ronghub.com")
                .build();

        SafeNetInterface safeNetInterface = retrofit.create(SafeNetInterface.class);
        SafeUser currentUser = BmobUtils.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        Call<TokenResult> resultCall = safeNetInterface.getToken(currentUser.getUsername(), currentUser.getmNick(), currentUser.getmPortrait());
        resultCall.enqueue(listener);
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
