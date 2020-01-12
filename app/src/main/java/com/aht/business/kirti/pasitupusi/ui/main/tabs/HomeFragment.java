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

import java.util.Calendar;
import java.util.TimeZone;

public class HomeFragment extends BaseFragment {

    private TextView textViewWelcomeMsg;
    private LinearLayout contentLayout;

    private ProgressDialog progressDialog;
    private Calendar calendar;

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

        calendar = Calendar.getInstance(TimeZone.getDefault());

        progressDialog = new ProgressDialog(this.getContext());
        textViewWelcomeMsg =  view.findViewById(R.id.home_welcome);
        contentLayout =  view.findViewById(R.id.content_layout);

        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tWelcome to Pasitu Pusi Menu");

        isAllTimeMenuAcquired = false;
        isDailyMenuAcquired = false;

        dailyMenuViewModel = ViewModelProviders.of(getActivity()).get(DailyMenuViewModel.class);

        String today = getDateString(calendar);

        if(menuCategoryList == null) {
            dailyMenuViewModel.getCategoryList().observe(getActivity(), mObserverResult1);
            dailyMenuViewModel.getAllTimeMenu();
        }

        if(dailyMenuList == null) {
            dailyMenuViewModel.getDailyMenuList().observe(this, mObserverResult2);
            dailyMenuViewModel.getDailyMenu(today);
            progressDialog.show();
        }

        return view;
    }

    private String getDateString(Calendar calendar) {

        String date = "";
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        date = String.valueOf(year);

        if(month > 9)
            date = date + "-" + month;
        else
            date = date + "-0" + month;

        if(d > 9)
            date = date + "-" + d;
        else
            date = date + "-0" + d;


        return date;
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

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        CheckBox cb = new CheckBox(this.getContext());
        TextView textView = new TextView(this.getContext());

        cb.setText(key);
        textView.setText(element.getName());

        cb.setChecked(selected);

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



}
