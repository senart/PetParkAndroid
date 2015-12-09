package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.User;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by gavriltonev on 12/9/15.
 */
public interface UserApiInterface {

    @POST("api/account/register")
    Call<ResponseBody> createUser(@Body User user);
}
