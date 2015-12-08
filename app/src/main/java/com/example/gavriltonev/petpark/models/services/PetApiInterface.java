package com.example.gavriltonev.petpark.models.services;

import com.example.gavriltonev.petpark.models.Pet;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by gavriltonev on 12/7/15.
 */
public interface PetApiInterface {

    @Headers({
            "Accept: application/json",
            "Authorization: Bearer OV8ql0SB02v02v7pA9HBF82Rxakcjc3k1lqfD7lc-xAzGkF7YMHz-ns6UIwjly9It6FOu3hqfha3Dzmx1JnDGhIpUPCuSXGVP4Muko-Fm0ZNSpZkjVcKKO5aXSJW0dliNDIHZ9VWKj2Cxwhxcdw_egvSIsq_bFj4G4jBoekT9nHsQVsOyo5yXDkv6QuaF7-TF_EQ9deV_46AKr9767MA0I8ygfhROHfYPRusQTHv11DtTVZY0CSHRFx5zfKHeh_4m4EDCgi6mx5Q6IFpE0OSRQCmggS3Phz7jmcvHlBDpOfbr12L6oP-vOwzamV9rPUnrZrD6yqG8kozxBrSKBbrSh_bBROq5gin_MI5Z4pOlUjB-I1ORDfKYm89QQebsxgHAMd46tykV6tqx4-YitM2l3GtRLEisoV1v0lwjFs5x7uF_sysXZ3zGGso0BJ083vJxrsiwZ3iBaA-43lW92fHLTh-Gopcqf18VxbGOApt2isCKJE18MNjW3VD3jop245M"
    })
    @GET("api/pets")
    Call<List<Pet>> getAllPets();
}
