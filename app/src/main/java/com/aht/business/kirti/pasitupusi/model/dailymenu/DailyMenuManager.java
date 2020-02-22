package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenu;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

//import com.google.api.core.ApiFuture;

public class DailyMenuManager {

    private FirebaseUser currentUser;

    public void getDailyMenu(final String date, final MutableLiveData<DailyMenuList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(date == null) {
            result.setValue(new DailyMenuList());
            return;
        }

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(date);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DailyMenuList dailyMenuList = new DailyMenuList();
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        dailyMenuList = document.toObject(DailyMenuList.class);

                    }
                }

                result.setValue(dailyMenuList);

            }
        });

    }

    public void getDishPicture(final DocumentReference dishIdRef, final MutableLiveData<String> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(dishIdRef == null) {
            result.setValue(null);
            return;
        }

        dishIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String picture = null;
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        picture = document.getString("picture");

                    }
                }

                result.setValue(picture);

            }
        });

    }

    public void updateDailyMenu(final String date, final DailyMenuList dailyMenuList, final MutableLiveData<DailyMenuList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(date);

        docRef.set(dailyMenuList).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                 if(task.isSuccessful()) {
                    result.setValue(dailyMenuList);
                } else {
                    //Exception message to be shown to user
                    //TODO
                    System.out.println("Error in updating the database: " + task.getException().getMessage());

                }

            }
        });

    }

}
