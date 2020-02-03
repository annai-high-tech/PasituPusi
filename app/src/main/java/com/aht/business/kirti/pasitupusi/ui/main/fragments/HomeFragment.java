package com.aht.business.kirti.pasitupusi.ui.main.fragments;

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
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends BaseFragment {

    private TextView textViewWelcomeMsg, welcomeMsgTextView, newsTextView, menuTitleTextView;
    private LinearLayout contentLayout;
    private TextView textViewDate;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;
    private ImageView menuDrawerImageView;
    private NavigationView navigationView;

    private ProgressDialog progressDialog;
    private Calendar calendar, minCalendar, maxCalendar;
    private String menuDay, today, tomorrow;

    private DailyMenuViewModel dailyMenuViewModel;
    private MenuCategoryList menuCategoryList = null;
    private DailyMenuList dailyMenuList = null;

    private boolean isAllTimeMenuAcquired, isDailyMenuAcquired;

    private ProfileData profileData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        progressDialog = new ProgressDialog(this.getContext());
        textViewWelcomeMsg =  view.findViewById(R.id.home_welcome);
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

        isAllTimeMenuAcquired = false;
        isDailyMenuAcquired = false;

        dailyMenuViewModel = new ViewModelProvider(this).get(DailyMenuViewModel.class);

        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
        today = menuDay = dateFormat(calendar);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow = dateFormat(calendar);
        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

        if(menuCategoryList == null) {
            dailyMenuViewModel.getCategoryList().observe(getViewLifecycleOwner(), mObserverResult1);
            dailyMenuViewModel.getAllTimeMenu();
        }

        dailyMenuViewModel.getDailyMenuList().observe(getViewLifecycleOwner(), mObserverResult2);
        dailyMenuViewModel.getDailyMenu(menuDay);
        progressDialog.show();

        textViewDate.setOnClickListener(listener);
        top_left_arrow.setOnClickListener(listener);
        top_right_arrow.setOnClickListener(listener);
        top_go_to_today.setOnClickListener(listener);

        return view;
    }

    Observer<MenuCategoryList> mObserverResult1 = new Observer<MenuCategoryList>() {
        @Override
        public void onChanged(@Nullable MenuCategoryList list) {

            menuCategoryList = list;
            isAllTimeMenuAcquired = true;

            if(isAllTimeMenuAcquired && isDailyMenuAcquired) {
                progressDialog.dismiss();
                updatePage();
            }

        }
    };

    Observer<DailyMenuList> mObserverResult2 = new Observer<DailyMenuList>() {
        @Override
        public void onChanged(@Nullable DailyMenuList list) {

            dailyMenuList = list;

            if(dailyMenuList != null)
                isDailyMenuAcquired = true;
            else
                isDailyMenuAcquired = false;

            if(isAllTimeMenuAcquired && isDailyMenuAcquired) {
                progressDialog.dismiss();
                updatePage();
            }

        }
    };

    private void updatePage() {

        profileData = ((MainActivity)getActivity()).getProfileData();

        if(profileData != null) {
            if (profileData.getPicture() != null) {
                menuDrawerImageView.setImageBitmap(BitmapUtils.StringToBitMap(profileData.getPicture()));
            } else {
                menuDrawerImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));
            }
            welcomeMsgTextView.setText("Hello " + profileData.getName() + "!");

            if(ProfileRole.getValue(profileData.getRole()) < ProfileRole.getValue(ProfileRole.MANAGER)) {

                minCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                minCalendar.add(Calendar.DAY_OF_YEAR, -1);

                maxCalendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                maxCalendar.add(Calendar.DAY_OF_YEAR, 7);
            } else {
                minCalendar = maxCalendar = null;
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
        menuTitleTextView.setText("");
        if(dailyMenuList == null || menuCategoryList == null) {
            addMenuTitle("No Menus for this day");
        } else if(dailyMenuList.getMenuList().size() <= 0) {
            addMenuTitle("No Menus for this day");
        } else {
            updateMenuItems(menuCategoryList, dailyMenuList, contentLayout);
        }
    }

    private void updateMenuItems(MenuCategoryList menuCategoryList, DailyMenuList dailyMenuList, LinearLayout contentLayout) {

        boolean isEmpty = true;

        if(dailyMenuList != null && dailyMenuList.getDescription() != null) {
            addMenuTitle(dailyMenuList.getDescription());
        }

        for(MenuCategory list:menuCategoryList.getMenuCategoryList().values()) {

            boolean menuListNotEmpty = false;
            LinearLayout layout = null;
            int countItem = 0;

            for(String element:list.getMenuList().keySet()) {

                MenuElement menuElement = list.getMenuList().get(element);

                if(menuElement.isActive()) {
                    if(dailyMenuList != null && dailyMenuList.getMenuList().containsKey(element)) {
                        if(!menuListNotEmpty) {
                            layout = addMenuCategory(contentLayout, list.getName());
                        }
                        menuListNotEmpty = true;

                        if(layout != null) {
                            countItem ++;
                            addMenuList(layout, menuElement, element, menuListNotEmpty);

                            if(countItem % 5 == 0) {
                                ((MainActivity)getActivity()).getAdsAHT().showNativeAds(this.getContext(), layout);
                            }
                        }

                    }
                    isEmpty = false;
                }
            }

            if(menuListNotEmpty && countItem % 5 != 0) {
                ((MainActivity)getActivity()).getAdsAHT().showNativeAds(this.getContext(), layout);
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
        final TextView textViewCollapse = new TextView(this.getContext());
        final LinearLayout contentLayout = new LinearLayout(this.getContext());

        textView.setText(text);
        textViewCollapse.setText("-");

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewCollapse.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row1Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rowLayout.setGravity(Gravity.CENTER_VERTICAL);
        row1Layout.setGravity(Gravity.END);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textViewCollapse.setGravity(Gravity.CENTER_VERTICAL);

        rowLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        textView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        textViewCollapse.setTextColor(getResources().getColor(R.color.colorWhiteText));
        textView.setTypeface(null, Typeface.BOLD);
        textViewCollapse.setTypeface(null, Typeface.BOLD);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        textViewCollapse.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        rowLayout.addView(textView);
        rowLayout.addView(row1Layout);
        row1Layout.addView(textViewCollapse);
        layout.addView(rowLayout);
        layout.addView(contentLayout);

        rowLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                toggle_contents(textViewCollapse, contentLayout);
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

    private void addMenuList(LinearLayout layout, MenuElement element, String key, boolean selected) {

        String desc = "", price = "";
        Bitmap thumbnail = null;
        LinearLayout rowLayout = new LinearLayout(this.getContext());
        LinearLayout row1Layout = new LinearLayout(this.getContext());
        LinearLayout row2Layout = new LinearLayout(this.getContext());
        LinearLayout row3Layout = new LinearLayout(this.getContext());
        TextView textViewName = new TextView(this.getContext());
        TextView textViewPrice = new TextView(this.getContext());
        TextView textViewDesc = new TextView(this.getContext());
        ImageView imageViewPic = new ImageView(this.getContext());

        if(element.getPrice() >= 0) {
            price = "Price " + "\u20B9" + element.getPrice();
        }
        if(element.getDescription() != null) {
            desc = element.getDescription();
        }
        if(element.getPicture() != null) {
            thumbnail = BitmapUtils.StringToBitMap(element.getPicture());
        }
        textViewName.setText(element.getName());
        textViewPrice.setText(price);
        textViewDesc.setText(desc);

        imageViewPic.setMinimumWidth(200);
        imageViewPic.setMinimumHeight(200);
        imageViewPic.setMaxWidth(700);
        imageViewPic.setMaxHeight(700);
        imageViewPic.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        if(thumbnail != null) {
            //int layout_width = (int)(getView().getWidth() * 0.80f);
            //int image_width = thumbnail.getWidth();
            //imageViewPic.setMinimumWidth(layout_width);
            //imageViewPic.setMaxWidth(layout_width);
            imageViewPic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageViewPic.setAdjustViewBounds(true);
            imageViewPic.setImageBitmap(thumbnail);
            //imageViewPic.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        }


        textViewName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewPrice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textViewName.setPadding(10, 10, 10, 10);
        textViewPrice.setPadding(10, 10, 10, 10);
        textViewDesc.setPadding(10, 10, 10, 10);
        rowLayout.setPadding(10, 10, 10, 10);
        imageViewPic.setPadding(10, 10, 10, 10);

        textViewName.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        textViewPrice.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        textViewDesc.setTextColor(getResources().getColor(R.color.colorSecondaryText));

        textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        textViewPrice.setGravity(Gravity.END);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        rowLayout.setLayoutParams(params);
        row1Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row2Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row3Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageViewPic.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        rowLayout.setOrientation(LinearLayout.VERTICAL);
        row1Layout.setOrientation(LinearLayout.HORIZONTAL);
        row2Layout.setOrientation(LinearLayout.HORIZONTAL);
        row3Layout.setOrientation(LinearLayout.HORIZONTAL);

        row1Layout.addView(textViewName);
        row1Layout.addView(textViewPrice);
        row2Layout.addView(imageViewPic);
        row3Layout.addView(textViewDesc);
        rowLayout.addView(row1Layout);
        rowLayout.addView(row2Layout);
        rowLayout.addView(row3Layout);
        layout.addView(rowLayout);

        rowLayout.setBackground(getResources().getDrawable(R.drawable.layout_bg));
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            //SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
            if(view.getId() == top_left_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this.getViewLifecycleOwner(), mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();

            } else if(view.getId() == top_right_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this.getViewLifecycleOwner(), mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();


            } else if(view.getId() == top_go_to_today.getId()) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                today = menuDay = dateFormat(calendar);

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this.getViewLifecycleOwner(), mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();

            } else if (view.getId() == textViewDate.getId()) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeFragment.this.getContext(), dateListener, calendar
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

            dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this.getViewLifecycleOwner(), mObserverResult2);
            dailyMenuViewModel.getDailyMenu(menuDay);
            progressDialog.show();
        }

    };

    private void toggle_contents(TextView sourceClick, View destView){

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
