package com.aht.business.kirti.pasitupusi.ui.components.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;

import java.util.Map;

public final class FoodDishLayoutAdapter {


    private FoodDishLayoutAdapter() {
    }

    public static View createLayout(final Context context, final DishOrderData data, final Bitmap picture, final Map<String, DishOrderData> orderList, final LinearLayout cartLayout) {

        String name, description, price, count;

        if(data == null)
            return null;

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_food_dish_layout, null);

        TextView nameTextView = (TextView) view.findViewById(R.id.dishname);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.dishdescription);
        TextView priceTextView = (TextView) view.findViewById(R.id.dishprice);
        ImageView pictureImageView = (ImageView) view.findViewById(R.id.dishpicture);
        final TextView orderCountTextView = (TextView) view.findViewById(R.id.orderCountTextView);
        final ImageView orderPlusImageView = (ImageView) view.findViewById(R.id.orderPlusImageView);
        final ImageView orderMinusImageView = (ImageView) view.findViewById(R.id.orderMinusImageView);

        name = data.getName();
        description = "";
        if(data.getDescription() != null) {
            description = data.getDescription();
        }
        price = "Invalid Price";
        if(data.getPrice() >= 0) {
            price = "Price " + "\u20B9" + data.getPrice();
        }
        count = "" + data.getOrderCount();


        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(price);
        if(picture != null) {
            pictureImageView.setImageBitmap(picture);
            pictureImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pictureImageView.setAdjustViewBounds(true);
        } else {
            pictureImageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
        }
        orderCountTextView.setText(count);
        if(data.getOrderCount() > 0 && !orderList.containsKey(data.getId())) {
            orderList.put(data.getId(), data);
        }
        if(data.getOrderCount() <= 0 && orderList.containsKey(data.getId())) {
            orderList.remove(data.getId());
        }

        setCartLayout(cartLayout, orderList);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String count;
                if(view.getId() == orderPlusImageView.getId()) {

                    if(data.getOrderCount() < 99) {
                        data.setOrderCount(data.getOrderCount() + 1);
                        count = "" + data.getOrderCount();
                        orderCountTextView.setText(count);
                    }

                }
                if(view.getId() == orderMinusImageView.getId()) {
                    if(data.getOrderCount() > 0) {
                        data.setOrderCount(data.getOrderCount() - 1);
                        count = "" + data.getOrderCount();
                        orderCountTextView.setText(count);
                    }

                }

                if(data.getOrderCount() > 0 && !orderList.containsKey(data.getId())) {
                    orderList.put(data.getId(), data);
                }
                if(data.getOrderCount() <= 0 && orderList.containsKey(data.getId())) {
                    orderList.remove(data.getId());
                }

                setCartLayout(cartLayout, orderList);

            }
        };

        orderPlusImageView.setOnClickListener(listener);
        orderMinusImageView.setOnClickListener(listener);



        return view;
    }

    private static void setCartLayout(LinearLayout cartLayout, Map<String, DishOrderData> orderList) {

        if(orderList.size() <= 0) {

            cartLayout.setVisibility(View.GONE);

        } else {

            String itemString = "Items";
            int itemsCount = 0;
            int cost = 0;

            for (Map.Entry<String, DishOrderData> entry : orderList.entrySet()) {
                itemsCount += entry.getValue().getOrderCount();
                cost += (entry.getValue().getOrderCount() * entry.getValue().getPrice());
            }

            if(itemsCount == 1)
                itemString = "Item";

            cartLayout.setVisibility(View.VISIBLE);
            TextView textView = cartLayout.findViewById(R.id.orderSummaryTextView);
            textView.setText(itemsCount + " " + itemString + "  |  " + "\u20B9" + cost);
        }

    }
}