package com.example.dansesshou.jcentertest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.P2PHandler;
import com.p2p.core.pano.BasePanoActivity;
import com.p2p.core.pano.PanoManager;
import com.p2p.core.pano.PanoParentRelativelayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import Utils.ToastUtils;
import Utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注意：
 * 1.需要实现：@see sdk.P2PListener#vRetPlaySize 以设置宽高
 * 2.需要引入库：compile 'com.jwkj:gwell-pano:1.4.0'
 * 3.此页面未展示功能同普通设备监控页功能
 */

public class PanoramaActivity extends BasePanoActivity {

    @BindView(R.id.btn_screenshot)
    Button btnScreenshot;
    @BindView(R.id.btn_change_four_split)
    Button btnChangeFourSplit;
    @BindView(R.id.btn_change_two_split)
    Button btnChangeTwoSplit;
    @BindView(R.id.btn_change_mix)
    Button btnChangeMix;
    @BindView(R.id.btn_change_column)
    Button btnChangeColumn;
    @BindView(R.id.btn_change_circle)
    Button btnChangeCircle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.layout_else)
    LinearLayout layoutElse;
    @BindView(R.id.activity_monitoer)
    LinearLayout activityMonitoer;
    PanoParentRelativelayout rlParent;
    RelativeLayout rlP2pview;
    private static final String TAG = "PanoramaActivity";
    public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
    public static String P2P_READY = "com.XXX.P2P_READY";
    public static String P2P_REJECT = "com.XXX.P2P_REJECT";
    private String callID, CallPwd;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra(LoginActivity.USERID);
        callID = getIntent().getStringExtra("callID");
        CallPwd = getIntent().getStringExtra("CallPwd");
        initPanoSDK();
        checkCamerPermission();
        regFilter();
        PanoManager.getInstance().cutShape(1);
    }

    private void regFilter() {
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
                P2PHandler.getInstance().openAudioAndStartPlaying(1, 1);
                tvContent.append("P2P_ACCEPT\n");
            } else if (intent.getAction().equals(P2P_READY)) {
                starPlay();
                tvContent.append("P2P_READY\n");
            } else if (intent.getAction().equals(P2P_REJECT)) {
                int reason_code = intent.getIntExtra("reason_code", -1);
                int code1 = intent.getIntExtra("exCode1", -1);
                int code2 = intent.getIntExtra("exCode2", -1);
                String reject = String.format("\n 监控挂断(reson:%d,code1:%d,code2:%d)", reason_code, code1, code2);
                tvContent.append(reject);
            }
        }
    };

    private void checkCamerPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        // 已经获取权限
                        Log.e("PanoramaActivity", "已授予CAMERA权限");
                    } else {
                        // 未获取权限
                        ToastUtils.ShowError(PanoramaActivity.this, "您没有授权CAMERA权限，请在设置中打开授权", Toast.LENGTH_SHORT, true);
                    }
                });
    }

    @Override
    public PanoParentRelativelayout initParent() {
        rlParent = findViewById(R.id.rl_parent);
        return rlParent;
    }

    @Override
    public ViewGroup initRelativeLayout() {
        rlP2pview = findViewById(R.id.rl_p2pview);
        return rlP2pview;
    }

    @Override
    public void callDevice() {
        Log.e(TAG, "callDevice: callID:" + callID + ",CallPwd:" + CallPwd + "\n");
        String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
        P2PHandler.getInstance().call(userId, pwd, true, 1, callID, "", "", 2, callID);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvContent.append("callDevice callID：" + callID + ",CallPwd:" + CallPwd + "\n");
            }
        });
    }

    @Override
    public void onCaptureSreenShot(boolean result, String path) {
        Log.e(TAG, "onCaptureSreenShot: result:" + result + ",path:" + path);
        if (result) {
            ToastUtils.ShowSuccess(this, getString(R.string.screenshot_success), Toast.LENGTH_LONG, true);
        } else {
            ToastUtils.ShowError(this, getString(R.string.screenshot_error), Toast.LENGTH_LONG, true);
        }
    }

    @Override
    public int getSubType() {
        //此处传设备子类型
        return 36;
    }

    @Override
    public void captureHeader() {

    }

    @Override
    public void onParentClick(View group) {

    }

    @Override
    protected void onVideoPTS(long videoPTS) {

    }

    @Override
    protected void onAvBytesPerSec(int videoPTS) {

    }

    @Override
    public int getActivityInfo() {
        return 100000;
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

    @OnClick({R.id.btn_screenshot, R.id.btn_change_four_split, R.id.btn_change_two_split, R.id.btn_change_mix, R.id.btn_change_column, R.id.btn_change_circle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_screenshot:
                checkSDPermision();
                break;
            case R.id.btn_change_four_split:
                //四分屏
                setShowMode(PanoManager.PM_QUARTER);
                break;
            case R.id.btn_change_two_split:
                //二分屏
                setShowMode(PanoManager.PM_2PLANE);
                break;
            case R.id.btn_change_mix:
                //混合
                setShowMode(PanoManager.PM_NAVMIX);
                break;
            case R.id.btn_change_column:
                //圆柱
                setShowMode(PanoManager.PM_CYLINDER);
                break;
            case R.id.btn_change_circle:
                //圆
                setShowMode(PanoManager.PM_NAVSPHERE);
                break;
        }
    }

    private void checkSDPermision() {
        RxPermissions rxPermissions = new RxPermissions(this);
        boolean result = false;
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        // 已经获取权限
                        capture(0);
                        Log.e("PanoramaActivity", "已授予STORAGE权限");
                    } else {
                        // 未获取权限
                        ToastUtils.ShowError(PanoramaActivity.this, "您没有授权STORAGE权限，请在设置中打开授权", Toast.LENGTH_SHORT, true);
                    }
                });
    }

    //Unity截图 0:普通截图  1:头像
    private void capture(int type) {
        String path = Util.getScreenShotPath() + "/test/123.jpg";
        Log.e(TAG, "capture: path:" + path);
        captureScreenUnity(-1, path);
    }

    //挂断
    private void reject() {
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().finish();
        finish();
    }

    @Override
    public void onHomePressed() {
        reject();
        super.onHomePressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            reject();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
