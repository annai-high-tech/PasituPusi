package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.viewallorder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
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

import java.text.DateFormat;
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

public class ViewAllOrderEntrySubFragment extends BaseFragment {


    private Map<String, OrderData> orderDataList = new HashMap<>();

    private Button buttonOrderList;
    private Button buttonDishesCount;

    public static ViewAllOrderEntrySubFragment newInstance() {
        Bundle args = new Bundle();
        //args.putBoolean("isNewObject", isNewObject);
        ViewAllOrderEntrySubFragment f = new ViewAllOrderEntrySubFragment();
        f.setArguments(args);
        //f.isNewObject = isNewObject;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_allorder_entry, container, false);

        buttonOrderList =  view.findViewById(R.id.buttonOrderList);
        buttonDishesCount =  view.findViewById(R.id.buttonDishesCount);

        buttonOrderList.setOnClickListener(listener);
        buttonDishesCount.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ViewAllOrderEntrySubFragmentArgs args = ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments());

        ArrayList<DishOrderData> dishList = args.getDishList();

        if(dishList == null || dishList.size() <= 0) {
            buttonOrderList.setVisibility(View.GONE);
            buttonDishesCount.setVisibility(View.GONE);
        }
    }


    private View.OnClickListener listener        =   new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("dishList", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
            bundle.putSerializable("breakFastCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
            bundle.putSerializable("lunchCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
            bundle.putSerializable("dinnerCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());
            bundle.putParcelableArrayList("orderList", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getOrderList());

            if(view.getId() == buttonDishesCount.getId()) {
                /*NavDirections action =
                        ViewAllOrderEntrySubFragmentDirections
                                .actionViewAllOrderEntryToDishesCount();
                Navigation.findNavController(view).navigate(action);*/
                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderEntry_to_DishesCount, bundle);
            }

            if(view.getId() == buttonOrderList.getId()) {
                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderEntry_to_OrderList, bundle);
            }
        }
    };
}
