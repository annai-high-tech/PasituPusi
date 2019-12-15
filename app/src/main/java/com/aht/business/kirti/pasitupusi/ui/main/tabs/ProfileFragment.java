package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileViewModel;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.ui.main.dialog.DatePickerFragment;

public class ProfileFragment extends BaseFragment {

    private Button buttonSave, buttonReset;
    private EditText editTextProfileName, editTextEmail, editTextPhone;

    private ProfileViewModel profileViewModel;
    private ProfileData currrentProfileData;

    public ProfileFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextProfileName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonReset = view.findViewById(R.id.buttonReset);

        buttonSave.setOnClickListener(listener);
        buttonReset.setOnClickListener(listener);

        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
        profileViewModel.getProfileData().observe(getActivity(), mObserverResult);
        profileViewModel.createProfileData();

        return view;
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){
            /* Go to next fragment in navigation stack*/
            //mActivity.pushFragments(AppConstants.TAB_A, new AppTabAFragment2(),true,true);

            if(v.getId() == buttonSave.getId() && currrentProfileData != null) {
                if (editTextProfileName.getText() != null)
                    currrentProfileData.setName(editTextProfileName.getText().toString());
                if (editTextEmail.getText() != null)
                    currrentProfileData.setEmail(editTextEmail.getText().toString());
                if (editTextPhone.getText() != null)
                    currrentProfileData.setPhone(editTextPhone.getText().toString());

            }

            if(v.getId() == buttonReset.getId()) {
                //profileViewModel.getProfileData().setValue();
            }

            profileViewModel.updateProfileData(currrentProfileData);

        }
    };

    Observer<ProfileData> mObserverResult = new Observer<ProfileData>() {
        @Override
        public void onChanged(@Nullable ProfileData profileData) {

            //loadingProgressBar.setVisibility(View.GONE);
            String name = "", email = "", phone = "";

            currrentProfileData = profileData;

            if (profileData != null) {
                if(profileData.getName() != null) {
                    name = profileData.getName();
                }
                if(profileData.getEmail() != null) {
                    email = profileData.getEmail();
                }
                if(profileData.getPhone() != null) {
                    phone = profileData.getPhone();
                }
            }

            editTextProfileName.setText(name);
            editTextEmail.setText(email);
            editTextPhone.setText(phone);
        }
    };


}
