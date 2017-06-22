package com.example.dansesshou.jcentertest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gwelldemo.R;
import com.hdl.elog.ELog;
import com.jwkj.shakmanger.LocalDevice;
import com.jwkj.shakmanger.ShakeListener;
import com.jwkj.shakmanger.ShakeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Utils.BaseView;
import Utils.ToastUtils;
import adapter.LocalDeviceAdapter;

/**
 * 搜索局域网设备
 * Created by dansesshou on 17/2/17.
 */

public class DeviceTestActivity extends AppCompatActivity implements BaseView {
    private Context mContext;
    private RecyclerView rvDeviceList;
    private List<LocalDevice> devices = new ArrayList<>();
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
        rvDeviceList = (RecyclerView) findViewById(R.id.rc_devicelist);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        btnSearch = (Button) findViewById(R.id.btn_search);
        adapter = new LocalDeviceAdapter(this, devices);
        adapter.setOnItemClickLinstener(new LocalDeviceAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, LocalDevice device) {
                startActivity(new Intent(mContext, DeviceActivity.class).putExtra("device", device));
            }
        });
        rvDeviceList.setAdapter(adapter);
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
//                .schedule(2, 3000)//（默认只会执行一次）执行两次扫描任务，间隔3s执行（上一次执行结束到下一次开始之间的时间）
                .shaking(new ShakeListener() {//开始搜索，并回调
                    @Override
                    public void onStart() {
                        showProgress();
                    }

                    @Override
                    public void onNext(LocalDevice device) {
                        //去重处理
                        boolean isExisted = false;
                        if (devices != null && devices.size() > 0) {
                            for (LocalDevice localDevice : devices) {
                                if (localDevice.getId().equals(device.getId())) {
                                    isExisted = true;
                                    break;
                                }
                            }
                        }
                        if (!isExisted) {
                            devices.add(device);
                            Collections.sort(devices);
                            adapter.notifyDataSetChanged();//搜索到一个就刷新一次列表
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ELog.hdl("error msg :" + throwable);
                        showMsg(getString(R.string.shake_task_running));
                    }

                    @Override
                    public void onCompleted() {
                        hideProgress();
                        showSuccessMsg(getString(R.string.shake_task_complete));
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
        ToastUtils.ShowError(mContext, msg, Toast.LENGTH_SHORT, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ShakeManager.getInstance().closeShake();
    }

    /**
     * 显示成功之后的消息
     *
     * @param msg
     */
    public void showSuccessMsg(String msg) {
        ToastUtils.ShowSuccess(mContext, msg, Toast.LENGTH_SHORT, true);
    }
}
