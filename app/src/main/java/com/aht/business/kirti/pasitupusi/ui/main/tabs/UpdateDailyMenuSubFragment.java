package com.aht.business.kirti.pasitupusi.ui.main.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.dailymenu.data.MenuCategoryList;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.SubPageFragment;

public class UpdateDailyMenuSubFragment extends SubPageFragment {

    MenuCategoryList menuCategoryList;
    String date;

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

        TextView tv = v.findViewById(R.id.home_welcome);
        tv.setText("Menu for the day (" + date + ")");
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

}