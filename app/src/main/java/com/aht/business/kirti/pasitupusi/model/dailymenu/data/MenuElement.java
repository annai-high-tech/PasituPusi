package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;

import java.io.Serializable;

public class MenuElement {


    public MenuElement() {
    }

    public MenuElement(String name) {
        setName(name);
        setActive(true);
        setPrice(0);
    }

    private String name;

    private String description;

    private String picture;

    private int price;

    private boolean active;

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
}
