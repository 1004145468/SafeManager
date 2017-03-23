package com.yl.safemanager.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by YL on 2017/3/2.
 */

public class UriUtils {

    public static Uri drawable2Uri(int id) {
        return Uri.parse("res:///" + id);
    }

    public static String queryUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
        cursor.close();
        return filePath;
    }
}
