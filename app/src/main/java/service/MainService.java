package service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.p2p.core.P2PHandler;

import Utils.Contants;

public class MainService extends Service {

    public MainService() {
    }

    @Override
    public void onCreate() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取鉴权码
        SharedPreferences sp=getSharedPreferences("Account",MODE_PRIVATE);
        int code1=sp.getInt("code1",-1);
        int code2 =sp.getInt("code2",-1);
        String sessionId =sp.getString("sessionId",null);
        String SessionId2 =sp.getString("sessionId2",null);
        int sessionid1=(int)Long.parseLong(sessionId);
        int sessionid2=(int)Long.parseLong(SessionId2);
        String userId=sp.getString("userId",null);
        //p2pConnect 在一次应用的生命周期中在应用启动时只调用一次，在应用结束时相应调用diaconnect一次
        //dwSoftwareVersion: app版本  默认传0即可
        boolean connect = P2PHandler.getInstance().p2pConnect(userId,sessionid1,sessionid2,
                code1, code2,0);
        Log.e("dxsTest","connect:"+connect);
        Intent i = new Intent();
        i.setAction(Contants.P2P_CONNECT);
        i.putExtra("connect",connect);
        sendBroadcast(i);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        P2PHandler.getInstance().p2pDisconnect();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

