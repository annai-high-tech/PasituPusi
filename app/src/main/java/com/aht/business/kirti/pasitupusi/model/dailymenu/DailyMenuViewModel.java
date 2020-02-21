package com.aht.business.kirti.pasitupusi.model.dailymenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.google.firebase.firestore.DocumentReference;

public class DailyMenuViewModel extends ViewModel {

    DailyMenuManager profileManager = new DailyMenuManager();

    public LiveData<DailyMenuList> getDailyMenu(String date) {

        MutableLiveData<DailyMenuList> dailyMenuList1 = new MutableLiveData<>();
        profileManager.getDailyMenu(date, dailyMenuList1);

        return dailyMenuList1;
    }

    public LiveData<String> getDishPicture(DocumentReference dishId) {

        MutableLiveData<String> dailyMenuList1 = new MutableLiveData<>();
        profileManager.getDishPicture(dishId, dailyMenuList1);

        return dailyMenuList1;
    }

    public LiveData<DailyMenuList> updateDailyMenu(String date, DailyMenuList list) {

        MutableLiveData<DailyMenuList> dailyMenuList2 = new MutableLiveData<>();
        profileManager.updateDailyMenu(date, list, dailyMenuList2);

        return dailyMenuList2;
    }
}