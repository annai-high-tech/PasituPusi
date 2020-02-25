package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.notification.NotificationsManager;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.SubPageFragment;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class ViewCartSubFragment extends SubPageFragment {

    private static final int NUMBER_OF_HEADER_ROWS = 2;
    private static final int NUMBER_OF_FOOTER_ROWS = 3;

    private TextView textViewTitle;
    private TableLayout contentLayout;
    private ProgressDialog progressDialog;

    private Calendar calendar;


    private OrderViewModel orderViewModel;
    private Map<String, OrderData> orderDataList;
    private String date;
    private ProfileData profileData = null;
    private NotificationsManager notificationsManager;

    public ViewCartSubFragment() {
        super("Order Summary");
    }

    public static ViewCartSubFragment newInstance(Map<String, OrderData> orderDataList, String date, ProfileData profileData) {
        Bundle args = new Bundle();
        ViewCartSubFragment f = new ViewCartSubFragment();
        f.setArguments(args);
        f.orderDataList = orderDataList;
        f.date = date;
        f.profileData = profileData;
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

        notificationsManager = new NotificationsManager();
        progressDialog = new ProgressDialog(this.getContext());
        textViewTitle = v.findViewById(R.id.titleTextView);
        contentLayout =  v.findViewById(R.id.contentTableLayout);

        textViewTitle.setText("Order for the day (" + date + ")");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        //progressDialog.show();

        updatePage(orderDataList.get(date), date, contentLayout);

        return v;
    }

    private void updatePage(OrderData orderData, String date, TableLayout contentLayout) {

        int childCount = contentLayout.getChildCount();

        // Remove all rows except the first one
        if (childCount > NUMBER_OF_HEADER_ROWS) {
            contentLayout.removeViews(NUMBER_OF_HEADER_ROWS, childCount - NUMBER_OF_HEADER_ROWS - NUMBER_OF_FOOTER_ROWS);
        }

        if(orderData == null) {
            return;
        }

        Map<String, DishOrderData> orderList = orderData.getOrderList();
        if(orderList.size() < 1) {
            return;
        }

        notificationsManager.init(this.getContext(), profileData);
        int allOrderCost = 0;
        boolean firstTime = true;

        for(DishOrderData list:orderList.values()) {
            if(list.getBreakfastQuantity() > 0) {

                updateOrderSummaryTable(contentLayout, list, list.getBreakfastQuantity(), "Breakfast", firstTime);
               if(firstTime) {
                    firstTime = false;
                }
                allOrderCost += list.getPrice() * list.getBreakfastQuantity();
            }
        }

        firstTime = true;
        for(DishOrderData list:orderList.values()) {
            if(list.getLunchQuantity() > 0) {

                updateOrderSummaryTable(contentLayout, list, list.getLunchQuantity(), "Lunch", firstTime);
                if(firstTime) {
                    firstTime = false;
                }
                allOrderCost += list.getPrice() * list.getLunchQuantity();
            }
        }

        firstTime = true;
        for(DishOrderData list:orderList.values()) {
            if(list.getDinnerQuantity() > 0) {

                updateOrderSummaryTable(contentLayout, list, list.getDinnerQuantity(), "Dinner", firstTime);
                if(firstTime) {
                    firstTime = false;
                }
                allOrderCost += list.getPrice() * list.getDinnerQuantity();
            }
        }
        orderData.setTotalCost(allOrderCost);
        if(allOrderCost > 0) {

            boolean isValidNewOrder = true;
            if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
                isValidNewOrder = false;
            }

            if(contentLayout != null) {
                addTotalCostAndOrderButtons(contentLayout, allOrderCost, isValidNewOrder);
            }
        }

        updateOrderStatus(contentLayout, orderData);

    }

    private void updateOrderSummaryTable(final TableLayout layout, final DishOrderData data, final int quantity, final String title, final boolean firstTime) {

        String price, name, total;

        if(firstTime) {
            addOrderHeading(contentLayout, title);
        }

        price = String.valueOf(data.getPrice());
        name = data.getName();
        total = String.valueOf(data.getPrice() * quantity);

        addOrderLineItem(layout, price, name, String.valueOf(quantity), total, false);
    }

    private TableLayout addOrderHeading(TableLayout layout, String title) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_order_summary_title, null);

        TextView textView = view.findViewById(R.id.textViewTitle);

        //System.out.println(layout.getChildCount());
        layout.addView(view, layout.getChildCount() - NUMBER_OF_FOOTER_ROWS);

        textView.setText(title);

        return layout;
    }

    private void addOrderLineItem(TableLayout layout, String price, String name, String quantity, String total, boolean isHeading) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_order_summary_row, null);
        //System.out.println(layout.getChildCount());
        layout.addView(view, layout.getChildCount() - NUMBER_OF_FOOTER_ROWS);

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewQuantity = view.findViewById(R.id.textViewQuantity);
        TextView textViewTotal = view.findViewById(R.id.textViewCost);

        textViewPrice.setText(String.valueOf(price));
        textViewName.setText(name);
        textViewQuantity.setText(quantity);
        textViewTotal.setText(String.valueOf(total));

    }

    private void addTotalCostAndOrderButtons(TableLayout layout, int allOrderCost, boolean isValidOrder) {

        final TextView textViewTotalCost = layout.findViewById(R.id.textViewTotalCost);
        final Button submitButton = layout.findViewById(R.id.buttonSubmit);
        final Button clearButton = layout.findViewById(R.id.buttonCancel);

        textViewTotalCost.setText(String.valueOf(allOrderCost));

        class ButtonListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                Button button = ((Button)view);

                if(button.getText().equals(submitButton.getText())) {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    orderDataList.get(date).setOrderPlacedTime(calendar.getTime());
                    orderViewModel.addOrder(date, orderDataList.get(date)).observe(getViewLifecycleOwner(), mObserverResult2);
                }

                if(button.getText().equals(clearButton.getText())) {
                    orderDataList.get(date).setOrderId("CANCEL");
                    submitButton.setEnabled(false);
                    clearButton.setEnabled(false);
                    updateOrderStatus(contentLayout, orderDataList.get(date));
                }
            }
        }

        submitButton.setOnClickListener(new ButtonListener());
        clearButton.setOnClickListener(new ButtonListener());

        if(!isValidOrder) {
            submitButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }

    private void updateOrderStatus(LinearLayout layout, OrderData orderData) {

        TextView textViewOrderStatus = layout.findViewById(R.id.textViewOrderStatus);

        if(orderData == null) {
            textViewOrderStatus.setText("Order Failure!\n\nYour Order is not complete due to technical reason");
        } else if(orderData.getOrderId() != null && orderData.getOrderId().equals("CANCEL")) {
            textViewOrderStatus.setText("Order cancelled by user");
            orderDataList.remove(date);
        } else if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
            textViewOrderStatus.setText("Order Received Successfully!\n\nYour Order Reference Number is \n" + orderData.getOrderId());
            orderDataList.remove(date);
        } else {
            textViewOrderStatus.setText("");
        }

    }

    Observer<OrderData> mObserverResult1 = new Observer<OrderData>() {
        @Override
        public void onChanged(@Nullable OrderData orderData) {

            if(orderData != null) {

                updatePage(orderData, date, contentLayout);

                if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
                    notificationsManager.showNewOrderNotification(orderData);
                }

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

}