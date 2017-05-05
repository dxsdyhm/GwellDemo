package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.P2PHandler;

import Utils.Contants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import service.MainService;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_play_back)
    Button btnPlayBack;
    @BindView(R.id.btn_getalarm_picture)
    Button btnGetalarmPicture;
    @BindView(R.id.tx_alert)
    TextView txAlert;
    @BindView(R.id.btn_alarmlist)
    Button btnAlarmlist;
    private Context mContext;
    String LoginID;
    @BindView(R.id.btn_test)
    Button btnIn;
    @BindView(R.id.btn_moniter)
    Button btnMoniter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LoginID = getIntent().getStringExtra("LoginID");
        initUI();
        initData();
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        registReg();
    }

    private void registReg() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Contants.P2P_CONNECT);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connect = intent.getBooleanExtra("connect", false);
            //p2p连接失败  相应处理，用户可以根据具体情况自定义
            if (!connect) {
                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                txAlert.setVisibility(View.VISIBLE);
                btnPlayBack.setEnabled(false);
                btnGetalarmPicture.setEnabled(false);
                btnIn.setEnabled(false);
                btnMoniter.setEnabled(false);
            }
        }
    };

    private void initUI() {

    }

    private void initData() {
        String[] name = new String[]{"1092482"};
        P2PHandler.getInstance().getFriendStatus(name, 1);
    }

    @OnClick(R.id.btn_test)
    public void toDeviceTest() {
        Log.e("dxsTest", "toDeviceTest" + LoginID);
        startActivity(new Intent(this, DeviceTestActivity.class));
    }

    @OnClick(R.id.btn_moniter)
    public void toMoniter() {
        Intent moniter = new Intent(this, MonitoerActivity.class);
        moniter.putExtra("LoginID", LoginID);
        startActivity(moniter);
        Log.e("dxsTest", "toMoniter" + LoginID);
    }

    @OnClick(R.id.btn_play_back)
    public void onClick() {
        Intent record = new Intent(this, RecordFilesActivity.class);
        record.putExtra("LoginID", LoginID);
        startActivity(record);
    }

    @OnClick(R.id.btn_getalarm_picture)
    public void GetAllarmImage() {
        Intent record = new Intent(this, AllarmImageActivity.class);
        record.putExtra("LoginID", LoginID);
        startActivity(record);
    }
    @OnClick(R.id.btn_alarmlist)
    public void AlarmList(){
        Intent record = new Intent(this, AllarmImageListActivity.class);
        record.putExtra("LoginID", LoginID);
        startActivity(record);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //此处disconnect是demo写法,正式工程只需在app结束时调用一次,与connect配对使用
        P2PHandler.getInstance().p2pDisconnect();
    }
}
