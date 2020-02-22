package com.aht.business.kirti.pasitupusi.model.dailymenu.enums;

public enum MenuType {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    private String value;
    private MenuType(String value){
        this.value=value;
    }

    public static String getValue(MenuType type) {
        return type.value;
    }

}
