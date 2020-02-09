package com.aht.business.kirti.pasitupusi.model.order.data;

import java.util.HashMap;
import java.util.Map;

public class OrderData {

    String refId;

    String date;

    private Map<String, DishOrderData> orderList = new HashMap<>();

    private OrderData() {
        this("");
    }

    public OrderData(String date) {
        setDate(date);
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
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
}
