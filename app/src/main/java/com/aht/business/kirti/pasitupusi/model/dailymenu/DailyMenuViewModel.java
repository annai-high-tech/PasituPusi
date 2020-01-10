package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;

public class DailyMenuViewModel extends ViewModel {

    private MutableLiveData<MenuCategoryList> categoryList = new MutableLiveData<>();
    private MutableLiveData<DailyMenuList> dailyMenuList = new MutableLiveData<>();
    DailyMenuManager profileManager = new DailyMenuManager();

    public LiveData<MenuCategoryList> getCategoryList() {
        return categoryList;
    }

    public void getAllTimeMenu() {

        //profileManager.addTestMenu(categoryList);
        profileManager.getAllTimeMenu(categoryList);
    }

    public LiveData<DailyMenuList> getDailyMenuList() {
        return dailyMenuList;
    }

    public void getDailyMenu(String date) {

        dailyMenuList.setValue(null);
        profileManager.getDailyMenu(date, dailyMenuList);

    }

    public void updateDailyMenu(String date, DailyMenuList list) {

        dailyMenuList.setValue(null);
        profileManager.updateDailyMenu(date, list, dailyMenuList);

    }
}