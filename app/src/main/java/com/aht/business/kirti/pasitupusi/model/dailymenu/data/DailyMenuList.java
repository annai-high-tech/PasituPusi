package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import com.aht.business.kirti.pasitupusi.model.dailymenu.enums.MenuType;

import java.util.HashMap;
import java.util.Map;

public class DailyMenuList {

    private String description;

    private Map<String, DailyMenu> menuList = new HashMap<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, DailyMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(Map<String, DailyMenu> menuList) {
        this.menuList = menuList;
    }
}
