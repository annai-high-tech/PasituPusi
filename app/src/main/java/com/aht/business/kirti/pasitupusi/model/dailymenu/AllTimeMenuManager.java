package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElementList;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Comparator;

public class AllTimeMenuManager {

    private FirebaseUser currentUser;

    public void getAllTimeMenu(final MutableLiveData<MenuElementList> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT);

        docRef.collection(Database.DATABASE_ALL_TIME_MENU_DOCUMENT_DISHES).orderBy(FieldPath.documentId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                MenuElementList menuElementList = new MenuElementList();
                if(task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MenuElement menuElement = document.toObject(MenuElement.class);
                        menuElementList.getMenuElementList().add(menuElement);
                        System.out.println(menuElement.getId());
                    }

                    Collections.sort(menuElementList.getMenuElementList(), new Comparator<MenuElement>() {
                        @Override
                        public int compare(MenuElement o1, MenuElement o2) {
                            return o1.getCategory().getId().compareTo(o2.getCategory().getId());
                        }
                    });

                }
                result.setValue(menuElementList);

            }
        });

    }

    private int menuCategoryCount = 0;
    private int menuElementCount = 0;

    public void addTestMenuCategory(final MutableLiveData<Boolean> result) {

        MenuCategoryList menuCategoryList = testMenuCategoryData();
        menuCategoryCount = 0;

        for(String menuCategoryKey: menuCategoryList.getMenuCategoryList().keySet()) {

            addMenuCategory(menuCategoryList.getMenuCategoryList().get(menuCategoryKey), menuCategoryList.getMenuCategoryList().size(), result);

        }

    }

    public void setMenuCategory(final MenuCategory menuCategory, final MutableLiveData<Boolean> result) {

        menuCategoryCount = 0;
        addMenuCategory(menuCategory, 1, result);

    }

    private void addMenuCategory(final MenuCategory menuCategory, final int count, final MutableLiveData<Boolean> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT);

        docRef.collection(Database.DATABASE_ALL_TIME_MENU_DOCUMENT_CATEGORY).document(menuCategory.getId())
                .set(menuCategory).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                menuCategoryCount ++;

                if (task.isSuccessful()) {
                    if(menuCategoryCount == count && !(result.getValue() != null && result.getValue() == false)) {
                        result.setValue(true);
                    }

                } else {
                    System.out.println(task.getException());
                    result.setValue(false);
                }
            }
        });

    }

    private MenuCategoryList testMenuCategoryData() {

        MenuCategoryList menuCategoryList = new MenuCategoryList();
        MenuCategory categoryKulambu = new MenuCategory("Kulambu", "Kulambu/குழம்பு");
        menuCategoryList.getMenuCategoryList().put("Kulambu", categoryKulambu);

        MenuCategory categoryPoriyal = new MenuCategory("Poriyal", "Poriyal/பொறியல்");
        menuCategoryList.getMenuCategoryList().put("Poriyal", categoryPoriyal);

        MenuCategory categoryRasam = new MenuCategory("Rasam", "Rasam/ரசம்");
        menuCategoryList.getMenuCategoryList().put("Rasam", categoryRasam);

        MenuCategory categoryThokayal = new MenuCategory("Thokayal", "Thokayal/தொகையல்");
        menuCategoryList.getMenuCategoryList().put("Thokayal", categoryThokayal);

        MenuCategory categoryDinner = new MenuCategory("Dinner", "Dinner/டின்னர்");
        menuCategoryList.getMenuCategoryList().put("Dinner", categoryDinner);

        MenuCategory categoryBiriyani = new MenuCategory("Biriyani", "Biriyani/பிரியாணி");
        menuCategoryList.getMenuCategoryList().put("Biriyani", categoryBiriyani);

        return menuCategoryList;
    }

    public void addTestMenuList(final MutableLiveData<Boolean> result) {

        MenuElementList menuElementList = testDishListData();
        menuElementCount = 0;

        for(MenuElement menuElement: menuElementList.getMenuElementList()) {

            addMenuElement(menuElement, menuElementList.getMenuElementList().size(), result);

        }

    }

    public void setMenuList(final MenuElement menuElement, final MutableLiveData<Boolean> result) {

        menuElementCount = 0;
        addMenuElement(menuElement, 1, result);

    }

    private void addMenuElement(final MenuElement menuElement, final int count, final MutableLiveData<Boolean> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT);

        docRef.collection(Database.DATABASE_ALL_TIME_MENU_DOCUMENT_DISHES).document(menuElement.getId())
                .set(menuElement).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {

                menuElementCount++;

                if (task.isSuccessful()) {
                    if(menuElementCount == count && !(result.getValue() != null && result.getValue() == false)) {
                        result.setValue(true);
                    }

                } else {
                    System.out.println(task.getException());
                    result.setValue(false);
                }


            }
        });

    }

    private MenuElementList testDishListData() {

        MenuElementList menuElementList = new MenuElementList();

        MenuElement elementBiriyani1 = new MenuElement("Biriyani_1", "Chicken Biriyani", "Biriyani");
        elementBiriyani1.setDescription("Seeraga Samba rice home made masala chicken biriyani");
        elementBiriyani1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/chicken_biriyani.jpg", 500)));
        elementBiriyani1.setPrice(200);
        elementBiriyani1.setActive(true);
        menuElementList.getMenuElementList().add(elementBiriyani1);
        MenuElement elementBiriyani2 = new MenuElement("Biriyani_2", "Mutton Biriyani", "Biriyani");
        elementBiriyani2.setDescription("Seeraga Samba rice home made masala mutton biriyani");
        elementBiriyani2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/mutton_biriyani.jpg", 500)));
        elementBiriyani2.setPrice(250);
        elementBiriyani2.setActive(true);
        menuElementList.getMenuElementList().add(elementBiriyani2);
        MenuElement elementKulambu1 = new MenuElement("Kulambu_1", "Kalyana Sambar", "Kulambu");
        elementKulambu1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/kalyana_sambar.jpg", 500)));
        elementKulambu1.setPrice(0);
        elementKulambu1.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu1);
        MenuElement elementKulambu2 = new MenuElement("Kulambu_2", "Kadamba Sambar", "Kulambu");
        elementKulambu2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/kadamba_sambar.jpg", 500)));
        elementKulambu2.setPrice(0);
        elementKulambu2.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu2);
        MenuElement elementKulambu3 = new MenuElement("Kulambu_3", "Arachuvitta Sambar", "Kulambu");
        elementKulambu3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/arachuvitta_sambar.jpg", 500)));
        elementKulambu3.setPrice(0);
        elementKulambu3.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu3);
        MenuElement elementKulambu4 = new MenuElement("Kulambu_4", "Murungai Keerai Sambar", "Kulambu");
        elementKulambu4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/murungai_keerai_sambar.jpg", 500)));
        elementKulambu4.setPrice(0);
        elementKulambu4.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu4);
        MenuElement elementKulambu5 = new MenuElement("Kulambu_5", "Vathal Kolambu", "Kulambu");
        elementKulambu5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/vathal_kolambu.jpg", 500)));
        elementKulambu5.setPrice(0);
        elementKulambu5.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu5);
        MenuElement elementKulambu6 = new MenuElement("Kulambu_6", "Thenkaipal kara kulambu", "Kulambu");
        elementKulambu6.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thenkaipal_kara_kulambu.jpg", 500)));
        elementKulambu6.setPrice(0);
        elementKulambu6.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu6);
        MenuElement elementKulambu7 = new MenuElement("Kulambu_7", "Ennai Katharikai Kulambu", "Kulambu");
        elementKulambu7.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/ennai_katharikai_kulambu.jpg", 500)));
        elementKulambu7.setPrice(0);
        elementKulambu7.setActive(true);
        menuElementList.getMenuElementList().add(elementKulambu7);
        MenuElement elementPoriyal1 = new MenuElement("Poriyal_1", "Katharikai Varuval", "Poriyal");
        elementPoriyal1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/katharikai_varuval.jpg", 500)));
        elementPoriyal1.setPrice(0);
        elementPoriyal1.setActive(true);
        menuElementList.getMenuElementList().add(elementPoriyal1);
        MenuElement elementPoriyal2 = new MenuElement("Poriyal_2", "Vurulai Varuval", "Poriyal");
        elementPoriyal2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/vurulai_varuval.jpg", 500)));
        elementPoriyal2.setPrice(0);
        elementPoriyal2.setActive(true);
        menuElementList.getMenuElementList().add(elementPoriyal2);
        MenuElement elementPoriyal3 = new MenuElement("Poriyal_3", "Senai Kilangu Chops", "Poriyal");
        elementPoriyal3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/senai_kilangu_chops.jpg", 500)));
        elementPoriyal3.setPrice(0);
        elementPoriyal3.setActive(true);
        menuElementList.getMenuElementList().add(elementPoriyal3);
        MenuElement elementPoriyal4 = new MenuElement("Poriyal_4", "Valaikai Pepper Fry", "Poriyal");
        elementPoriyal4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/valaikai_pepper_fry.jpg", 500)));
        elementPoriyal4.setPrice(0);
        elementPoriyal4.setActive(true);
        menuElementList.getMenuElementList().add(elementPoriyal4);
        MenuElement elementPoriyal5 = new MenuElement("Poriyal_5", "Valaikai Podimass", "Poriyal");
        elementPoriyal5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/valaikai_podimass.jpg", 500)));
        elementPoriyal5.setPrice(0);
        elementPoriyal5.setActive(true);
        menuElementList.getMenuElementList().add(elementPoriyal5);
        MenuElement elementRasam1 = new MenuElement("Rasam_1", "Parupu Rasam", "Rasam");
        elementRasam1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/parupu_rasam.jpg", 500)));
        elementRasam1.setPrice(0);
        elementRasam1.setActive(true);
        menuElementList.getMenuElementList().add(elementRasam1);
        MenuElement elementRasam2 = new MenuElement("Rasam_2", "Thakali Rasam", "Rasam");
        elementRasam2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thakali_rasam.jpg", 500)));
        elementRasam2.setPrice(0);
        elementRasam2.setActive(true);
        menuElementList.getMenuElementList().add(elementRasam2);
        MenuElement elementRasam3 = new MenuElement("Rasam_3", "Poondu Rasam", "Rasam");
        elementRasam3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/poondu_rasam.jpg", 500)));
        elementRasam3.setPrice(0);
        elementRasam3.setActive(true);
        menuElementList.getMenuElementList().add(elementRasam3);
        MenuElement elementThokayal1 = new MenuElement("Thokayal_1", "Parupu Thokayal", "Thokayal");
        elementThokayal1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/parupu_thokayal.jpg", 500)));
        elementThokayal1.setPrice(0);
        elementThokayal1.setActive(true);
        menuElementList.getMenuElementList().add(elementThokayal1);
        MenuElement elementThokayal2 = new MenuElement("Thokayal_2", "Inchi Thokayal", "Thokayal");
        elementThokayal2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/inchi_thokayal.jpg", 500)));
        elementThokayal2.setPrice(0);
        elementThokayal2.setActive(true);
        menuElementList.getMenuElementList().add(elementThokayal2);
        MenuElement elementThokayal3 = new MenuElement("Thokayal_3", "Pudina Thokayal", "Thokayal");
        elementThokayal3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/pudina_thokayal.jpg", 500)));
        elementThokayal3.setPrice(0);
        elementThokayal3.setActive(true);
        menuElementList.getMenuElementList().add(elementThokayal3);
        MenuElement elementThokayal4 = new MenuElement("Thokayal_4", "Pirandai Thokayal", "Thokayal");
        elementThokayal4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/pirandai_thokayal.jpg", 500)));
        elementThokayal4.setPrice(0);
        elementThokayal4.setActive(true);
        menuElementList.getMenuElementList().add(elementThokayal4);
        MenuElement elementDinner1 = new MenuElement("Dinner_1", "Saithapettai Special Vadaicurry", "Dinner");
        elementDinner1.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/saithapettai_special_vadaicurry.jpg", 500)));
        elementDinner1.setPrice(0);
        elementDinner1.setActive(true);
        menuElementList.getMenuElementList().add(elementDinner1);
        MenuElement elementDinner2 = new MenuElement("Dinner_2", "Udupi Special Veg Kuruma", "Dinner");
        elementDinner2.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/udupi_special_veg_kuruma.jpg", 500)));
        elementDinner2.setPrice(0);
        elementDinner2.setActive(true);
        menuElementList.getMenuElementList().add(elementDinner2);
        MenuElement elementDinner3 = new MenuElement("Dinner_3", "Channa Masala", "Dinner");
        elementDinner3.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/channa_masala.jpg", 500)));
        elementDinner3.setPrice(0);
        elementDinner3.setActive(true);
        menuElementList.getMenuElementList().add(elementDinner3);
        MenuElement elementDinner4 = new MenuElement("Dinner_4", "Paneer Butter Masala", "Dinner");
        elementDinner4.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/paneer_butter_masala.jpg", 500)));
        elementDinner4.setPrice(0);
        elementDinner4.setActive(true);
        menuElementList.getMenuElementList().add(elementDinner4);
        MenuElement elementDinner5 = new MenuElement("Dinner_5", "Thakali Thokku", "Dinner");
        elementDinner5.setPicture(BitmapUtils.BitMapToString(BitmapUtils.getBitMap("/storage/emulated/0/Pictures/pasitupusi/thakali_thokku.jpg", 500)));
        elementDinner5.setPrice(0);
        elementDinner5.setActive(true);
        menuElementList.getMenuElementList().add(elementDinner5);

        return menuElementList;
    }

}
