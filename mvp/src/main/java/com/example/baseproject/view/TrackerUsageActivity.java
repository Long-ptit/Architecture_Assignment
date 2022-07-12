package com.example.baseproject.view;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AppOpsManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.baseproject.R;
import com.example.baseproject.databinding.ActivityTrackerUsageBinding;
import com.example.baseproject.model.AppInfor;
import com.example.baseproject.presenter.MainContact;
import com.example.baseproject.presenter.MainPresenter;
import com.example.baseproject.service.TrackerUsageService;

import java.util.List;

public class TrackerUsageActivity extends AppCompatActivity implements MainContact.MainView {

    private static final String TAG = "TrackerUsageActivity";
    private ActivityTrackerUsageBinding mBinding;
    private AppInforAdapter mAppInforAdapter;
    private ProgressDialog mProgessDialog;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_usage);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tracker_usage);
        mBinding.executePendingBindings();
        mBinding.setLifecycleOwner(this);
        setUpRecycleView();
        createPresenter();
        createDialog();
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, TrackerUsageService.class);
        startService(intent);
    }

    private void createPresenter() {
        mMainPresenter = new MainPresenter();
        mMainPresenter.onCreate(this);
    }

    private void createDialog() {
        mProgessDialog = new ProgressDialog(this);
        mProgessDialog.setTitle("Loading");
        mProgessDialog.setCancelable(true);
        mProgessDialog.show();
    }

    private void setUpRecycleView() {
        mAppInforAdapter = new AppInforAdapter(this);
        mBinding.rcv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        mBinding.rcv.setAdapter(mAppInforAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        handleGetData();
    }

    private void handleGetData() {
        if (checkUsageStatsPermission()) {
            mMainPresenter.getAppLongest(getApplication());
        } else {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    private boolean checkUsageStatsPermission() {
        int mode = AppOpsManagerCompat.noteOpNoThrow(
                getApplicationContext(),
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                getPackageName()
        );
        return mode == AppOpsManagerCompat.MODE_ALLOWED;
    }

    @Override
    public void sendListAppInfor(List<AppInfor> listAppInfor) {
        mAppInforAdapter.setListData(listAppInfor);
    }

    @Override
    public void sendLongestAppInfor(AppInfor appInforLongest) {
        mBinding.tvNameLongestApp.setText(appInforLongest.getmAppName());
        Glide.with(this).load(appInforLongest.getmIconApp()).into(mBinding.imgIconLongestApp);
    }

    @Override
    public void dismissLoading() {
        mProgessDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        mBinding = null;
        mMainPresenter = null;
        super.onDestroy();
    }
}