package com.aht.business.kirti.pasitupusi.ui.components.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.user.UserDishSelectionFragment;

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

    public View createLayout(final DishOrderData data, final Bitmap picture, final Map<String, DishOrderData> orderList, final LinearLayout cartLayout, final boolean isOrderEnable) {

        String name, description, price, count;

        if(data == null)
            return null;

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.component_food_dish_layout, null);

        TextView nameTextView = (TextView) view.findViewById(R.id.dishname);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.dishdescription);
        TextView priceTextView = (TextView) view.findViewById(R.id.dishprice);
        pictureImageView = (ImageView) view.findViewById(R.id.dishpicture);
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
        count = "" + data.getQuantity();


        nameTextView.setText(name);
        descriptionTextView.setText(description);
        priceTextView.setText(price);

        updatePicture(picture);

        orderCountTextView.setText(count);
        if(data.getQuantity() > 0 && !orderList.containsKey(data.getId())) {
            orderList.put(data.getId(), data);
        }
        if(data.getQuantity() <= 0 && orderList.containsKey(data.getId())) {
            orderList.remove(data.getId());
        }

        setCartLayout(cartLayout, orderList);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String count;
                if(view.getId() == orderPlusImageView.getId()) {

                    if(data.getQuantity() < 99) {
                        data.setQuantity(data.getQuantity() + 1);
                        count = "" + data.getQuantity();
                        orderCountTextView.setText(count);
                    }

                }
                if(view.getId() == orderMinusImageView.getId()) {
                    if(data.getQuantity() > 0) {
                        data.setQuantity(data.getQuantity() - 1);
                        count = "" + data.getQuantity();
                        orderCountTextView.setText(count);
                    }

                }

                if(data.getQuantity() > 0 && !orderList.containsKey(data.getId())) {
                    orderList.put(data.getId(), data);
                }
                if(data.getQuantity() <= 0 && orderList.containsKey(data.getId())) {
                    orderList.remove(data.getId());
                }

                setCartLayout(cartLayout, orderList);

            }
        };

        if(isOrderEnable) {
            orderPlusImageView.setOnClickListener(listener);
            orderMinusImageView.setOnClickListener(listener);
        } else {
            orderPlusImageView.setVisibility(View.GONE);
            orderMinusImageView.setVisibility(View.GONE);
            orderCountTextView.setVisibility(View.GONE);
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

    private static void setCartLayout(LinearLayout cartLayout, Map<String, DishOrderData> orderList) {

        if(orderList.size() <= 0) {

            cartLayout.setVisibility(View.GONE);

        } else {

            String itemString = "Items";
            int itemsCount = 0;
            int cost = 0;

            for (Map.Entry<String, DishOrderData> entry : orderList.entrySet()) {
                itemsCount += entry.getValue().getQuantity();
                cost += (entry.getValue().getQuantity() * entry.getValue().getPrice());
            }

            if(itemsCount == 1)
                itemString = "Item";

            cartLayout.setVisibility(View.VISIBLE);
            TextView textView = cartLayout.findViewById(R.id.orderSummaryTextView);
            textView.setText(itemsCount + " " + itemString + "  |  " + "\u20B9" + cost);
        }

    }
}
