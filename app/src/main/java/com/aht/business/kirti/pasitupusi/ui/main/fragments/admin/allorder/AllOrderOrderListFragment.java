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
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.util.ArrayList;

public class AllOrderOrderListFragment extends BaseFragment {

    private TableLayout tableLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_all_order_orderlist, container, false);

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

                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderOrderList_to_Entry, bundle);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        AllOrderEntrySubFragmentArgs args = AllOrderEntrySubFragmentArgs.fromBundle(getArguments());

        ArrayList<OrderData> orderList = args.getOrderList();

        if(orderList != null && orderList.size() > 0) {

            for(OrderData orderData: orderList) {
                addOrderSummary(tableLayout, orderData);
            }
        }
    }

    private void addOrderSummary(final TableLayout layout, final OrderData orderData) {

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.component_all_order_orderlist_row, null);

        layout.addView(view, layout.getChildCount());

        TextView textViewOrderID = view.findViewById(R.id.textViewOrderID);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewPhone = view.findViewById(R.id.textViewPhone);
        TextView textViewCost = view.findViewById(R.id.textViewCost);

        textViewOrderID.setText(orderData.getOrderId());
        textViewName.setText(orderData.getDate());
        textViewPhone.setText(orderData.getDate());
        textViewCost.setText(String.valueOf(orderData.getTotalCost()));

        textViewOrderID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dishList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
                bundle.putSerializable("breakFastCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
                bundle.putSerializable("lunchCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
                bundle.putSerializable("dinnerCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());
                bundle.putParcelableArrayList("orderList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getOrderList());
                bundle.putParcelable("orderData", orderData);

                Navigation.findNavController(view).navigate(R.id.action_viewAllOrderOrderList_to_OneOrder, bundle);
            }
        });

    }

}
