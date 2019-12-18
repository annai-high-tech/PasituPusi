package com.aht.business.kirti.pasitupusi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.ui.main.tabs.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private BaseFragment currentFragment;
    private PageViewModel pageViewModel;

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }


    public static PlaceholderFragment newInstance(BaseFragment currentFragment) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.setCurrentFragment(currentFragment);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

     }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = currentFragment.initView(this, inflater, container, savedInstanceState);

        return root;
    }

    public void onBackPressed(){
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //super.onActivityResult(requestCode, resultCode, data);
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }


}