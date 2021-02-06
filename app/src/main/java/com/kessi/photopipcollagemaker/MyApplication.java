package com.kessi.photopipcollagemaker;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }

    }

}
