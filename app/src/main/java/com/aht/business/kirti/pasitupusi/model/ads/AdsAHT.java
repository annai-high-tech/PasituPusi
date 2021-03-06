package com.aht.business.kirti.pasitupusi.model.ads;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.ads.nativetemplates.NativeTemplateStyle;
import com.aht.business.kirti.pasitupusi.model.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.LinkedList;
import java.util.Queue;

public class AdsAHT {

    private InterstitialAd mInterstitialAd = null;
    private AdView mAdView = null;
    private AdLoader mAdLoader = null;
    private Queue<UnifiedNativeAd> unifiedNativeAdQueue = new LinkedList<>();
    private LinearLayout mAdViewLayout = null;
    private boolean adsBannerEnable = false;
    private boolean adsFullScreenEnable = false;
    private boolean adsNativeEnable = false;
    private AppCompatActivity context;
    private String bannerUnitId;
    private String fullScreenUnitId;
    private String nativeUnitId;

    public AdsAHT(AppCompatActivity context,
                  LinearLayout adContainerView, String bannerUnitId, String fullScreenUnitId, String nativeUnitId,
                  boolean adsBannerEnable, boolean adsFullScreenEnable, boolean adsNativeEnable) {
        this.context = context;
        this.adsBannerEnable = adsBannerEnable;
        this.adsFullScreenEnable = adsFullScreenEnable;
        this.adsNativeEnable = adsNativeEnable;
        this.bannerUnitId = bannerUnitId;
        this.fullScreenUnitId = fullScreenUnitId;
        this.nativeUnitId = nativeUnitId;
        init(context, adContainerView);
    }

    private void init(AppCompatActivity context, LinearLayout adContainerView) {

        mAdView = new AdView(context);
        mAdView.setAdUnitId(bannerUnitId);
        if(adsBannerEnable && mAdView != null) {
            //((ViewGroup)mAdView.getParent()).setVisibility(View.VISIBLE);
            if(adContainerView != null) {
                adContainerView.addView(mAdView);
                AdSize adSize = getAdSize(context);
                mAdView.setAdSize(adSize);
            }
            mAdView.setAdListener(bannerAdListener);
        }
        else {
            //((ViewGroup)mAdView.getParent()).setVisibility(View.GONE);
            mAdView.destroy();
        }

        if(adsFullScreenEnable) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(fullScreenUnitId);
            mInterstitialAd.setAdListener(fullscreenAdListener);
        }

        if(adsNativeEnable) {
            AdLoader.Builder mNativeBuilder = new AdLoader.Builder(context, nativeUnitId);

            mNativeBuilder.forUnifiedNativeAd(nativeListener)
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build());

            mAdLoader = mNativeBuilder.build();
        }


    }

    public void loadAllAds() {
        loadBannerAds();
        loadFullScreenAds();
        loadNativeAds();
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

    public void loadNativeAds() {
        if(adsNativeEnable) {
            mAdLoader.loadAds(new AdRequest.Builder().build(), 5);
        }
    }

    public boolean showFullScreenAds() {

        boolean status = false;
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            status = true;
        }

        if(!mInterstitialAd.isLoading()) {
            loadFullScreenAds();
        }

        return status;
    }

    public boolean showNativeAds(final Context context, final LinearLayout layout) {

        if(unifiedNativeAdQueue.size() < 15) {
            loadNativeAds();
        }
        if (mAdLoader != null && unifiedNativeAdQueue.size() > 0) {

            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.default_text_color));

            NativeTemplateStyle styles = new
                    NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();

            TemplateView template = new TemplateView(context, null);
            template.onFinishInflate();
            template.setStyles(styles);
            template.setNativeAd(unifiedNativeAdQueue.remove());

            layout.addView(template);

            return true;
        }
        return false;
    }

    AdListener bannerAdListener = new AdListener() {

        @Override
        public void onAdLoaded() {
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            //Toast.makeText(context, "1 Initialisation failed: " + errorCode, Toast.LENGTH_LONG).show();
        }

    };

    AdListener fullscreenAdListener = new AdListener() {

        @Override
        public void onAdLoaded() {
            //System.out.println("Full screen ad Loaded");
        }

        @Override
        public void onAdClosed() {
            // Load the next interstitial.
            if(!mInterstitialAd.isLoading()) {
                loadFullScreenAds();
            }
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            //Toast.makeText(context, "2 Initialisation failed: " + errorCode, Toast.LENGTH_LONG).show();
        }

    };

    UnifiedNativeAd.OnUnifiedNativeAdLoadedListener nativeListener= new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
        @Override
        public void onUnifiedNativeAdLoaded(UnifiedNativeAd pUnifiedNativeAd) {

            unifiedNativeAdQueue.add(pUnifiedNativeAd);

            //showNativeAds(context);
        }
    };

    private AdSize getAdSize(AppCompatActivity context) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }


}
