package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.viewallorder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ViewAllOrderDishesCountFragment extends BaseFragment {

    private TableLayout tableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_view_allorder_dishescount, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);

        Button buttonBack =  view.findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dishList", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
                bundle.putSerializable("breakFastCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
                bundle.putSerializable("lunchCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
                bundle.putSerializable("dinnerCount", ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());

                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderDishesCount_to_Entry, bundle);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ViewAllOrderEntrySubFragmentArgs args = ViewAllOrderEntrySubFragmentArgs.fromBundle(getArguments());

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
        View view = inflater.inflate(R.layout.component_order_summary_row, null);

        layout.addView(view, layout.getChildCount());

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewQuantity = view.findViewById(R.id.textViewQuantity);
        TextView textViewTotal = view.findViewById(R.id.textViewCost);

        textViewName.setText(dishName);
        textViewPrice.setText(String.valueOf(breakfastQuantity));
        textViewQuantity.setText(String.valueOf(lunchQuantity));
        textViewTotal.setText(String.valueOf(dinnerQuantity));

    }

}
