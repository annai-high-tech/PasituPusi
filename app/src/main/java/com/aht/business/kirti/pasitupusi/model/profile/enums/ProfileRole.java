package com.aht.business.kirti.pasitupusi.model.profile.enums;

import androidx.annotation.NonNull;

public enum ProfileRole {
    GUEST(0),
    USER(1),
    MANAGER(2),
    ADMIN(9);

    private int value;
    private ProfileRole(int value){
        this.value=value;
    }

    public static int getValue(ProfileRole type) {
        return type.value;
    }

    public static ProfileRole find(int code) {
        for (ProfileRole type : ProfileRole.values()) {
            if (getValue(type) == code) {
                return type;
            }
        }
        return null;
    }
}
