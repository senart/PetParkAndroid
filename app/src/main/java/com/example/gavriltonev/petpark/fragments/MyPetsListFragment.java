package com.example.gavriltonev.petpark.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.services.PetApiInterface;
import com.example.gavriltonev.petpark.utils.Preferences;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by gavriltonev on 12/7/15.
 */
public class MyPetsListFragment extends ListFragment {

    public static MyPetsListFragment getInstance() {
        MyPetsListFragment fragment = new MyPetsListFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<List<Pet>> call = petApiInterface.getAllPets("Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR));
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Response<List<Pet>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    Log.e("NULL RESPONSE BODY: ", response.message());
                } else {
                    for (Pet pet : response.body()) {
                        Log.e("PET FOUND:", pet.getBreed());
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
