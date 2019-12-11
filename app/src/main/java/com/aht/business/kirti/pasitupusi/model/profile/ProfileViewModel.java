package com.aht.business.kirti.pasitupusi.model.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<ProfileData> profileData = new MutableLiveData<>();
    ProfileManager profileManager = new ProfileManager();

    public LiveData<ProfileData> getProfileData() {
        return profileData;
    }

    public void createProfileData() {
        profileManager.createProfileData(profileData);
    }

    public void updateProfileData(ProfileData data) {
        profileManager.updateProfileData(data, profileData);
    }
}