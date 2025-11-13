package com.arzrp.acs;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class Ads implements IUnityAdsInitializationListener {

    private static final String GAME_ID = "5807161";
    private static final String PLACEMENT_VIDEO = "Interstitial_Android";
    private static final boolean TEST_MODE = false;

    private final Activity activity;

    public Ads(Activity activity) {
        this.activity = activity;
        initialize();
    }

    private void initialize() {
        Log.v("UnityAds", "🟢 Initializing Unity Ads...");
        UnityAds.initialize(activity.getApplicationContext(), GAME_ID, TEST_MODE, this);
    }

    private final IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            Log.v("UnityAds", "✅ Ad loaded: " + placementId);
            UnityAds.show(activity, PLACEMENT_VIDEO, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAds", "❌ Failed to load " + placementId + ": " + message);
        }
    };

    private final IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAds", "Show failed: " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAds", "Show start: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAds", "Clicked: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAds", "Show complete: " + placementId);
            Toast.makeText(activity, "😍 Спасибо за просмотр 😍", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onInitializationComplete() {
        Log.v("UnityAds", "✅ Initialized");
        UnityAds.load(PLACEMENT_VIDEO, loadListener);
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("UnityAds", "Initialization failed: " + message);
    }
}