package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.enums.MenuType;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.utils.AndroidUtils;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrackOrderFragment extends BaseFragment {

    private static final int NUMBER_OF_HEADER_ROWS = 4;
    private static final int NUMBER_OF_FOOTER_ROWS = 1;

    private TextView menuTitleTextView;
    private LinearLayout contentLayout;

    private OrderViewModel orderViewModel;

    public static TrackOrderFragment newInstance() {
        Bundle args = new Bundle();
        TrackOrderFragment f = new TrackOrderFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_order, container, false);

        menuTitleTextView = view.findViewById(R.id.titleTextView);
        contentLayout = view.findViewById(R.id.contentLayout);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getUserOrders().observe(getViewLifecycleOwner(), mObserverResult1);

        return view;
    }

    Observer<List<OrderData>> mObserverResult1 = new Observer<List<OrderData>>() {
        @Override
        public void onChanged(@Nullable List<OrderData> orderList) {

            if(orderList != null) {

                updatePage(orderList);

            }

        }
    };

    private void updatePage(List<OrderData> orderDataList) {

        menuTitleTextView.setText("Order Tracking");

        contentLayout.removeAllViewsInLayout();
        contentLayout.invalidate();
        contentLayout.requestLayout();

        if(orderDataList == null || orderDataList.size() <= 0) {
            menuTitleTextView.setText("No Orders for Tracking");
        } else {
            updateMenuItems(orderDataList, contentLayout);
        }
    }

    private void updateMenuItems(List<OrderData> orderList, LinearLayout contentLayout) {

        int countOrder = 0;

        for(OrderData list:orderList) {
            boolean isLatestOrder = true;
            TableLayout layout = null;
            countOrder ++;

            if(countOrder > 1) {
                isLatestOrder = false;
            }

            layout = addOrderHeader(contentLayout, list.getOrderId(), list.getDate(), list.getOrderPlacedTime(), list.getOrderDeliveredTime(), list.getTotalCost(), isLatestOrder);

            boolean firstTime = true;

            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                if(orderElement.getBreakfastQuantity() > 0) {
                    String price, name, quantity, total;
                    if(firstTime) {
                        addOrderHeading(layout, "Breakfast", "");
                        firstTime = false;
                    }
                    price = String.valueOf(orderElement.getPrice());
                    name = orderElement.getName();
                    quantity = String.valueOf(orderElement.getBreakfastQuantity());
                    total = String.valueOf(orderElement.getPrice() * orderElement.getBreakfastQuantity());

                    addOrderLineItem(layout, price, name, quantity, total, false);
                }
            }

            firstTime = true;
            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                if(orderElement.getLunchQuantity() > 0) {
                    String price, name, quantity, total;
                    if(firstTime) {
                        addOrderHeading(layout, "Lunch", "");
                        firstTime = false;
                    }
                    price = String.valueOf(orderElement.getPrice());
                    name = orderElement.getName();
                    quantity = String.valueOf(orderElement.getLunchQuantity());
                    total = String.valueOf(orderElement.getPrice() * orderElement.getLunchQuantity());

                    addOrderLineItem(layout, price, name, quantity, total, false);
                }
            }

            firstTime = true;
            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                if(orderElement.getDinnerQuantity() > 0) {
                    String price, name, quantity, total;
                    if(firstTime) {
                        addOrderHeading(layout, "Dinner", "");
                        firstTime = false;
                    }
                    price = String.valueOf(orderElement.getPrice());
                    name = orderElement.getName();
                    quantity = String.valueOf(orderElement.getDinnerQuantity());
                    total = String.valueOf(orderElement.getPrice() * orderElement.getDinnerQuantity());

                    addOrderLineItem(layout, price, name, quantity, total, false);
                }
            }

        }

    }

    private TableLayout addOrderHeader(LinearLayout layout, String orderId, String orderDate, Date orderedDate, Date deliveredDate, int totalCost, boolean isCollapse) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_track_order_layout, null);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        TextView textViewOrderId = view.findViewById(R.id.textViewOrderID);
        TextView textViewOrderDate = view.findViewById(R.id.textViewOrderDate);
        TextView textViewOrderedDate = view.findViewById(R.id.textViewOrderedDate);
        TextView textViewDeliveredDate = view.findViewById(R.id.textViewDeliveredDate);
        TextView textViewTotalCost = view.findViewById(R.id.textViewTotalCost);
        final LinearLayout headerLayout = view.findViewById(R.id.layoutOrderCollapse);
        final ImageView imageViewCollapse = view.findViewById(R.id.imageViewCollapse);
        final TableLayout tableLayout = view.findViewById(R.id.tableLayout);

        textViewOrderId.setText(orderId);
        textViewOrderDate.setText(orderDate);
        textViewTotalCost.setText(String.valueOf(totalCost));
        if(orderedDate != null) {
            textViewOrderedDate.setText(dateFormat.format(orderedDate));
        } else {
            textViewOrderedDate.setText("");
        }
        if(deliveredDate != null) {
            textViewDeliveredDate.setText(dateFormat.format(deliveredDate));
        } else {
            textViewDeliveredDate.setText("");
        }

        layout.addView(view);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(imageViewCollapse, tableLayout);
            }
        });

        if(!isCollapse) {
            headerLayout.performClick();
        }

        return tableLayout;
    }

    private void addOrderHeading(TableLayout layout, String title, String orderStatus) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.component_order_summary_title, null);
        View view2 = inflater.inflate(R.layout.component_track_order_status, null);

        layout.addView(view1, layout.getChildCount() - NUMBER_OF_FOOTER_ROWS);
        layout.addView(view2, layout.getChildCount() - NUMBER_OF_FOOTER_ROWS);

        TextView textViewTitle = view1.findViewById(R.id.textViewTitle);
        TextView textViewStatus = view2.findViewById(R.id.textViewOrderStatus);

        textViewTitle.setText(title);
        textViewStatus.setText(orderStatus);

    }

    private void addOrderLineItem(TableLayout layout, String price, String name, String quantity, String total, boolean isHeading) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_order_summary_row, null);

        layout.addView(view, layout.getChildCount() - NUMBER_OF_FOOTER_ROWS - 1);

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewQuantity = view.findViewById(R.id.textViewQuantity);
        TextView textViewTotal = view.findViewById(R.id.textViewCost);

        textViewPrice.setText(price);
        textViewName.setText(name);
        textViewQuantity.setText(quantity);
        textViewTotal.setText(total);

    }

    private void toggle_contents(ImageView sourceClick, View destView){

        if(destView.isShown()){
            AnimationUtil.slide_up(this.getContext(), destView, sourceClick);
        }
        else{
            AnimationUtil.slide_down(this.getContext(), destView, sourceClick);
        }

    }

}
