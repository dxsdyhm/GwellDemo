package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwelldemo.R;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Observer;
import java.util.concurrent.TimeUnit;

import Utils.RxBUSAction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.AlarmInfo;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpicture);
        ButterKnife.bind(this);
        LoginID = getIntent().getStringExtra("LoginID");
        txUserid.setText(LoginID);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_ALARM)
            }
    )
    public void initUI(AlarmInfo info) {
        txAlarmid.setText(info.getSrcId());
        txAlarmInfo.setText(info.toString());
    }
    @OnClick(R.id.btn_getalarm_picture)
    public void GetAlarmImage(){

    }

    private void getAlarmImage(){
        Observable<Integer> sender=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        });
    }


}
