package com.aht.business.kirti.pasitupusi.model.order.data;

import java.util.HashMap;
import java.util.Map;

public class OrderData {

    String orderId;

    String date;

    String userId;

    int totalCost;

    private Map<String, DishOrderData> orderList = new HashMap<>();

    private OrderData() {
        this("");
    }

    public OrderData(String date) {
        setDate(date);
    }

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
}
