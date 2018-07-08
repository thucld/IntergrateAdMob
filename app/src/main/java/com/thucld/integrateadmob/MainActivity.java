package com.thucld.integrateadmob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity {

    private AdView adView;
    private Button btnFullscreenAd;
    private Button btnShowRewardedVideoAd;
    private Button btnShowNativeAd;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private AdLoader adLoader;

    private final AdListener bannerAdListener = new AdListener() {

        @Override
        public void onAdLoaded() {
        }

        @Override
        public void onAdClosed() {
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
        }

        @Override
        public void onAdLeftApplication() {
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
        }

    };
    private final AdListener interstitialAdListener = new AdListener() {

        @Override
        public void onAdLoaded() {
            showToast("Ad is loaded!");
            btnFullscreenAd.setEnabled(true);
        }

        @Override
        public void onAdClosed() {
//            showToast("Ad is closed!");
            btnFullscreenAd.setEnabled(false);
            loadInterstitialAd();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            showToast(AdsError.getErrorMessage(errorCode));
            btnFullscreenAd.setEnabled(false);
        }

        @Override
        public void onAdLeftApplication() {
//            showToast("Ad left application!");
            btnFullscreenAd.setEnabled(false);
        }

        @Override
        public void onAdOpened() {
//            showToast("Ad opened!");
            btnFullscreenAd.setEnabled(false);
            super.onAdOpened();
        }

    };
    private final RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {

        @Override
        public void onRewarded(RewardItem rewardItem) {
            showToast("onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount());
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            showToast("onRewardedVideoAdLeftApplication");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            showToast("onRewardedVideoAdClosed");
            btnShowRewardedVideoAd.setEnabled(false);
            loadRewardedVideoAd();
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            showToast("onRewardedVideoAdFailedToLoad");
            btnShowRewardedVideoAd.setEnabled(false);
        }

        @Override
        public void onRewardedVideoCompleted() {
            showToast("onRewardedVideoCompleted");
            btnShowRewardedVideoAd.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            showToast("onRewardedVideoAdLoaded");
            btnShowRewardedVideoAd.setEnabled(true);
        }

        @Override
        public void onRewardedVideoAdOpened() {
            showToast("onRewardedVideoAdOpened");
        }

        @Override
        public void onRewardedVideoStarted() {
            showToast("onRewardedVideoStarted");
        }

    };
    private final AdListener nativeAdListener = new AdListener() {
        @Override
        public void onAdClosed() {
            // showToast("NativeAd:::onAdClosed");
            loadNativeAd(adLoader, 5);
            btnShowNativeAd.setEnabled(false);
            super.onAdClosed();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Handle the failure by logging, altering the UI, and so on.
            showToast(AdsError.getErrorMessage(errorCode));
            btnShowNativeAd.setEnabled(false);
            super.onAdFailedToLoad(errorCode);
        }

        @Override
        public void onAdLeftApplication() {
            // showToast("NativeAd:::onAdLeftApplication");
            super.onAdLeftApplication();
        }

        @Override
        public void onAdOpened() {
            // showToast("NativeAd:::onAdOpened");
            super.onAdOpened();
        }

        @Override
        public void onAdLoaded() {
            // showToast("NativeAd:::onAdLoaded");
            btnShowNativeAd.setEnabled(true);
            super.onAdLoaded();
        }

        @Override
        public void onAdClicked() {
            // showToast("NativeAd:::onAdClicked");
            super.onAdClicked();
        }

        @Override
        public void onAdImpression() {
            // showToast("NativeAd:::onAdImpression");
            super.onAdImpression();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupListener();

    }

    private void initView() {

        btnFullscreenAd = findViewById(R.id.btn_fullscreen_ad);
        btnShowRewardedVideoAd = findViewById(R.id.btn_show_rewarded_video);
        btnShowNativeAd = findViewById(R.id.btn_show_native_ad);

        /*------- Banner Ads -------*/
        adView = findViewById(R.id.adView);
        // Load ads into Banner Ad
        AdRequest bAdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // for test and debug, remove when release app
                .addTestDevice(getString(R.string.str_test_device_id))
                .build();
        adView.loadAd(bAdRequest);

        /*------- Interstitial Ads -------*/
        interstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        // interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_dedicated_for_test));
        loadInterstitialAd();

        /*------- Rewarded Video Ads -------*/
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();

        /*------- Native Ads -------*/
        adLoader = new AdLoader.Builder(getApplicationContext(), getString(R.string.native_ad_dedicated_for_test))
                .forUnifiedNativeAd(unifiedNativeAd -> {
                    /*if (adLoader.isLoading()) {
                        // The AdLoader is still loading ads.
                        // Expect more adLoaded or onAdFailedToLoad callbacks.
                        btnShowNativeAd.setEnabled(false);
                    } else {
                        // The AdLoader has finished loading ads.
                        btnShowNativeAd.setEnabled(true);
                    }*/
                })
                .withAdListener(nativeAdListener)
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        loadNativeAd(adLoader, 5);

    }

    private void loadInterstitialAd() {
        // Load ads into Interstitial Ads
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void loadRewardedVideoAd() {
        // set the ad unit ID and load ads
        rewardedVideoAd.loadAd(getString(R.string.rewarded_video_dedicated_for_test), new AdRequest.Builder().build());
    }

    private void loadNativeAd(AdLoader adLoader, int num) {
        if (num == 0) {
            adLoader.loadAd(new AdRequest.Builder().build());
        } else if (num >= 1 && num <= 5) {
            adLoader.loadAds(new AdRequest.Builder().build(), num);
        }
    }

    private void setupListener() {

        /*------- Show Ads -------*/
        // Make sure the ad is loaded completely before showing it
        btnFullscreenAd.setOnClickListener(v -> {
            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                showToast("The interstitial wasn't loaded yet.");
            }
        });
        btnShowRewardedVideoAd.setOnClickListener(v -> {
            if (rewardedVideoAd.isLoaded()) {
                rewardedVideoAd.show();
            } else {
                showToast("The rewarded wasn't loaded yet.");
            }
        });
        btnShowNativeAd.setOnClickListener(v -> {
            if (rewardedVideoAd.isLoaded()) {
                rewardedVideoAd.show();
            } else {
                showToast("The rewarded wasn't loaded yet.");
            }
        });

        /*------- Add Ads listener -------*/
        adView.setAdListener(bannerAdListener);
        interstitialAd.setAdListener(interstitialAdListener);
        rewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);

    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (adView != null) {
            adView.resume();
        }
        rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        rewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(
                getApplicationContext(),
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }

}
