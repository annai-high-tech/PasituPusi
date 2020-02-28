package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.allorder;

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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class AllOrderFragment extends BaseFragment {

    private static final int NUMBER_OF_HEADER_ROWS = 4;
    private static final int NUMBER_OF_FOOTER_ROWS = 1;

    private TextView textViewWelcomeMsg, welcomeMsgTextView, menuTitleTextView;
    private TextView textViewDate;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;
    private ImageView menuDrawerImageView;
    private NavigationView navigationView;

    private ProgressDialog progressDialog;
    private Calendar calendar, minCalendar, maxCalendar;
    private String menuDay, today, tomorrow;

    private OrderViewModel orderViewModel;

    private List<OrderData> orderList = null;
    private List<DishOrderData> dishList = new ArrayList<>();
    private Map<String, Integer> breakFastCount = new HashMap<>();
    private Map<String, Integer> lunchCount = new HashMap<>();
    private Map<String, Integer> dinnerCount = new HashMap<>();


    private boolean isNewObject;

    private ProfileData profileData = null;


    public static AllOrderFragment newInstance(boolean isNewObject) {
        Bundle args = new Bundle();
        args.putBoolean("isNewObject", isNewObject);
        AllOrderFragment f = new AllOrderFragment();
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

        View view = inflater.inflate(R.layout.fragment_all_order, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        textViewWelcomeMsg =  view.findViewById(R.id.titleTextView);
        menuTitleTextView =  view.findViewById(R.id.menu_heading);
        textViewDate =  view.findViewById(R.id.top_date_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);
        navigationView = getActivity().findViewById(R.id.nav_view);
        welcomeMsgTextView = navigationView.getHeaderView(0).findViewById(R.id.nameTxt);
        menuDrawerImageView= navigationView.getHeaderView(0).findViewById(R.id.imageView);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        if(isNewObject) {
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

        menuTitleTextView.setText("");

        dishList.clear();
        breakFastCount.clear();
        lunchCount.clear();
        dinnerCount.clear();

        if(orderList == null || orderList.size() <= 0) {
            addMenuTitle("No Orders for this day");
        } else {
            for(OrderData list:orderList) {
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
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dishList", (ArrayList<? extends Parcelable>) dishList);
        bundle.putSerializable("breakFastCount", (Serializable) breakFastCount);
        bundle.putSerializable("lunchCount", (Serializable) lunchCount);
        bundle.putSerializable("dinnerCount", (Serializable) dinnerCount);
        bundle.putParcelableArrayList("orderList", (ArrayList<? extends Parcelable>) orderList);

        NavController navController = Navigation.findNavController(this.getActivity(), R.id.fragment_allorder_entry);
        navController.setGraph(navController.getGraph(), bundle);

    }

    private void addMenuTitle(String text) {
        menuTitleTextView.setText(text);
        menuTitleTextView.setSelected(true);

        AnimationUtil.shake_right_left((LinearLayout)menuTitleTextView.getParent(), menuTitleTextView, 3);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AllOrderFragment.this.getContext(), dateListener, calendar
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
