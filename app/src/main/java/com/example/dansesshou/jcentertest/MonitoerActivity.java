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

import Utils.ToastUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonitoerActivity extends BaseMonitorActivity{
    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
    public static String P2P_READY = "com.XXX.P2P_READY";
    public static String P2P_REJECT = "com.XXX.P2P_REJECT";
    @BindView(R.id.rl_p2pview)
    RelativeLayout rlP2pview;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.btn_sd)
    Button btnSd;
    @BindView(R.id.btn_hd)
    Button btnHd;
    @BindView(R.id.btn_ld)
    Button btnLd;
    @BindView(R.id.tx_acount)
    TextView txAcount;
    @BindView(R.id.btn_screenshot)
    Button btnScreenshot;
    @BindView(R.id.btn_mute)
    Button btnMute;
    @BindView(R.id.btn_talk)
    Button btnTalk;
    @BindView(R.id.btn_no)
    Button btnNo;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.layout_else)
    LinearLayout layoutElse;
    @BindView(R.id.activity_monitoer)
    LinearLayout activityMonitoer;
    private String callID, CallPwd;
    private String LoginID;
    private boolean isMute = false;
    OrientationEventListener mOrientationEventListener;
    private boolean mIsLand = false;
    private int screenWidth, screenHeigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        ButterKnife.bind(this);
        LoginID = getIntent().getStringExtra("LoginID");
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
        txAcount.setText(LoginID);
        //pView已在父类声明，不要在子类重复
        pView = (P2PView) findViewById(R.id.p2pview);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
        btnTalk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
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
        Log.e("dxsTest", "config:" + newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setHalfScreen(false);
            layoutElse.setVisibility(View.GONE);
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
            rlP2pview.setLayoutParams(parames);//调整画布容器宽高(比例)
        } else {
            setHalfScreen(true);
            layoutElse.setVisibility(View.VISIBLE);
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
            rlP2pview.setLayoutParams(parames);
        }
    }
    @OnClick(R.id.btn_call)
    void CallOnClick(){
        callID = etId.getText().toString().trim();//设备号
        CallPwd = etPwd.getText().toString().trim();
        String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
        P2PHandler.getInstance().call(LoginID, pwd, true, 1, callID, "", "", 2, callID);
    }

    @OnClick(R.id.btn_sd)
    void SDOnclick(){
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
    }
    @OnClick(R.id.btn_ld)
    void LDOnClick(){
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
    }
    @OnClick(R.id.btn_hd)
    void HDOnClick(){
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
    }
    @OnClick(R.id.btn_screenshot)
    void ScreenShotClock(){
        // 参数是一个标记,截图回调会原样返回这个标记
        //注意SD卡权限
        int d = P2PHandler.getInstance().setScreenShotpath("/sdcard/11/22/33", "123.jpg");
        Log.e("dxsTest", "d:" + d);
        captureScreen(-1);
    }
    @OnClick(R.id.btn_mute)
    void MuteOnClick(){
        changeMuteState();
    }

    private void changeMuteState() {
        isMute = !isMute;
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            if (isMute) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                btnMute.setText("");
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
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
            if (intent.getAction().equals(P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                tvContent.append("\n 监控数据接收");
                Log.e("dxsTest", "监控数据接收:" + callID);
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            } else if (intent.getAction().equals(P2P_READY)) {
                tvContent.append("\n 监控准备,开始监控");
                Log.e("dxsTest", "监控准备,开始监控" + callID);
                pView.sendStartBrod();
            } else if (intent.getAction().equals(P2P_REJECT)) {
                tvContent.append("\n 监控挂断");
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
        if (isSuccess) {
            ToastUtils.ShowSuccess(this, getString(R.string.screenshot_success), Toast.LENGTH_LONG,true);
        } else {
            ToastUtils.ShowError(this, getString(R.string.screenshot_error), Toast.LENGTH_LONG,true);
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
