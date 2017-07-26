package Utils;

/**
 * Created by USER on 2017/5/4.
 */

public class ErrorCodeUtils {
    public static String getErrorCode(int code) {
        String error = "";
        switch (code) {
            case 1000:
                error="正常";
                break;
            case 1001:
                error = "设备无ip";
                break;
            case 1002:
                error = "设备无法ｐｉｎｇ通路由器";
                break;
            case 1003:
                error = "域名解析失败";
                break;
            case 1004:
                error = "无法ｐｉｎｇ通　百度，谷歌，和ｌｉｓｔ服务";
                break;
            case 1005:
                error = "无法ｐｉｎｇ通　ｌｉｓｔ服务";
                break;
            case 1006:
                error = "设备网络正常，但服务器无回应，尝试改变设备ip地址  1006";
                break;
            case 1007:
                error = "设备无网卡";
                break;
            case 1008:
                error = "设备连接不上路由器";
                break;
            case 1009:
                error = "无 wpa 进程";
                break;
            case 1010:
                error = "无　ｄｈｃｐ进程";
                break;
            case 1011:
                error = "直接读取网卡ip成功";
                break;
            case 1012:
                error = "EEPROM原因";
                break;
            case 1013:
                error = "无　网关　";
                break;
            case 1014:
                error = "无　dns解析文件";
                break;
            case 1015:
                error = "其他原因";
                break;
            case 1016:
                error = "信号全为０";
                break;
            case 1017:
                error = "扫描不到　ｗｉｆｉ　信号";
                break;
            case 1018:
                error = "TX 无变化";
                break;
            case 1019:
                error = "信号全为０";
                break;
            case 1020:
                error = "扫描不到　ｗｉｆｉ　信号";
                break;
            case 1021:
                error = "TX 无变化";
                break;
            case 2000:
                error = "51880 --- 51880 端口 服务器检测成功";
                break;
            case 2001:
                error = "51880 --- 51880  端口 服务器检测失败";
                break;
            case 2002:
                error = "51870 ---- 51880 端口 服务器检测成功";
                break;
            case 2003:
                error = "51870 ---- 51880端口 服务器检测失败";
                break;
            case 2004:
                error = "List 服务 无法获得p2p服务器地址";
                break;
            case 2005:
                error = "51880 --- 51881 端口 服务器检测成功";
                break;
            case 2006:
                error = "51880 --- 51881  端口 服务器检测失败";
                break;
            case 2007:
                error = "51870 ---- 51881 端口 服务器检测成功";
                break;
            case 2008:
                error = "51870 ---- 5181s0端口 服务器检测失败";
                break;
            case 2009:
                error = "tcp 服务器检测成功";
                break;
            case 2010:
                error = "tcp 服务器检测失败";
                break;
            default:
                error = String.valueOf(code);
        }
        return error;
    }
}
