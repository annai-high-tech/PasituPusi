package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.PlaceholderFragment;

public abstract class BaseFragment {

    MainActivity mActivity;

    public BaseFragment(Activity activity) {
        mActivity = (MainActivity) activity;
    }

    MainActivity getActivity() {
        return mActivity;
    }

    public abstract View initView(Fragment fragment, LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState);

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

}
