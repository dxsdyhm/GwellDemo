package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gwelldemo.R;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PView;

public class MonitoerActivity extends BaseMonitorActivity implements View.OnClickListener{
    public static String P2P_ACCEPT = "com.yoosee.P2P_ACCEPT";
    public static String P2P_READY = "com.yoosee.P2P_READY";
    public static String P2P_REJECT = "com.yoosee.P2P_REJECT";
    private Button btnCall;
    private EditText et_callId;
    private EditText et_callPwd ;
    private String callID,CallPwd;
    private TextView tv_content,tx_acount;
    private String LoginID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        LoginID=getIntent().getStringExtra("LoginID");
        initUI();
        regFilter();
        initData();
    }

    private void initUI() {
        btnCall= (Button) findViewById(R.id.btn_call);
        pView= (P2PView) findViewById(R.id.p2pview);
        tv_content = (TextView) findViewById(R.id.tv_content);
        et_callId = (EditText) findViewById(R.id.et_id);
        et_callPwd = (EditText) findViewById(R.id.et_pwd);
        tx_acount= (TextView) findViewById(R.id.tx_acount);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
        btnCall.setOnClickListener(this);
    }

    private void initData() {
        tx_acount.setText(LoginID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_call:
                callID = et_callId.getText().toString().trim();//设备号
                CallPwd = et_callPwd.getText().toString().trim();
                String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
                Log.e("dxsTest","LoginID:"+LoginID+"callID:"+callID);
                P2PHandler.getInstance().call(LoginID, pwd, true, 1,callID, "", "", 2,callID);
                break;
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().reject();
        P2PHandler.getInstance().p2pDisconnect();
        super.onDestroy();

    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(P2P_ACCEPT)){
                tv_content.append("\n 监控数据接收");
                Log.e("dxsTest","监控数据接收:"+callID);
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            }else if(intent.getAction().equals(P2P_READY)){
                tv_content.append("\n 监控准备,开始监控");
                Log.e("dxsTest","监控准备,开始监控"+callID);
                pView.sendStartBrod();
                setMute(true);  //设置手机静音
            }else if(intent.getAction().equals(P2P_REJECT)){
                tv_content.append("\n 监控挂断");
            }
        }
    };

    @Override
    protected void onP2PViewSingleTap() {

    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {

    }

    @Override
    protected void onVideoPTS(long videoPTS) {

    }

    @Override
    public int getActivityInfo() {
        return 0;
    }

    @Override
    protected void onGoBack() {

    }

    @Override
    protected void onGoFront() {

    }

    @Override
    protected void onExit() {

    }
}
