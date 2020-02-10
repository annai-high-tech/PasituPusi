package com.aht.business.kirti.pasitupusi.model.order.data;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DishOrderData {

    private String id;

    private String name;

    @Exclude
    private String description;

    private int price;

    private int quantity;

    public DishOrderData() {
        this("", "", "", 0);
    }

    public DishOrderData(String id, String name, String description, int price) {

        setId(id);
        setName(name);
        setDescription(description);
        setPrice(price);
        setQuantity(0);
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
