package com.yl.safemanager.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.yl.safemanager.entities.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YL on 2017/3/8.
 */

public class AppUtils {

    private static final String TAG = "AppUtils";

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
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
            if (serviceInfo.service.getShortClassName().equals(shortclassname)) {
                Log.d(TAG, "isRunByServiceName: " + serviceInfo.service.getShortClassName());
                return true;
            }
        }
        return false;
    }

    /**
     * 5.0 以下
     *
     * @return 栈顶应用的包名
     */
    public static String getTopAppPackageName(ActivityManager activityManager, UsageStatsManager usageStatsManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return activityManager.getRunningTasks(1).get(0).topActivity.getPackageName(); //顶层应用的包名
        } else { //5.0 及 以上
            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000, ts);
            if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                return null;
            }
            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }
            return recentStats.getPackageName();
        }
    }
}
