package com.aht.business.kirti.pasitupusi.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.BaseFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ChatFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ProfileFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private BaseFragment currentFragment;
    private PageViewModel pageViewModel;
    private int currentIndex = 1;


    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        currentIndex = 1;
        if (getArguments() != null) {
            currentIndex = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        if(currentIndex == 1) {
            currentFragment = new ProfileFragment(this.getActivity());
        } else if(currentIndex == 2) {
            currentFragment = new ChatFragment(this.getActivity());
        } else {
            currentFragment = new ChatFragment(this.getActivity());
        }
        //pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = currentFragment.initView(inflater, container, savedInstanceState);

        final TextView textView = root.findViewById(R.id.section_label);
        final Activity activity = getActivity();

        textView.setText("Hello world from section: " + currentIndex);

        /*pageViewModel.getText().observe(this, new Observer<PageData>() {
            @Override
            public void onChanged(@Nullable PageData data) {
                textView.setText(activity.getResources().getString(data.getId()));
            }
        });*/
        return root;
    }

    public void onBackPressed(){
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
    }


}