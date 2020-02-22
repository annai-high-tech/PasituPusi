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
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuTime;
import com.aht.business.kirti.pasitupusi.model.dailymenu.enums.MenuType;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;

import java.util.Map;

public class FoodDishLayoutAdapter {

    private View view;

    private Context context;

    private ImageView pictureImageView;

    private FoodDishLayoutAdapter() {
    }

    public FoodDishLayoutAdapter(final Context context) {
        this.context = context;
    }

    public View createLayout(final DishOrderData data, final Map<String, DishOrderData> orderList, final Bitmap picture, final MenuTime menuTime, final LinearLayout cartLayout, final boolean isOrderEnable) {

        String name, description, price;
        int quantity;

        if(data == null)
            return null;

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.component_food_dish_layout, null);

        TextView nameTextView = (TextView) view.findViewById(R.id.dishname);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.dishdescription);
        TextView priceTextView = (TextView) view.findViewById(R.id.dishprice);
        pictureImageView = (ImageView) view.findViewById(R.id.dishpicture);
        final TextView orderCountTextViewBF = (TextView) view.findViewById(R.id.orderCountTextViewBF);
        final ImageView orderPlusImageViewBF = (ImageView) view.findViewById(R.id.orderPlusImageViewBF);
        final ImageView orderMinusImageViewBF = (ImageView) view.findViewById(R.id.orderMinusImageViewBF);
        final TextView orderCountTextViewL = (TextView) view.findViewById(R.id.orderCountTextViewL);
        final ImageView orderPlusImageViewL = (ImageView) view.findViewById(R.id.orderPlusImageViewL);
        final ImageView orderMinusImageViewL = (ImageView) view.findViewById(R.id.orderMinusImageViewL);
        final TextView orderCountTextViewD = (TextView) view.findViewById(R.id.orderCountTextViewD);
        final ImageView orderPlusImageViewD = (ImageView) view.findViewById(R.id.orderPlusImageViewD);
        final ImageView orderMinusImageViewD = (ImageView) view.findViewById(R.id.orderMinusImageViewD);

        final LinearLayout layoutBreakfast = (LinearLayout) view.findViewById(R.id.layoutBreakfast);
        final LinearLayout layoutLunch = (LinearLayout) view.findViewById(R.id.layoutLunch);
        final LinearLayout layoutDinner = (LinearLayout) view.findViewById(R.id.layoutDinner);

        name = data.getName();
        description = "";
        if(data.getDescription() != null) {
            description = data.getDescription();
        }
        price = "Invalid Price";
        if(data.getPrice() >= 0) {
            price = "Price " + "\u20B9" + data.getPrice();
        }

        quantity = (data.getBreakfastQuantity() + data.getLunchQuantity() + data.getDinnerQuantity());

        layoutBreakfast.setVisibility(View.GONE);
        layoutLunch.setVisibility(View.GONE);
        layoutDinner.setVisibility(View.GONE);

        if(menuTime != null && isOrderEnable) {
            if(menuTime.isBreakfast()) {
                layoutBreakfast.setVisibility(View.VISIBLE);
            }
            if(menuTime.isLunch()) {
                layoutLunch.setVisibility(View.VISIBLE);
            }
            if(menuTime.isDinner()) {
                layoutDinner.setVisibility(View.VISIBLE);
            }
        }

        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(price);

        updatePicture(picture);

        orderCountTextViewBF.setText("" + data.getBreakfastQuantity());
        orderCountTextViewL.setText("" + data.getLunchQuantity());
        orderCountTextViewD.setText("" + data.getDinnerQuantity());

        if(quantity > 0 && !orderList.containsKey(data.getId())) {
            orderList.put(data.getId(), data);
        }
        if(quantity <= 0 && orderList.containsKey(data.getId())) {
            orderList.remove(data.getId());
        }

        setCartLayout(cartLayout, orderList);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId() == orderPlusImageViewBF.getId()) {
                    incrementQuantity(MenuType.BREAKFAST, data.getBreakfastQuantity(), orderCountTextViewBF);
                }
                if(view.getId() == orderPlusImageViewL.getId()) {
                    incrementQuantity(MenuType.LUNCH, data.getLunchQuantity(), orderCountTextViewL);
                }
                if(view.getId() == orderPlusImageViewD.getId()) {
                    incrementQuantity(MenuType.DINNER, data.getDinnerQuantity(), orderCountTextViewD);
                }


                if(view.getId() == orderMinusImageViewBF.getId()) {
                    decrementQuantity(MenuType.BREAKFAST, data.getBreakfastQuantity(), orderCountTextViewBF);
                }
                if(view.getId() == orderMinusImageViewL.getId()) {
                    decrementQuantity(MenuType.LUNCH, data.getLunchQuantity(), orderCountTextViewL);
                }
                if(view.getId() == orderMinusImageViewD.getId()) {
                    decrementQuantity(MenuType.DINNER, data.getDinnerQuantity(), orderCountTextViewD);
                }

                int quantity = (data.getBreakfastQuantity() + data.getLunchQuantity() + data.getDinnerQuantity());

                if(quantity > 0 && !orderList.containsKey(data.getId())) {
                    orderList.put(data.getId(), data);
                }
                if(quantity <= 0 && orderList.containsKey(data.getId())) {
                    orderList.remove(data.getId());
                }

                setCartLayout(cartLayout, orderList);

            }

            private void incrementQuantity(MenuType menuType, int quantity, TextView orderCountTextView) {
                if(quantity < 99) {
                    if(menuType == MenuType.BREAKFAST) {
                        data.setBreakfastQuantity(quantity + 1);
                        orderCountTextView.setText("" + data.getBreakfastQuantity());
                    } else if(menuType == MenuType.LUNCH) {
                        data.setLunchQuantity(quantity + 1);
                        orderCountTextView.setText("" + data.getLunchQuantity());
                    } else if(menuType == MenuType.DINNER) {
                        data.setDinnerQuantity(quantity + 1);
                        orderCountTextView.setText("" + data.getDinnerQuantity());
                    }
                }
            }

            private void decrementQuantity(MenuType menuType, int quantity, TextView orderCountTextView) {
                if(quantity > 0) {
                    if(menuType == MenuType.BREAKFAST) {
                        data.setBreakfastQuantity(quantity - 1);
                        orderCountTextView.setText("" + data.getBreakfastQuantity());
                    } else if(menuType == MenuType.LUNCH) {
                        data.setLunchQuantity(quantity - 1);
                        orderCountTextView.setText("" + data.getLunchQuantity());
                    } else if(menuType == MenuType.DINNER) {
                        data.setDinnerQuantity(quantity - 1);
                        orderCountTextView.setText("" + data.getDinnerQuantity());
                    }

                }
            }
        };

        if(isOrderEnable) {
            orderPlusImageViewBF.setOnClickListener(listener);
            orderMinusImageViewBF.setOnClickListener(listener);
            orderPlusImageViewL.setOnClickListener(listener);
            orderMinusImageViewL.setOnClickListener(listener);
            orderPlusImageViewD.setOnClickListener(listener);
            orderMinusImageViewD.setOnClickListener(listener);
        }

        return view;
    }

    public void updatePicture(final Bitmap picture) {

        if(picture != null) {
            pictureImageView.setImageBitmap(picture);
            pictureImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            pictureImageView.setAdjustViewBounds(true);
        } else {
            pictureImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_logo_faded));
        }
    }

    private void setCartLayout(LinearLayout cartLayout, Map<String, DishOrderData> orderList) {

        if(orderList.size() <= 0) {

            cartLayout.setVisibility(View.GONE);

        } else {

            String itemString = "Items";
            int itemsCount = 0;
            int cost = 0;

            for (Map.Entry<String, DishOrderData> entry : orderList.entrySet()) {

                int count = (entry.getValue().getBreakfastQuantity() + entry.getValue().getLunchQuantity() + entry.getValue().getDinnerQuantity());
                itemsCount += count;
                cost += (count * entry.getValue().getPrice());
            }

            if(itemsCount == 1)
                itemString = "Item";

            cartLayout.setVisibility(View.VISIBLE);
            TextView textView = cartLayout.findViewById(R.id.orderSummaryTextView);
            textView.setText(itemsCount + " " + itemString + "  |  " + "\u20B9" + cost);
        }

    }
}
