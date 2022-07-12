package com.example.baseproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AppOpsManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.baseproject.R;
import com.example.baseproject.databinding.ActivityTrackerUsageBinding;
import com.example.baseproject.model.AppInfor;
import com.example.baseproject.service.TrackerUsageService;
import com.example.baseproject.viewmodel.TrackerUsageViewModel;

import java.util.Collections;
import java.util.Comparator;

public class TrackerUsageActivity extends AppCompatActivity {

    private static final String TAG = "TrackerUsageActivity";
    private ActivityTrackerUsageBinding mBinding;
    private TrackerUsageViewModel mViewModel;
    private AppInforAdapter mAppInforAdapter;
    private ProgressDialog mProgessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_usage);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tracker_usage);
        mBinding.executePendingBindings();
        mViewModel = new ViewModelProvider(this).get(TrackerUsageViewModel.class);
        mBinding.setLifecycleOwner(this);
        setUpRecycleView();
        setUpObservable();
        createDialog();
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, TrackerUsageService.class);
        startService(intent);
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

    private void setUpObservable() {
        mViewModel.getListApp().observe(this, inforApps -> {
            mProgessDialog.dismiss();
            mAppInforAdapter.setListData(inforApps);
        });

        mViewModel.getLongestAppInfor().observe(this, inforApp -> {
            mBinding.tvNameLongestApp.setText(inforApp.getmAppName());
            Glide.with(this).load(inforApp.getmIconApp()).into(mBinding.imgIconLongestApp);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleGetData();
    }

    private void handleGetData() {
        if (checkUsageStatsPermission()) {
            mViewModel.requestInfoAppList();
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
    protected void onDestroy() {
        mBinding = null;
        super.onDestroy();
    }
}