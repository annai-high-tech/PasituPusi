package com.aht.business.kirti.pasitupusi.model.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;

import java.util.Map;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<MenuCategoryList> orderList = new MutableLiveData<>();
    OrderManager orderManager = new OrderManager();

    public LiveData<MenuCategoryList> getOrderList() {
        return orderList;
    }

    public void getAllOrders() {

        orderManager.getAllOrders(orderList);

    }

    public void getOrder(String date, String refId) {

        orderManager.getOrder(date, refId, orderList);

    }

    public void addOrder(String date, String refId, Map<String, DishOrderData> dishOrderDataList) {

        orderManager.addOrder(date, refId, dishOrderDataList, orderList);

    }
}
