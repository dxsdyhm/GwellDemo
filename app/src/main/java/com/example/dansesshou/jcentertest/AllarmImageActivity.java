package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gwelldemo.R;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.util.Locale;

import Utils.RxBUSAction;
import Utils.ToastUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.AlarmInfo;
import rx.Observable;

/**
 * Created by USER on 2017/3/21.
 */

public class AllarmImageActivity extends BaseActivity {
    @BindView(R.id.tx_userid)
    TextView txUserid;
    @BindView(R.id.tx_alarmid)
    TextView txAlarmid;
    @BindView(R.id.tx_alarm_info)
    TextView txAlarmInfo;
    @BindView(R.id.btn_getpicture)
    Button btnGetpicture;
    @BindView(R.id.tx_tips)
    TextView txTips;
    @BindView(R.id.iv_alarmpicture)
    ImageView ivAlarmpicture;
    @BindView(R.id.et_password)
    AutoCompleteTextView etPassword;
    private String LoginID;
    private AlarmInfo info;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpicture);
        ButterKnife.bind(this);
        mContext = this;
        LoginID = getIntent().getStringExtra("LoginID");
        txUserid.setText("User ID:");
        txUserid.append(LoginID);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_ALARM)
            }
    )
    public void initUI(AlarmInfo info) {
        this.info = info;
        txAlarmid.setText("Alarm ID:");
        txAlarmid.setText(info.getSrcId());
        txAlarmInfo.setText(info.toString());
    }

    @OnClick(R.id.btn_getpicture)
    public void GetAlarmImage() {
        String userpwd=etPassword.getText().toString().trim();
        if (info != null) {
            String pwd = P2PHandler.getInstance().EntryPassword(userpwd);
            String imagpath = getImagePath();
            String LocalPath = getLocalImagePath();
            Log.e("dxsTest", "imagpath:" + imagpath + "LocalPath:" + LocalPath);
            P2PHandler.getInstance().GetAllarmImage(info.getSrcId(), pwd, imagpath, LocalPath);
            isGetProgress = true;
            new MyThread().start();
        } else {
            ToastUtils.ShowError(this, getString(R.string.no_alarm), Toast.LENGTH_SHORT, true);
        }
    }

    private String getImagePath() {
        return info.getAlarmCapDir() + "01.jpg";
    }

    private String getLocalImagePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/011.jpg";
    }


    boolean isGetProgress = false;
    private Handler prohandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            int progress = msg.what;
            btnGetpicture.setText(String.format(Locale.getDefault(), "正在加载(%d)", progress));
            if (progress >= 80) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isGetProgress = false;
                        Glide.with(mContext).load(getLocalImagePath()).into(ivAlarmpicture);
                        ToastUtils.ShowSuccess(mContext, getString(R.string.download_success), Toast.LENGTH_LONG, true);
                    }
                },4*1000);
            }
            return false;
        }
    });

    private Observable<Integer> GetProgress() {
        return null;
    }

    class MyThread extends Thread {
        int progress = 0;

        public void run() {
            isGetProgress = true;
            while (isGetProgress) {
                progress = P2PHandler.getInstance().GetAllarmImageProgress();
                prohandler.sendEmptyMessage(progress);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


}
