package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class HomeFragment extends BaseFragment {

    private MainActivity activity;
    private Button mGotoButton;
    private TextView textViewWelcomeMsg;

    public HomeFragment(MainActivity activity) {
        super(activity);

        this.activity = activity;
    }

    @Override
    public View initView(Fragment fragment, LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        textViewWelcomeMsg =  view.findViewById(R.id.home_welcome);
        //mGoToButton = (Button) view.findViewById(R.id.goto_button);
        //mGoToButton.setOnClickListener(listener);

        textViewWelcomeMsg.setText("Hello " + activity.getUserDisplayName() + "!\n\tWelcome to Pasitu Pusi Menu");
        return view;
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){
            /* Go to next fragment in navigation stack*/
            //mActivity.pushFragments(AppConstants.TAB_A, new AppTabAFragment2(),true,true);
        }
    };
}