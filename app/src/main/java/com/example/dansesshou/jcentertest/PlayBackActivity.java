package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gwelldemo.R;
import com.p2p.core.BasePlayBackActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.RecordFile;

public class PlayBackActivity extends BasePlayBackActivity {

    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
    public static String P2P_READY = "com.XXX.P2P_READY";
    public static String P2P_REJECT = "com.XXX.P2P_REJECT";
    @BindView(R.id.btn_palyback)
    Button btnPalyback;
    @BindView(R.id.tx_text)
    TextView txText;
    @BindView(R.id.rl_p2pview)
    RelativeLayout rlP2pview;

    private RecordFile recordFile;
    private String deviceId;
    private String devicePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("recordFile");
        recordFile = (RecordFile) bundle.getSerializable("file");
        deviceId = getIntent().getStringExtra("deviceId");
        devicePwd = getIntent().getStringExtra("devicePwd");
        initp2pView();
        regFilter();
    }

    public void initp2pView() {
        pView = (P2PView) findViewById(R.id.pview);
        //7是设备类型(技威定义的)
        //LAYOUTTYPE_TOGGEDER 录像回放连接命令和P2P_ACCEPT、P2P_READY、P2P_REJECT等命令在同一界面
        //LAYOUTTYPE_SEPARATION 录像回放连接命令和P2P_ACCEPT、P2P_READY、P2P_REJECT等命令不在同一界面
        this.initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);
        pView.halfScreen();
    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {

    }

    @Override
    protected void onVideoPTS(long videoPTS) {

    }

    @Override
    protected void onP2PViewSingleTap() {

    }


    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                txText.append("\n 监控数据接收");
                Log.e("dxsTest", "监控数据接收:" + deviceId);
                P2PHandler.getInstance().openAudioAndStartPlaying(2);//打开音频并准备播放，calllType与call时type一致
            } else if (intent.getAction().equals(P2P_READY)) {
                txText.append("\n 监控准备,开始监控");
                Log.e("dxsTest", "监控准备,开始监控" + deviceId);
                pView.sendStartBrod();
            } else if (intent.getAction().equals(P2P_REJECT)) {
                txText.append("\n 监控挂断");
            }
        }
    };

    @OnClick(R.id.btn_palyback)
    public void onClick() {
        play();
    }

    public void play() {
        String filename = recordFile.getName();
        //录像回放连接
        P2PHandler.getInstance().playbackConnect(deviceId,
                devicePwd, filename, recordFile.getPosition(), 0, 0, 896, 896, 0);
    }

    @Override
    public int getActivityInfo() {
        return 33;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
