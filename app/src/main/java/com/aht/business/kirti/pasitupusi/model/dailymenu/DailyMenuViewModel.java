package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;

public class DailyMenuViewModel extends ViewModel {

    DailyMenuManager profileManager = new DailyMenuManager();

    private MutableLiveData<MenuCategoryList> categoryList1 = new MutableLiveData<>();
    public LiveData<MenuCategoryList> getAllTimeMenu() {

        profileManager.getAllTimeMenu(categoryList1);

        return categoryList1;
    }

    private MutableLiveData<MenuCategoryList> categoryList2 = new MutableLiveData<>();
    public LiveData<MenuCategoryList> addAllTimeMenu() {

        profileManager.addTestMenu(categoryList2);

        return categoryList2;
    }

    private MutableLiveData<DailyMenuList> dailyMenuList1 = new MutableLiveData<>();
    public LiveData<DailyMenuList> getDailyMenu(String date) {

        dailyMenuList1.setValue(null);
        profileManager.getDailyMenu(date, dailyMenuList1);

        return dailyMenuList1;
    }

    private MutableLiveData<DailyMenuList> dailyMenuList2 = new MutableLiveData<>();
    public LiveData<DailyMenuList> updateDailyMenu(String date, DailyMenuList list) {

        dailyMenuList2.setValue(null);
        profileManager.updateDailyMenu(date, list, dailyMenuList2);

        return dailyMenuList2;
    }
}