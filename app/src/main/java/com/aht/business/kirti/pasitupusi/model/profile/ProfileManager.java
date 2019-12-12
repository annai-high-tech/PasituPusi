package com.aht.business.kirti.pasitupusi.model.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

public class ProfileManager {

    private FirebaseUser currentUser;

    public void createProfileData(final MutableLiveData<ProfileData> result) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("user_data").document(currentUser.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ProfileData user = null;

                    if(document.exists()) {

                        user = document.toObject(ProfileData.class);

                        //System.out.println(user.getEmail());

                    } else {
                        user = new ProfileData();

                        if(isNonEmptyString(currentUser.getDisplayName()))
                            user.setName(currentUser.getDisplayName());
                        if(isNonEmptyString(currentUser.getEmail()))
                            user.setEmail(currentUser.getEmail());
                        if(isNonEmptyString(currentUser.getPhoneNumber()))
                            user.setPhone(currentUser.getPhoneNumber());

                        //docRef.set(user, SetOptions.merge());
                        docRef.set(user);
                    }

                    result.setValue(user);

                } else {
                    //Excepion message to be shown to user
                    //TODO
                    System.out.println("Error in accessing the database: " + task.getException().getMessage());
                }
            }
        });
    }

    public void updateProfileData(final ProfileData data, final MutableLiveData<ProfileData> result) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("user_data").document(currentUser.getUid());

        docRef.set(data).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                if(task.isSuccessful()) {

                    if(data.getName() != null && !data.getName().equals(currentUser.getDisplayName())) {

                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(data.getName())
                                .build();
                        currentUser.updateProfile(userProfileChangeRequest);
                    }
                    if(data.getEmail() != null && !data.getEmail().equals(currentUser.getEmail())) {
                        currentUser.updateEmail(data.getEmail());
                    }
                    if(data.getPhone() != null && !data.getPhone().equals(currentUser.getPhoneNumber())) {
                        //currentUser.updatePhoneNumber(data.getPhone());
                    }

                     result.setValue(data);

                } else {
                    //Excepion message to be shown to user
                    //TODO
                    System.out.println("Error in accessing the database: " + task.getException().getMessage());
                }
            }
        });
    }

    private boolean isNonEmptyString(String data) {

        if(data != null && !data.isEmpty())
            return true;

        return false;
    }
}