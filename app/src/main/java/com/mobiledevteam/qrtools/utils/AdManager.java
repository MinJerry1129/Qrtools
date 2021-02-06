package com.mobiledevteam.qrtools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mobiledevteam.qrtools.R;


public class AdManager {
    public static int adCounter = 1;

    public static boolean isloadFbAd = true;


    public static void initAd(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
    }

    static AdView gadView;
    public static void loadBannerAd(Context context, LinearLayout adContainer) {
        gadView = new AdView(context);
        gadView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adContainer.addView(gadView);
        loadBanner(context);
    }

    static void loadBanner(Context context) {
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize((Activity) context);
        gadView.setAdSize(adSize);
        gadView.loadAd(adRequest);
    }

    static AdSize getAdSize(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }

    public static void adptiveBannerAd(Context context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(context.getString(R.string.admob_banner_id));
        adView.loadAd(adRequest);
        adContainer.addView(adView);
    }

    static InterstitialAd mInterstitialAd;

    public static void loadInterAd(Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.admob_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
    }

    public static void showInterAd(final Activity context, final Intent intent, final int requestCode) {
        if (adCounter == 6 && mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            adCounter = 1;
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                    startActivity(context, intent, requestCode);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    mInterstitialAd.show();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }
            });
        } else {
            if (adCounter == 6){
                adCounter =1;
            }
            startActivity(context, intent, requestCode);
        }

    }

    static void startActivity(Activity context, Intent intent, int requestCode) {
        if (intent != null) {
            context.startActivityForResult(intent, requestCode);
        }
    }

    static com.facebook.ads.AdView adView;

    public static void fbBannerAd(Context context, LinearLayout adContainer) {
        adView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        adContainer.addView(adView);
        adView.loadAd();
    }

    static com.facebook.ads.AdView adaptiveView;

    public static void fbAdaptiveBannerAd(Context context, LinearLayout adContainer) {
        adaptiveView = new com.facebook.ads.AdView(context, context.getString(R.string.fbad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
        adContainer.addView(adaptiveView);
        adaptiveView.loadAd();
    }

    static com.facebook.ads.InterstitialAd fbInterstitialAd;

    public static void loadFbInterAd(final Activity context) {
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.fbad_interstitial_id));

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFbInterAd(context);
                startActivity(context, fbIntent, fbRequstCode);
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
//        fbInterstitialAd.loadAd();

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        fbInterstitialAd.loadAd(
                fbInterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    static Intent fbIntent;
    static int fbRequstCode;
    public static void showFbInterAd(final Activity context, final Intent intent, final int requestCode) {
        fbIntent = intent;
        fbRequstCode = requestCode;
        if (adCounter == 6 && fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
            adCounter = 1;
            fbInterstitialAd.show();
        } else {
            if (adCounter == 6){
                adCounter =1;
            }
            startActivity(context, intent, requestCode);
        }
    }

    public static void destroyFbAd() {
        if (adView != null) {
            adView.destroy();
        }
        if (adaptiveView != null) {
            adaptiveView.destroy();
        }

        if (fbInterstitialAd != null) {
            fbInterstitialAd.destroy();
        }
    }
}
