package com.aht.business.kirti.pasitupusi.model.order.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aht.business.kirti.pasitupusi.model.order.enums.OrderStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderData implements Parcelable {

    private String orderId;

    private String date;

    private Date orderPlacedTime;

    private Date orderDeliveredTime;

    private String userId;

    private int totalCost;

    private OrderStatus breakfastOrderStatus;

    private OrderStatus lunchOrderStatus;

    private OrderStatus dinnerOrderStatus;

    private Map<String, DishOrderData> orderList = new HashMap<>();

    private OrderData() {
        this("");
    }

    public OrderData(String date) {
        setDate(date);
        setBreakfastOrderStatus(OrderStatus.NO_ORDER);
        setLunchOrderStatus(OrderStatus.NO_ORDER);
        setDinnerOrderStatus(OrderStatus.NO_ORDER);
    }

    protected OrderData(Parcel in) {
        orderId = in.readString();
        date = in.readString();
        userId = in.readString();
        totalCost = in.readInt();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, DishOrderData> getOrderList() {
        return orderList;
    }

    public void setOrderList(Map<String, DishOrderData> orderList) {
        this.orderList = orderList;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public void setOrderPlacedTime(Date orderPlacedTime) {
        this.orderPlacedTime = orderPlacedTime;
    }

    public Date getOrderDeliveredTime() {
        return orderDeliveredTime;
    }

    public void setOrderDeliveredTime(Date orderDeliveredTime) {
        this.orderDeliveredTime = orderDeliveredTime;
    }

    public OrderStatus getBreakfastOrderStatus() {
        return breakfastOrderStatus;
    }

    public void setBreakfastOrderStatus(OrderStatus breakfastOrderStatus) {
        this.breakfastOrderStatus = breakfastOrderStatus;
    }

    public OrderStatus getLunchOrderStatus() {
        return lunchOrderStatus;
    }

    public void setLunchOrderStatus(OrderStatus lunchOrderStatus) {
        this.lunchOrderStatus = lunchOrderStatus;
    }

    public OrderStatus getDinnerOrderStatus() {
        return dinnerOrderStatus;
    }

    public void setDinnerOrderStatus(OrderStatus dinnerOrderStatus) {
        this.dinnerOrderStatus = dinnerOrderStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(date);
        dest.writeString(userId);
        dest.writeInt(totalCost);
    }
}
