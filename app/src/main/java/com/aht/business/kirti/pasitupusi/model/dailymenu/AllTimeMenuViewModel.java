package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElementList;

public class AllTimeMenuViewModel extends ViewModel {

    AllTimeMenuManager profileManager = new AllTimeMenuManager();

    private MutableLiveData<MenuElementList> categoryList1 = new MutableLiveData<>();
    public LiveData<MenuElementList> getAllTimeMenu() {

        profileManager.getAllTimeMenu(categoryList1);

        return categoryList1;
    }

    private MutableLiveData<Boolean> categoryList2 = new MutableLiveData<>();
    public LiveData<Boolean> addAllTimeMenuCategory() {

        profileManager.addTestMenuCategory(categoryList2);

        return categoryList2;
    }

    private MutableLiveData<Boolean> categoryList3 = new MutableLiveData<>();
    public LiveData<Boolean> addAllTimeMenuCategory(MenuCategory menuCategory) {

        profileManager.setMenuCategory(menuCategory, categoryList3);

        return categoryList3;
    }

    private MutableLiveData<Boolean> categoryList4 = new MutableLiveData<>();
    public LiveData<Boolean> addAllTimeMenuElement() {

        profileManager.addTestMenuList(categoryList4);

        return categoryList4;
    }

    private MutableLiveData<Boolean> categoryList5 = new MutableLiveData<>();
    public LiveData<Boolean> addAllTimeMenuElement(MenuElement menuElement) {

        profileManager.setMenuList(menuElement, categoryList5);

        return categoryList5;
    }

}