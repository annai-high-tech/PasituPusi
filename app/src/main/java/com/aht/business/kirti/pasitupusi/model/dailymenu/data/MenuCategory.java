package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCategory {

    private String id;

    private String name;

    public MenuCategory() {
    }

    public MenuCategory(String id, String name) {
        setId(id);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
