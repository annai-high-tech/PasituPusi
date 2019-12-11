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

    private Button buttonSave, buttonReset, buttonDatePicker;
    private EditText editTextProfileName, editTextEmail, editTextPhone, editTextDob, editTextAbout;

    private ProfileViewModel profileViewModel;
    private ProfileData currrentProfileData;

    public ProfileFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);

        profileViewModel.getProfileData().observe(getActivity(), mObserverResult);


        editTextProfileName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextDob = view.findViewById(R.id.editTextDOB);
        editTextAbout = view.findViewById(R.id.editTextAbout);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonReset = view.findViewById(R.id.buttonReset);
        buttonDatePicker = view.findViewById(R.id.buttonDatePick);

        buttonSave.setOnClickListener(listener);
        buttonReset.setOnClickListener(listener);
        buttonDatePicker.setOnClickListener(listener);

        profileViewModel.createProfileData();

        return view;
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){
            /* Go to next fragment in navigation stack*/
            //mActivity.pushFragments(AppConstants.TAB_A, new AppTabAFragment2(),true,true);

            if(v.getId() == buttonSave.getId()) {
                if (editTextProfileName.getText() != null)
                    currrentProfileData.setName(editTextProfileName.getText().toString());
                if (editTextEmail.getText() != null)
                    currrentProfileData.setEmail(editTextEmail.getText().toString());
                if (editTextPhone.getText() != null)
                    currrentProfileData.setPhone(editTextPhone.getText().toString());
                if (editTextDob.getText() != null)
                    currrentProfileData.setDob(editTextDob.getText().toString());
                if (editTextAbout.getText() != null)
                    currrentProfileData.setAbout(editTextAbout.getText().toString());

            }

            if(v.getId() == buttonReset.getId()) {
                //profileViewModel.getProfileData().setValue();
            }

            if(v.getId() == buttonDatePicker.getId()) {
                DialogFragment newFragment = new DatePickerFragment(editTextDob);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }

            profileViewModel.updateProfileData(currrentProfileData);

        }
    };

    Observer<ProfileData> mObserverResult = new Observer<ProfileData>() {
        @Override
        public void onChanged(@Nullable ProfileData profileData) {

            //loadingProgressBar.setVisibility(View.GONE);

            currrentProfileData = profileData;

            if (profileData == null) {
                return;
            }

            editTextProfileName.setText(profileData.getName());
            editTextEmail.setText(profileData.getEmail());
            editTextPhone.setText(profileData.getPhone());
            editTextDob.setText(profileData.getDob());
            editTextAbout.setText(profileData.getAbout());
        }
    };


}
