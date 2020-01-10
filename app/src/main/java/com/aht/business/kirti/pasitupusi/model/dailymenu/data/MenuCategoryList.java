package com.aht.business.kirti.pasitupusi.model.dailymenu.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuCategoryList {


    private Map<String, MenuCategory> menuCategoryList = new HashMap<>();

    public Map<String, MenuCategory> getMenuCategoryList() {
        return menuCategoryList;
    }

    public void setMenuCategoryList(Map<String, MenuCategory> menuCategoryList) {
        this.menuCategoryList = menuCategoryList;
    }
}
