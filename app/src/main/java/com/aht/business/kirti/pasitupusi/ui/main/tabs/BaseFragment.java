package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class BaseFragment extends Fragment{

    private MainActivity mActivity;

    public BaseFragment() {
        mActivity = (MainActivity)getActivity();
    }

}
