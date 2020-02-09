package com.aht.business.kirti.pasitupusi.ui.main.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class SubPageFragment extends BaseFragment {

    private String title;

    public SubPageFragment() {
        this(null);
    }

    public SubPageFragment(String title) {
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        if(title != null) {
            actionBar.setTitle(title);
        } else {
            actionBar.setTitle(this.getResources().getString(R.string.app_name));
        }
    }

}
