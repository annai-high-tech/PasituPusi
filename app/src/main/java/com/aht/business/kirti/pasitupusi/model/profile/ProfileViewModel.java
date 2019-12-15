package com.aht.business.kirti.pasitupusi.model.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileData> profileData = new MutableLiveData<>();
    private MutableLiveData<Boolean> profileCheckData = new MutableLiveData<>();
    ProfileManager profileManager = new ProfileManager();

    public LiveData<ProfileData> getProfileData() {
        return profileData;
    }

    public LiveData<Boolean> getProfileCheckData() {
        return profileCheckData;
    }

    public void createProfileData() {
        profileManager.createProfileData(profileData);
    }

    public void updateProfileData(ProfileData data) {
        profileManager.updateProfileData(data, profileData);
    }

    public void isProfileCreated() {
        profileManager.isValidProfile(profileCheckData);
    }
}