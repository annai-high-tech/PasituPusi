package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.profile.ProfilePhotoManager;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileViewModel;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.ui.main.dialog.DatePickerFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    private Button buttonSave, buttonReset;
    private EditText editTextProfileName, editTextEmail, editTextPhone;
    private EditText editTextAddress1, editTextAddress2, editTextLandMark;
    private EditText editTextCity, editTextState, editTextPinCode;
    private ImageView imageViewProfilePic;

    private Fragment currentFragment;

    private ProfileViewModel profileViewModel;
    private ProfileData currrentProfileData;
    private ProfilePhotoManager profilePhotoManager;

    public ProfileFragment(Activity activity) {
        super(activity);
    }

    @Override
    public View initView(Fragment fragment, LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentFragment = fragment;
        profilePhotoManager = new ProfilePhotoManager(getActivity());

        editTextProfileName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextAddress1 = view.findViewById(R.id.editTextAddr1);
        editTextAddress2 = view.findViewById(R.id.editTextAddr2);
        editTextLandMark = view.findViewById(R.id.editTextLandmark);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextState = view.findViewById(R.id.editTextState);
        editTextPinCode = view.findViewById(R.id.editTextPinCode);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonReset = view.findViewById(R.id.buttonReset);

        imageViewProfilePic = view.findViewById(R.id.profile_pic);

        buttonSave.setOnClickListener(listener);
        buttonReset.setOnClickListener(listener);
        imageViewProfilePic.setOnClickListener(listener);

        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
        profileViewModel.getProfileData().observe(getActivity(), mObserverResult);
        profileViewModel.createProfileData();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Bitmap thumbnail = null;

            if (requestCode == 1) {
                thumbnail = profilePhotoManager.onCaptureImageResult(data);
            } else if (requestCode == 2) {
                thumbnail = profilePhotoManager.onSelectFromGalleryResult(data);
            }

            if(thumbnail != null) {
                imageViewProfilePic.setImageBitmap(thumbnail);
            }

        }
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){
            /* Go to next fragment in navigation stack*/
            //mActivity.pushFragments(AppConstants.TAB_A, new AppTabAFragment2(),true,true);

            if(v.getId() == imageViewProfilePic.getId()) {

                profilePhotoManager.selectImage(currentFragment);
                return;
            }

            if(v.getId() == buttonSave.getId() && currrentProfileData != null) {
                if (editTextProfileName.getText() != null)
                    currrentProfileData.setName(editTextProfileName.getText().toString());
                if (editTextEmail.getText() != null)
                    currrentProfileData.setEmail(editTextEmail.getText().toString());
                if (editTextPhone.getText() != null)
                    currrentProfileData.setPhone(editTextPhone.getText().toString());
                if (editTextAddress1.getText() != null)
                    currrentProfileData.setAddress_line1(editTextAddress1.getText().toString());
                if (editTextAddress2.getText() != null)
                    currrentProfileData.setAddress_line2(editTextAddress2.getText().toString());
                if (editTextLandMark.getText() != null)
                    currrentProfileData.setLandMark(editTextLandMark.getText().toString());
                if (editTextCity.getText() != null)
                    currrentProfileData.setCity(editTextCity.getText().toString());
                if (editTextState.getText() != null)
                    currrentProfileData.setState(editTextState.getText().toString());
                if (editTextPinCode.getText() != null) {
                    currrentProfileData.setPincode(0);
                    if(!editTextPinCode.getText().toString().equals(""))
                        currrentProfileData.setPincode(Integer.parseInt(editTextPinCode.getText().toString()));
                }

                BitmapDrawable drawable = (BitmapDrawable) imageViewProfilePic.getDrawable();
                if(drawable != null)
                    currrentProfileData.setPicture(BitMapToString(drawable.getBitmap()));

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
            String addr1 = "", addr2 = "", landmark = "";
            String city = "", state = "", pincode = "";
            Bitmap thumbnail = null;

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
                if(profileData.getAddress_line1() != null) {
                    addr1 = profileData.getAddress_line1();
                }
                if(profileData.getAddress_line2() != null) {
                    addr2 = profileData.getAddress_line2();
                }
                if(profileData.getLandMark() != null) {
                    landmark = profileData.getLandMark();
                }
                if(profileData.getCity() != null) {
                    city = profileData.getCity();
                }
                if(profileData.getState() != null) {
                    state = profileData.getState();
                }
                if(profileData.getPincode() != 0) {
                    pincode = String.valueOf(profileData.getPincode());
                }
                if(profileData.getPicture() != null) {
                    thumbnail = StringToBitMap(profileData.getPicture());
                }
            }

            editTextProfileName.setText(name);
            editTextEmail.setText(email);
            editTextPhone.setText(phone);
            editTextAddress1.setText(addr1);
            editTextAddress2.setText(addr2);
            editTextLandMark.setText(landmark);
            editTextCity.setText(city);
            editTextState.setText(state);
            editTextPinCode.setText(pincode);

            if(thumbnail != null)
                imageViewProfilePic.setImageBitmap(thumbnail);
        }
    };


    private String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] arr=baos.toByteArray();
        String result= Base64.encodeToString(arr, Base64.DEFAULT);
        return result;
    }

    private Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
