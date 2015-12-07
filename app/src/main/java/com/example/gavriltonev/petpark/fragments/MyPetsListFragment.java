package com.example.gavriltonev.petpark.fragments;

import android.support.v4.app.ListFragment;

/**
 * Created by gavriltonev on 12/7/15.
 */
public class MyPetsListFragment extends ListFragment {

    public static MyPetsListFragment getInstance() {
        MyPetsListFragment fragment = new MyPetsListFragment();

        return fragment;
    }
}
