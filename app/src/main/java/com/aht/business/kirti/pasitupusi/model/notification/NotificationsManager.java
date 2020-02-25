package com.aht.business.kirti.pasitupusi.model.notification;

import android.app.NotificationChannel;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;

public class NotificationsManager {

    private static final String CHANNEL_ID_ORDER = "notifications_order";
    private static final String CHANNEL_ID_DISHES= "notifications_new_dishes";

    private Context context;

    public void init(Context context, ProfileData profileData) {

        this.context = context;

        if(profileData != null && ProfileRole.getValue(profileData.getRole()) > ProfileRole.getValue(ProfileRole.USER)) {
            createNotificationChannel(CHANNEL_ID_ORDER, "New Order", "User placed a new order");
        }

        createNotificationChannel(CHANNEL_ID_DISHES, "New Dish Menu", "New dishes for the day is added");

    }

    private void createNotificationChannel(String channelId, String name, String description) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            android.app.NotificationManager notificationManager = context.getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNewOrderNotification(OrderData orderData) {

        String shortMsg = "";
        String fullMsg = "";

        if(orderData != null && orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {

            shortMsg = "Order id: " + orderData.getOrderId();
            fullMsg = "Order id: " + orderData.getOrderId();

            boolean firstTime = true;
            for (DishOrderData dishOrderData : orderData.getOrderList().values()) {
                if(dishOrderData.getBreakfastQuantity() > 0) {
                    if(firstTime) {
                        firstTime = false;
                        fullMsg += "\n Breakfast Menu";
                    }
                    fullMsg += "\n " + dishOrderData.getName() + "  " + dishOrderData.getBreakfastQuantity();
                }
            }

            firstTime = true;
            for (DishOrderData dishOrderData : orderData.getOrderList().values()) {
                if(dishOrderData.getLunchQuantity() > 0) {
                    if(firstTime) {
                        firstTime = false;
                        fullMsg += "\n Lunch Menu";
                    }
                    fullMsg += "\n " + dishOrderData.getName() + "  " + dishOrderData.getLunchQuantity();
                }
            }

            firstTime = true;
            for (DishOrderData dishOrderData : orderData.getOrderList().values()) {
                if(dishOrderData.getDinnerQuantity() > 0) {
                    if(firstTime) {
                        firstTime = false;
                        fullMsg += "\n Dinner Menu";
                    }
                    fullMsg += "\n " + dishOrderData.getName() + "  " + dishOrderData.getDinnerQuantity();
                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_ORDER)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("New order Placed")
                    .setContentText(shortMsg)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(fullMsg))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            int notificationId = Integer.parseInt(orderData.getOrderId().replace("OID", "").substring(5, 13));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(notificationId, builder.build());
        }

    }
}
