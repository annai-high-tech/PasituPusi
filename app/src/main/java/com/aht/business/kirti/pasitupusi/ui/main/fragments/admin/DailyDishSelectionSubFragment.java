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
    private MenuCategoryList menuCategoryList;
    private String date;


    public DailyDishSelectionSubFragment(MenuCategoryList menuCategoryList, String date) {
        this.menuCategoryList = menuCategoryList;
        this.date = date;
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
        textViewTitle = v.findViewById(R.id.home_welcome);
        contentLayout =  v.findViewById(R.id.content_layout);

        textViewTitle.setText("Menu for the day (" + date + ")");

        dailyMenuViewModel = new ViewModelProvider(this).get(DailyMenuViewModel.class);
        dailyMenuViewModel.getDailyMenuList().observe(getViewLifecycleOwner(), mObserverResult);
        dailyMenuViewModel.getDailyMenu(date);
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

    private void updatePage(MenuCategoryList menuCategoryList, DailyMenuList dailyMenuList, LinearLayout contentLayout) {

        boolean isEmpty = true;
        boolean firstTime = true;
        boolean isOldDate = isOldDate(date);
        contentLayout.removeAllViewsInLayout();
        save = reset = null;

        for(MenuCategory list:menuCategoryList.getMenuCategoryList().values()) {
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
        }
    }

    private boolean isOldDate(String date) {

        String[] dateSplit = date.split("-");

        if(dateSplit.length != 3) {
            return true;
        }

        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; //Calendar.JANUARY;
        int curDate = calendar.get(Calendar.DAY_OF_MONTH);

        if(currentYear > Integer.parseInt(dateSplit[0])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth > Integer.parseInt(dateSplit[1])) {
            return true;
        }
        if(currentYear == Integer.parseInt(dateSplit[0])
                && currentMonth == Integer.parseInt(dateSplit[1])
                && curDate > Integer.parseInt(dateSplit[2])) {
            return true;
        }

        return false;

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

    Observer<DailyMenuList> mObserverResult = new Observer<DailyMenuList>() {
        @Override
        public void onChanged(@Nullable DailyMenuList list) {

            dailyMenuList = list;

            if(dailyMenuList != null) {
                updatePage(menuCategoryList, dailyMenuList, contentLayout);

                progressDialog.dismiss();
            }

        }
    };

    private View.OnClickListener buttonListener =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(((Button)view).getText().equals("Save")) {
                dailyMenuViewModel.updateDailyMenu(date, dailyMenuList);
            }

            dailyMenuViewModel.getDailyMenu(date);
            save.setEnabled(false);
            reset.setEnabled(false);

        }
    };

    private CompoundButton.OnCheckedChangeListener checkBoxListener =   new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Map<String, DailyMenu> menuList = dailyMenuList.getMenuList();
            String key = buttonView.getText().toString();

            if(isChecked && !menuList.containsKey(key)) {
                menuList.put(key, new DailyMenu(key));
                save.setEnabled(true);
                reset.setEnabled(true);

            } else if(!isChecked && menuList.containsKey(key)) {
                menuList.remove(key);
                save.setEnabled(true);
                reset.setEnabled(true);
            }
        }

    };

}