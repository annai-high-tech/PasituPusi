package com.aht.business.kirti.pasitupusi.ui.main.fragments.user;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aht.business.kirti.pasitupusi.BuildConfig;
import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.share.ShareApp;
import com.aht.business.kirti.pasitupusi.ui.main.fragments.BaseFragment;

public class ContactFragment extends BaseFragment {

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment f = new ContactFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_contactus, container, false);

        final ShareApp shareApp = new ShareApp();
        TextView appDescriptionView = view.findViewById(R.id.textViewAppInfo);
        Button shareButton = view.findViewById(R.id.buttonShareApp);

        String appDesc = view.getResources().getString(R.string.app_contact_description, BuildConfig.VERSION_NAME);

        appDescriptionView.setText(appDesc);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String appName = view.getResources().getString(R.string.app_name);
                String appLink = view.getResources().getString(R.string.app_link, BuildConfig.APPLICATION_ID);
                String shareMsg = view.getResources().getString(R.string.share_text, appName, appLink);
                shareApp.shareText(ContactFragment.this.getActivity(), shareMsg);
            }
        });

        return view;
    }

}
