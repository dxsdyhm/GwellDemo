package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.gwelldemo.R;
import com.p2p.core.P2PHandler;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    private Context mContext;
    String LoginID;
    @BindView(R.id.btn_test) Button btnIn;
    @BindView(R.id.btn_moniter) Button btnMoniter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LoginID=getIntent().getStringExtra("LoginID");
        initUI();
        initData();
    }
    private void initUI() {

    }

    private void initData() {
        String[] name=new String[]{"1092482"};
        P2PHandler.getInstance().getFriendStatus(name,1);
        Log.e("dxsTest","call:"+ Arrays.toString(name));
    }

    @OnClick(R.id.btn_test)
    public void toDeviceTest(){
        Log.e("dxsTest","toDeviceTest"+LoginID);
        startActivity(new Intent(this,DeviceTestActivity.class));
    }

    @OnClick(R.id.btn_moniter)
    public void toMoniter(){
        Intent moniter=new Intent(this,MonitoerActivity.class);
        moniter.putExtra("LoginID",LoginID);
        startActivity(moniter);
        Log.e("dxsTest","toMoniter"+LoginID);
    }
}
