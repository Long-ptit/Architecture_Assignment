package com.example.baseproject.model;

import android.graphics.drawable.Drawable;

public class AppInfor {
    private String mPackageName;
    private long mTimeUsage;
    private Drawable mIconApp;
    private String mAppName;

    public Drawable getmIconApp() {
        return mIconApp;
    }

    public void setmIconApp(Drawable mIconApp) {
        this.mIconApp = mIconApp;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public long getTimeUsage() {
        return mTimeUsage;
    }

    public void setTimeUsage(long mTimeUsage) {
        this.mTimeUsage = mTimeUsage;
    }
}
