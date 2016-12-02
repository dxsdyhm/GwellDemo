package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.P2PSpecial.P2PSpecial;
import com.p2p.core.network.LoginResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnIn;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }
    private void initUI() {
        btnIn= (Button) findViewById(R.id.btn_in);
        btnIn.setOnClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_in:
                P2PSpecial.getInstance().P2PConnect(callBack);
                break;
        }
    }
    private P2PSpecial.P2PConnectCallBack callBack=new P2PSpecial.P2PConnectCallBack() {
        @Override
        public void P2PConnectResult(boolean connectState, LoginResult result) {
            if(connectState){
                Intent callIntent=new Intent(mContext,MonitoerActivity.class);
                callIntent.putExtra("LoginID",result.contactId);
                startActivity(callIntent);
            }else{
                if(result!=null){
                    Toast.makeText(mContext,"error_code:"+result.error_code, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext,"error_code:result==null", Toast.LENGTH_LONG).show();
                }

            }
        }
    };
}
