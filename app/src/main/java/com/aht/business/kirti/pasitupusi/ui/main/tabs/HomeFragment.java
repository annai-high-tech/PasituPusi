package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomeFragment extends BaseFragment {

    private TextView textViewWelcomeMsg;
    private LinearLayout contentLayout;
    private TextView textViewDate;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;

    private ProgressDialog progressDialog;
    private Calendar calendar;
    private String menuDay, today, tomorrow;

    private DailyMenuViewModel dailyMenuViewModel;
    private MenuCategoryList menuCategoryList = null;
    private DailyMenuList dailyMenuList = null;

    private boolean isAllTimeMenuAcquired, isDailyMenuAcquired;

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
        textViewDate =  view.findViewById(R.id.top_date_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);
        contentLayout =  view.findViewById(R.id.content_layout);

        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi Menu");

        isAllTimeMenuAcquired = false;
        isDailyMenuAcquired = false;

        dailyMenuViewModel = ViewModelProviders.of(getActivity()).get(DailyMenuViewModel.class);

        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
        today = menuDay = simpleDate.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        tomorrow = simpleDate.format(calendar.getTime());
        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

        if(menuCategoryList == null) {
            dailyMenuViewModel.getCategoryList().observe(getActivity(), mObserverResult1);
            dailyMenuViewModel.getAllTimeMenu();
        }

        dailyMenuViewModel.getDailyMenuList().observe(this, mObserverResult2);
        dailyMenuViewModel.getDailyMenu(menuDay);
        progressDialog.show();

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

            if(isAllTimeMenuAcquired && isDailyMenuAcquired) {
                progressDialog.dismiss();
                updatePage();
            }

        }
    };

    private void updatePage() {

        textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi");

        SimpleDateFormat i_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat o_format = new SimpleDateFormat("dd MMM yyyy");
        String dateTitle = "";
        try {
            Date curDate = i_format.parse(menuDay);
            dateTitle = o_format.format(curDate);

            if(menuDay.equals(today)) {
                dateTitle += " (Today)";
            }
            if(menuDay.equals(tomorrow)) {
                dateTitle += " (Tomorrow)";
            }
        } catch (ParseException e) {
            dateTitle = "No Dates";
        }
        textViewDate.setText(dateTitle);

        contentLayout.removeAllViewsInLayout();
        if(dailyMenuList == null || menuCategoryList == null) {
            addMenuCategory(contentLayout, "No Menus for the day");
        } else if(dailyMenuList.getMenuList().size() <= 0) {
            addMenuCategory(contentLayout, "No Menus for the day");
        } else {
            updateMenuItems(menuCategoryList, dailyMenuList, contentLayout);
        }
    }

    private void updateMenuItems(MenuCategoryList menuCategoryList, DailyMenuList dailyMenuList, LinearLayout contentLayout) {

        boolean isEmpty = true;
        contentLayout.removeAllViewsInLayout();

        if(dailyMenuList != null && dailyMenuList.getDescription() != null) {
            addMenuTitle(contentLayout, dailyMenuList.getDescription());
        }

        for(MenuCategory list:menuCategoryList.getMenuCategoryList().values()) {

            boolean menuListNotEmpty = false;

            for(String element:list.getMenuList().keySet()) {

                MenuElement menuElement = list.getMenuList().get(element);

                if(menuElement.isActive()) {
                    if(dailyMenuList != null && dailyMenuList.getMenuList().containsKey(element)) {
                        if(!menuListNotEmpty) {
                            addMenuCategory(contentLayout, list.getName());
                        }
                        menuListNotEmpty = true;
                        addMenuList(contentLayout, menuElement, element, menuListNotEmpty);
                    }
                    isEmpty = false;
                }
            }
        }

    }


    private void addMenuTitle(LinearLayout layout, String text) {
        TextView textView = new TextView(this.getContext());
        textView.setText(text);

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        //textView.setTextColor(getResources().getColor(R.color.date_sel_text_color));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);

        layout.addView(textView);

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

    private void addMenuList(LinearLayout layout, MenuElement element, String key, boolean selected) {

        String desc = "", price = "";
        LinearLayout rowLayout = new LinearLayout(this.getContext());
        LinearLayout row1Layout = new LinearLayout(this.getContext());
        LinearLayout row2Layout = new LinearLayout(this.getContext());
        LinearLayout row3Layout = new LinearLayout(this.getContext());
        TextView textViewName = new TextView(this.getContext());
        TextView textViewPrice = new TextView(this.getContext());
        TextView textViewDesc = new TextView(this.getContext());

        if(element.getPrice() >= 0) {
            price = "Price " + "\u20B9" + element.getPrice();
        }
        if(element.getDescription() != null) {
            desc = element.getDescription();
        }
        textViewName.setText(element.getName());
        textViewPrice.setText(price);
        textViewDesc.setText(desc);


        textViewName.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewPrice.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textViewDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        textViewName.setPadding(10, 0, 0, 0);
        textViewPrice.setPadding(10, 0, 0, 0);
        textViewDesc.setPadding(10, 0, 0, 0);
        //textViewName.setGravity(GravityView view, .CENTER);
        //textViewName.setTextColor(getResources().getColor(R.color.date_sel_text_color));
        //textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);

        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row1Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row2Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row3Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rowLayout.setOrientation(LinearLayout.VERTICAL);
        row1Layout.setOrientation(LinearLayout.HORIZONTAL);
        row2Layout.setOrientation(LinearLayout.HORIZONTAL);
        row3Layout.setOrientation(LinearLayout.HORIZONTAL);

        row1Layout.addView(textViewName);
        row1Layout.addView(textViewPrice);
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

            SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd");
            if(view.getId() == top_left_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                menuDay = simpleDate.format(calendar.getTime());

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this, mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();

            } else if(view.getId() == top_right_arrow.getId()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                menuDay = simpleDate.format(calendar.getTime());

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this, mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();


            } else if(view.getId() == top_go_to_today.getId()) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));
                today = menuDay = simpleDate.format(calendar.getTime());

                dailyMenuViewModel.getDailyMenuList().observe(HomeFragment.this, mObserverResult2);
                dailyMenuViewModel.getDailyMenu(menuDay);
                progressDialog.show();

            }

        }
    };


}
