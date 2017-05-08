package entity;

import android.support.annotation.NonNull;

import com.p2p.core.P2PValue;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 设备对象
 * Created by dali on 2017/4/11.
 */

public class Device implements Serializable,Comparable<Device> {
    /**
     * 设备id
     */
    private String id;
    /**
     * 设备的IP
     */
    private String IP;
    /**
     * 设备名字
     */
    private String name;
    /**
     * 设备的版本
     */
    private int version;
    /**
     * 设备的标记
     */
    private int flag = 1;
    private int rtspflag;
    /**
     * 设备类型
     */
    private int type = P2PValue.DeviceType.UNKNOWN;
    /**
     * 设备子类型
     */
    private int subType;

    public Device() {
    }

    public String getId() {
        return "" + id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIP() {
        return "" + IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return "" + ((rtspflag >> 4) & 0x1);
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getType() {
        String typeName = "";
        switch (type) {
            case P2PValue.DeviceType.DOORBELL:
                typeName += "门铃(" + type + ")";
                break;
            case P2PValue.DeviceType.IPC:
                typeName += "IPC(" + type + ")";
                break;
            case P2PValue.DeviceType.NPC:
                typeName += "NPC(" + type + ")";
                break;
            case P2PValue.DeviceType.NVR:
                typeName += "NVR(" + type + ")";
                break;
            case P2PValue.DeviceType.PC:
                typeName += "PC(" + type + ")";
                break;
            case P2PValue.DeviceType.PHONE:
                typeName += "Phone(" + type + ")";
                break;
            case P2PValue.DeviceType.UNKNOWN:
                typeName += "UNKNOWN(" + type + ")";
                break;
            default:
                typeName += "UNKNOWN(" + type + ")";
        }
        return typeName;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getRtspflag() {
        return (rtspflag >> 2) & 1;
    }

    public void setRtspflag(int rtspflag) {
        this.rtspflag = rtspflag;
    }

    public String getSubType() {
        String typeName = "";
        switch (subType) {
            case P2PValue.subType.IPC_8:
                typeName += "IPC_8(" + subType + ")";
                break;
            case P2PValue.subType.IPC_18:
                typeName += "IPC_18(" + subType + ")";
                break;
            case P2PValue.subType.IPC_28:
                typeName += "IPC_28(" + subType + ")";
                break;
            case P2PValue.subType.IPC_868:
                typeName += "IPC_868(" + subType + ")";
                break;
            case P2PValue.subType.IPC_868_SCENE_MODE:
                typeName += "IPC_868_SCENE_MODE(" + subType + ")";
                break;
            case P2PValue.subType.IPC_868_SCENE_MODE_SHARE:
                typeName += "IPC_868_SCENE_MODE_SHARE(" + subType + ")";
                break;
            case P2PValue.subType.IPC_DEV_SUB_TYPE_100W_DOORBELL:
                typeName += "IPC_DEV_SUB_TYPE_100W_DOORBELL(" + subType + ")";
                break;
            case P2PValue.subType.IPC_DEV_SUB_TYPE_130W_DOORBELL:
                typeName += "IPC_DEV_SUB_TYPE_130W_DOORBELL(" + subType + ")";
                break;
            case P2PValue.subType.IPC_DEV_SUB_TYPE_200W_DOORBELL:
                typeName += "IPC_DEV_SUB_TYPE_200W_DOORBELL(" + subType + ")";
                break;
            case P2PValue.subType.IPC_PANOMA_180_720:
                typeName += "IPC_PANOMA_180_720(" + subType + ")";
                break;
            case P2PValue.subType.IPC_PANOMA_180_960:
                typeName += "IPC_PANOMA_180_960(" + subType + ")";
                break;
            case P2PValue.subType.IPC_PANOMA_360_720:
                typeName += "IPC_PANOMA_360_720(" + subType + ")";
                break;
            case P2PValue.subType.IPC_PANOMA_360_960:
                typeName += "IPC_PANOMA_360_960(" + subType + ")";
                break;
            case P2PValue.subType.UNKOWN:
                typeName += "UNKNOWN(" + subType + ")";
                break;
            default:
                typeName += "UNKNOWN(" + subType + ")";
        }
        return typeName;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", IP='" + IP + '\'' +
                ", name='" + name + '\'' +
                ", version=" + version +
                ", flag=" + flag +
                ", type=" + type +
                ", rtspflag=" + rtspflag +
                ", subType=" + subType +
                '}';
    }

    @Override
    public int compareTo(@NonNull Device o) {
        return id.compareTo(o.getId());
    }
}
