package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

public class AdminViewOrderFragment extends BaseFragment {

    private Button mGotoButton;
    private TextView textViewWelcomeMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_vieworder, container, false);

        textViewWelcomeMsg =  view.findViewById(R.id.home_welcome);
        //mGoToButton = (Button) view.findViewById(R.id.goto_button);
        //mGoToButton.setOnClickListener(listener);

        textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi - view order page");
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