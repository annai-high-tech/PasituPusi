package com.aht.business.kirti.pasitupusi.model.ads;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class AdsAHT extends AdListener {

    private InterstitialAd mInterstitialAd = null;
    private AdView mAdView = null;
    private AdLoader mAdLoader = null;
    private UnifiedNativeAd unifiedNativeAd;
    private LinearLayout mAdViewLayout = null;
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
            ((ViewGroup)mAdView.getParent()).setVisibility(View.VISIBLE);
            mAdView.setAdListener(this);
        }
        else {
            ((ViewGroup)mAdView.getParent()).setVisibility(View.GONE);
            mAdView.destroy();
        }

        if(adsFullScreenEnable) {
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(fullScreenUnitId);
            mInterstitialAd.setAdListener(this);
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
        loadNativenAds();
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

    public void loadNativenAds() {
        if(adsNativeEnable)
            mAdLoader.loadAd(new AdRequest.Builder().build());
    }

    public boolean showFullScreenAds() {
        loadFullScreenAds();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            return true;
        }
        return false;
    }

    public boolean showNativeAds(final Context context, final LinearLayout layout) {

        loadNativenAds();
        if (mAdLoader != null && unifiedNativeAd != null) {

            //NativeTemplateStyle styles = new
            //        NativeTemplateStyle.Builder().withMainBackgroundColor((ColorDrawable)fragmentActivity.getDrawable(R.drawable.ic_launcher_background)).build();


            TemplateView template = new TemplateView(context, null);
            template.onFinishInflate();
            //template.setStyles(styles);
            template.setNativeAd(unifiedNativeAd);

            layout.addView(template);

            return true;
        }
        return false;
    }

    UnifiedNativeAd.OnUnifiedNativeAdLoadedListener nativeListener= new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
        @Override
        public void onUnifiedNativeAdLoaded(UnifiedNativeAd pUnifiedNativeAd) {

            unifiedNativeAd = pUnifiedNativeAd;

            //showNativeAds(context);

            //System.out.println(".....................1");
        }
    };

    @Override
    public void onAdFailedToLoad(int errorCode) {
        // Code to be executed when an ad request fails.
        Toast.makeText(context, "Initialisation failed: " + errorCode, Toast.LENGTH_LONG).show();
    }



}
