package com.aht.business.kirti.pasitupusi.model.login;

public enum UserType {

    ADMIN(0, "Admin"), USER(1, "User"), GUEST(2, "Guest");

    private int index;
    private String value;

    private UserType(int index, String value){
        this.index=index;
        this.value=value;
    }

    public static int getIndex(UserType type) {
        return type.index;
    }

    public static String getValue(UserType type) {
        return type.value;
    }

    public static UserType findByIndex(int index) {
        UserType[] testEnums = UserType.values();
        for (UserType testEnum : testEnums) {
            if (testEnum.index == index) {
                return testEnum;
            }
        }
        return null;
    }
}
