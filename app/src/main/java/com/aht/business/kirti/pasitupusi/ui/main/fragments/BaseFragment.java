package com.aht.business.kirti.pasitupusi.ui.main.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class BaseFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(this.getResources().getString(R.string.app_name));
        }

    }

}
