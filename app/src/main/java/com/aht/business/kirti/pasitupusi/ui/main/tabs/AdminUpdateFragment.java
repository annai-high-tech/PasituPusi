package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.DailyMenuViewModel;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

import java.util.Calendar;
import java.util.TimeZone;

public class AdminUpdateFragment extends BaseFragment {

    private static final String MONTH_YEAR_SEPERATOR = " ";

    private TextView textViewMonth;
    private ImageView top_left_arrow, top_right_arrow, top_go_to_today;

    private Calendar calendar;
    private ProgressDialog progressDialog;

    private DailyMenuViewModel dailyMenuViewModel;
    private MenuCategoryList menuCategoryList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_update, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        textViewMonth =  view.findViewById(R.id.top_month_value);
        top_left_arrow =  view.findViewById(R.id.top_left_arrow);
        top_right_arrow =  view.findViewById(R.id.top_right_arrow);
        top_go_to_today =  view.findViewById(R.id.top_go_to_today);

        dailyMenuViewModel = ViewModelProviders.of(getActivity()).get(DailyMenuViewModel.class);
        dailyMenuViewModel.getCategoryList().observe(getActivity(), mObserverResult);
        dailyMenuViewModel.getAllTimeMenu();

        progressDialog.show();

        calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH); //Calendar.JANUARY;
        //currentMonth = Calendar.FEBRUARY;
        int curDate = calendar.get(Calendar.DAY_OF_MONTH);

        updateCalendar(view, currentYear, currentMonth);

        top_left_arrow.setOnClickListener(listener);
        top_right_arrow.setOnClickListener(listener);
        top_go_to_today.setOnClickListener(listener);

        return view;
    }


    private void updateCalendar(View view, int currentYear, int currentMonth) {

        Calendar newCalendar = Calendar.getInstance(TimeZone.getDefault());
        newCalendar.set(currentYear, currentMonth, 1);

        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_WEEK);
        int lastDate = newCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int curDate = 0;

        if(month == calendar.get(Calendar.MONTH)
                && year == calendar.get(Calendar.YEAR)) {
            curDate = calendar.get(Calendar.DAY_OF_MONTH);
        }

        textViewMonth.setText(getMonth(month) + MONTH_YEAR_SEPERATOR + year);

        updateDates(view, day, lastDate, curDate);
    }

    private String getMonth(int month) {

        if(month == Calendar.JANUARY)
            return getResources().getString(R.string.january);
        else if(month == Calendar.FEBRUARY)
            return getResources().getString(R.string.february);
        else if(month == Calendar.MARCH)
            return getResources().getString(R.string.march);
        else if(month == Calendar.APRIL)
            return getResources().getString(R.string.april);
        else if(month == Calendar.MAY)
            return getResources().getString(R.string.may);
        else if(month == Calendar.JUNE)
            return getResources().getString(R.string.june);
        else if(month == Calendar.JULY)
            return getResources().getString(R.string.july);
        else if(month == Calendar.AUGUST)
            return getResources().getString(R.string.august);
        else if(month == Calendar.SEPTEMBER)
            return getResources().getString(R.string.september);
        else if(month == Calendar.OCTOBER)
            return getResources().getString(R.string.october);
        else if(month == Calendar.NOVEMBER)
            return getResources().getString(R.string.november);
        else if(month == Calendar.DECEMBER)
            return getResources().getString(R.string.december);

        return "";
    }

    private int getMonthInt(String month) {

        if(month.equals(getResources().getString(R.string.january)))
            return Calendar.JANUARY;
        else if(month.equals(getResources().getString(R.string.february)))
            return Calendar.FEBRUARY;
        else if(month.equals(getResources().getString(R.string.march)))
            return Calendar.MARCH;
        else if(month.equals(getResources().getString(R.string.april)))
            return Calendar.APRIL;
        else if(month.equals(getResources().getString(R.string.may)))
            return Calendar.MAY;
        else if(month.equals(getResources().getString(R.string.june)))
            return Calendar.JUNE;
        else if(month.equals(getResources().getString(R.string.july)))
            return Calendar.JULY;
        else if(month.equals(getResources().getString(R.string.august)))
            return Calendar.AUGUST;
        else if(month.equals(getResources().getString(R.string.september)))
            return Calendar.SEPTEMBER;
        else if(month.equals(getResources().getString(R.string.october)))
            return Calendar.OCTOBER;
        else if(month.equals(getResources().getString(R.string.november)))
            return Calendar.NOVEMBER;
        else if(month.equals(getResources().getString(R.string.december)))
            return Calendar.DECEMBER;

        return 0;
    }

    private void updateDates(View view, int startDay, int endDate, int dateSelect) {

        boolean start = false, end = false;

        for(int i = 1; i <= 42; i++) {
            View viewById = view.findViewById(getResources().getIdentifier("day_" + i, "id", this.getContext().getPackageName()));

            if(i == startDay) start = true;

            ((LinearLayout)viewById).removeAllViewsInLayout();
            viewById.setOnClickListener(null);

            if(viewById != null && start && !end) {

                int date = i - startDay + 1;
                TextView textView = new TextView(this.getContext());
                textView.setText("" + date);

                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(getResources().getColor(R.color.default_text_color));

                ((LinearLayout)viewById).addView(textView);

                if(date == dateSelect) {
                    textView.setTextColor(getResources().getColor(R.color.date_sel_text_color));
                }
                textSelection(textView, date == dateSelect);

                viewById.setOnClickListener(menuDaylistener);

                if(date == endDate) end = true;
            }


        }
    }

    private void textSelection(TextView textView, boolean isSelected) {

        if(isSelected) {
            textView.setBackground(getResources().getDrawable(R.drawable.calendar_circle_text));
        } else {
            textView.setBackground(new TextView(this.getContext()).getBackground());
        }
    }

    private View.OnClickListener listener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            if(view.getId() == top_left_arrow.getId()) {

                String[] month_year = textViewMonth.getText().toString().split(MONTH_YEAR_SEPERATOR);
                int currentYear = Integer.parseInt(month_year[1]);
                int currentMonth = getMonthInt(month_year[0]);

                if(currentMonth == Calendar.JANUARY) {
                    currentYear = currentYear - 1;
                    currentMonth = Calendar.DECEMBER;
                } else {
                    currentMonth--;
                }

                updateCalendar(AdminUpdateFragment.this.getView(), currentYear, currentMonth);

            } else if(view.getId() == top_right_arrow.getId()) {

                String[] month_year = textViewMonth.getText().toString().split(MONTH_YEAR_SEPERATOR);
                int currentYear = Integer.parseInt(month_year[1]);
                int currentMonth = getMonthInt(month_year[0]);

                if(currentMonth == Calendar.DECEMBER) {
                    currentYear = currentYear + 1;
                    currentMonth = Calendar.JANUARY;
                } else {
                    currentMonth++;
                }

                updateCalendar(AdminUpdateFragment.this.getView(), currentYear, currentMonth);

            } else if(view.getId() == top_go_to_today.getId()) {

                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH); //Calendar.JANUARY;

                updateCalendar(AdminUpdateFragment.this.getView(), currentYear, currentMonth);

            }

        }
    };

    //static int aaaaaaaaaa = 0;
    private View.OnClickListener menuDaylistener        =   new View.OnClickListener(){
        @Override
        public void onClick(View view){

            //aaaaaaaaaa++;

            ((LinearLayout)view).removeViews(1, ((LinearLayout)view).getChildCount() - 1);

            /*if(aaaaaaaaaa == 1) {
                addTextView(view, "Idly");
                addTextView(view, "Dosa");
                addTextView(view, "Biriyani");
                addTextView(view, "Vatha Kulambu");
            } else if(aaaaaaaaaa == 2) {
                addTextView(view, "Puri");
                addTextView(view, "Meals");
                addTextView(view, "Kara Kulambu");
            }else if(aaaaaaaaaa == 3) {
                aaaaaaaaaa = 0;
            }*/

            showDialog((LinearLayout)view);
        }

        private void addTextView(View view, String text) {
            TextView textView = new TextView(AdminUpdateFragment.this.getContext());
            textView.setText(text);

            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.date_sel_text_color));

            ((LinearLayout)view).addView(textView);

        }
    };

    private void showDialog(LinearLayout layout) {

        String date = "";
        int d = Integer.parseInt(((TextView)layout.getChildAt(0)).getText().toString());
        int month = getMonthInt(textViewMonth.getText().toString().split(MONTH_YEAR_SEPERATOR)[0]) + 1;
        String year = textViewMonth.getText().toString().split(MONTH_YEAR_SEPERATOR)[1];

        date = year;

        if(month > 9)
            date = date + "-" + month;
        else
            date = date + "-0" + month;

        if(d > 9)
            date = date + "-" + d;
        else
            date = date + "-0" + d;

        // Create and show the dialog.
        UpdateDailyMenuSubFragment newFragment = new UpdateDailyMenuSubFragment(menuCategoryList, date);

        ((MainActivity)getActivity()).changeFragments(newFragment);
        //newFragment.show(ft, "dialog");
    }

    Observer<MenuCategoryList> mObserverResult = new Observer<MenuCategoryList>() {
        @Override
        public void onChanged(@Nullable MenuCategoryList menuCategory) {


            menuCategoryList = menuCategory;
            progressDialog.dismiss();

       }
    };


}
