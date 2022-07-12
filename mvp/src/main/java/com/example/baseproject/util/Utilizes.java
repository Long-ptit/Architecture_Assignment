package com.example.baseproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.example.baseproject.R;

public class Utilizes {
    @SuppressLint("UseCompatLoadingForDrawables")
    public static Drawable getAppIconByPackageName(@NonNull Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return context.getDrawable(R.drawable.ic_launcher_background);
    }

    public static String getAppNameByPackageName(@NonNull Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return applicationInfo == null
                ? "Not exist"
                : (String) packageManager.getApplicationLabel(applicationInfo);

    }
}
