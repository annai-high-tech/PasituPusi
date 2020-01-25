package com.aht.business.kirti.pasitupusi.ui.main.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aht.business.kirti.pasitupusi.R;

public class ContactFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactus, container, false);

        Spanned email_link = Html.fromHtml("&nbsp;<a href='"
                + "mailto:" + getResources().getString(R.string.email_id)
                + "'>"
                + getResources().getString(R.string.email_id)
                + "</a>");

        Spanned phone_link = Html.fromHtml("&nbsp;<a href='"
                + "tel:" + getResources().getString(R.string.phone_number).replaceAll(" ", "")
                + "'>"
                + getResources().getString(R.string.phone_number)
                + "</a>");

        //((TextView)view.findViewById(R.id.textViewContactusEmail)).setMovementMethod(LinkMovementMethod.getInstance());
        //((TextView)view.findViewById(R.id.textViewContactusEmail)).setText(email_link);

        //((TextView)view.findViewById(R.id.textViewContactusPhone)).setText(phone_link);

        return view;
    }

}
