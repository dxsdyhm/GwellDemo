package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.gwelldemo.R;

import entity.Device;
import entity.LocalDevice;

/**
 * Created by dansesshou on 17/2/17.
 */

public class DeviceActivity extends AppCompatActivity {
    private Context mContext;
    private TextView txInfo;
    private Device device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        mContext=this;
        device= (Device) getIntent().getSerializableExtra("device");
        initUI();
    }

    private void initUI() {
        txInfo= (TextView) findViewById(R.id.tx_deviceinfo);
        txInfo.setText(device.toString());

    }

    private void getDeviceInfo(){

    }
}
