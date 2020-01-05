package com.aht.business.kirti.pasitupusi.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.login.UserType;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileManager;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileViewModel;
import com.aht.business.kirti.pasitupusi.ui.login.LoginMainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.BaseFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ContactFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.HomeFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    private BaseFragment currentFragment;
    private ProfileViewModel profileViewModel;

    private String userDisplayName = null;
    private UserType userType = UserType.GUEST;

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /*SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);*/

        logoutButton = findViewById(R.id.buttonSignOut);
        logoutButton.setOnClickListener(mOnClickSignoutListener);

        userDisplayName = getIntent().getStringExtra("USER_NAME");
        String mUserType = getIntent().getStringExtra("USER_TYPE");

        if(mUserType != null) {
            userType = UserType.valueOf(mUserType);
        }

        if(!ProfileManager.isValidUser() && !(userDisplayName != null && userDisplayName.equals("Guest"))) {
            logoutButton.performClick();
        }

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                logoutButton.performClick();
            }
        });

        changeFragments(new HomeFragment());

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getProfileCheckData().observe(this, mObserverResult);
        profileViewModel.isProfileCreated();

        //getSupportActionBar().hide();

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_logout).setTitle("Logout");
        nav_Menu.findItem(R.id.nav_profile).setVisible(true);

        if(ProfileManager.isValidUser()) {
            userDisplayName = ProfileManager.getUserName();
        }
        else if(!ProfileManager.isValidUser() && userType == UserType.GUEST) {
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setTitle("Login");
        }

    }

    Observer<Boolean> mObserverResult = new Observer<Boolean>() {

        @Override
        public void onChanged(@Nullable Boolean status) {

            if(!status){
                changeFragments(new ProfileFragment());
            }
        }
    };

    private void signOut() {

        FirebaseAuth.getInstance().signOut();

        Intent mainPage = new Intent(MainActivity.this, LoginMainActivity.class);
        MainActivity.this.finish();
        MainActivity.this.startActivity(mainPage);

    }
    View.OnClickListener mOnClickSignoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            signOut();

        }
    };

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            processMenuItem(menuItem);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    private boolean processMenuItem(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                if(currentFragment == null || !(currentFragment instanceof HomeFragment)){
                    changeFragments(new HomeFragment());
                }
                break;

            case R.id.nav_profile:
                if(currentFragment == null || !(currentFragment instanceof ProfileFragment)) {
                    changeFragments(new ProfileFragment());
                }
                break;

            case R.id.nav_contact:
                if(currentFragment == null || !(currentFragment instanceof ContactFragment)) {
                    changeFragments(new ContactFragment());
                }
                break;

            case R.id.nav_logout:
                signOut();
                break;

            default:
                break;
        }

        return true;
    }

    public void changeFragments(BaseFragment selectedFragment) {

        //PlaceholderFragment fragment = PlaceholderFragment.newInstance(selectedFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, selectedFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        currentFragment = selectedFragment;

    }


}