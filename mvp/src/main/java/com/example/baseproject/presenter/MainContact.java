package com.example.baseproject.presenter;

import android.app.Application;

import com.example.baseproject.model.AppInfor;

import java.util.List;

public interface MainContact {
    interface MainView {

        void sendListAppInfor(List<AppInfor> listAppInfor);

        void sendLongestAppInfor(AppInfor appInforLongest);

        void dismissLoading();

    }

    interface MainPresenter {
        void onCreate(MainView mainView);

        void getAppLongest(Application application);
    }
}
