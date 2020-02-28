package com.aht.business.kirti.pasitupusi.ui.main.fragments.admin.allorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllOrderEntrySubFragment extends BaseFragment {


    private Map<String, OrderData> orderDataList = new HashMap<>();

    private Button buttonOrderList;
    private Button buttonDishesCount;

    public static AllOrderEntrySubFragment newInstance() {
        Bundle args = new Bundle();
        //args.putBoolean("isNewObject", isNewObject);
        AllOrderEntrySubFragment f = new AllOrderEntrySubFragment();
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

        View view = inflater.inflate(R.layout.fragment_all_order_entry, container, false);

        buttonOrderList =  view.findViewById(R.id.buttonOrderList);
        buttonDishesCount =  view.findViewById(R.id.buttonDishesCount);

        buttonOrderList.setOnClickListener(listener);
        buttonDishesCount.setOnClickListener(listener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        AllOrderEntrySubFragmentArgs args = AllOrderEntrySubFragmentArgs.fromBundle(getArguments());

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
            bundle.putParcelableArrayList("dishList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDishList());
            bundle.putSerializable("breakFastCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getBreakFastCount());
            bundle.putSerializable("lunchCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getLunchCount());
            bundle.putSerializable("dinnerCount", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getDinnerCount());
            bundle.putParcelableArrayList("orderList", AllOrderEntrySubFragmentArgs.fromBundle(getArguments()).getOrderList());

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
