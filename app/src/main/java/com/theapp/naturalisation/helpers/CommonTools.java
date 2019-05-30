package com.theapp.naturalisation.helpers;

import com.theapp.naturalisation.BuildConfig;

public class CommonTools {

    private static final String LITE = "lite";
    private static final String FULL = "full";
    private static final String DEBUG = "debug";
    private static final String SEPARATOR = " - ";
    private static final String REPLACEMENT = "\n- ";

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
