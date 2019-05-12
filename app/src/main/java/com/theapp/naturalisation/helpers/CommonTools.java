package com.theapp.naturalisation.helpers;

public class CommonTools {

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static String formatResponse(String response) {
        return response.replaceAll(" - ", "\n- ");

    }
}
