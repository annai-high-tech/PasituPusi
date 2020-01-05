package com.aht.business.kirti.pasitupusi.model.ads;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdsAHT extends AdListener {

    private InterstitialAd mInterstitialAd = null;
    private AdView mAdView = null;
    private boolean adsBannerEnable = false;
    private boolean adsFullScreenEnable = false;
    private boolean adsNativeEnable = false;
    private AppCompatActivity context;
    private String fullScreenUnitId;
    private String nativeUnitId;

    public AdsAHT(AppCompatActivity context,
                  AdView mAdView, String fullScreenUnitId, String nativeUnitId,
                  boolean adsBannerEnable, boolean adsFullScreenEnable, boolean adsNativeEnable) {
        this.context = context;
        this.adsBannerEnable = adsBannerEnable;
        this.adsFullScreenEnable = adsFullScreenEnable;
        this.adsNativeEnable = adsNativeEnable;
        this.fullScreenUnitId = fullScreenUnitId;
        this.nativeUnitId = nativeUnitId;
        init(context, mAdView);
    }

    private void init(AppCompatActivity context, AdView mAdView) {

        //context.findViewById(R.id.adView)
        this.mAdView = mAdView;
        if(adsBannerEnable && mAdView != null) {
            mAdView.setAdListener(this);
        }
        else {
            mAdView.destroy();
        }

        if(adsFullScreenEnable) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(fullScreenUnitId);
            mInterstitialAd.setAdListener(this);
        }
    }

    public void loadAllAds() {
        loadBannerAds();
        loadFullScreenAds();
    }

    public void loadBannerAds() {
        if(adsBannerEnable) {
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    public void loadFullScreenAds() {
        if(adsFullScreenEnable)
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public boolean showFullScreenAds() {
        loadFullScreenAds();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return true;
        }
        return false;
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        // Code to be executed when an ad request fails.
        Toast.makeText(context, "Initialisation failed: " + errorCode, Toast.LENGTH_LONG).show();
    }

}
