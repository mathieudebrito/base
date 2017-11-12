package com.github.mathieudebrito.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.github.mathieudebrito.base.R;

public class AppUtils {

    public static String getVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            ExceptionsHandler.handle(e);
            return null;
        }
    }

    public static String getAppName(Context context) {
        return context.getString(R.string.app_name);
    }

    public static void goToStore(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            ExceptionsHandler.handle(e);
        }
    }

    public static String getPackage(Context context) {
        return context.getPackageName();
    }

    public static boolean isRelease(Context context) {
        return !getPackage(context).contains("debug") && !getPackage(context).contains("alpha") && !getPackage(context).contains("beta");
    }
}
