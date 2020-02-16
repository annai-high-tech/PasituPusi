package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.profile.ProfilePhotoManager;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileViewModel;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    private TextInputEditText editTextPhone, editTextProfileName, editTextEmail, editTextSecPhone;
    private TextInputEditText editTextAddress1, editTextAddress2, editTextLandMark;
    private TextInputEditText editTextCity, editTextState, editTextCountry, editTextPinCode;
    private ImageView imageViewProfilePic;
    private TextView textViewProfilePicEdit, textViewProfilePicSave, textViewProfilePicCancel;
    private TextView textViewProfileEdit, textViewProfileSave, textViewProfileCancel;
    private TextView textViewProfileAddrEdit, textViewProfileAddrSave, textViewProfileAddrCancel;
    private ImageView imageViewProfileCollapse, imageViewProfileAddrCollapse;
    private LinearLayout layoutProfileCollapse, layoutProfileAddrCollapse;

    private LinearLayout editableProfileLayout, editableProfileAddressLayout;

    private ProfileViewModel profileViewModel;
    private ProfileData currrentProfileData;
    private ProfilePhotoManager profilePhotoManager;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment f = new ProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePhotoManager = new ProfilePhotoManager(getActivity());

        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextSecPhone = view.findViewById(R.id.editTextAlternatePhone);
        editTextProfileName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextAddress1 = view.findViewById(R.id.editTextAddr1);
        editTextAddress2 = view.findViewById(R.id.editTextAddr2);
        editTextLandMark = view.findViewById(R.id.editTextLandmark);
        editTextCity = view.findViewById(R.id.editTextCity);
        editTextState = view.findViewById(R.id.editTextState);
        editTextCountry = view.findViewById(R.id.editTextCountry);
        editTextPinCode = view.findViewById(R.id.editTextPinCode);

        textViewProfilePicEdit = view.findViewById(R.id.textViewProfilePicEdit);
        textViewProfilePicSave = view.findViewById(R.id.textViewProfilePicSave);
        textViewProfilePicCancel = view.findViewById(R.id.textViewProfilePicCancel);
        textViewProfileEdit = view.findViewById(R.id.textViewProfileEdit);
        textViewProfileSave = view.findViewById(R.id.textViewProfileSave);
        textViewProfileCancel = view.findViewById(R.id.textViewProfileCancel);
        imageViewProfileCollapse = view.findViewById(R.id.imageViewProfileCollapse);
        layoutProfileCollapse = view.findViewById(R.id.layoutProfileCollapse);
        textViewProfileAddrEdit = view.findViewById(R.id.textViewProfileAddressEdit);
        textViewProfileAddrSave = view.findViewById(R.id.textViewProfileAddressSave);
        textViewProfileAddrCancel = view.findViewById(R.id.textViewProfileAddressCancel);
        imageViewProfileAddrCollapse = view.findViewById(R.id.imageViewProfileAddressCollapse);
        layoutProfileAddrCollapse = view.findViewById(R.id.layoutProfileAddressCollapse);

        imageViewProfilePic = view.findViewById(R.id.profile_pic);

        editableProfileLayout = view.findViewById(R.id.editableProfileLayout);
        editableProfileAddressLayout = view.findViewById(R.id.editableProfileAddressLayout);

        textViewProfilePicEdit.setOnClickListener(listener);
        textViewProfilePicSave.setOnClickListener(listener);
        textViewProfilePicCancel.setOnClickListener(listener);
        textViewProfileEdit.setOnClickListener(listener);
        textViewProfileSave.setOnClickListener(listener);
        textViewProfileCancel.setOnClickListener(listener);
        imageViewProfileCollapse.setOnClickListener(listener);
        layoutProfileCollapse.setOnClickListener(listener);
        textViewProfileAddrEdit.setOnClickListener(listener);
        textViewProfileAddrSave.setOnClickListener(listener);
        textViewProfileAddrCancel.setOnClickListener(listener);
        imageViewProfileAddrCollapse.setOnClickListener(listener);
        layoutProfileAddrCollapse.setOnClickListener(listener);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), mObserverResult);
        profileViewModel.createProfile();

        setProfilePicView(false);
        setProfileDetailView(false);
        setProfileAddressView(false);
        editableProfileLayout.setVisibility(View.VISIBLE);
        editableProfileAddressLayout.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            Bitmap thumbnail = null;

            if (requestCode == ProfilePhotoManager.PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_UPDATE) {
                thumbnail = profilePhotoManager.onSelectImageResult(data);
            }
            else if (requestCode == ProfilePhotoManager.PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_REMOVE) {
                thumbnail = null;
                imageViewProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));
            }

            if(thumbnail != null) {
                imageViewProfilePic.setImageBitmap(thumbnail);
            }

        }
    }

    private void setProfilePicView(boolean editable) {

        if(editable) {
            textViewProfilePicEdit.setVisibility(View.GONE);
            textViewProfilePicSave.setVisibility(View.VISIBLE);
            textViewProfilePicCancel.setVisibility(View.VISIBLE);
            profilePhotoManager.selectImage(ProfileFragment.this);
        } else {
            textViewProfilePicEdit.setVisibility(View.VISIBLE);
            textViewProfilePicSave.setVisibility(View.GONE);
            textViewProfilePicCancel.setVisibility(View.GONE);
        }

    }

    private void setProfileDetailView(boolean editable) {

        if(editable) {
            textViewProfileEdit.setVisibility(View.GONE);
            textViewProfileSave.setVisibility(View.VISIBLE);
            textViewProfileCancel.setVisibility(View.VISIBLE);
        } else {
            textViewProfileEdit.setVisibility(View.VISIBLE);
            textViewProfileSave.setVisibility(View.GONE);
            textViewProfileCancel.setVisibility(View.GONE);
        }

        editTextPhone.setEnabled(false);
        editTextSecPhone.setEnabled(editable);
        editTextProfileName.setEnabled(editable);
        editTextEmail.setEnabled(editable);

    }

    private void setProfileAddressView(boolean editable) {

        if(editable) {
            textViewProfileAddrEdit.setVisibility(View.GONE);
            textViewProfileAddrSave.setVisibility(View.VISIBLE);
            textViewProfileAddrCancel.setVisibility(View.VISIBLE);
        } else {
            textViewProfileAddrEdit.setVisibility(View.VISIBLE);
            textViewProfileAddrSave.setVisibility(View.GONE);
            textViewProfileAddrCancel.setVisibility(View.GONE);
        }

        editTextAddress1.setEnabled(editable);
        editTextAddress2.setEnabled(editable);
        editTextLandMark.setEnabled(editable);
        editTextCity.setEnabled(editable);
        editTextState.setEnabled(editable);
        editTextCountry.setEnabled(editable);
        editTextPinCode.setEnabled(editable);
    }

    private void saveProfilePicture(boolean save) {

        if(save) {
            BitmapDrawable drawable = (BitmapDrawable) imageViewProfilePic.getDrawable();
            if(drawable != null) {

                if(BitmapUtils.BitMapToString(drawable.getBitmap()).equals(BitmapUtils.BitMapToString(((BitmapDrawable)getResources().getDrawable(R.drawable.ic_person)).getBitmap()))) {
                    currrentProfileData.setPicture(null);
                } else {
                    currrentProfileData.setPicture(BitmapUtils.BitMapToString(drawable.getBitmap()));
                }
            }
        }

        setProfilePicView(false);
        profileViewModel.updateProfile(currrentProfileData);
        ((MainActivity)getActivity()).setProfileData(currrentProfileData);
    }

    private void saveProfileDetails(boolean save) {

        if(save) {
            if (editTextProfileName.getText() != null)
                currrentProfileData.setName(editTextProfileName.getText().toString());
            if (editTextEmail.getText() != null)
                currrentProfileData.setEmail(editTextEmail.getText().toString());
            if (editTextPhone.getText() != null)
                currrentProfileData.setPhone(editTextPhone.getText().toString());
            if (editTextSecPhone.getText() != null)
                currrentProfileData.setSecondaryPhone(editTextSecPhone.getText().toString());
        }

        setProfileDetailView(false);
        profileViewModel.updateProfile(currrentProfileData);
        ((MainActivity)getActivity()).setProfileData(currrentProfileData);
    }

    private void saveProfileAddress(boolean save) {

        if(save) {
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
            if (editTextCountry.getText() != null)
                currrentProfileData.setCountry(editTextCountry.getText().toString());
            if (editTextPinCode.getText() != null) {
                currrentProfileData.setPincode(0);
                if(!editTextPinCode.getText().toString().equals(""))
                    currrentProfileData.setPincode(Integer.parseInt(editTextPinCode.getText().toString()));
            }
        }

        setProfileAddressView(false);
        profileViewModel.updateProfile(currrentProfileData);
        ((MainActivity)getActivity()).setProfileData(currrentProfileData);
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View v){

            if(v.getId() == textViewProfilePicEdit.getId()) {
                setProfilePicView(true);
            }
            else if(v.getId() == textViewProfilePicSave.getId()) {
                saveProfilePicture(true);
            }
            else if(v.getId() == textViewProfilePicCancel.getId()) {
                saveProfilePicture(false);
            }

            if(v.getId() == textViewProfileEdit.getId()) {
                setProfileDetailView(true);
            }
            else if(v.getId() == textViewProfileSave.getId()) {
                saveProfileDetails(true);
            }
            else if(v.getId() == textViewProfileCancel.getId()) {
                saveProfileDetails(false);
            }
            else if(v.getId() == imageViewProfileCollapse.getId() || v.getId() == layoutProfileCollapse.getId()) {
                toggle_contents(imageViewProfileCollapse, editableProfileLayout);
            }

            if(v.getId() == textViewProfileAddrEdit.getId()) {
                setProfileAddressView(true);
            }
            else if(v.getId() == textViewProfileAddrSave.getId()) {
                saveProfileAddress(true);
            }
            else if(v.getId() == textViewProfileAddrCancel.getId()) {
                saveProfileAddress(false);
            }
            else if(v.getId() == imageViewProfileAddrCollapse.getId() || v.getId() == layoutProfileAddrCollapse.getId()) {
                toggle_contents(imageViewProfileAddrCollapse, editableProfileAddressLayout);
            }

        }
    };

    Observer<ProfileData> mObserverResult = new Observer<ProfileData>() {
        @Override
        public void onChanged(@Nullable ProfileData profileData) {

            //loadingProgressBar.setVisibility(View.GONE);
            String name = "", email = "", phone = "", secPhone = "";
            String addr1 = "", addr2 = "", landmark = "";
            String city = "", state = "", country = "", pincode = "";
            Bitmap thumbnail = null;
            String profileText = "", fullAddress = "";

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
                    profileText = phone;
                }
                if(profileData.getSecondaryPhone() != null) {
                    secPhone = profileData.getSecondaryPhone();
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
                if(profileData.getCountry() != null) {
                    country = profileData.getCountry();
                }
                if(profileData.getPincode() != 0) {
                    pincode = String.valueOf(profileData.getPincode());
                }
                if(profileData.getPicture() != null) {
                    thumbnail = BitmapUtils.StringToBitMap(profileData.getPicture());
                }
            }

            if(email != null && !email.equals("")) {
                if(!profileText.equals("")) {
                    profileText += " - ";
                }
                profileText += email;
            }

            if(!addr1.equals(""))
                fullAddress += addr1 + "\n";
            if(!addr2.equals(""))
                fullAddress += addr2 + "\n";
            if(!city.equals(""))
                fullAddress += city + "\n";
            if(!state.equals("")) {
                fullAddress += state;
                if(!pincode.equals(""))
                    fullAddress += " - ";
                else
                    fullAddress += "\n";
            }
            if(!pincode.equals(""))
                fullAddress += pincode + "\n";
            if(!landmark.equals(""))
                fullAddress += "Landmark: " + landmark;

            editTextProfileName.setText(name);
            editTextEmail.setText(email);
            editTextPhone.setText(phone);
            editTextSecPhone.setText(secPhone);
            editTextAddress1.setText(addr1);
            editTextAddress2.setText(addr2);
            editTextLandMark.setText(landmark);
            editTextCity.setText(city);
            editTextState.setText(state);
            editTextCountry.setText(country);
            editTextPinCode.setText(pincode);

            if(thumbnail != null)
                imageViewProfilePic.setImageBitmap(thumbnail);
        }
    };


    private void toggle_contents(ImageView sourceClick, View destView){

        if(destView.isShown()){
            AnimationUtil.slide_up(this.getContext(), destView, sourceClick);

        }
        else{
            AnimationUtil.slide_down(this.getContext(), destView, sourceClick);

            /*if(!isFullyVisible(destView)) {
                System.out.println("....................................................................");
            }*/
        }

        /*if(destView.isShown()) {
            sourceClick.setText("---");
        } else {
            sourceClick.setText("+++");
        }*/
    }

    public boolean isFullyVisible(final View view) {
        if (view == null) {
            return false;
        }
        if (!view.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        view.getDrawingRect(actualPosition);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        final Rect screen = new Rect(0, 0, width, height);
        return actualPosition.intersect(screen);
    }
}
