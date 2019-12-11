package com.aht.business.kirti.pasitupusi.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.login.LoginMainActivity;
import com.aht.business.kirti.pasitupusi.ui.login.LoginType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.buttonSignOut);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        logoutButton.setOnClickListener(mOnClickSignoutListener);

        //String uid = getIntent().getStringExtra("uid");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            logoutButton.performClick();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                logoutButton.performClick();
            }
        });

        //getSupportActionBar().hide();

    }

    View.OnClickListener mOnClickSignoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            FirebaseAuth.getInstance().signOut();

            Intent mainPage = new Intent(MainActivity.this, LoginMainActivity.class);
            MainActivity.this.finish();
            MainActivity.this.startActivity(mainPage);

        }
    };


}