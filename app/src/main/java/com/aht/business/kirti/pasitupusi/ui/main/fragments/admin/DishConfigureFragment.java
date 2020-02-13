package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

public class DishConfigureFragment extends BaseFragment {

    private Button mGoToButton;
    private TextView textViewWelcomeMsg;

    private DailyMenuViewModel dailyMenuViewModel;

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

        textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi - Dish Configure page");

        dailyMenuViewModel = new ViewModelProvider(this).get(DailyMenuViewModel.class);
        mGoToButton = new Button(this.getContext());
        mGoToButton.setOnClickListener(listener);
        mGoToButton.setText("Refresh All Time menu");


        return view;
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(view.getId() == mGoToButton.getId()) {

                dailyMenuViewModel.addAllTimeMenu();

            }
        }
    };
}
