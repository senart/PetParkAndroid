package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Token;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by gavriltonev on 12/8/15.
 */
public interface TokenApiInterface {
    @Headers ({
        "Accept: application/json"
    })
    @FormUrlEncoded
    @POST("token")
    Call<Token> getToken(@Field("grant_type") String grantType, @Field("username") String email, @Field("password") String password);
}
