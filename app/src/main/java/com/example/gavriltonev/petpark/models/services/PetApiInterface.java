package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.utils.Preferences;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;

/**
 * Created by gavriltonev on 12/7/15.
 */
public interface PetApiInterface {

    @GET("api/pets")
    Call<List<Pet>> getAllPets(@Header("Authorization") String authorizationToken);
}
