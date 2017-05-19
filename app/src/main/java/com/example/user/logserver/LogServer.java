package com.example.user.logserver;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.Arrays;

import Utils.DBManager;
import entity.LogInfo;
import sdk.MyApp;

/**
 * Created by USER on 2017/5/3.
 */

public class LogServer {
    static {
        System.loadLibrary("LogCollect");
    }
    public static native int init(int ip,int port);
    public static native void LogCollectExit();
    public static native int getVersion(String version,String descrip);
    public static native int fgGetLogCollectTcpForwardPort();
    public static native int SendDataToDevice(int DevId, byte[] data, int DataLen);
    public static native int GetConnectIdTable(int[] table);
    public static void ReciveMasage(int SrcID, int DataType,byte[] Data, int DataLen){
        long time=System.currentTimeMillis();
        Log.e("dxsTest","SrcID:"+SrcID+"Data:"+Arrays.toString(Data)+"time:"+time);
        LogInfo info=new LogInfo(SrcID,DataType,Data,DataLen,time);
        DBManager.getInstance(MyApp.app).insertLogInfo(info);
        RxBus.get().post(info);
    }
}
