package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.allorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AllOrderDishesCountFragment extends BaseFragment {

    private TableLayout tableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_all_order_dishescount, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);

        Button buttonBack =  view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dishList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
                bundle.putSerializable("breakFastCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
                bundle.putSerializable("lunchCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
                bundle.putSerializable("dinnerCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());
                bundle.putParcelableArrayList("orderList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getOrderList());

                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderDishesCount_to_Entry, bundle);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        AllOrderEntrySubFragmentArgs args = AllOrderEntrySubFragmentArgs.fromBundle(getArguments());

        ArrayList<DishOrderData> dishList = args.getDishList();
        HashMap<String, Integer> breakFastCount = args.getBreakFastCount();
        HashMap<String, Integer> lunchCount = args.getLunchCount();
        HashMap<String, Integer> dinnerCount = args.getDinnerCount();

        if(dishList != null && dishList.size() > 0) {

            Collections.sort(dishList, new Comparator<DishOrderData>() {
                @Override
                public int compare(DishOrderData o1, DishOrderData o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });

            for(DishOrderData orderData: dishList) {
                addOrderSummary(tableLayout, orderData.getName(), breakFastCount.get(orderData.getId()), lunchCount.get(orderData.getId()), dinnerCount.get(orderData.getId()));
            }
        }
    }

    private void addOrderSummary(TableLayout layout, String dishName, int breakfastQuantity, int lunchQuantity, int dinnerQuantity) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_all_order_dishescount_row, null);

        layout.addView(view, layout.getChildCount());

        TextView textViewDishName = view.findViewById(R.id.textViewDishName);
        TextView textViewBFQuantity = view.findViewById(R.id.textViewBFQuantity);
        TextView textViewLQuantity = view.findViewById(R.id.textViewLQuantity);
        TextView textViewDQuantity = view.findViewById(R.id.textViewDQuantity);

        textViewDishName.setText(dishName);
        textViewBFQuantity.setText(String.valueOf(breakfastQuantity));
        textViewLQuantity.setText(String.valueOf(lunchQuantity));
        textViewDQuantity.setText(String.valueOf(dinnerQuantity));

    }

}
