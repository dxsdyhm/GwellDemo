package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.util.Locale;

import Utils.RxBUSAction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.AlarmInfo;
import es.dmoral.toasty.Toasty;
import rx.Observable;
import rx.Subscriber;

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
    private String LoginID;
    private AlarmInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpicture);
        ButterKnife.bind(this);
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
        this.info=info;
        txAlarmid.setText("Alarm ID:");
        txAlarmid.setText(info.getSrcId());
        txAlarmInfo.setText(info.toString());
    }
    @OnClick(R.id.btn_getpicture)
    public void GetAlarmImage(){
        if(info!=null){
            String pwd=P2PHandler.getInstance().EntryPassword("qwe123");
            String imagpath=getImagePath();
            String LocalPath=getLocalImagePath();
            Log.e("dxsTest","imagpath:"+imagpath+"LocalPath:"+LocalPath);
            P2PHandler.getInstance().GetAllarmImage(info.getSrcId(),pwd,imagpath,LocalPath);
            isGetProgress = true;
            new MyThread().start();
        }else{
            Toasty.error(this, getString(R.string.no_alarm), Toast.LENGTH_SHORT, true).show();
        }
    }

    private String getImagePath() {
        return info.getAlarmCapDir()+"01.jpg";
    }

    private String getLocalImagePath(){
        return Environment.getExternalStorageDirectory().getPath()+"/011.jpg";
    }


    boolean isGetProgress=false;
    private Handler prohandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            int progress=msg.what;
            btnGetpicture.setText(String.format(Locale.getDefault(),"正在加载(%d)",progress));
            if(progress>=99){
                isGetProgress = false;
            }
            return false;
        }
    });

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
