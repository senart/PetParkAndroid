package com.example.gavriltonev.petpark.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.activities.PetDetailActivity;
import com.example.gavriltonev.petpark.adapters.MyPetsListAdapter;
import com.example.gavriltonev.petpark.models.Pet;
import com.example.gavriltonev.petpark.models.PetImage;
import com.example.gavriltonev.petpark.models.services.PetApiInterface;
import com.example.gavriltonev.petpark.utils.PetImageDeserializer;
import com.example.gavriltonev.petpark.utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    private MyPetsListAdapter myPetsListAdapter;

    public static MyPetsListFragment getInstance() {
        MyPetsListFragment fragment = new MyPetsListFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListShown(false);

        myPetsListAdapter = new MyPetsListAdapter(getActivity(),0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Gets only the pets of the user
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<List<Pet>> call = petApiInterface.getUserPets("Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR));
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Response<List<Pet>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    Log.e("NULL RESPONSE BODY: ", response.message());
                } else {
                    for (Pet pet : response.body()) {
                        myPetsListAdapter.add(pet);
                    }

                    myPetsListAdapter.notifyDataSetChanged();
                    setListAdapter(myPetsListAdapter);
                    setListShown(true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), PetDetailActivity.class);
        intent.putExtra(PetDetailActivity.EXTRA_PET, myPetsListAdapter.getItem(position));
        intent.putExtra("foreignUser", false);
        startActivity(intent);
    }
}
