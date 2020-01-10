package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;

import java.io.Serializable;

public class MenuElement {


    public MenuElement() {
    }

    public MenuElement(String name) {
        setName(name);
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
