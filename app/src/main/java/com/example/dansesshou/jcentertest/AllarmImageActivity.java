package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import java.util.concurrent.TimeUnit;

import Utils.RxBUSAction;
import Utils.ToastUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.AlarmImageInfo;
import entity.AlarmInfo;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

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
    private AlarmImageInfo ImageInfo;
    private Context mContext;
    private Subscription ImageProgress;

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

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_ALARMIMAGE)
            }
    )
    public void GetAlarmImageInfo(AlarmImageInfo info) {
        this.ImageInfo=info;
        isGetProgress=false;
        Log.e("dxsTest","GetAlarmImageInfo:"+info.toString());
        if(info.getErrorCode()==0){
            if(ImageInfo!=null&& !TextUtils.isEmpty(ImageInfo.getPath())){
                Glide.with(mContext).load(ImageInfo.getPath()).into(ivAlarmpicture);
            }else{
                Glide.with(mContext).load(getLocalImagePath()).into(ivAlarmpicture);
            }
        }else{
            ToastUtils.ShowError(this, getString(R.string.error)+"("+info.getErrorCode()+")", Toast.LENGTH_SHORT, true);
        }
    }

    @OnClick(R.id.btn_getpicture)
    public void GetAlarmImage() {
        String userpwd=etPassword.getText().toString().trim();
        if (info != null) {
            if(isGetProgress){
                ToastUtils.ShowError(this, getString(R.string.downloading), Toast.LENGTH_SHORT, true);
                return;
            }
            String pwd = P2PHandler.getInstance().EntryPassword(userpwd);
            String imagpath = getImagePath();
            String LocalPath = getLocalImagePath();
            Log.e("dxsTest", "imagpath:" + imagpath + "LocalPath:" + LocalPath);
            P2PHandler.getInstance().GetAllarmImage(info.getSrcId(), pwd, imagpath, LocalPath);
            //如果没有UI上展示进度的需求，获取下载进度的代码可不写
            getImageProgress();
        } else {
            ToastUtils.ShowError(this, getString(R.string.no_alarm), Toast.LENGTH_SHORT, true);
        }
    }

    //报警时设备传的路径
    private String getImagePath() {
        return info.getAlarmCapDir() + "01.jpg";
    }

    //手机本地保存路径
    private String getLocalImagePath() {
        return Environment.getExternalStorageDirectory().getPath() + "/011.jpg";
    }


    boolean isGetProgress = false;
    private void getImageProgress(){
        final Subscriber<Integer> p=new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                isGetProgress=false;
                UnRegist();
                btnGetpicture.setText(R.string.getpicture);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.ShowError(mContext, getString(R.string.error), Toast.LENGTH_SHORT, true);
            }

            @Override
            public void onNext(Integer o) {
                btnGetpicture.setText(String.format(Locale.getDefault(), "正在加载(%d)", o));
            }
        };
        ImageProgress= Observable.interval(200, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        int ret=P2PHandler.getInstance().GetAllarmImageProgress();
                        if(ret>=85){
                            p.onCompleted();
                        }
                        return ret;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(p);
    }

    //解除订阅
    private void UnRegist(){
        if(ImageProgress!=null&&!ImageProgress.isUnsubscribed()){
            ImageProgress.unsubscribe();
        }
    }

}
