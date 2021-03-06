package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenu;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElementList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuTime;
import com.aht.business.kirti.pasitupusi.model.utils.DateUtils;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.SubPageFragment;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class DailyDishSelectionSubFragment extends SubPageFragment {

    private TextView textViewTitle;
    private LinearLayout contentLayout;
    private ProgressDialog progressDialog;
    private Button save, reset;

    private Calendar calendar;


    private DailyMenuViewModel dailyMenuViewModel;
    private DailyMenuList dailyMenuList;
    private MenuElementList menuElementList;
    private String date;

    public DailyDishSelectionSubFragment() {
        super("Daily Dish Selection");
    }

    public static DailyDishSelectionSubFragment newInstance(MenuElementList menuElementList, String date) {
        Bundle args = new Bundle();
        DailyDishSelectionSubFragment f = new DailyDishSelectionSubFragment();
        f.setArguments(args);
        f.menuElementList = menuElementList;
        f.date = date;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dish_selection_daily, container, false);

        calendar = Calendar.getInstance(TimeZone.getTimeZone(getResources().getString(R.string.timezone)));

        progressDialog = new ProgressDialog(this.getContext());
        textViewTitle = v.findViewById(R.id.titleTextView);
        contentLayout =  v.findViewById(R.id.content_layout);

        textViewTitle.setText("Menu for the day (" + date + ")");

        dailyMenuViewModel = new ViewModelProvider(this).get(DailyMenuViewModel.class);
        dailyMenuViewModel.getDailyMenu(date).observe(getViewLifecycleOwner(), mObserverResult1);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        /*((TextView)tv).setText("Dialog #" + mNum + ": using style "
                + getNameForNum(mNum));

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((FragmentDialog)getActivity()).showDialog();
            }
        });*/

        return v;
    }

    private void updatePage(MenuElementList menuElementList, DailyMenuList dailyMenuList, LinearLayout contentLayout) {

        boolean isEmpty = true;
        boolean firstTime = true;
        boolean isOldDate = DateUtils.isOldDate(date, calendar);
        contentLayout.removeAllViewsInLayout();
        save = reset = null;

        String menuCategory = "";

        for(MenuElement menuElement:menuElementList.getMenuElementList()) {
            if(firstTime && dailyMenuList != null) {
                firstTime = false;
                addMenuDescription(contentLayout, dailyMenuList.getDescription(), isOldDate);
            }

            if(!menuCategory.equals(menuElement.getCategory().getId())) {
                addMenuCategory(contentLayout, menuElement.getCategory().getId());
            }
            menuCategory = menuElement.getCategory().getId();

            if(menuElement.isActive()) {
                boolean selected = false;

                if(dailyMenuList != null && dailyMenuList.getMenuList().containsKey(menuElement.getId())) {
                    selected = true;
                }
                addMenuList(contentLayout, menuElement, menuElement.getId(), selected, isOldDate);
                isEmpty = false;
            }

        }

        if(!isEmpty && !isOldDate) {
            addMenuButtons(contentLayout);
        }
    }

    private void addMenuDescription(LinearLayout layout, String text, boolean isOldDate) {
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

    private void addMenuList(final LinearLayout layout, final MenuElement element, final String key, final boolean selected, final boolean isOldDate) {

        final LinearLayout rowLayout = new LinearLayout(this.getContext());
        final CheckBox cb = new CheckBox(this.getContext());
        final TextView textView = new TextView(this.getContext());
        final CheckBox cb1 = new CheckBox(this.getContext());
        final CheckBox cb2 = new CheckBox(this.getContext());
        final CheckBox cb3 = new CheckBox(this.getContext());

        cb.setText(key);
        textView.setText(element.getName());
        cb1.setText("Breakfast");
        cb2.setText("Lunch");
        cb3.setText("Dinner");

        if(selected) {
            MenuTime menuTime = dailyMenuList.getMenuList().get(key).getMenuTime();
            cb1.setChecked(menuTime.isBreakfast());
            cb2.setChecked(menuTime.isLunch());
            cb3.setChecked(menuTime.isDinner());
        }

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, DailyMenu> menuList = dailyMenuList.getMenuList();
                String key = buttonView.getText().toString();

                if(isChecked && !menuList.containsKey(key)) {
                    menuList.put(key, new DailyMenu(key, element, new MenuTime(cb1.isChecked(), cb2.isChecked(), cb3.isChecked())));
                    save.setEnabled(true);
                    reset.setEnabled(true);

                } else if(!isChecked && menuList.containsKey(key)) {
                    menuList.remove(key);
                    save.setEnabled(true);
                    reset.setEnabled(true);
                }

                if(isChecked) {
                    cb1.setEnabled(true);
                    cb2.setEnabled(true);
                    cb3.setEnabled(true);
                } else {
                    cb1.setEnabled(false);
                    cb2.setEnabled(false);
                    cb3.setEnabled(false);
                }
            }
        });

        class MenuTimeListener implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Map<String, DailyMenu> menuList = dailyMenuList.getMenuList();
                String key = cb.getText().toString();

                if(cb.isChecked() && menuList.containsKey(key)) {
                    MenuTime menuTime = menuList.get(key).getMenuTime();
                    menuTime.setBreakfast(cb1.isChecked());
                    menuTime.setLunch(cb2.isChecked());
                    menuTime.setDinner(cb3.isChecked());
                    save.setEnabled(true);
                    reset.setEnabled(true);
                }

            }
        }

        cb1.setOnCheckedChangeListener(new MenuTimeListener());
        cb2.setOnCheckedChangeListener(new MenuTimeListener());
        cb3.setOnCheckedChangeListener(new MenuTimeListener());

        cb.setChecked(selected);
        cb.setEnabled(!isOldDate);

        cb1.setEnabled(selected);
        cb2.setEnabled(selected);
        cb3.setEnabled(selected);

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
        rowLayout.addView(cb1);
        rowLayout.addView(cb2);
        rowLayout.addView(cb3);
        layout.addView(rowLayout);

    }

    Observer<DailyMenuList> mObserverResult1 = new Observer<DailyMenuList>() {
        @Override
        public void onChanged(@Nullable DailyMenuList list) {

            dailyMenuList = list;

            if(dailyMenuList != null) {
                updatePage(menuElementList, dailyMenuList, contentLayout);

                progressDialog.dismiss();
            }

        }
    };

    Observer<DailyMenuList> mObserverResult2 = new Observer<DailyMenuList>() {
        @Override
        public void onChanged(@Nullable DailyMenuList list) {

            if(list != null) {

                dailyMenuViewModel.getDailyMenu(date).observe(getViewLifecycleOwner(), mObserverResult1);

            } else {

                updatePage(menuElementList, dailyMenuList, contentLayout);

                progressDialog.dismiss();
            }

        }
    };

    private View.OnClickListener buttonListener =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(((Button)view).getText().equals("Save")) {

                progressDialog.setMessage("Loading...");
                progressDialog.show();
                dailyMenuViewModel.updateDailyMenu(date, dailyMenuList).observe(getViewLifecycleOwner(), mObserverResult2);

            } else {

                dailyMenuViewModel.getDailyMenu(date).observe(getViewLifecycleOwner(), mObserverResult1);
            }


            save.setEnabled(false);
            reset.setEnabled(false);

        }
    };

}