package com.yl.safemanager.networkinterface;

import com.yl.safemanager.entities.TokenResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by YL on 2017/3/19.
 */

public interface SafeNetInterface {

    @POST("/user/getToken.json")
    Call<TokenResult> getToken(@Field("userId") String userId, @Field("name") String name, @Field("portraitUri") String portraitUri);
}
