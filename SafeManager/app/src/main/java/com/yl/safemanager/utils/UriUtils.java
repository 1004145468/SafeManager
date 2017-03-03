package com.yl.safemanager.utils;

import android.net.Uri;

/**
 * Created by YL on 2017/3/2.
 */

public class UriUtils {

    public static Uri drawable2Uri(int id) {
        return Uri.parse("res:///" + id);
    }
}
