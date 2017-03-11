package com.yl.safemanager.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yl.safemanager.entities.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YL on 2017/3/8.
 */

public class AppUtils {

    public static ArrayList<AppInfo> loadAppInfos(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        int size = packageInfos.size();
        ApplicationInfo applicationInfo = null;
        String appName = null;
        Drawable icon = null;
        String packageName = null;
        ArrayList<AppInfo> appinfos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            applicationInfo = packageInfos.get(i).applicationInfo;
            icon = applicationInfo.loadIcon(packageManager);
            appName = applicationInfo.loadLabel(packageManager).toString();
            packageName = applicationInfo.packageName;
            appinfos.add(new AppInfo(packageName, appName, icon));
        }
        return appinfos;
    }

    public static boolean isRunByServiceName(Context context, String shortclassname) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(50);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            if (serviceInfo.service.getShortClassName().equals(shortclassname)) {
                return true;
            }
        }
        return false;
    }
}
