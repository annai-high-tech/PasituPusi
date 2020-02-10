package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.model.utils.DateUtils;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ViewAllOrderFragment extends BaseFragment {

    private TextView textViewWelcomeMsg, welcomeMsgTextView, newsTextView, menuTitleTextView;
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

    private ViewAllOrderFragment() {

    }

    public ViewAllOrderFragment(boolean isNewObject) {
        this.isNewObject = isNewObject;
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
        newsTextView =  view.findViewById(R.id.newsTextView);
        menuTitleTextView =  view.findViewById(R.id.menu_heading);
        textViewDate =  view.findViewById(R.id.top_date_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);
        contentLayout =  view.findViewById(R.id.content_layout);
        navigationView = getActivity().findViewById(R.id.nav_view);
        welcomeMsgTextView = navigationView.getHeaderView(0).findViewById(R.id.nameTxt);
        menuDrawerImageView= navigationView.getHeaderView(0).findViewById(R.id.imageView);

        newsTextView.setSelected(true);
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

        progressDialog.show();

        textViewDate.setOnClickListener(listener);
        top_left_arrow.setOnClickListener(listener);
        top_right_arrow.setOnClickListener(listener);
        top_go_to_today.setOnClickListener(listener);

        isNewObject = false;

        return view;
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
        if(orderList == null || orderList.size() <= 0) {
            addMenuTitle("No Orders for this day");
        } else {
            updateMenuItems(orderList, contentLayout);
        }
    }

    private void updateMenuItems(List<OrderData> orderList, LinearLayout contentLayout) {

        boolean isEmpty = true;

        addMenuTitle("Order Summary");

        for(OrderData list:orderList) {

            boolean menuListNotEmpty = false;
            LinearLayout layout = null;
            int countItem = 0;

            layout = addMenuCategory(contentLayout, list.getOrderId());

            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                //addMenuList(layout, orderElement, element, menuListNotEmpty, isOrderEnable);
            }

        }

    }


    private void addMenuTitle(String text) {
        menuTitleTextView.setText(text);
        menuTitleTextView.setSelected(true);

        AnimationUtil.shake_right_left((LinearLayout)menuTitleTextView.getParent(), menuTitleTextView, 3);

    }

    private LinearLayout addMenuCategory(LinearLayout layout, String text) {

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
        row1Layout.addView(imageViewCollapse);
        layout.addView(rowLayout);
        layout.addView(contentLayout);

        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(imageViewCollapse, contentLayout);
            }
        });

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

    private void addMenuList(LinearLayout layout, MenuElement element, String key, boolean selected, boolean isOrderEnable) {

        Bitmap thumbnail = null;

        if(element.getPicture() != null) {
            thumbnail = BitmapUtils.StringToBitMap(element.getPicture());
        }

        DishOrderData dishOrderData = null;

        if(!orderDataList.containsKey(menuDay)) {
            orderDataList.put(menuDay, new OrderData(menuDay));
        }

        if(orderDataList.get(menuDay).getOrderList().size() > 0
                && orderDataList.get(menuDay).getOrderList().containsKey(key)) {
            dishOrderData = orderDataList.get(menuDay).getOrderList().get(key);
        } else {
            dishOrderData = new DishOrderData(key, element.getName(), element.getDescription(), element.getPrice());
        }

        //View view = FoodDishLayoutAdapter.createLayout(this.getContext(), dishOrderData, thumbnail, orderDataList.get(menuDay).getOrderList(), cartLayout, isOrderEnable);

        //layout.addView(view);
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            //SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
            if(view.getId() == top_left_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
                progressDialog.show();

            } else if(view.getId() == top_right_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
                progressDialog.show();


            } else if(view.getId() == top_go_to_today.getId()) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                today = menuDay = dateFormat(calendar);

                orderViewModel.getAllOrders(menuDay).observe(getViewLifecycleOwner(), mObserverResult1);
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
