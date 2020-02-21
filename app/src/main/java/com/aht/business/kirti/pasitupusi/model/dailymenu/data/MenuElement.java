package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class MenuElement {

    private String id;

    private String name;

    private DocumentReference category;

    private String categoryName;

    private String description;

    private String picture;

    private int price;

    private boolean active;

    public MenuElement() {
    }

    public MenuElement(String id, String name, String category) {
        this();

        setId(id);
        setName(name);
        setActive(true);
        setPrice(0);
        setCategoryName(category);

        DocumentReference categoryRef = FirebaseFirestore.getInstance().collection(Database.DATABASE_MENU_DATA_COLLECTION).document(Database.DATABASE_ALL_TIME_MENU_DOCUMENT)
                .collection(Database.DATABASE_ALL_TIME_MENU_DOCUMENT_CATEGORY).document(category);
        setCategory(categoryRef);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getCategory() {
        return category;
    }

    public void setCategory(DocumentReference category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
