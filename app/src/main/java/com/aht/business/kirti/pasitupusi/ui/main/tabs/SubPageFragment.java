package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
