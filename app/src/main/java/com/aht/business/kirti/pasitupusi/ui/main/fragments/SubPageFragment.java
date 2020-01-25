package com.aht.business.kirti.pasitupusi.ui.main.fragments;

import android.os.Bundle;

import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class SubPageFragment extends BaseFragment {

    public SubPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
