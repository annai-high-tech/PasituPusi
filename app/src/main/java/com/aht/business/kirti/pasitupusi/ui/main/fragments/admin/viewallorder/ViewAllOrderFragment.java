package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.viewallorder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ViewAllOrderFragment extends BaseFragment {

    private static final int NUMBER_OF_HEADER_ROWS = 4;
    private static final int NUMBER_OF_FOOTER_ROWS = 1;

    private TextView textViewWelcomeMsg, welcomeMsgTextView, menuTitleTextView;
    private LinearLayout contentLayout;
    private TextView textViewDate;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;
    private ImageView menuDrawerImageView;
    private NavigationView navigationView;

    private ProgressDialog progressDialog;
    private Calendar calendar, minCalendar, maxCalendar;
    private String menuDay, today, tomorrow;

    private OrderViewModel orderViewModel;
    private List<OrderData> orderList = null;

    private boolean isNewObject;

    private ProfileData profileData = null;

    private Map<String, OrderData> orderDataList = new HashMap<>();

    private List<DishOrderData> dishList = new ArrayList<>();
    private Map<String, Integer> breakFastCount = new HashMap<>();
    private Map<String, Integer> lunchCount = new HashMap<>();
    private Map<String, Integer> dinnerCount = new HashMap<>();


    public static ViewAllOrderFragment newInstance(boolean isNewObject) {
        Bundle args = new Bundle();
        args.putBoolean("isNewObject", isNewObject);
        ViewAllOrderFragment f = new ViewAllOrderFragment();
        f.setArguments(args);
        f.isNewObject = isNewObject;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_allorder, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        textViewWelcomeMsg =  view.findViewById(R.id.titleTextView);
        menuTitleTextView =  view.findViewById(R.id.menu_heading);
        textViewDate =  view.findViewById(R.id.top_date_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);
        contentLayout =  view.findViewById(R.id.content_layout);
        navigationView = getActivity().findViewById(R.id.nav_view);
        welcomeMsgTextView = navigationView.getHeaderView(0).findViewById(R.id.nameTxt);
        menuDrawerImageView= navigationView.getHeaderView(0).findViewById(R.id.imageView);

        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi Menu");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        if(isNewObject) {
            orderDataList.clear();

            calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
            today = menuDay = dateFormat(calendar);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tomorrow = dateFormat(calendar);
            calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
        }

        orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        textViewDate.setOnClickListener(listener);
        top_left_arrow.setOnClickListener(listener);
        top_right_arrow.setOnClickListener(listener);
        top_go_to_today.setOnClickListener(listener);

        isNewObject = false;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    Observer<List<OrderData>> mObserverResult1 = new Observer<List<OrderData>>() {
        @Override
        public void onChanged(@Nullable List<OrderData> list) {

            orderList = list;

            if(list != null) {
                updatePage();
            }

            progressDialog.dismiss();

        }
    };

    private void updatePage() {

        minCalendar = maxCalendar = null;
        profileData = ((MainActivity)getActivity()).getProfileData();

        textViewWelcomeMsg.setText("Order Summary");

        SimpleDateFormat i_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat o_format = new SimpleDateFormat("dd MMM yyyy");
        String dateTitle = "";
        String minDate, maxDate;

        try {
            minDate = dateFormat(minCalendar);
            maxDate = dateFormat(maxCalendar);

            top_left_arrow.setVisibility(View.VISIBLE);
            top_right_arrow.setVisibility(View.VISIBLE);

            Date curDate = i_format.parse(menuDay);
            dateTitle = o_format.format(curDate);

            if(minDate != null) {
                Date date = i_format.parse(minDate);
                if (dateTitle.equals(o_format.format(date))) {
                    top_left_arrow.setVisibility(View.INVISIBLE);
                }
            }
            if(maxDate != null) {
                Date date = i_format.parse(maxDate);
                if (dateTitle.equals(o_format.format(date))) {
                    top_right_arrow.setVisibility(View.INVISIBLE);
                }
            }

            if(menuDay.equals(today)) {
                dateTitle += " (Today)";
            }
            if(menuDay.equals(tomorrow)) {
                dateTitle += " (Tomorrow)";
            }
        } catch (ParseException e) {
            dateTitle = "No Dates";
            e.printStackTrace();
        }
        textViewDate.setText(dateTitle);

        contentLayout.removeAllViewsInLayout();
        contentLayout.invalidate();
        contentLayout.requestLayout();
        menuTitleTextView.setText("");

        dishList.clear();
        breakFastCount.clear();
        lunchCount.clear();
        dinnerCount.clear();

        if(orderList == null || orderList.size() <= 0) {
            addMenuTitle("No Orders for this day");
        } else {
            updateMenuItems(orderList, contentLayout);
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dishList", (ArrayList<? extends Parcelable>) dishList);
        bundle.putSerializable("breakFastCount", (Serializable) breakFastCount);
        bundle.putSerializable("lunchCount", (Serializable) lunchCount);
        bundle.putSerializable("dinnerCount", (Serializable) dinnerCount);

        NavController navController = Navigation.findNavController(this.getActivity(), R.id.fragment_allorder_entry);
        navController.setGraph(navController.getGraph(), bundle);

    }

    private void updateMenuItems(List<OrderData> orderList, LinearLayout contentLayout) {

        boolean isEmpty = true;

        addMenuTitle("Order Summary");

        for(OrderData list:orderList) {

            TableLayout layout = null;

            layout = addOrderHeader(contentLayout, list.getOrderId(), list.getDate(), list.getOrderPlacedTime(), list.getOrderDeliveredTime(), list.getTotalCost(), false);

            boolean firstTime = true;

            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                if(breakFastCount.containsKey(element)) {
                    breakFastCount.put(element, breakFastCount.get(element) + orderElement.getBreakfastQuantity());
                    lunchCount.put(element, lunchCount.get(element) + orderElement.getLunchQuantity());
                    dinnerCount.put(element, dinnerCount.get(element) + orderElement.getDinnerQuantity());
                } else {
                    breakFastCount.put(element, orderElement.getBreakfastQuantity());
                    lunchCount.put(element, orderElement.getLunchQuantity());
                    dinnerCount.put(element, orderElement.getDinnerQuantity());
                    dishList.add(orderElement);
                }

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


    private void addMenuTitle(String text) {
        menuTitleTextView.setText(text);
        menuTitleTextView.setSelected(true);

        AnimationUtil.shake_right_left((LinearLayout)menuTitleTextView.getParent(), menuTitleTextView, 3);

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

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            //SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
            if(view.getId() == top_left_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            } else if(view.getId() == top_right_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
                progressDialog.setMessage("Loading...");
                progressDialog.show();


            } else if(view.getId() == top_go_to_today.getId()) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                today = menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            } else if (view.getId() == textViewDate.getId()) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewAllOrderFragment.this.getContext(), dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if(minCalendar != null) {
                    datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
                }
                if(maxCalendar != null) {
                    datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());
                }

                datePickerDialog.show();
            }

        }


    };

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            menuDay = dateFormat(calendar);

            orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

    };

    private void toggle_contents(ImageView sourceClick, View destView){

        if(destView.isShown()){
            AnimationUtil.slide_up(this.getContext(), destView, sourceClick);
        }
        else{
            AnimationUtil.slide_down(this.getContext(), destView, sourceClick);
        }

    }

    private String dateFormat(Calendar calendar) {

        if(calendar == null) {
            return null;
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String str = "" + year + "-";

        if(month < 10)
            str += "0";
        str += month + "-";

        if(day < 10)
            str += "0";
        str += day;

        return str;
    }
}
