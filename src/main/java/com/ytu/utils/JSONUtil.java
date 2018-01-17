package com.ytu.utils;

import com.ytu.constant.JSONConstant;
import org.apache.commons.lang3.StringUtils;

public class JSONUtil {

    public static boolean isJsonArray(String json) {
        return json.charAt(0) == JSONConstant.BEGIN_ARRAY.getCharacter().charAt(0);
    }

    public static boolean isJsonObject(String json) {
        return json.charAt(0) == JSONConstant.BEGIN_OBJECT.getCharacter().charAt(0);
    }

    public static String encode(String s) {
        if (!(s instanceof String)) {
            return s;
        }
        s = StringUtils.replace(s, "%", "%25");
        s = StringUtils.replace(s, " ", "+");
        s = StringUtils.replace(s, "+", "%2B");
        s = StringUtils.replace(s, "|", "%7C");
        s = StringUtils.replace(s, "^", "%5E");
        return s;
    }

    public static String decode(String s) {
        if (!(s instanceof String)) {
            return s;
        }
        s = StringUtils.replace(s, "%25", "%");
        s = StringUtils.replace(s, "%2B", "+");
        s = StringUtils.replace(s, "+", " ");
        s = StringUtils.replace(s, "%7C", "|");
        s = StringUtils.replace(s, "%5E", "^");
        return s;
    }

    public static String base10To36(String string) {
        return Integer.toString(Integer.valueOf(string), 36).toUpperCase();
    }

    public static Integer base36To10(String s) {
        return Integer.parseInt(s, 36);
    }
}
