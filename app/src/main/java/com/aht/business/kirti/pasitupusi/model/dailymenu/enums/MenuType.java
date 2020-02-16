package com.aht.business.kirti.pasitupusi.model.dailymenu.enums;

public enum MenuType {
    BREAKFAST(0),
    LUNCH(1),
    DINNER(9);

    private int value;
    private MenuType(int value){
        this.value=value;
    }

    public static int getValue(MenuType type) {
        return type.value;
    }

    public static MenuType find(int code) {
        for (MenuType type : MenuType.values()) {
            if (getValue(type) == code) {
                return type;
            }
        }
        return null;
    }
}
