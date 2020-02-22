package com.aht.business.kirti.pasitupusi.model.order.data;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuTime;
import com.aht.business.kirti.pasitupusi.model.dailymenu.enums.MenuType;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DishOrderData {

    private String id;

    private String name;

    @Exclude
    private String description;

    private int price;

    private int breakfastQuantity;

    private int lunchQuantity;

    private int dinnerQuantity;

    public DishOrderData() {
        this("", "", "", 0);
    }

    public DishOrderData(String id, String name, String description, int price) {

        setId(id);
        setName(name);
        setDescription(description);
        setPrice(price);
        setBreakfastQuantity(0);
        setLunchQuantity(0);
        setDinnerQuantity(0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBreakfastQuantity() {
        return breakfastQuantity;
    }

    public void setBreakfastQuantity(int breakfastQuantity) {
        this.breakfastQuantity = breakfastQuantity;
    }

    public int getLunchQuantity() {
        return lunchQuantity;
    }

    public void setLunchQuantity(int lunchQuantity) {
        this.lunchQuantity = lunchQuantity;
    }

    public int getDinnerQuantity() {
        return dinnerQuantity;
    }

    public void setDinnerQuantity(int dinnerQuantity) {
        this.dinnerQuantity = dinnerQuantity;
    }
}
