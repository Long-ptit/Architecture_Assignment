package com.example.baseproject.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.baseproject.model.AppInfor;
import com.example.baseproject.util.Utilizes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackerUsageViewModel extends AndroidViewModel {

    private MutableLiveData<AppInfor> mLongestAppInfor = new MutableLiveData<>();
    private MutableLiveData<List<AppInfor>> mListApp = new MutableLiveData<>();

    public TrackerUsageViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<AppInfor> getLongestAppInfor() {
        return mLongestAppInfor;
    }

    public MutableLiveData<List<AppInfor>> getListApp() {
        return mListApp;
    }

    public void requestInfoAppList() {
        new Thread(() -> {
            List<AppInfor> infoApps = getInfoApps();
            mListApp.postValue(infoApps);
        }).start();
    }

    private List<AppInfor> getInfoApps() {
        List<AppInfor> inforApps = new ArrayList<>();
        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) getApplication().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                System.currentTimeMillis() - (60 * 60 * 1000 * 24),
                System.currentTimeMillis()
        );
        for (UsageStats usageStats : usageStatsList) {
            String packageName = usageStats.getPackageName();

            String appName = Utilizes.getAppNameByPackageName(getApplication(), packageName);
            Drawable appIcon = Utilizes.getAppIconByPackageName(getApplication(), packageName);
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
        mLongestAppInfor.postValue(appInforsCopy.get(0));
        return inforApps;
    }

}
