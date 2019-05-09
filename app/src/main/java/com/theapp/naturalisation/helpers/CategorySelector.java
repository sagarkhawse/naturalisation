package com.theapp.naturalisation.helpers;

import java.util.HashMap;
import java.util.Map;

public class CategorySelector {
    private static final String HISTORY = "Histoire";
    private static final String PERSONAL = "Personnel";
    private static final String REPUBLIC = "République";

    private static final String LABEL_HISTORY = "history";
    private static final String LABEL_PERSONAL = "perso";
    private static final String LABEL_REPUBLIC = "republic";

    static Map<String, String> conversionMap = new HashMap<>();

    static {
        conversionMap.put(LABEL_HISTORY, HISTORY);
        conversionMap.put(LABEL_PERSONAL, PERSONAL);
        conversionMap.put(LABEL_REPUBLIC, REPUBLIC);
    }

    public static String getName(String label) {
        return conversionMap.get(label);
    }
}
