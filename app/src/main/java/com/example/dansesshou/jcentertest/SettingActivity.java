package com.example.dansesshou.jcentertest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.gwelldemo.R;
import com.p2p.core.P2PHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tx_device_id)
    EditText txDeviceId;
    @BindView(R.id.tx_device_pwd)
    EditText txDevicePwd;
    @BindView(R.id.btn_alarm_setting)
    Button btnAlarmSetting;

    private String idOrIp, password, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        SharedPreferences sp = getSharedPreferences("Account", MODE_PRIVATE);
        userId = sp.getString("userId", null);

    }

    @OnClick(R.id.btn_alarm_setting)
    public void onAlarmSetting() {
        idOrIp = txDeviceId.getText().toString().trim();
        password = txDevicePwd.getText().toString().trim();
        password = P2PHandler.getInstance().EntryPassword(password);//经过转换后的设备密码

        Intent intent = new Intent(this, AlarmSettingActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("idOrIp", idOrIp);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}
