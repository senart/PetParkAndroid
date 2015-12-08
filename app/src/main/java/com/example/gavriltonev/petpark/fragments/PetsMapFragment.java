package com.example.gavriltonev.petpark.fragments;

import com.google.android.gms.maps.SupportMapFragment;


/**
 * Created by gavriltonev on 12/7/15.
 */
public class PetsMapFragment extends SupportMapFragment {

    public static PetsMapFragment getInstance() {
        PetsMapFragment fragment = new PetsMapFragment();

        return fragment;
    }
}
