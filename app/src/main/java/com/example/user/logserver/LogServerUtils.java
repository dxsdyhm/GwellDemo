package com.example.user.logserver;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;

/**
 * Created by USER on 2017/5/3.
 */

public class LogServerUtils {
    public static int getIpAdrress(){
        String ipStr=NetworkUtils.getIPAddress(true);
        if(TextUtils.isEmpty(ipStr)){
            throw new NullPointerException("get IP Address is null");
        }
        Log.e("dxsTest","ipStr:"+ipStr);
        return (int) ipToLong(ipStr);
    }

    public static long ipToLong(String strIp){
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[3] = Long.parseLong(strIp.substring(0, position1));
        ip[2] = Long.parseLong(strIp.substring(position1+1, position2));
        ip[1] = Long.parseLong(strIp.substring(position2+1, position3));
        ip[0] = Long.parseLong(strIp.substring(position3+1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }
}
