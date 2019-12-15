package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aht.business.kirti.pasitupusi.R;

public class HomeFragment extends BaseFragment {

    private Button mGotoButton;

    public HomeFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //mGoToButton = (Button) view.findViewById(R.id.goto_button);
        //mGoToButton.setOnClickListener(listener);

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
