package com.example.baseproject.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.baseproject.Application;
import com.example.baseproject.R;
import com.example.baseproject.model.AppInfor;
import com.example.baseproject.util.Utilizes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackerUsageService extends Service {

    private static final int ID_NOTIFICATION = 1;
    public static final String ACTION_NAME = "ACTION_NAME";
    public static final int ACTION_DELETE = 2;
    private Handler mHandler = new Handler();
    private String mPackageNameCurrent = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int action = intent.getIntExtra(ACTION_NAME, -1);
            handleAction(action);
            getInfoApps();
        }
        return START_STICKY;
    }

    private void createNotification(String nameApp) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_tracker_usage);
        remoteViews.setOnClickPendingIntent(R.id.btn_delete, getPendingIntent(ACTION_DELETE));
        remoteViews.setTextViewText(R.id.tv_name_longest_app_noti, nameApp);
        Notification notification = new NotificationCompat.Builder(this, Application.NOTIFICATION_CHANNEL_ID)
                .setCustomContentView(remoteViews)
                .build();
        startForeground(ID_NOTIFICATION, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void getInfoApps() {
        @SuppressLint("WrongConstant")
        UsageStatsManager usageStatsManager =
                (UsageStatsManager) getApplication().getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST,
                System.currentTimeMillis() - (1000 * 60 * 60 * 24),
                System.currentTimeMillis()
        );
        if (usageStatsList != null && usageStatsList.size() > 0) {
            Collections.sort(usageStatsList, (o1, o2) -> {
                if (o1.getTotalTimeInForeground() == o2.getTotalTimeInForeground()) return 0;
                return o1.getTotalTimeInForeground() > o2.getTotalTimeInForeground() ? -1 : 1;
            });
            AppInfor appInforLongest = new AppInfor();
            appInforLongest.setPackageName(usageStatsList.get(0).getPackageName());
            if (!appInforLongest.getPackageName().equals(mPackageNameCurrent)) {
                mPackageNameCurrent = appInforLongest.getPackageName();
                createNotification(Utilizes.getAppNameByPackageName(this, appInforLongest.getPackageName()));
            }
        }
        if (mHandler != null) {
            mHandler.postDelayed(() -> getInfoApps(), 1000);
        }
    }

    @Override
    public void onDestroy() {
        mHandler = null;
        super.onDestroy();
    }

    private PendingIntent getPendingIntent(int action) {
        Intent intent = new Intent(getApplicationContext(), TrackerUsageService.class);
        intent.putExtra(ACTION_NAME, action);
        return PendingIntent.getService(getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void handleAction(int action) {
        switch (action) {
            case ACTION_DELETE:
                delete();
                break;
            default:
                break;
        }

    }

    private void delete() {
        stopSelf();
    }
}
