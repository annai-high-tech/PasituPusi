package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
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

    public void getAllTimeMenu(final MutableLiveData<MenuCategoryList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT);

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

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT);

        docRef.set(testData()).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                MenuCategoryList menuCategoryList = null;
                if(task.isSuccessful()) {
                    menuCategoryList = null;
                    /*DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        menuCategoryList = document.toObject(MenuCategoryList.class);
                    }*/
                } else {
                    System.out.println(task.getException());
                }
                result.setValue(menuCategoryList);

            }
        });

    }

    private MenuCategoryList testData() {

        /*MenuCategoryList menuCategoryList = new MenuCategoryList();

        MenuCategory category1 = new MenuCategory("Kulambu/குழம்பு");
        category1.getMenuList().put("kulambu_1", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_2", new MenuElement("Kadamba Sambar"));
        category1.getMenuList().put("kulambu_3", new MenuElement("Arachuvitta Sambar"));
        category1.getMenuList().put("kulambu_4", new MenuElement("Murungai keerai Sambar"));
        category1.getMenuList().put("kulambu_5", new MenuElement("Vathal kolambu"));
        category1.getMenuList().put("kulambu_6", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_7", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_8", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_9", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_10", new MenuElement("Kalyana Sambar"));
        category1.getMenuList().put("kulambu_11", new MenuElement("Kalyana Sambar"));

        MenuCategory category2 = new MenuCategory("Poriyal/பொறியல்");
        category2.getMenuList().put("starter_1", new MenuElement("Chicken Kebab"));
        category2.getMenuList().put("starter_2", new MenuElement("Chicken Tantori"));
        category2.getMenuList().put("starter_3", new MenuElement("Chicken Lollipop"));

        MenuCategory category3 = new MenuCategory("Rasam/ரசம்");
        category3.getMenuList().put("main_1", new MenuElement("Chicken Biriyani"));
        category3.getMenuList().put("main_2", new MenuElement("Mutton Biriyani"));
        category3.getMenuList().put("main_3", new MenuElement("Prawn Biriyani"));
        category3.getMenuList().put("main_4", new MenuElement("Meals"));

        MenuCategory category4 = new MenuCategory("Thokayal/தொகையல்");
        category4.getMenuList().put("curry_1", new MenuElement("Chicken Butter Masala"));
        category4.getMenuList().put("curry_2", new MenuElement("Paneer Butter Masala"));
        category4.getMenuList().put("curry_3", new MenuElement("Vatha Kulambu"));

        MenuCategory category5 = new MenuCategory("Dinner/டின்னர்");
        category5.getMenuList().put("curry_1", new MenuElement("Chicken Butter Masala"));
        category5.getMenuList().put("curry_2", new MenuElement("Paneer Butter Masala"));
        category5.getMenuList().put("curry_3", new MenuElement("Vatha Kulambu"));

        MenuCategory category6 = new MenuCategory("Biriyani/பிரியாணி");
        category6.getMenuList().put("biriyani_1", new MenuElement("Chicken Biriyani"));
        category6.getMenuList().put("biriyani_2", new MenuElement("Mutton Biriyani"));
        category6.getMenuList().put("biriyani_3", new MenuElement("Boneless Chicken Biriyani"));

        menuCategoryList.getMenuCategoryList().put("Kulambu", category1);
        menuCategoryList.getMenuCategoryList().put("Poriyal", category2);
        menuCategoryList.getMenuCategoryList().put("Rasam", category3);
        menuCategoryList.getMenuCategoryList().put("Thokayal", category4);
        menuCategoryList.getMenuCategoryList().put("Dinner", category5);
        menuCategoryList.getMenuCategoryList().put("Biriyani", category6);*/

        MenuCategoryList menuCategoryList = new MenuCategoryList();
        MenuCategory categoryKulambu = new MenuCategory("Kulambu/குழம்பு");
        menuCategoryList.getMenuCategoryList().put("Kulambu", categoryKulambu);

        MenuCategory categoryPoriyal = new MenuCategory("Poriyal/பொறியல்");
        menuCategoryList.getMenuCategoryList().put("Poriyal", categoryPoriyal);

        MenuCategory categoryRasam = new MenuCategory("Rasam/ரசம்");
        menuCategoryList.getMenuCategoryList().put("Rasam", categoryRasam);

        MenuCategory categoryThokayal = new MenuCategory("Thokayal/தொகையல்");
        menuCategoryList.getMenuCategoryList().put("Thokayal", categoryThokayal);

        MenuCategory categoryDinner = new MenuCategory("Dinner/டின்னர்");
        menuCategoryList.getMenuCategoryList().put("Dinner", categoryDinner);

        MenuCategory categoryBiriyani = new MenuCategory("Biriyani/பிரியாணி");
        menuCategoryList.getMenuCategoryList().put("Biriyani", categoryBiriyani);

        MenuElement elementBiriyani1 = new MenuElement("Chicken Biriyani");
        elementBiriyani1.setDescription("Seeraga Samba rice home made masala chicken biriyani");
        elementBiriyani1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/chicken_biriyani.jpg", 500)));
        elementBiriyani1.setPrice(200);
        elementBiriyani1.setActive(true);
        categoryBiriyani.getMenuList().put("Biriyani_1", elementBiriyani1);
        MenuElement elementBiriyani2 = new MenuElement("Mutton Biriyani");
        elementBiriyani2.setDescription("Seeraga Samba rice home made masala mutton biriyani");
        elementBiriyani2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/mutton_biriyani.jpg", 500)));
        elementBiriyani2.setPrice(250);
        elementBiriyani2.setActive(true);
        categoryBiriyani.getMenuList().put("Biriyani_2", elementBiriyani2);
        MenuElement elementKulambu1 = new MenuElement("Kalyana Sambar");
        elementKulambu1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/kalyana_sambar.jpg", 500)));
        elementKulambu1.setPrice(0);
        elementKulambu1.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_1", elementKulambu1);
        MenuElement elementKulambu2 = new MenuElement("Kadamba Sambar");
        elementKulambu2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/kadamba_sambar.jpg", 500)));
        elementKulambu2.setPrice(0);
        elementKulambu2.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_2", elementKulambu2);
        MenuElement elementKulambu3 = new MenuElement("Arachuvitta Sambar");
        elementKulambu3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/arachuvitta_sambar.jpg", 500)));
        elementKulambu3.setPrice(0);
        elementKulambu3.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_3", elementKulambu3);
        MenuElement elementKulambu4 = new MenuElement("Murungai Keerai Sambar");
        elementKulambu4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/murungai_keerai_sambar.jpg", 500)));
        elementKulambu4.setPrice(0);
        elementKulambu4.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_4", elementKulambu4);
        MenuElement elementKulambu5 = new MenuElement("Vathal Kolambu");
        elementKulambu5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/vathal_kolambu.jpg", 500)));
        elementKulambu5.setPrice(0);
        elementKulambu5.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_5", elementKulambu5);
        MenuElement elementKulambu6 = new MenuElement("Thenkaipal kara kulambu");
        elementKulambu6.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thenkaipal_kara_kulambu.jpg", 500)));
        elementKulambu6.setPrice(0);
        elementKulambu6.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_6", elementKulambu6);
        MenuElement elementKulambu7 = new MenuElement("Ennai Katharikai Kulambu");
        elementKulambu7.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/ennai_katharikai_kulambu.jpg", 500)));
        elementKulambu7.setPrice(0);
        elementKulambu7.setActive(true);
        categoryKulambu.getMenuList().put("Kulambu_7", elementKulambu7);
        MenuElement elementPoriyal1 = new MenuElement("Katharikai Varuval");
        elementPoriyal1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/katharikai_varuval.jpg", 500)));
        elementPoriyal1.setPrice(0);
        elementPoriyal1.setActive(true);
        categoryPoriyal.getMenuList().put("Poriyal_1", elementPoriyal1);
        MenuElement elementPoriyal2 = new MenuElement("Vurulai Varuval");
        elementPoriyal2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/vurulai_varuval.jpg", 500)));
        elementPoriyal2.setPrice(0);
        elementPoriyal2.setActive(true);
        categoryPoriyal.getMenuList().put("Poriyal_2", elementPoriyal2);
        MenuElement elementPoriyal3 = new MenuElement("Senai Kilangu Chops");
        elementPoriyal3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/senai_kilangu_chops.jpg", 500)));
        elementPoriyal3.setPrice(0);
        elementPoriyal3.setActive(true);
        categoryPoriyal.getMenuList().put("Poriyal_3", elementPoriyal3);
        MenuElement elementPoriyal4 = new MenuElement("Valaikai Pepper Fry");
        elementPoriyal4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/valaikai_pepper_fry.jpg", 500)));
        elementPoriyal4.setPrice(0);
        elementPoriyal4.setActive(true);
        categoryPoriyal.getMenuList().put("Poriyal_4", elementPoriyal4);
        MenuElement elementPoriyal5 = new MenuElement("Valaikai Podimass");
        elementPoriyal5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/valaikai_podimass.jpg", 500)));
        elementPoriyal5.setPrice(0);
        elementPoriyal5.setActive(true);
        categoryPoriyal.getMenuList().put("Poriyal_5", elementPoriyal5);
        MenuElement elementRasam1 = new MenuElement("Parupu Rasam");
        elementRasam1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/parupu_rasam.jpg", 500)));
        elementRasam1.setPrice(0);
        elementRasam1.setActive(true);
        categoryRasam.getMenuList().put("Rasam_1", elementRasam1);
        MenuElement elementRasam2 = new MenuElement("Thakali Rasam");
        elementRasam2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thakali_rasam.jpg", 500)));
        elementRasam2.setPrice(0);
        elementRasam2.setActive(true);
        categoryRasam.getMenuList().put("Rasam_2", elementRasam2);
        MenuElement elementRasam3 = new MenuElement("Poondu Rasam");
        elementRasam3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/poondu_rasam.jpg", 500)));
        elementRasam3.setPrice(0);
        elementRasam3.setActive(true);
        categoryRasam.getMenuList().put("Rasam_3", elementRasam3);
        MenuElement elementThokayal1 = new MenuElement("Parupu Thokayal");
        elementThokayal1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/parupu_thokayal.jpg", 500)));
        elementThokayal1.setPrice(0);
        elementThokayal1.setActive(true);
        categoryThokayal.getMenuList().put("Thokayal_1", elementThokayal1);
        MenuElement elementThokayal2 = new MenuElement("Inchi Thokayal");
        elementThokayal2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/inchi_thokayal.jpg", 500)));
        elementThokayal2.setPrice(0);
        elementThokayal2.setActive(true);
        categoryThokayal.getMenuList().put("Thokayal_2", elementThokayal2);
        MenuElement elementThokayal3 = new MenuElement("Pudina Thokayal");
        elementThokayal3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/pudina_thokayal.jpg", 500)));
        elementThokayal3.setPrice(0);
        elementThokayal3.setActive(true);
        categoryThokayal.getMenuList().put("Thokayal_3", elementThokayal3);
        MenuElement elementThokayal4 = new MenuElement("Pirandai Thokayal");
        elementThokayal4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/pirandai_thokayal.jpg", 500)));
        elementThokayal4.setPrice(0);
        elementThokayal4.setActive(true);
        categoryThokayal.getMenuList().put("Thokayal_4", elementThokayal4);
        MenuElement elementDinner1 = new MenuElement("Saithapettai Special Vadaicurry");
        elementDinner1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/saithapettai_special_vadaicurry.jpg", 500)));
        elementDinner1.setPrice(0);
        elementDinner1.setActive(true);
        categoryDinner.getMenuList().put("Dinner_1", elementDinner1);
        MenuElement elementDinner2 = new MenuElement("Udupi Special Veg Kuruma");
        elementDinner2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/udupi_special_veg_kuruma.jpg", 500)));
        elementDinner2.setPrice(0);
        elementDinner2.setActive(true);
        categoryDinner.getMenuList().put("Dinner_2", elementDinner2);
        MenuElement elementDinner3 = new MenuElement("Channa Masala");
        elementDinner3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/channa_masala.jpg", 500)));
        elementDinner3.setPrice(0);
        elementDinner3.setActive(true);
        categoryDinner.getMenuList().put("Dinner_3", elementDinner3);
        MenuElement elementDinner4 = new MenuElement("Paneer Butter Masala");
        elementDinner4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/paneer_butter_masala.jpg", 500)));
        elementDinner4.setPrice(0);
        elementDinner4.setActive(true);
        categoryDinner.getMenuList().put("Dinner_4", elementDinner4);
        MenuElement elementDinner5 = new MenuElement("Thakali Thokku");
        elementDinner5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thakali_thokku.jpg", 500)));
        elementDinner5.setPrice(0);
        elementDinner5.setActive(true);
        categoryDinner.getMenuList().put("Dinner_5", elementDinner5);

        return menuCategoryList;
    }

    public void getDailyMenu(final String date, final MutableLiveData<DailyMenuList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    public void updateDailyMenu(final String date, final DailyMenuList dailyMenuList, final MutableLiveData<DailyMenuList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(date);

        docRef.set(dailyMenuList).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                 if(task.isSuccessful()) {
                    result.setValue(dailyMenuList);
                } else {
                    //Excepion message to be shown to user
                    //TODO
                    System.out.println("Error in updating the database: " + task.getException().getMessage());

                }

            }
        });

    }

}
