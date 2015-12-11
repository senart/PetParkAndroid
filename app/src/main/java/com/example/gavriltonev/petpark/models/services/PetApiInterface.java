package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.PetImage;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by gavriltonev on 12/7/15.
 */
public interface PetApiInterface {

    @GET("api/pets")
    Call<List<Pet>> getAllPets(@Header("Authorization") String authorizationToken);

    @GET("api/pets/{id}")
    Call<Pet> getPet(@Header("Authorization") String authorizationToken, @Path("id") Integer id);

    @GET("api/pets/{species}/bySpecies")
    Call<List<Pet>> getAllPetsBySpecies(@Header("Authorization") String authorizationToken, @Path("species") String species);

    @GET("api/account/pets")
    Call<List<Pet>> getUserPets(@Header("Authorization") String authorizationToken);

    @GET("api/pets/{id}/images")
    Call<List<PetImage>> getPetImages(@Header("Authorization") String authorizationToken, @Path("id") Integer id);

    @Multipart
    @POST("api/pets/{id}/uploadImage")
    Call<ResponseBody> uploadImage(
            @Header("Authorization") String authorizationToken,
            @Path("id") Integer id,
            @Part("someFile") RequestBody image
    );

    @POST("api/pets/{id}/attatchprofileimage")
    Call<ResponseBody> attatchProfileImage(@Header("Authorization") String authorizationToken, @Path("id") Integer id, @Body PetImage petImage);

    @POST("api/pets")
    Call<ResponseBody> createPet(@Header("Authorization") String authorizationToken, @Body Pet pet);
}
