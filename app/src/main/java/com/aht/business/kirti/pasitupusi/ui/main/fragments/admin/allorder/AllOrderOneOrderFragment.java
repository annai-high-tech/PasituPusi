package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.allorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllOrderOneOrderFragment extends BaseFragment {

    private static final int NUMBER_OF_HEADER_ROWS = 5;
    private static final int NUMBER_OF_FOOTER_ROWS = 1;

    private TableLayout tableLayout;
    private TextView textViewOrderId;
    private TextView textViewOrderDate;
    private TextView textViewOrderedDate;
    private TextView textViewDeliveredDate;
    private TextView textViewTotalCost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_all_order_oneorder, container, false);

        tableLayout =  view.findViewById(R.id.tableLayout);
        textViewOrderId = view.findViewById(R.id.textViewOrderID);
        textViewOrderDate = view.findViewById(R.id.textViewOrderDate);
        textViewOrderedDate = view.findViewById(R.id.textViewOrderedDate);
        textViewDeliveredDate = view.findViewById(R.id.textViewDeliveredDate);
        textViewTotalCost = view.findViewById(R.id.textViewTotalCost);

        Button buttonBack =  view.findViewById(R.id.buttonBack);
        Button buttonBack2 =  view.findViewById(R.id.buttonBack2);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dishList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
                bundle.putSerializable("breakFastCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
                bundle.putSerializable("lunchCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
                bundle.putSerializable("dinnerCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());
                bundle.putParcelableArrayList("orderList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getOrderList());

                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderOneOrder_to_OrderList, bundle);

            }
        };

        buttonBack.setOnClickListener(listener);
        buttonBack2.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        AllOrderOneOrderFragmentArgs args = AllOrderOneOrderFragmentArgs.fromBundle(getArguments());

        OrderData orderData = args.getOrderData();

        if(orderData == null) {
            return;
        }

        updatePage(orderData);
    }

    private void updatePage(OrderData orderData) {

        updateOrderHeader(tableLayout, orderData.getOrderId(), orderData.getDate(), orderData.getOrderPlacedTime(), orderData.getOrderDeliveredTime(), orderData.getTotalCost());

        boolean firstTime = true;
        for(String element:orderData.getOrderList().keySet()) {
            DishOrderData orderElement = orderData.getOrderList().get(element);
            if(orderElement.getBreakfastQuantity() > 0) {
                String price, name, quantity, total;
                if(firstTime) {
                    updateOrderMenuHeading(tableLayout, "Breakfast", "");
                    firstTime = false;
                }
                price = String.valueOf(orderElement.getPrice());
                name = orderElement.getName();
                quantity = String.valueOf(orderElement.getBreakfastQuantity());
                total = String.valueOf(orderElement.getPrice() * orderElement.getBreakfastQuantity());

                addOrderLineItem(tableLayout, price, name, quantity, total, false);
            }

        }

        firstTime = true;
        for(String element:orderData.getOrderList().keySet()) {

            DishOrderData orderElement = orderData.getOrderList().get(element);

            if(orderElement.getLunchQuantity() > 0) {
                String price, name, quantity, total;
                if(firstTime) {
                    updateOrderMenuHeading(tableLayout, "Lunch", "");
                    firstTime = false;
                }
                price = String.valueOf(orderElement.getPrice());
                name = orderElement.getName();
                quantity = String.valueOf(orderElement.getLunchQuantity());
                total = String.valueOf(orderElement.getPrice() * orderElement.getLunchQuantity());

                addOrderLineItem(tableLayout, price, name, quantity, total, false);
            }
        }

        firstTime = true;
        for(String element:orderData.getOrderList().keySet()) {

            DishOrderData orderElement = orderData.getOrderList().get(element);

            if(orderElement.getDinnerQuantity() > 0) {
                String price, name, quantity, total;
                if(firstTime) {
                    updateOrderMenuHeading(tableLayout, "Dinner", "");
                    firstTime = false;
                }
                price = String.valueOf(orderElement.getPrice());
                name = orderElement.getName();
                quantity = String.valueOf(orderElement.getDinnerQuantity());
                total = String.valueOf(orderElement.getPrice() * orderElement.getDinnerQuantity());

                addOrderLineItem(tableLayout, price, name, quantity, total, false);
            }
        }
    }

    private TableLayout updateOrderHeader(TableLayout layout, String orderId, String orderDate, Date orderedDate, Date deliveredDate, int totalCost) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

        return tableLayout;
    }

    private void updateOrderMenuHeading(TableLayout layout, String title, String orderStatus) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.component_all_order_oneorder_menu_heading_row, null);
        View view2 = inflater.inflate(R.layout.component_all_order_oneorder_menu_status_row, null);

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

}
