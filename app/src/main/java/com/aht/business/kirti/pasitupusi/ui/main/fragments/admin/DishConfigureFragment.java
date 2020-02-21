package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.AllTimeMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

public class DishConfigureFragment extends BaseFragment {

    private Button button;
    private TextView textViewWelcomeMsg;
    private LinearLayout content_layout;
    private ProgressDialog progressDialog;

    private AllTimeMenuViewModel allTimeMenuViewModel;

    public static DishConfigureFragment newInstance() {
        Bundle args = new Bundle();
        DishConfigureFragment f = new DishConfigureFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dish_configure, container, false);

        textViewWelcomeMsg =  view.findViewById(R.id.titleTextView);
        content_layout = view.findViewById(R.id.content_layout);

        textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi - Dish Configure page");

        progressDialog = new ProgressDialog(this.getContext());

        allTimeMenuViewModel = new ViewModelProvider(this).get(AllTimeMenuViewModel.class);
        button = new Button(this.getContext());
        button.setOnClickListener(listener);
        button.setText("Refresh All Time menu");

        content_layout.addView(button);

        return view;
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(view.getId() == button.getId()) {

                progressDialog.setMessage("Loading...");
                progressDialog.show();

                allTimeMenuViewModel.addAllTimeMenuCategory().observe(getViewLifecycleOwner(), mObserverResult1);

            }
        }
    };

    Observer<Boolean> mObserverResult1 = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean result) {

            if(result) {
                allTimeMenuViewModel.addAllTimeMenuElement().observe(getViewLifecycleOwner(), mObserverResult2);

            } else {

                progressDialog.dismiss();
            }

        }
    };

    Observer<Boolean> mObserverResult2 = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean result) {

            if(result) {
                progressDialog.dismiss();

            } else {

                progressDialog.dismiss();
            }

        }
    };
}
