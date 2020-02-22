package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import android.graphics.Bitmap;

import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

public class DailyMenu {

    private DocumentReference id;

   private String categoryName;

    private String name;

    private String description;

    private String picture;

    private int price;

    private MenuTime menuTime;

    public DailyMenu() {

    }

    public DailyMenu(String id, MenuElement menuElement, MenuTime menuTime) {

        DocumentReference categoryRef = FirebaseFirestore.getInstance().collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT)
                .collection(Database.DATABASE_ALL_TIME_MENU_DOCUMENT_DISHES).document(id);

        setId(categoryRef);
        setName(menuElement.getName());
        setDescription(menuElement.getDescription());
        setPrice(menuElement.getPrice());
        setCategoryName(menuElement.getCategoryName());
        //setMenuElement(menuElement);

        Bitmap thumbnail = null;

        if(menuElement.getPicture() != null) {
            Bitmap thumbnailTemp = BitmapUtils.StringToBitMap(menuElement.getPicture());
            thumbnail = BitmapUtils.getResizedBitmap(thumbnailTemp, 10);
        }

        setPicture(BitmapUtils.BitMapToString(thumbnail));

        setMenuTime(menuTime);

    }

    public DocumentReference getId() {
        return id;
    }

    public void setId(DocumentReference id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public MenuTime getMenuTime() {
        return menuTime;
    }

    public void setMenuTime(MenuTime menuTime) {
        this.menuTime = menuTime;
    }
}
