package com.example.dansesshou.jcentertest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gwelldemo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utils.BaseView;
import adapter.LocalDeviceAdapter;
import entity.Device;
import shake.ShakeData;
import shake.ShakeListener;
import shake.ShakeManager;
import udpsender.UDPManger;
import udpsender.UDPResult;
import udpsender.UDPResultCallback;

/**
 * Created by dansesshou on 17/2/17.
 */

public class DeviceTestActivity extends AppCompatActivity implements BaseView {
    private Context mContext;
    private RecyclerView deviceList;
    private List<Device> devices = new ArrayList<>();
    private Button btnSearch;
    private ProgressDialog mProgressDialog;
    private LocalDeviceAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_devicetest);
        initUI();
        initData();
    }

    private void initUI() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.searching));
        deviceList = (RecyclerView) findViewById(R.id.rc_devicelist);
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        btnSearch = (Button) findViewById(R.id.btn_search);
        adapter = new LocalDeviceAdapter(this, devices);
        adapter.setOnItemClickLinstener(new LocalDeviceAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Device device) {
                startActivity(new Intent(mContext, DeviceActivity.class).putExtra("device", device));
            }
        });
        deviceList.setAdapter(adapter);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getLocalDevice();
    }

    /**
     * 获取局域网内的设备
     */
    private void getLocalDevice() {
        if (devices.size() > 0) {
            devices.clear();//清除当前设备（保持每次搜索都能拿到最新的设备）
        }
        ShakeManager.getInstance()
                .setSearchTime(5000)//设置搜索时间（时间的毫秒值），默认10s
                .shaking(new ShakeListener() {//开始搜索，并回调
                    @Override
                    public void onStart() {
                        showProgress();
                    }

                    @Override
                    public void onNext(Device device) {
                        boolean isExisted = false;
                        if (devices != null && devices.size() > 0) {
                            for (Device localDevice : devices) {
                                if (localDevice.getId().equals(device.getId())) {
                                    isExisted = true;
                                    break;
                                }
                            }
                        }
                        if (!isExisted) {
                            devices.add(device);
                            Collections.sort(devices);
                            adapter.notifyDataSetChanged();//可以搜索到一个就刷新一次列表
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showMsg("任务正在执行中....");
                    }

                    @Override
                    public void onCompleted() {
                        hideProgress();
                        //adapter.notifyDataSetChanged();//可以搜索完成之后再刷新列表
                    }
                });
    }

    @Override
    public void showProgress() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
