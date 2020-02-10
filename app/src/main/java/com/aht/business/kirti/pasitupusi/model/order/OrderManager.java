package com.aht.business.kirti.pasitupusi.model.order;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderManager {


    public void getUserOrders(final MutableLiveData<List<OrderData>> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionRef = db.collection(Database.DATABASE_ORDER_DATA_COLLECTION);

        collectionRef.whereEqualTo("userId", currentUser.getUid()).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    List<OrderData> list = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        OrderData orderData = document.toObject(OrderData.class);
                        list.add(orderData);
                    }
                    result.setValue(list);
                } else {
                    result.setValue(null);
                }

            }
        });
    }

    public void getAllOrders(final String date, final MutableLiveData<List<OrderData>> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionRef = db.collection(Database.DATABASE_ORDER_DATA_COLLECTION);

        collectionRef.whereEqualTo("date", date).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()) {

                    List<OrderData> list = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        OrderData orderData = document.toObject(OrderData.class);
                        list.add(orderData);
                    }
                    result.setValue(list);
                } else {
                    result.setValue(null);
                }

            }
        });
    }

    public void getLastOrder(final String date, final MutableLiveData<OrderData> result) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionRef = db.collection(Database.DATABASE_ORDER_DATA_COLLECTION);

        collectionRef.whereEqualTo("date", date).orderBy(FieldPath.documentId(), Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                OrderData orderData = null;
                if(task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    int size = document.getDocuments().size();
                    if(size > 0) {
                        orderData = document.getDocuments().get(size - 1).toObject(OrderData.class);
                    }
                } else {
                    orderData = null;
                    //System.out.println(task.getException());
                }

                result.setValue(orderData);
            }
        });

    }

    public void addOrder(final String date, final OrderData orderData, final MutableLiveData<Boolean> result) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        final CollectionReference collectionRef = db.collection(Database.DATABASE_ORDER_DATA_COLLECTION);
        String orderId = orderData.getOrderId();
        if(orderId == null || orderId.equals("")) {
            orderId = generateOrderId(date);
            orderData.setOrderId(orderId);
        }
        orderData.setUserId(currentUser.getUid());

        final DocumentReference documentRef = collectionRef.document(String.valueOf(orderId));

        documentRef.set(orderData).addOnCompleteListener(new OnCompleteListener() {

            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {

                    result.setValue(true);

                } else {
                    result.setValue(false);
                }


            }
        });


    }

    private String generateOrderId(String date) {

        String id = "OID";

        id += String.valueOf(new Date().getTime());

        id += date.replaceAll("-", "");

        return id;
    }

}
