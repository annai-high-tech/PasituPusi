package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenu;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.notification.NotificationsManager;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.utils.DateUtils;
import com.aht.business.kirti.pasitupusi.ui.components.layout.FoodDishLayoutAdapter;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;
import com.google.android.material.navigation.NavigationView;

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

public class UserDishSelectionFragment extends BaseFragment {

    private TextView textViewWelcomeMsg, welcomeMsgTextView, newsTextView, menuTitleTextView;
    private LinearLayout contentLayout;
    private LinearLayout cartLayout, viewCartLayout;
    private TextView textViewDate;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;
    private ImageView menuDrawerImageView;
    private NavigationView navigationView;

    private ProgressDialog progressDialog;
    private Calendar calendar, minCalendar, maxCalendar;
    private String menuDay, today, tomorrow;

    private DailyMenuViewModel dailyMenuViewModel;
    private DailyMenuList dailyMenuList = null;

    private boolean isDailyMenuAcquired, isNewObject;

    private ProfileData profileData = null;
    private NotificationsManager notificationsManager;

    private Map<String, OrderData> orderDataList = new HashMap<>();

    public static UserDishSelectionFragment newInstance(boolean isNewObject) {
        Bundle args = new Bundle();
        args.putBoolean("isNewObject", isNewObject);
        UserDishSelectionFragment f = new UserDishSelectionFragment();
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

        View view = inflater.inflate(R.layout.fragment_dish_selection_user, container, false);

        //isNewObject = getArguments().getBoolean("isNewObject");

        notificationsManager = new NotificationsManager();
        ((MainActivity)getActivity()).getAdsAHT().loadNativeAds();

        progressDialog = new ProgressDialog(this.getContext());
        textViewWelcomeMsg =  view.findViewById(R.id.titleTextView);
        newsTextView =  view.findViewById(R.id.newsTextView);
        menuTitleTextView =  view.findViewById(R.id.menu_heading);
        textViewDate =  view.findViewById(R.id.top_date_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);
        contentLayout =  view.findViewById(R.id.content_layout);
        cartLayout =  view.findViewById(R.id.homeViewCartLayout);
        viewCartLayout = view.findViewById(R.id.viewCartLayout);
        navigationView = getActivity().findViewById(R.id.nav_view);
        welcomeMsgTextView = navigationView.getHeaderView(0).findViewById(R.id.nameTxt);
        menuDrawerImageView= navigationView.getHeaderView(0).findViewById(R.id.imageView);

        newsTextView.setSelected(true);
        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi Menu");

        isDailyMenuAcquired = false;

        dailyMenuViewModel = new ViewModelProvider(this).get(DailyMenuViewModel.class);

        if(isNewObject) {
            orderDataList.clear();

            calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
            today = menuDay = dateFormat(calendar);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tomorrow = dateFormat(calendar);
            calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
        }

        //if(menuCategoryList == null) {
            //dailyMenuViewModel.getAllTimeMenu().observe(getViewLifecycleOwner(), mObserverResult1);
        //}

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        dailyMenuViewModel.getDailyMenu(menuDay).observe(getViewLifecycleOwner(), mObserverResult2);

        textViewDate.setOnClickListener(listener);
        top_left_arrow.setOnClickListener(listener);
        top_right_arrow.setOnClickListener(listener);
        top_go_to_today.setOnClickListener(listener);
        viewCartLayout.setOnClickListener(listener);

        isNewObject = false;

        return view;
    }

    private void updateCartLayout() {

        if(orderDataList.containsKey(menuDay) && orderDataList.get(menuDay).getOrderList().size() > 0) {
            cartLayout.setVisibility(View.VISIBLE);
        } else {
            cartLayout.setVisibility(View.GONE);
        }

    }

    Observer<DailyMenuList> mObserverResult2 = new Observer<DailyMenuList>() {
        @Override
        public void onChanged(@Nullable DailyMenuList list) {

            dailyMenuList = list;

            if(dailyMenuList != null)
                isDailyMenuAcquired = true;
            else
                isDailyMenuAcquired = false;

            if(isDailyMenuAcquired) {
                updatePage();
                progressDialog.dismiss();
            }

        }
    };

    private void updatePage() {

        profileData = ((MainActivity)getActivity()).getProfileData();

        notificationsManager.init(this.getContext(), profileData);

        updateCartLayout();

        if(profileData != null) {
            if (profileData.getPicture() != null) {
                menuDrawerImageView.setImageBitmap(BitmapUtils.StringToBitMap(profileData.getPicture()));
            } else {
                menuDrawerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));
            }
            welcomeMsgTextView.setText("Hello " + profileData.getName() + "!");

            minCalendar = maxCalendar = null;

            if(ProfileRole.getValue(profileData.getRole()) <= ProfileRole.getValue(ProfileRole.GUEST)) {

                newsTextView.setText("Guest user cannot order dishes and cannot see past and future dish menus");
                minCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                maxCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

                textViewDate.setOnClickListener(null);
                textViewDate.setClickable(false);
            } else if(ProfileRole.getValue(profileData.getRole()) <= ProfileRole.getValue(ProfileRole.USER)) {

                //minCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                //minCalendar.add(Calendar.DAY_OF_YEAR, -1);

                //maxCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                //maxCalendar.add(Calendar.DAY_OF_YEAR, 7);

                minCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                maxCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

                textViewDate.setOnClickListener(null);
                textViewDate.setClickable(false);
            }

        }

        textViewWelcomeMsg.setText("Welcome to PASITU PUSI");

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
        if(dailyMenuList == null) {
            addMenuTitle("No Menus for this day");
        } else if(dailyMenuList.getMenuList().size() <= 0) {
            addMenuTitle("No Menus for this day");
        } else {
            updateMenuItems(dailyMenuList, contentLayout);
        }
    }

    private void updateMenuItems(DailyMenuList dailyMenuList, LinearLayout contentLayout) {

        boolean isEmpty = true;
        boolean isOrderEnable = ProfileRole.getValue(profileData.getRole()) > ProfileRole.getValue(ProfileRole.GUEST);

        if(isOrderEnable) {
            isOrderEnable = !DateUtils.isOldDate(menuDay, today);
        }

        if(dailyMenuList != null && dailyMenuList.getDescription() != null) {
            addMenuTitle(dailyMenuList.getDescription());
        }

        List<DailyMenu> list = new ArrayList<>(dailyMenuList.getMenuList().values());
        Collections.sort(list, new Comparator<DailyMenu>() {
            @Override
            public int compare(DailyMenu o1, DailyMenu o2) {
                if(o1.getCategoryName() != null && o2.getCategoryName() != null) {
                    return o1.getCategoryName().compareTo(o2.getCategoryName());
                }
                return 0;
            }
        });

        int countItem = 0;
        String menuCategoryName = "";
        LinearLayout layout = null;

        for(DailyMenu dailyMenu : list) {

            if(!menuCategoryName.equals(dailyMenu.getCategoryName())) {

                if(countItem % 5 != 0) {
                    ((MainActivity)getActivity()).getAdsAHT().showNativeAds(this.getContext(), layout);
                }

                menuCategoryName = dailyMenu.getCategoryName();
                layout = addMenuCategory(contentLayout, menuCategoryName);
                countItem = 0;
            }

            if(layout != null) {
                countItem ++;
                addMenuList(layout,
                        dailyMenu,
                        isOrderEnable);

                if(countItem % 5 == 0) {
                    ((MainActivity)getActivity()).getAdsAHT().showNativeAds(this.getContext(), layout);
                }

            }

            isEmpty = false;

        }

        if(!isEmpty && countItem % 5 != 0) {
            ((MainActivity)getActivity()).getAdsAHT().showNativeAds(this.getContext(), layout);
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

    private void addMenuList(LinearLayout layout, final DailyMenu dish, boolean isOrderEnable) {

        Bitmap thumbnail = null;

        if(dish.getPicture() != null) {
            thumbnail = BitmapUtils.StringToBitMap(dish.getPicture());
        }

        DishOrderData dishOrderData = null;

        if(!orderDataList.containsKey(menuDay)) {
            orderDataList.put(menuDay, new OrderData(menuDay));
        }

        if(orderDataList.get(menuDay).getOrderList().size() > 0
                && orderDataList.get(menuDay).getOrderList().containsKey(dish.getId().getId())) {
            dishOrderData = orderDataList.get(menuDay).getOrderList().get(dish.getId().getId());
        } else {
            dishOrderData = new DishOrderData(dish.getId().getId(), dish.getName(), dish.getDescription(), dish.getPrice());
        }

        final FoodDishLayoutAdapter foodDishLayoutAdapter = new FoodDishLayoutAdapter(this.getContext());
        View view = foodDishLayoutAdapter.createLayout(dishOrderData, orderDataList.get(menuDay).getOrderList(), thumbnail, dish.getMenuTime(), cartLayout, isOrderEnable);

        layout.addView(view);


        dailyMenuViewModel.getDishPicture(dish.getId()).observe(getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String picture) {

                        Bitmap thumbnail = null;

                        if(picture != null) {
                            thumbnail = BitmapUtils.StringToBitMap(picture);
                        }

                        foodDishLayoutAdapter.updatePicture(thumbnail);
                    }
                });
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            //SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
            if(view.getId() == top_left_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenu(menuDay).observe(UserDishSelectionFragment.this.getViewLifecycleOwner(), mObserverResult2);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            } else if(view.getId() == top_right_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenu(menuDay).observe(UserDishSelectionFragment.this.getViewLifecycleOwner(), mObserverResult2);
                progressDialog.setMessage("Loading...");
                progressDialog.show();


            } else if(view.getId() == top_go_to_today.getId()) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                today = menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenu(menuDay).observe(UserDishSelectionFragment.this.getViewLifecycleOwner(), mObserverResult2);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

            } else if (view.getId() == textViewDate.getId()) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserDishSelectionFragment.this.getContext(), dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                if(minCalendar != null) {
                    datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
                }
                if(maxCalendar != null) {
                    datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());
                }

                datePickerDialog.show();
            } else if (view.getId() == viewCartLayout.getId()) {

                ViewCartSubFragment newFragment = ViewCartSubFragment.newInstance(orderDataList, menuDay, profileData);

                ((MainActivity)getActivity()).changeFragments(newFragment);
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

            dailyMenuViewModel.getDailyMenu(menuDay).observe(UserDishSelectionFragment.this.getViewLifecycleOwner(), mObserverResult2);
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
