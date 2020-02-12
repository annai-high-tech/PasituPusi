package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

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
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.utils.AndroidUtils;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.List;

public class TrackOrderFragment extends BaseFragment {

    private TextView menuTitleTextView;
    private LinearLayout contentLayout;

    private OrderViewModel orderViewModel;

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
        //mGoToButton = (Button) view.findViewById(R.id.goto_button);
        //mGoToButton.setOnClickListener(listener);

        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tTrack your order page - COMING SOON");

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

        menuTitleTextView.setText("Track Order");

        contentLayout.removeAllViewsInLayout();
        contentLayout.invalidate();
        contentLayout.requestLayout();
        menuTitleTextView.setText("");
        if(orderDataList == null || orderDataList.size() <= 0) {
            addOrderTitle(contentLayout, "No Orders for Tracking", false, false);
        } else {
            updateMenuItems(orderDataList, contentLayout);
        }
    }

    private void updateMenuItems(List<OrderData> orderList, LinearLayout contentLayout) {

        int countOrder = 0;

        for(OrderData list:orderList) {
            boolean isLatestOrder = true;
            LinearLayout layout = null;
            countOrder ++;

            if(countOrder > 1) {
                isLatestOrder = false;
            }
            layout = addOrderTitle(contentLayout, list.getOrderId(), true, isLatestOrder);

            TableLayout tableLayout = addOrderHeading(layout);

            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                String price, name, quantity, total;

                price = String.valueOf(orderElement.getPrice());
                name = orderElement.getName();
                quantity = String.valueOf(orderElement.getQuantity());
                total = String.valueOf(orderElement.getPrice() * orderElement.getQuantity());

                addOrderLineItem(tableLayout, price, name, quantity, total, false);
            }

            addTotalCost(tableLayout, list.getTotalCost());

        }

    }

    private LinearLayout addOrderTitle(LinearLayout layout, String text, boolean isCollapseButtonRequired, boolean isCollapse) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        LinearLayout row1Layout = new LinearLayout(this.getContext());
        TextView textView = new TextView(this.getContext());
        final ImageView imageViewCollapse = new ImageView(this.getContext());
        final LinearLayout contentLayout = new LinearLayout(this.getContext());

        textView.setText(text);
        imageViewCollapse.setImageDrawable(this.getResources().getDrawable(android.R.drawable.arrow_up_float));

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageViewCollapse.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row1Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rowLayout.setGravity(Gravity.CENTER_VERTICAL);
        row1Layout.setGravity(Gravity.END);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        rowLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        textView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        textView.setTypeface(null, Typeface.BOLD);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        rowLayout.addView(textView);
        rowLayout.addView(row1Layout);

        layout.addView(rowLayout);
        layout.addView(contentLayout);

        if(isCollapseButtonRequired) {
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggle_contents(imageViewCollapse, contentLayout);
                }
            });
            row1Layout.addView(imageViewCollapse);

            if(!isCollapse) {
                rowLayout.performClick();
            }
        }

        rowLayout.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setLayoutParams(params);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(0, 30, 0, 0);
        rowLayout.setLayoutParams(params1);

        return contentLayout;
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

    private void addTotalCost(TableLayout layout, int allOrderCost) {

        TableRow row1 = new TableRow(this.getContext());
        TextView textViewTotalCostTitle = new TextView(this.getContext());
        TextView textViewTotalCost = new TextView(this.getContext());

        textViewTotalCostTitle.setText("Total");
        textViewTotalCost.setText("\u20B9" + String.valueOf(allOrderCost));

        row1.addView(new TextView(this.getContext()));
        row1.addView(new TextView(this.getContext()));
        row1.addView(textViewTotalCostTitle);
        row1.addView(textViewTotalCost);

        layout.addView(row1);

        int fontSize = 16;
        textViewTotalCostTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewTotalCostTitle.setTypeface(null, Typeface.BOLD);

        fontSize = 14;
        textViewTotalCost.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
        textViewTotalCost.setTypeface(null, Typeface.BOLD);

        int paddingPix = AndroidUtils.dpToPixel(this.getContext(), 10);
        row1.setPadding(0, paddingPix, 0, paddingPix);

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
