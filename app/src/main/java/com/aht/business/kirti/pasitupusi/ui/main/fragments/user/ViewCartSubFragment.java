package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.SubPageFragment;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class ViewCartSubFragment extends SubPageFragment {

    private static final String ORDER_BUTTON_TEXT = "Place Order";

    private TextView textViewTitle;
    private LinearLayout contentLayout;
    private ProgressDialog progressDialog;
    private Button submit;

    private Calendar calendar;


    private OrderViewModel orderViewModel;
    private Map<String, OrderData> orderDataList;
    private String date;


    public ViewCartSubFragment(Map<String, OrderData> orderDataList, String date) {
        super("Order Summary");
        this.orderDataList = orderDataList;
        this.date = date;
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
        submit = null;

        if(orderData == null) {
            return;
        }

        Map<String, DishOrderData> orderList = orderData.getOrderList();
        if(orderList.size() < 1) {
            return;
        }

        int allOrderCost = 0;

        if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
            TextView textViewBillNo = new TextView(this.getContext());
            textViewBillNo.setText("Order Number:" + orderData.getOrderId());
            contentLayout.addView(textViewBillNo);
        }

        for(DishOrderData list:orderList.values()) {

            String price, name, quantity, total;

            LinearLayout eachRow = new LinearLayout(this.getContext());
            TextView textViewPrice = new TextView(this.getContext());
            TextView textViewName = new TextView(this.getContext());
            TextView textViewQuantity = new TextView(this.getContext());
            TextView textViewTotal = new TextView(this.getContext());

            price = "\u20B9" + list.getPrice();
            name = list.getName();
            quantity = String.valueOf(list.getQuantity());
            total = "\u20B9" + String.valueOf(list.getPrice() * list.getQuantity());
            allOrderCost += list.getPrice() * list.getQuantity();

            textViewPrice.setText(price);
            textViewName.setText(name);
            textViewQuantity.setText(quantity);
            textViewTotal.setText(total);

            eachRow.addView(textViewPrice);
            eachRow.addView(textViewName);
            eachRow.addView(textViewQuantity);
            eachRow.addView(textViewTotal);
            contentLayout.addView(eachRow);
        }

        orderData.setTotalCost(allOrderCost);
        if(allOrderCost > 0) {
            TextView textViewTotalCost = new TextView(this.getContext());
            textViewTotalCost.setText("\u20B9" + String.valueOf(allOrderCost));

            submit = new Button(this.getContext());
            submit.setText(ORDER_BUTTON_TEXT);
            submit.setOnClickListener(buttonListener);

            contentLayout.addView(textViewTotalCost);
            contentLayout.addView(submit);

            if(orderData.getOrderId() != null && !orderData.getOrderId().equals("")) {
                submit.setEnabled(false);
                orderDataList.clear();
            }

        }


/*        for(MenuCategory list:menuCategoryList.getMenuCategoryList().values()) {
            if(firstTime && dailyMenuList != null) {
                firstTime = false;
                addMenuDescription(contentLayout, dailyMenuList.getDescription(), isOldDate);
            }

            addMenuCategory(contentLayout, list.getName());

            for(String element:list.getMenuList().keySet()) {

                MenuElement menuElement = list.getMenuList().get(element);

                if(menuElement.isActive()) {
                    boolean selected = false;

                    if(dailyMenuList != null && dailyMenuList.getMenuList().containsKey(element)) {
                        selected = true;
                    }
                    addMenuList(contentLayout, menuElement, element, selected, isOldDate);
                    isEmpty = false;
                }
            }
        }

        if(!isEmpty && !isOldDate) {
            addMenuButtons(contentLayout);
        }*/
    }

/*    private void addMenuDescription(LinearLayout layout, String text, boolean isOldDate) {
        EditText editText = new EditText(this.getContext());
        editText.setText(text);
        editText.setEnabled(!isOldDate);

        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setGravity(Gravity.LEFT);
        editText.setHint("Enter menu description");
        editText.setPadding(10, 10, 10, 20);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                dailyMenuList.setDescription(s.toString());
                save.setEnabled(true);
                reset.setEnabled(true);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        layout.addView(editText);

    }

    private void addMenuCategory(LinearLayout layout, String text) {
        TextView textView = new TextView(this.getContext());
        textView.setText(text);

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        //textView.setTextColor(getResources().getColor(R.color.date_sel_text_color));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        layout.addView(textView);

    }

    private void addMenuButtons(LinearLayout layout) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        save = new Button(this.getContext());
        reset = new Button(this.getContext());

        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setGravity(Gravity.CENTER);

        save.setText("Save");
        reset.setText("Reset");

        save.setOnClickListener(buttonListener);
        reset.setOnClickListener(buttonListener);

        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        save.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        reset.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rowLayout.addView(save);
        rowLayout.addView(reset);
        layout.addView(rowLayout);

        save.setEnabled(false);
        reset.setEnabled(false);

    }

    private void addMenuList(LinearLayout layout, MenuElement element, String key, boolean selected, boolean isOldDate) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        CheckBox cb = new CheckBox(this.getContext());
        TextView textView = new TextView(this.getContext());

        cb.setText(key);
        textView.setText(element.getName());

        cb.setOnCheckedChangeListener(checkBoxListener);
        cb.setChecked(selected);
        cb.setEnabled(!isOldDate);

        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textView.setPadding(10, 0, 0, 0);
        //textView.setGravity(GravityView view, .CENTER);
        //textView.setTextColor(getResources().getColor(R.color.date_sel_text_color));
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        rowLayout.addView(cb);
        rowLayout.addView(textView);
        layout.addView(rowLayout);

    }
*/
    Observer<OrderData> mObserverResult1 = new Observer<OrderData>() {
        @Override
        public void onChanged(@Nullable OrderData orderList) {

            if(orderList != null) {

                updatePage(orderList, date, contentLayout);
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
                //TODO
            }

        }
    };

    private View.OnClickListener buttonListener =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(((Button)view).getText().equals(ORDER_BUTTON_TEXT)) {
                orderViewModel.addOrder(date, orderDataList.get(date)).observe(getViewLifecycleOwner(), mObserverResult2);
            }

            //orderViewModel.getLastOrder(date);
            submit.setEnabled(false);

        }
    };

}