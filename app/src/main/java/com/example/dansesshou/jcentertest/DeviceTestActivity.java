package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.P2PValue;
import com.p2p.shake.ShakeManager;

import java.io.IOException;
import java.net.InetAddress;

import Utils.Utils;
import adapter.LocalDeviceProvider;
import entity.LocalDevice;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import sdk.MyApp;

/**
 * Created by dansesshou on 17/2/17.
 */

public class DeviceTestActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView deviceList;
    private Items items=new Items();
    private MultiTypeAdapter adapter;
    private boolean isFindeDevice=false;
    private Button btnSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_devicetest);
        initUI();
    }

    private void initUI() {
        deviceList= (RecyclerView) findViewById(R.id.rc_devicelist);
        btnSearch= (Button) findViewById(R.id.btn_search);
        adapter=new MultiTypeAdapter(items);
        LocalDeviceProvider provider=new LocalDeviceProvider();
        provider.setOnItemClickListner(new LocalDeviceProvider.OnItemClickListner() {
            @Override
            public void onItemClick(int position, LocalDevice localDevice) {
                Intent device=new Intent(mContext,DeviceActivity.class);
                device.putExtra("device",localDevice);
                startActivity(device);
            }
        });
        adapter.register(LocalDevice.class,provider);
        deviceList.setAdapter(adapter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initData() {
        getLocalDevice();
    }

    private void getLocalDevice(){
        new Thread(){
            @Override
            public void run() {
                while(isFindeDevice){
                    try {
                        ShakeManager.getInstance().setSearchTime(5000);
                        ShakeManager.getInstance().setInetAddress(Utils.getIntentAddress(MyApp.app));
                        ShakeManager.getInstance().setHandler(mHandler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    private Handler mHandler = new Handler(MyApp.app.getMainLooper(),new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case ShakeManager.HANDLE_ID_SEARCH_END:
                    Toast.makeText(MyApp.app,"搜索结束:"+items.size(),Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    break;
                case ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO:
                    Bundle bundle = msg.getData();
                    String id = bundle.getString("id");
                    String name = bundle.getString("name");
                    int flag = bundle.getInt("flag",1);
                    int type = bundle.getInt("type", P2PValue.DeviceType.UNKNOWN);

                    int rflag=bundle.getInt("rtspflag", 0);
                    int rtspflag=(rflag>>2)&1;
                    int subType=bundle.getInt("subType");
                    int curVersion=(rflag>>4)&0x1;
                    InetAddress address = (InetAddress) bundle
                            .getSerializable("address");

                    String mark = address.getHostAddress();
                    String ip = mark.substring(mark.lastIndexOf(".") + 1,
                            mark.length());
                    LocalDevice device=new LocalDevice(id,mark,String.valueOf(curVersion));
                    if(!items.contains(device)){
                        items.add(device);
                    }
                    break;
            }
            return false;
        }
    });

}
