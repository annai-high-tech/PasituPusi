package com.aht.business.kirti.pasitupusi.model.order;

import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;

import java.util.Map;

public class OrderManager {


    public void getAllOrders(final MutableLiveData<MenuCategoryList> result) {

        result.setValue(null);
    }

    public void getOrder(final String date, final String refId, final MutableLiveData<MenuCategoryList> result) {

        result.setValue(null);
    }

    public void addOrder(final String date, final String refId, final Map<String, DishOrderData> dishOrderDataList, final MutableLiveData<MenuCategoryList> result) {

        result.setValue(null);
    }


}
