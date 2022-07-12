package com.example.baseproject.presenter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.baseproject.model.AppInfor;
import com.example.baseproject.util.Utilizes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainPresenter implements MainContact.MainPresenter{

    private MainContact.MainView mMainView;

    @Override
    public void onCreate(MainContact.MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void getAppLongest(Application application) {
        getInfoApps(application);
        mMainView.dismissLoading();
    }

    private void getInfoApps(Application application) {
        List<AppInfor> inforApps = new ArrayList<>();
        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) application.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                System.currentTimeMillis() - (60 * 60 * 1000 * 24),
                System.currentTimeMillis()
        );
        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();

            String appName = Utilizes.getAppNameByPackageName(application, packageName);
            Drawable appIcon = Utilizes.getAppIconByPackageName(application, packageName);
            long appUsageTime = usageStats.getTotalTimeInForeground();

            AppInfor appInfor = new AppInfor();
            appInfor.setPackageName(packageName);
            appInfor.setmAppName(appName);
            appInfor.setmIconApp(appIcon);
            appInfor.setTimeUsage(appUsageTime);
            inforApps.add(appInfor);
        }
        List<AppInfor> appInforsCopy = new ArrayList<>(inforApps);
        Collections.sort(appInforsCopy, (o1, o2) -> {
            if (o1.getTimeUsage() == o2.getTimeUsage()) return 0;
            return o1.getTimeUsage() > o2.getTimeUsage() ? -1 :  1;
        });
        mMainView.sendListAppInfor(inforApps);
        mMainView.sendLongestAppInfor(appInforsCopy.get(0));
    }


}
