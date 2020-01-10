package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;

public class DailyMenuViewModel extends ViewModel {

    private MutableLiveData<MenuCategoryList> categoryList = new MutableLiveData<>();
    DailyMenuManager profileManager = new DailyMenuManager();

    public LiveData<MenuCategoryList> getCategoryList() {
        return categoryList;
    }

    public void getAllTimeMenu() {

        //profileManager.addTestMenu(categoryList);
        profileManager.getAllTimeMenu(categoryList);
    }
}