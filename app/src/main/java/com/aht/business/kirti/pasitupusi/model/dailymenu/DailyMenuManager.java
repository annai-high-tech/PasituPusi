package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

//import com.google.api.core.ApiFuture;

public class DailyMenuManager {

    private FirebaseUser currentUser;

    public void getAllTimeMenu(final MutableLiveData<MenuCategoryList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("menu_data").document("all_time_menu");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                MenuCategoryList menuCategoryList = null;
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        menuCategoryList = document.toObject(MenuCategoryList.class);
                    }
                }
                result.setValue(menuCategoryList);

            }
        });

    }

    public void addTestMenu(final MutableLiveData<MenuCategoryList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("menu_data").document("all_time_menu");

        docRef.set(testData()).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                MenuCategoryList menuCategoryList = null;
                if(task.isSuccessful()) {
                    /*DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        menuCategoryList = document.toObject(MenuCategoryList.class);
                    }*/
                }
                result.setValue(menuCategoryList);

            }
        });

    }

    private MenuCategoryList testData() {

        MenuCategoryList menuCategoryList = new MenuCategoryList();

        MenuCategory category1 = new MenuCategory("Tiffins");
        category1.getMenuList().put("tiffin_1", new MenuElement("Idly"));
        category1.getMenuList().put("tiffin_2", new MenuElement("Plain Dosa"));
        category1.getMenuList().put("tiffin_3", new MenuElement("Masala Dosa"));
        category1.getMenuList().put("tiffin_4", new MenuElement("Puri"));

        MenuCategory category2 = new MenuCategory("Starters");
        category2.getMenuList().put("starter_1", new MenuElement("Chicken Kebab"));
        category2.getMenuList().put("starter_2", new MenuElement("Chicken Tantori"));
        category2.getMenuList().put("starter_3", new MenuElement("Chicken Lollipop"));

        MenuCategory category3 = new MenuCategory("Main Course");
        category3.getMenuList().put("main_1", new MenuElement("Chicken Biriyani"));
        category3.getMenuList().put("main_2", new MenuElement("Mutton Biriyani"));
        category3.getMenuList().put("main_3", new MenuElement("Prawn Biriyani"));
        category3.getMenuList().put("main_4", new MenuElement("Meals"));

        MenuCategory category4 = new MenuCategory("Curry");
        category4.getMenuList().put("curry_1", new MenuElement("Chicken Butter Masala"));
        category4.getMenuList().put("curry_2", new MenuElement("Paneer Butter Masala"));
        category4.getMenuList().put("curry_3", new MenuElement("Vatha Kulambu"));

        menuCategoryList.getMenuCategoryList().put("tiffins", category1);
        menuCategoryList.getMenuCategoryList().put("starters", category2);
        menuCategoryList.getMenuCategoryList().put("main_course", category3);
        menuCategoryList.getMenuCategoryList().put("curry", category4);

        return menuCategoryList;
    }

}
