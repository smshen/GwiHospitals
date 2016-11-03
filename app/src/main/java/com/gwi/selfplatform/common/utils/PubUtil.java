package com.gwi.selfplatform.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PubUtil {
    public static int CATEGORY_TAB_INDEX = 0;

    public static String parseXml(String xmlData) {
        String result = xmlData;
        if (result.contains("#")) {
            int start = xmlData.indexOf("#") - 1;
            result = xmlData.substring(0, start) + xmlData.substring(start + 26, xmlData.length());
        }
        return result;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}
