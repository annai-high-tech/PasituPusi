package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenu;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.DailyMenuList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategory;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuElement;

import java.util.Map;

public class UpdateDailyMenuSubFragment extends SubPageFragment {

    private TextView textViewTitle;
    private LinearLayout contentLayout;
    private ProgressDialog progressDialog;

    private DailyMenuViewModel dailyMenuViewModel;
    private DailyMenuList dailyMenuList;
    private MenuCategoryList menuCategoryList;
    private String date;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public UpdateDailyMenuSubFragment(MenuCategoryList menuCategoryList, String date) {
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

        View v = inflater.inflate(R.layout.fragment_menu_update, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        textViewTitle = v.findViewById(R.id.home_welcome);
        contentLayout =  v.findViewById(R.id.content_layout);

        textViewTitle.setText("Menu for the day (" + date + ")");

        dailyMenuViewModel = ViewModelProviders.of(getActivity()).get(DailyMenuViewModel.class);
        dailyMenuViewModel.getDailyMenuList().observe(this, mObserverResult);
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
        contentLayout.removeAllViewsInLayout();

        for(MenuCategory list:menuCategoryList.getMenuCategoryList().values()) {
            addMenuCategory(contentLayout, list.getName());

            for(String element:list.getMenuList().keySet()) {

                MenuElement menuElement = list.getMenuList().get(element);

                if(menuElement.isActive()) {
                    boolean selected = false;

                    if(dailyMenuList != null && dailyMenuList.getMenuList().containsKey(element)) {
                        selected = true;
                    }
                    addMenuList(contentLayout, menuElement, element, selected);
                    isEmpty = false;
                }
            }
        }

        if(!isEmpty) {
            addMenuButtons(contentLayout);
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

    private void addMenuButtons(LinearLayout layout) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        Button save = new Button(this.getContext());
        Button reset = new Button(this.getContext());

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

    }

    private void addMenuList(LinearLayout layout, MenuElement element, String key, boolean selected) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        CheckBox cb = new CheckBox(this.getContext());
        TextView textView = new TextView(this.getContext());

        cb.setText(key);
        textView.setText(element.getName());

        cb.setOnCheckedChangeListener(checkBoxListener);
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
        }
    };

    private CompoundButton.OnCheckedChangeListener checkBoxListener =   new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Map<String, DailyMenu> menuList = dailyMenuList.getMenuList();
            String key = buttonView.getText().toString();

            if(isChecked && !menuList.containsKey(key)) {
                menuList.put(key, new DailyMenu(key));
            } else if(!isChecked && menuList.containsKey(key)) {
                menuList.remove(key);
            }
        }

    };

}