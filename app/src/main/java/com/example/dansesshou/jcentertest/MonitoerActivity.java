package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

public class MonitoerActivity extends BaseMonitorActivity implements View.OnClickListener{
    public static String P2P_ACCEPT = "com.yoosee.P2P_ACCEPT";
    public static String P2P_READY = "com.yoosee.P2P_READY";
    public static String P2P_REJECT = "com.yoosee.P2P_REJECT";
    private Button btnCall,btnTalk,btnSD,btnHD,btnLD,btnMute,btnScreenshot;
    private EditText et_callId;
    private EditText et_callPwd ;
    private String callID,CallPwd;
    private TextView tv_content,tx_acount;
    private String LoginID;
    private boolean isMute=false;
    OrientationEventListener mOrientationEventListener;
    private boolean mIsLand=false;
    private RelativeLayout rl_p2pview;
    private int screenWidth,screenHeigh;
    private LinearLayout llElse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        LoginID=getIntent().getStringExtra("LoginID");
        initUI();
        getScreenWithHeigh();
        regFilter();
        initData();
    }

    public void getScreenWithHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    private void initUI() {
        btnCall= (Button) findViewById(R.id.btn_call);
        btnTalk= (Button) findViewById(R.id.btn_talk);
        btnSD= (Button) findViewById(R.id.btn_sd);
        btnHD= (Button) findViewById(R.id.btn_hd);
        btnLD= (Button) findViewById(R.id.btn_ld);
        btnMute= (Button) findViewById(R.id.btn_mute);
        btnScreenshot= (Button) findViewById(R.id.btn_screenshot);

        pView= (P2PView) findViewById(R.id.p2pview);
        llElse= (LinearLayout) findViewById(R.id.layout_else);
        rl_p2pview= (RelativeLayout) findViewById(R.id.rl_p2pview);
        tv_content = (TextView) findViewById(R.id.tv_content);
        et_callId = (EditText) findViewById(R.id.et_id);
        et_callPwd = (EditText) findViewById(R.id.et_pwd);
        tx_acount= (TextView) findViewById(R.id.tx_acount);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
        btnCall.setOnClickListener(this);

        btnSD.setOnClickListener(this);
        btnHD.setOnClickListener(this);
        btnLD.setOnClickListener(this);
        btnMute.setOnClickListener(this);
        btnScreenshot.setOnClickListener(this);
        btnTalk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        setMute(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        setMute(true);
                        return true;
                }
                return false;
            }
        });
    }
    private void initData() {
        tx_acount.setText(LoginID);
        //此处是一种并不常见的横竖屏监听,客户可自行修改实现
        mOrientationEventListener = new OrientationEventListener(this) {

            @Override
            public void onOrientationChanged(int rotation) {
                // 设置横屏
                if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
                    if (mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mIsLand = false;
                        setHalfScreen(true);
                    }
                } else if (((rotation >= 230) && (rotation <= 310))) {
                    if (!mIsLand) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        mIsLand = true;
                        setHalfScreen(false);
                    }
                }
            }
        };
        mOrientationEventListener.enable();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("dxsTest","config:"+newConfig.orientation);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            setHalfScreen(false);
            llElse.setVisibility(View.GONE);
            //以下代码是因为 方案商设备类型很多,视频比例也比较多
            //客户更具自己的视频比例调整画布大小
            //这里的实现比较绕,如果能弄清楚这部分原理,客户可自行修改此处代码
            if (P2PView.type == 1) {
                if (P2PView.scale == 0) {
                    isFullScreen = false;
                    pView.halfScreen();//刷新画布比例
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            } else {
                //这里本应该用设备类型判断,如果只有一种类型可不用这么麻烦
                if (7 == P2PValue.DeviceType.NPC) {
                    isFullScreen = false;
                    pView.halfScreen();
                } else {
                    isFullScreen = true;
                    pView.fullScreen();
                }
            }
            LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rl_p2pview.setLayoutParams(parames);//调整画布容器宽高(比例)
        }else{
            setHalfScreen(true);
            llElse.setVisibility(View.VISIBLE);
            if (isFullScreen) {
                isFullScreen = false;
                pView.halfScreen();
            }
            //这里简写,只考虑了16:9的画面类型  大部分设备画面比例是这种
            int Heigh = screenWidth * 9 / 16;
            LinearLayout.LayoutParams parames = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            parames.height = Heigh;
            rl_p2pview.setLayoutParams(parames);
        }
    }

    @Override
    public void onClick(View v) {
        callID = et_callId.getText().toString().trim();//设备号
        CallPwd = et_callPwd.getText().toString().trim();
        String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
        switch (v.getId()){
            case R.id.btn_call:
                P2PHandler.getInstance().call(LoginID, pwd, true, 1,callID, "", "", 2,callID);
                break;
            case R.id.btn_hd:
                P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
                break;
            case R.id.btn_sd:
                P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
                break;
            case R.id.btn_ld:
                P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
                break;
            case R.id.btn_screenshot:
                // 参数是一个标记,截图回调会原样返回这个标记
                //注意SD卡权限
                captureScreen(-1);
                break;
            case R.id.btn_mute:
                changeMuteState();
                break;
        }
    }

    private void changeMuteState(){
        isMute=!isMute;
        AudioManager manager= (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(manager!=null){
            if(isMute){
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                btnMute.setText("");
            }else{
                manager.setStreamVolume(AudioManager.STREAM_MUSIC,manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
                btnMute.setText(R.string.mute);
            }
        }
    }

    public void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(P2P_ACCEPT)){
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                tv_content.append("\n 监控数据接收");
                Log.e("dxsTest","监控数据接收:"+callID);
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            }else if(intent.getAction().equals(P2P_READY)){
                tv_content.append("\n 监控准备,开始监控");
                Log.e("dxsTest","监控准备,开始监控"+callID);
                pView.sendStartBrod();
            }else if(intent.getAction().equals(P2P_REJECT)){
                tv_content.append("\n 监控挂断");
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().reject();
        //此处disconnect是demo写法,正式工程只需在app结束时调用一次,与connect配对使用
        P2PHandler.getInstance().p2pDisconnect();
        super.onDestroy();

    }

    @Override
    protected void onP2PViewSingleTap() {

    }

    @Override
    protected void onP2PViewFilling() {

    }


    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        if(isSuccess){
            Toast.makeText(this,R.string.screenshot_success,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,R.string.screenshot_error,Toast.LENGTH_LONG).show();
        }
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
