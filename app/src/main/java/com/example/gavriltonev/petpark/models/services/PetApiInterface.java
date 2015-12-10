package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by gavriltonev on 12/7/15.
 */
public interface PetApiInterface {

    @GET("api/pets")
    Call<List<Pet>> getAllPets(@Header("Authorization") String authorizationToken);

    @GET("api/account/pets")
    Call<List<Pet>> getUserPets(@Header("Authorization") String authorizationToken);

    @POST("api/pets")
    Call<ResponseBody> createPet(@Header("Authorization") String authorizationToken, @Body Pet pet);
}
