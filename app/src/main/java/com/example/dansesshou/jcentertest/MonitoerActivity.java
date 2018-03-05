package com.example.dansesshou.jcentertest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Utils.ToastUtils;
import Utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MonitoerActivity extends BaseMonitorActivity {
    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
    public static String P2P_READY = "com.XXX.P2P_READY";
    public static String P2P_REJECT = "com.XXX.P2P_REJECT";
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
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.layout_else)
    LinearLayout layoutElse;
    @BindView(R.id.activity_monitoer)
    LinearLayout activityMonitoer;
    @BindView(R.id.btn_record)
    Button btnRecord;
    @BindView(R.id.btn_palyback)
    Button btnPalyback;
    @BindView(R.id.rl_p2pview)
    RelativeLayout rlP2pview;
    private String callID, CallPwd;
    private String userId;
    private boolean isMute = false;
    OrientationEventListener mOrientationEventListener;
    private boolean mIsLand = false;
    private int screenWidth, screenHeigh;
    private int recordFlag = 0;
    private String pathName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoer);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra(LoginActivity.USERID);
        initUI();
        checkCamerPermission();
        getScreenWithHeigh();
        regFilter();
        initData();
    }

    private void checkCamerPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        // 已经获取权限
                        Log.e("MonitoerActivity","已授予CAMERA权限");
                    } else {
                        // 未获取权限
                        ToastUtils.ShowError(MonitoerActivity.this, "您没有授权CAMERA权限，请在设置中打开授权", Toast.LENGTH_SHORT,true);
                    }
                });
    }

    public void getScreenWithHeigh() {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeigh = dm.heightPixels;
    }

    private void initUI() {
        txAcount.setText(userId);
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
        if(TextUtils.isEmpty(callID)||TextUtils.isEmpty(CallPwd)){
            ToastUtils.ShowError(this,getString(R.string.tips_idpwd),2000,true);
            return;
        }
        String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
        P2PHandler.getInstance().call(userId, pwd, true, 1, callID, "", "", 2, callID);
    }

    @OnClick(R.id.btn_sd)
    void SDOnclick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
    }

    @OnClick(R.id.btn_ld)
    void LDOnClick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
    }

    @OnClick(R.id.btn_hd)
    void HDOnClick() {
        P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
    }

    @OnClick(R.id.btn_screenshot)
    void ScreenShotClock() {
        // 参数是一个标记,截图回调会原样返回这个标记
        //注意SD卡权限
        //int d = P2PHandler.getInstance().setScreenShotpath("/sdcard/11/22/33", "123.jpg");
        checkSDPermision();
    }

    private void checkSDPermision(){
        RxPermissions rxPermissions = new RxPermissions(this);
        boolean result=false;
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        // 已经获取权限
                        int d = P2PHandler.getInstance().setScreenShotpath(Util.getScreenShotPath(), "123.jpg");
                        Log.e("dxsTest", "d:" + d);
                        captureScreen(-1);
                        Log.e("MonitoerActivity","已授予STORAGE权限");
                    } else {
                        // 未获取权限
                        ToastUtils.ShowError(MonitoerActivity.this, "您没有授权STORAGE权限，请在设置中打开授权", Toast.LENGTH_SHORT,true);
                    }
                });
    }

    @OnClick(R.id.btn_mute)
    void MuteOnClick() {
        changeMuteState();
    }

    @OnClick(R.id.btn_record)
    public void onClick() {
        if (recordFlag == 0) {
            //开始录像
            startMoniterRecoding();
            recordFlag = 1;
            btnRecord.setText(getResources().getText(R.string.stop_record));
        } else {
            //停止录像
            stopMoniterReocding();
            recordFlag = 0;
            btnRecord.setText(getResources().getText(R.string.record));
        }
    }

    @OnClick(R.id.btn_palyback)
    public void onPalyBack() {
        callID = etId.getText().toString().trim();//设备号
        CallPwd = etPwd.getText().toString().trim();
        Intent playback = new Intent(this, RecordFilesActivity.class);
        playback.putExtra("callID", callID);
        playback.putExtra("callPwd", CallPwd);
        startActivity(playback);
        P2PHandler.getInstance().reject();
        finish();
    }

    public void startMoniterRecoding() {

        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                    Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "gwellvideorec" + File.separator + callID;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                long time = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());// 制定日期的显示格式
                String name = "gwell" + "_" + sdf.format(new Date(time));
                pathName = file.getPath() + File.separator + name + ".mp4";
            } else {
                throw new NoSuchFieldException("sd卡");
            }
        } catch (NoSuchFieldException | NullPointerException e) {
            Toast.makeText(MonitoerActivity.this, " 没有内存卡", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.e("dxsTest", "pathName:" + pathName);
        if (P2PHandler.getInstance().starRecoding(pathName)) {
            Toast.makeText(MonitoerActivity.this, " 正在录像", Toast.LENGTH_SHORT).show();
        } else {
            //录像初始化失败
            Toast.makeText(MonitoerActivity.this, " 初始化录像失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopMoniterReocding() {
        if (P2PHandler.getInstance().stopRecoding() == 0) {
            //录像不正常
            Toast.makeText(MonitoerActivity.this, " 录像不正常", Toast.LENGTH_SHORT).show();
        } else {
            //正常停止
            Toast.makeText(MonitoerActivity.this, " 停止录像", Toast.LENGTH_SHORT).show();
        }
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
                int reason_code = intent.getIntExtra("reason_code", -1);
                int code1 = intent.getIntExtra("exCode1", -1);
                int code2 = intent.getIntExtra("exCode2", -1);
                String reject = String.format("\n 监控挂断(reson:%d,code1:%d,code2:%d)", reason_code, code1, code2);
                tvContent.append(reject);
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().finish();
        super.onDestroy();
    }

    @Override
    protected void onP2PViewSingleTap() {

    }

    @Override
    protected void onP2PViewFilling() {

    }

    @Override
    protected void turnCamera() {

    }


    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        if (isSuccess) {
            ToastUtils.ShowSuccess(this, getString(R.string.screenshot_success), Toast.LENGTH_LONG, true);
        } else {
            ToastUtils.ShowError(this, getString(R.string.screenshot_error), Toast.LENGTH_LONG, true);
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
