package com.aht.business.kirti.pasitupusi.model.order.enums;

public enum OrderStatus {
    NO_ORDER("No Order"),
    ORDER_PLACED("Order Placed"),
    ORDER_ACCEPTED("Order Accepted"),
    DELIVERY_ON_THE_WAY("Delivery On the way"),
    USER_CANCELED("Cancelled by user"),
    RESTAURANT_CANCELED("Cancelled by Restaurant"),
    DELIVERED("Delivered");

    private String value;
    private OrderStatus(String value){
        this.value=value;
    }

    public static String getValue(OrderStatus type) {
        return type.value;
    }

}
