package com.aht.business.kirti.pasitupusi.model.order.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DishOrderData implements Parcelable {

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

    protected DishOrderData(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readInt();
        breakfastQuantity = in.readInt();
        lunchQuantity = in.readInt();
        dinnerQuantity = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.price);
        dest.writeInt(this.breakfastQuantity);
        dest.writeInt(this.lunchQuantity);
        dest.writeInt(this.dinnerQuantity);
    }

    public static final Creator<DishOrderData> CREATOR = new Creator<DishOrderData>() {
        @Override
        public DishOrderData createFromParcel(Parcel in) {
            return new DishOrderData(in);
        }

        @Override
        public DishOrderData[] newArray(int size) {
            return new DishOrderData[size];
        }
    };

}
