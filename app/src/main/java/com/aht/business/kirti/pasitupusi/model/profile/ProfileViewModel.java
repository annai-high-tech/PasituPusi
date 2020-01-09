package com.aht.business.kirti.pasitupusi.model.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileData> profileData = new MutableLiveData<>();
    private MutableLiveData<ProfileData> profileCheckData = new MutableLiveData<>();
    ProfileManager profileManager = new ProfileManager();

    public LiveData<ProfileData> getProfileData() {
        return profileData;
    }

    public LiveData<ProfileData> getProfileCheckData() {
        return profileCheckData;
    }

    public void createProfile() {
        profileManager.createProfile(profileData);
    }

    public void updateProfile(ProfileData data) {
        profileManager.updateProfile(data, profileData);
    }

    public void getProfile() {
        profileManager.getProfile(profileCheckData);
    }
}