package com.yl.safemanager.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by YL on 2017/3/17.
 */

public class FileUtils {

    /**
     * 在根目录创建一个文件夹
     *
     * @param dirName
     */
    public static File createFile(String dirName, String fileName) {
        File storyDir = null;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            storyDir = Environment.getRootDirectory();
        } else {
            storyDir = Environment.getExternalStorageDirectory();
        }
        File Dirfile = new File(storyDir, dirName);
        if (!Dirfile.exists() || !Dirfile.isDirectory()) {
            Dirfile.mkdir();
        }
        File file = new File(Dirfile,fileName);
        return file;
    }
}
