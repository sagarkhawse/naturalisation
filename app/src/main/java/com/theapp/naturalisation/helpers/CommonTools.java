package com.theapp.naturalisation.helpers;

import com.theapp.naturalisation.BuildConfig;

public class CommonTools {

    private static final String LITE = "lite";
    private static final String FULL = "full";
    private static final String DEBUG = "debug";
    private static final String SEPARATOR = " - ";
    private static final String REPLACEMENT = "\n- ";
    public static final String LITE_VERSION = "https://play.google.com/store/apps/details?id=com.theapp.naturalisation";
    public static final String FULL_VERSION = "https://play.google.com/store/apps/details?id=com.theapp.naturalisation.full";
    public static final String TERMS = "https://docs.google.com/document/d/e/2PACX-1vTHUThxL3g4AsJO2aFALoYaAoBbvBRVzqSkQi9MT1_78pr_5jBtOzGmXVLSh0mXCjf0B8tkglApy1aJ/pub";
    public static final String FACEBOOK_PAGE = "https://www.facebook.com/Naturalisation-Fran%C3%A7aise-1272280599597926/";

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

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
}
