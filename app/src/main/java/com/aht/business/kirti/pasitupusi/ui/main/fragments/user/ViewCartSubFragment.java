package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.utils.AndroidUtils;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.SubPageFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class ViewCartSubFragment extends SubPageFragment {

    private static final String PLACE_ORDER_BUTTON_TEXT = "Place Order";
    private static final String CANCEL_ORDER_BUTTON_TEXT = "Cancel Order";

    private TextView textViewTitle;
    private LinearLayout contentLayout;
    private ProgressDialog progressDialog;

    private Calendar calendar;


    private OrderViewModel orderViewModel;
    private Map<String, OrderData> orderDataList;
    private String date;

    public ViewCartSubFragment() {
        super("Order Summary");
    }

    public static ViewCartSubFragment newInstance(Map<String, OrderData> orderDataList, String date) {
        Bundle args = new Bundle();
        ViewCartSubFragment f = new ViewCartSubFragment();
        f.setArguments(args);
        f.orderDataList = orderDataList;
        f.date = date;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_viewcart, container, false);

        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

        progressDialog = new ProgressDialog(this.getContext());
        textViewTitle = v.findViewById(R.id.titleTextView);
        contentLayout =  v.findViewById(R.id.content_layout);

        textViewTitle.setText("Order for the day (" + date + ")");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        //progressDialog.show();

        updatePage(orderDataList.get(date), date, contentLayout);

        return v;
    }

    private void updatePage(OrderData orderData, String date, LinearLayout contentLayout) {

        contentLayout.removeAllViewsInLayout();

        if(orderData == null) {
            return;
        }

        Map<String, DishOrderData> orderList = orderData.getOrderList();
        if(orderList.size() < 1) {
            return;
        }

        int allOrderCost = 0;

        TableLayout tableLayout = addOrderHeading(contentLayout);

        for(DishOrderData list:orderList.values()) {

            String price, name, quantity, total;

            price = String.valueOf(list.getPrice());
            name = list.getName();
            quantity = String.valueOf(list.getQuantity());
            total = String.valueOf(list.getPrice() * list.getQuantity());
            allOrderCost += list.getPrice() * list.getQuantity();

            addOrderLineItem(tableLayout, price, name, quantity, total, false);
        }

        orderData.setTotalCost(allOrderCost);
        if(allOrderCost > 0) {

            boolean isValidNewOrder = true;
            if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
                isValidNewOrder = false;
                orderDataList.clear();
            }

            addTotalCostAndOrderButtons(tableLayout, allOrderCost, isValidNewOrder);
        }

        updateOrderStatus(contentLayout, orderData);

    }

    private TableLayout addOrderHeading(LinearLayout layout) {

        TableLayout tableLayout = new TableLayout(this.getContext());
        layout.addView(tableLayout);
        //layout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout.setStretchAllColumns(true);

        addOrderLineItem(tableLayout, "Price", "Dish Name", "Quantity", "Cost", true);

        return tableLayout;
    }

    private void addOrderLineItem(TableLayout layout, String price, String name, String quantity, String total, boolean isHeading) {

        TableRow eachRow = new TableRow(this.getContext());
        TextView textViewPrice = new TextView(this.getContext());
        TextView textViewName = new TextView(this.getContext());
        TextView textViewQuantity = new TextView(this.getContext());
        TextView textViewTotal = new TextView(this.getContext());

        textViewPrice.setText("\u20B9" + price);
        textViewName.setText(name);
        textViewQuantity.setText(quantity);
        textViewTotal.setText("\u20B9" + total);

        eachRow.addView(textViewName);
        eachRow.addView(textViewPrice);
        eachRow.addView(textViewQuantity);
        eachRow.addView(textViewTotal);
        layout.addView(eachRow);

        //eachRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textViewQuantity.setGravity(Gravity.CENTER);

        int fontSize = 14;
        if(isHeading) {
            fontSize = 18;

            textViewPrice.setTypeface(null, Typeface.BOLD);
            textViewName.setTypeface(null, Typeface.BOLD);
            textViewQuantity.setTypeface(null, Typeface.BOLD);
            textViewTotal.setTypeface(null, Typeface.BOLD);

            int paddingPix = AndroidUtils.dpToPixel(this.getContext(), 5);
            eachRow.setPadding(0, paddingPix, 0, paddingPix);
        } else {
            int paddingPix = AndroidUtils.dpToPixel(this.getContext(), 5);
            eachRow.setPadding(0, 0, 0, paddingPix);

        }

        textViewPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewQuantity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewTotal.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);

        //textViewPrice.setWidth(AndroidUtils.dpToPixel(this.getContext(), 40));
        //textViewQuantity.setWidth(AndroidUtils.dpToPixel(this.getContext(), 40));
        //textViewTotal.setWidth(AndroidUtils.dpToPixel(this.getContext(), 60));
    }

    private void addTotalCostAndOrderButtons(TableLayout layout, int allOrderCost, boolean isValidOrder) {

        TableRow row1 = new TableRow(this.getContext());
        TableRow row2 = new TableRow(this.getContext());
        LinearLayout buttonLayout = new LinearLayout(this.getContext());
        TextView textViewTotalCostTitle = new TextView(this.getContext());
        TextView textViewTotalCost = new TextView(this.getContext());
        Button submitButton = new Button(this.getContext());
        Button clearButton = new Button(this.getContext());

        textViewTotalCostTitle.setText("Total");
        textViewTotalCost.setText("\u20B9" + String.valueOf(allOrderCost));

        submitButton.setText(PLACE_ORDER_BUTTON_TEXT);
        clearButton.setText(CANCEL_ORDER_BUTTON_TEXT);

        submitButton.setOnClickListener(buttonListener);
        clearButton.setOnClickListener(buttonListener);

        row1.addView(new TextView(this.getContext()));
        row1.addView(new TextView(this.getContext()));
        row1.addView(textViewTotalCostTitle);
        row1.addView(textViewTotalCost);

        buttonLayout.addView(submitButton);
        buttonLayout.addView(clearButton);

        row2.addView(buttonLayout);

        layout.addView(row1);
        layout.addView(row2);

        if(!isValidOrder) {
            submitButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
        int fontSize = 16;
        textViewTotalCostTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewTotalCostTitle.setTypeface(null, Typeface.BOLD);

        fontSize = 14;
        textViewTotalCost.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewTotalCost.setTypeface(null, Typeface.BOLD);

        int paddingPix = AndroidUtils.dpToPixel(this.getContext(), 10);
        row1.setPadding(0, paddingPix, 0, paddingPix);

        paddingPix = AndroidUtils.dpToPixel(this.getContext(), 10);
        row2.setPadding(paddingPix, 0, paddingPix, 0);

        //submitButton.setWidth(AndroidUtils.dpToPixel(this.getContext(), 30));
        //clearButton.setWidth(AndroidUtils.dpToPixel(this.getContext(), 30));

        TableRow.LayoutParams params = (TableRow.LayoutParams) buttonLayout.getLayoutParams();
        params.span = 4; //amount of columns you will span
        params.gravity = Gravity.CENTER;
        /*params = (TableRow.LayoutParams) clearButton.getLayoutParams();
        params.span = 2; //amount of columns you will span*/

    }

    private void updateOrderStatus(LinearLayout layout, OrderData orderData) {

        TextView textViewOrderStatus = new TextView(this.getContext());
        textViewOrderStatus.setGravity(Gravity.CENTER);

        if(orderData == null) {
            textViewOrderStatus.setText("Order Failure!\n\nYour Order is not complete due to technical reason");
            layout.addView(textViewOrderStatus);
        } else if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
            textViewOrderStatus.setText("Order Received Successfully!\n\nYour Order Reference Number is \n" + orderData.getOrderId());
            layout.addView(textViewOrderStatus);
        }

        int paddingPix = AndroidUtils.dpToPixel(this.getContext(), 10);
        textViewOrderStatus.setPadding(paddingPix, paddingPix * 2, paddingPix, paddingPix);

        int fontSize = 14;
        textViewOrderStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewOrderStatus.setTypeface(null, Typeface.ITALIC);

        textViewOrderStatus.setTextColor(Color.parseColor("#ff4500"));
    }

    Observer<OrderData> mObserverResult1 = new Observer<OrderData>() {
        @Override
        public void onChanged(@Nullable OrderData orderData) {

            if(orderData != null) {

                updatePage(orderData, date, contentLayout);
            }
            progressDialog.dismiss();

        }
    };

    Observer<Boolean> mObserverResult2 = new Observer<Boolean>() {
        @Override
        public void onChanged(@Nullable Boolean resultStatus) {

            if(resultStatus) {
                orderViewModel.getLastOrder(date).observe(getViewLifecycleOwner(), mObserverResult1);
            } else {
                updateOrderStatus(contentLayout, null);
                progressDialog.dismiss();
            }
        }
    };

    private View.OnClickListener buttonListener =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            Button button = ((Button)view);
            LinearLayout buttonLayout;

            if(button.getText().equals(PLACE_ORDER_BUTTON_TEXT)) {
                progressDialog.show();
                orderViewModel.addOrder(date, orderDataList.get(date)).observe(getViewLifecycleOwner(), mObserverResult2);
            }

            if(button.getText().equals(CANCEL_ORDER_BUTTON_TEXT)) {
                orderDataList.clear();
            }

            buttonLayout = (LinearLayout)(button.getParent());

            // loop through them, if they are instances of Button, disable them.
            for(int i = 0; i < buttonLayout.getChildCount(); i++){
                if(buttonLayout.getChildAt(i) instanceof Button ) {
                    ((Button)buttonLayout.getChildAt(i)).setEnabled(false);
                }
            }
        }
    };

}