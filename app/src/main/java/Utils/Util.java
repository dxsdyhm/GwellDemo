package Utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by dansesshou on 17/2/17.
 */

public class Util {

    public static InetAddress getIntentAddress(Context mContext)
            throws IOException {
        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public static String getScreenShotPath(){
        String path= Environment.getExternalStorageDirectory().getPath();
        return path;
    }
}
