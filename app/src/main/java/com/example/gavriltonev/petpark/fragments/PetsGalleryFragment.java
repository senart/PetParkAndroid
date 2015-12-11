package com.example.gavriltonev.petpark.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.gavriltonev.petpark.R;
import com.example.gavriltonev.petpark.activities.PetDetailActivity;
import com.example.gavriltonev.petpark.adapters.PetsGalleryAdapter;
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
 * Created by gavriltonev on 12/8/15.
 */
public class PetsGalleryFragment extends Fragment {


    private GridView mGridView;
    private PetsGalleryAdapter petsGalleryAdapter;

    public static PetsGalleryFragment getInstance() {
        PetsGalleryFragment fragment = new PetsGalleryFragment();

        return fragment;
    }

    private void listPets() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.petparkAPI))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Gets all pets from the database
        // TODO: Add pagination?
        PetApiInterface petApiInterface = retrofit.create(PetApiInterface.class);
        Call<List<Pet>> call = petApiInterface.getAllPets("Bearer " + Preferences.getInstance(getContext()).getString(Preferences.Key.TOKEN_STR));
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Response<List<Pet>> response, Retrofit retrofit) {
                if (response.body() == null) {
                    Log.e("NULL RESPONSE BODY: ", response.message());
                } else {
                    for (Pet pet : response.body()) {
                        petsGalleryAdapter.add(pet);
                    }

                    petsGalleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getContext(), "Failure on request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGridView = (GridView) view.findViewById(R.id.petGalleryGrid);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        petsGalleryAdapter = new PetsGalleryAdapter(getActivity(),0);
        mGridView.setAdapter(petsGalleryAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PetDetailActivity.class);
                intent.putExtra(PetDetailActivity.EXTRA_PET, petsGalleryAdapter.getItem(position));
                startActivity(intent);
            }
        });
        listPets();
    }
}
