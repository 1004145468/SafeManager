package com.yl.safemanager.utils;

import java.util.regex.Pattern;

/**
 * Created by YL on 2017/2/27.
 */

public class RegexUtils {

    private static String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"; //邮箱正则

    public static boolean isEmail(CharSequence input) {
       return input != null && input.length() > 0 && Pattern.matches(REGEX_EMAIL, input);
    }
}
