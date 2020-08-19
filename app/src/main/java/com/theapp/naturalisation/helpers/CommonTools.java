package com.theapp.naturalisation.helpers;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.theapp.naturalisation.BuildConfig;
import com.theapp.naturalisation.R;

public class CommonTools {

    private static final String LITE = "lite";
    private static final String FULL = "full";
    private static final String DEBUG = "debug";
    private static final String SEPARATOR = " - ";
    private static final String REPLACEMENT = "\n- ";
    public static final String LITE_VERSION = "https://play.google.com/store/apps/details?id=com.theapp.naturalisation";
    public static final String FULL_VERSION = "https://play.google.com/store/apps/details?id=com.theapp.naturalisation.full";
    public static final String TERMS = "https://docs.google.com/document/d/e/2PACX-1vTHUThxL3g4AsJO2aFALoYaAoBbvBRVzqSkQi9MT1_78pr_5jBtOzGmXVLSh0mXCjf0B8tkglApy1aJ/pub";
    public static final String FACEBOOK_PAGE = "https://www.facebook.com/groups/frenchcitizenship/";
    public static final String DEVICE_ID = "3D14E6058AE6036AEA01F03949D34D5B";

    // Remote Config keys
    private static final String MAX_ITEMS_LITE_VERSION_CONFIG_KEY = "lite_max_items";
    private static final String ITEMS_PER_AD_CONFIG_KEY = "items_per_ad";
    private static final String JO_TIME_TO_WAIT_CONFIG_KEY = "jo_time_to_wait";
    private static final int CONFIG_FETCH_INTERVAL_IN_SECONDS = 3600;

    private static int itemsPerAd;
    private static long liteMaxItems;
    private static long joTimeToWait;

    public static String formatResponse(String response) {
        return response.replaceAll(SEPARATOR, REPLACEMENT);
    }

    public static boolean isFullVersion() {
        return BuildConfig.FLAVOR.equals(FULL);
    }

    public static boolean isLiteVersion() {
        return BuildConfig.FLAVOR.equals(LITE);
    }

    public static boolean isDebug() {
        return BuildConfig.BUILD_TYPE.equals(DEBUG);
    }

    public static void setupFirebaseRemoteConfig() {
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setFetchTimeoutInSeconds(2)
                .setMinimumFetchIntervalInSeconds(CONFIG_FETCH_INTERVAL_IN_SECONDS)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

//        itemsPerAd = (int) mFirebaseRemoteConfig.getLong(ITEMS_PER_AD_CONFIG_KEY);
        itemsPerAd = 20;
        liteMaxItems = mFirebaseRemoteConfig.getLong(MAX_ITEMS_LITE_VERSION_CONFIG_KEY);
        joTimeToWait = mFirebaseRemoteConfig.getLong(JO_TIME_TO_WAIT_CONFIG_KEY);
    }

    public static int getItemsPerAd() {
        return itemsPerAd;
    }

    public static long getLiteMaxItems() {
        return liteMaxItems;
    }

    static long getJoTimeToWait() {
        return joTimeToWait;
    }
}
