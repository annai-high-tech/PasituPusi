package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.order.OrderViewModel;
import com.aht.business.kirti.pasitupusi.model.order.data.DishOrderData;
import com.aht.business.kirti.pasitupusi.model.order.data.OrderData;
import com.aht.business.kirti.pasitupusi.model.utils.AnimationUtil;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrackOrderFragment extends BaseFragment {

    private TextView menuTitleTextView;
    private LinearLayout contentLayout;

    private OrderViewModel orderViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_track_order, container, false);

        menuTitleTextView = view.findViewById(R.id.titleTextView);
        contentLayout = view.findViewById(R.id.contentLayout);
        //mGoToButton = (Button) view.findViewById(R.id.goto_button);
        //mGoToButton.setOnClickListener(listener);

        //textViewWelcomeMsg.setText("Hello " + ((MainActivity)getActivity()).getProfileData().getName() + "!\n\tTrack your order page - COMING SOON");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getUserOrders().observe(getViewLifecycleOwner(), mObserverResult1);

        return view;
    }

    Observer<List<OrderData>> mObserverResult1 = new Observer<List<OrderData>>() {
        @Override
        public void onChanged(@Nullable List<OrderData> orderList) {

            if(orderList != null) {

                updatePage(orderList);

            }

        }
    };

    private void updatePage(List<OrderData> orderDataList) {

        menuTitleTextView.setText("Track Order");

        contentLayout.removeAllViewsInLayout();
        contentLayout.invalidate();
        contentLayout.requestLayout();
        menuTitleTextView.setText("");
        if(orderDataList == null || orderDataList.size() <= 0) {
            return;
        } else {
            updateMenuItems(orderDataList, contentLayout);
        }
    }

    private void updateMenuItems(List<OrderData> orderList, LinearLayout contentLayout) {

        boolean isEmpty = true;

        for(OrderData list:orderList) {

            boolean menuListNotEmpty = false;
            LinearLayout layout = null;
            int countItem = 0;

            layout = addMenuCategory(contentLayout, list.getOrderId());

            for(String element:list.getOrderList().keySet()) {

                DishOrderData orderElement = list.getOrderList().get(element);

                //addMenuList(layout, orderElement, element, menuListNotEmpty, isOrderEnable);
            }

        }

    }

    private LinearLayout addMenuCategory(LinearLayout layout, String text) {

        LinearLayout rowLayout = new LinearLayout(this.getContext());
        LinearLayout row1Layout = new LinearLayout(this.getContext());
        TextView textView = new TextView(this.getContext());
        final ImageView imageViewCollapse = new ImageView(this.getContext());
        final LinearLayout contentLayout = new LinearLayout(this.getContext());

        textView.setText(text);
        imageViewCollapse.setImageDrawable(this.getResources().getDrawable(android.R.drawable.arrow_up_float));

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageViewCollapse.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row1Layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        rowLayout.setGravity(Gravity.CENTER_VERTICAL);
        row1Layout.setGravity(Gravity.END);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        rowLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        textView.setTextColor(getResources().getColor(R.color.colorWhiteText));
        textView.setTypeface(null, Typeface.BOLD);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);

        rowLayout.addView(textView);
        rowLayout.addView(row1Layout);
        row1Layout.addView(imageViewCollapse);
        layout.addView(rowLayout);
        layout.addView(contentLayout);

        rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_contents(imageViewCollapse, contentLayout);
            }
        });

        rowLayout.setPadding(10, 10, 10, 10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setLayoutParams(params);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(0, 30, 0, 0);
        rowLayout.setLayoutParams(params1);

        return contentLayout;
    }

    private void toggle_contents(ImageView sourceClick, View destView){

        if(destView.isShown()){
            AnimationUtil.slide_up(this.getContext(), destView, sourceClick);
        }
        else{
            AnimationUtil.slide_down(this.getContext(), destView, sourceClick);
        }

    }

}
