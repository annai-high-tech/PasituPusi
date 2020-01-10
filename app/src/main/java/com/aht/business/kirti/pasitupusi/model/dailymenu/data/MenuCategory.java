package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCategory {

    public MenuCategory() {
    }

    public MenuCategory(String name) {
        setName(name);
    }

    private Map<String, MenuElement> menuList = new HashMap<>();

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, MenuElement> getMenuList() {
        return menuList;
    }

    public void setMenuList(Map<String, MenuElement> menuList) {
        this.menuList = menuList;
    }
}
