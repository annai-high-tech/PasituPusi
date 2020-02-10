package com.aht.business.kirti.pasitupusi.model.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;

import java.util.List;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<List<OrderData>> orderList = new MutableLiveData<>();

    private MutableLiveData<OrderData> orderData = new MutableLiveData<>();

    private MutableLiveData<Boolean> resultStatus = new MutableLiveData<>();

    OrderManager orderManager = new OrderManager();

    public MutableLiveData<List<OrderData>> getUserOrders() {

        orderManager.getUserOrders(orderList);

        return orderList;

    }

    public MutableLiveData<List<OrderData>> getAllOrders(String date) {

        orderManager.getAllOrders(date, orderList);

        return orderList;

    }

    public LiveData<OrderData> getLastOrder(String date) {

        orderManager.getLastOrder(date, orderData);

        return orderData;

    }

    public LiveData<Boolean> addOrder(String date, OrderData dishOrderData) {

        orderManager.addOrder(date, dishOrderData, resultStatus);

        return resultStatus;

    }
}
