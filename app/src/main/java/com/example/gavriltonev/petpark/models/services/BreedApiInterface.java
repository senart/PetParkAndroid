package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Breed;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Created by gavriltonev on 12/10/15.
 */
public interface BreedApiInterface {

    // Gets all the breeds by species name
    @GET("api/breeds/{speciesName}")
    Call<List<Breed>> getBreeds(@Header("Authorization") String authorizationToken, @Path("speciesName") String species);
}
