package com.aht.business.kirti.pasitupusi.ui.main;

import android.content.Intent;
import android.os.Bundle;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.ads.AdsAHT;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileManager;
import com.aht.business.kirti.pasitupusi.model.profile.ProfileViewModel;
import com.aht.business.kirti.pasitupusi.model.profile.data.ProfileData;
import com.aht.business.kirti.pasitupusi.model.profile.enums.ProfileRole;
import com.aht.business.kirti.pasitupusi.ui.login.LoginMainActivity;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.AdminUpdateFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.AdminViewOrderFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.BaseFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ContactFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.HomeFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.ProfileFragment;
import com.aht.business.kirti.pasitupusi.ui.main.tabs.SubPageFragment;
import com.google.android.gms.ads.AdView;
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

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private TextView privacyTextView;
    private Button logoutButton;
    private RelativeLayout footerView;
    private BaseFragment currentFragment;
    private ProfileViewModel profileViewModel;
    private AdsAHT adsAHT;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ProfileData profileData = null;

    public ProfileData getProfileData() {
        return profileData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setMenuDrawerInToolbar();

        /*SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);*/

        logoutButton = findViewById(R.id.buttonSignOut);
        logoutButton.setOnClickListener(mOnClickSignoutListener);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                logoutButton.performClick();
            }
        });

        profileData = (ProfileData) getIntent().getSerializableExtra("USER_PROFILE");

        if(!ProfileManager.isValidUser() && !(profileData != null && profileData.getRole() == ProfileRole.GUEST)) {
            logoutButton.performClick();
        }

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.getProfileCheckData().observe(this, mObserverResult);
        profileViewModel.getProfile();

        //Ads and privacy policy link
        footerView = findViewById(R.id.footer);
        adView = findViewById(R.id.adView);
        privacyTextView = findViewById(R.id.privacy_content);

        adsAHT = new AdsAHT(this,
                adView,
                getResources().getString(R.string.ADMOB_APP_ID_FULLSCREEN),
                getResources().getString(R.string.ADMOB_APP_ID_NATIVE),
                getResources().getBoolean(R.bool.enable_ads_banner),
                getResources().getBoolean(R.bool.enable_ads_fullscreen),
                getResources().getBoolean(R.bool.enable_ads_native));

        adsAHT.loadAllAds();

        showFooter(true, true, false);

    }

    Observer<ProfileData> mObserverResult = new Observer<ProfileData>() {

        @Override
        public void onChanged(@Nullable ProfileData data) {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_logout).setTitle("Logout");
            nav_Menu.findItem(R.id.nav_profile).setVisible(true);

            if(!ProfileManager.isValidUser() && profileData != null && profileData.getRole() == ProfileRole.GUEST) {
                nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                nav_Menu.findItem(R.id.nav_logout).setTitle("Login");
            } else if(!ProfileManager.isValidUser() && profileData != null && profileData.getRole() == ProfileRole.GUEST) {
                nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                nav_Menu.findItem(R.id.nav_logout).setTitle("Login");
            }

            if(data == null ){

                changeFragments(new ProfileFragment());

            } else {

                if(ProfileRole.getValue(data.getRole()) < ProfileRole.getValue(ProfileRole.MANAGER)) {
                    nav_Menu.findItem(R.id.nav_admin_update).setVisible(false);
                    nav_Menu.findItem(R.id.nav_admin_view_order).setVisible(false);
                }

                if(ProfileRole.getValue(data.getRole()) < ProfileRole.getValue(ProfileRole.USER)) {
                    nav_Menu.findItem(R.id.nav_profile).setVisible(false);
                    nav_Menu.findItem(R.id.nav_logout).setTitle("Login");
                }

                profileData  = data;
                changeFragments(new HomeFragment());

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

        showFooter(true, true, false);

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
                adsAHT.showFullScreenAds();
                showFooter(true, true, true);
                if(currentFragment == null || !(currentFragment instanceof ContactFragment)) {
                    changeFragments(new ContactFragment());
                }
                break;

            case R.id.nav_admin_update:
                if(currentFragment == null || !(currentFragment instanceof AdminUpdateFragment)){
                    changeFragments(new AdminUpdateFragment());
                }
                break;

            case R.id.nav_admin_view_order:
                if(currentFragment == null || !(currentFragment instanceof AdminViewOrderFragment)){
                    changeFragments(new AdminViewOrderFragment());
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

        if(selectedFragment instanceof SubPageFragment) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(mToolbarClickListener);
        } else if(selectedFragment instanceof BaseFragment) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            setMenuDrawerInToolbar();

        }

        currentFragment = selectedFragment;

    }

    public void showFooter(boolean showFooter, boolean showAds, boolean showPrivacy){

        footerView.setVisibility(showFooter ? View.VISIBLE : View.GONE);
        if (showFooter) {
            adView.setVisibility(showAds ? View.VISIBLE : View.GONE);
            privacyTextView.setVisibility(showPrivacy ? View.VISIBLE : View.GONE);
            if (showPrivacy) {
                //privacyTextView.setOnClickListener(mOnClickListener);
                Spanned text = Html.fromHtml("<a href='"
                        + getResources().getString(R.string.privacy_link)
                        + "'>"
                        + getResources().getString(R.string.privacy_title)
                        + "</a>");
                privacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
                privacyTextView.setText(text);
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private View.OnClickListener mToolbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(currentFragment instanceof SubPageFragment) {
                MainActivity.super.onBackPressed();
                MainActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                setMenuDrawerInToolbar();
            }
        }
    };

    private void setMenuDrawerInToolbar() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }
}