package com.aht.business.kirti.pasitupusi.model.updates.enums;

public enum UpdateType {
    NO_UPDATE(0), ONCE(1), ALWAYS(2), MANDATORY(3);

    private int value;
    private UpdateType(int value){
        this.value=value;
    }

    public static int getValue(UpdateType type) {
        return type.value;
    }


    public static UpdateType find(int code) {
        for (UpdateType type : UpdateType.values()) {
            if (getValue(type) == code) {
                return type;
            }
        }
        return null;
    }

}

