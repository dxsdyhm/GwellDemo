package entity;

import com.p2p.core.utils.MyUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Arrays;

import Utils.ErrorCodeUtils;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by USER on 2017/5/3.
 */
@Entity
public class LogInfo {

    @Id
    private Long id;
    private int SrcID;
    private int DataType;
    private byte[] data;
    private int dataLen;

    private int  dwChkOpt;           // 设备体检选项
    private int  dwChkSubState;      // 设备体检步骤
    private int  dwErrCode;          //错误码
    private byte   fgChkEndFlag;       //体检过程是否已完成 0 未完成 1 完成

    private int dwCurAppVersion;      // 版本号
    private int dwUbootVersion;       // uboot 版本号
    private int dwKernelVersion;      // 内核 版本号
    private int dwRootfsVersion;      // rootfs 版本号
    private long RecTime;
    public LogInfo() {
    }

    public LogInfo(int srcID, int dataType, byte[] data, int dataLen) {
        SrcID = srcID;
        DataType = dataType;
        this.data = data;
        this.dataLen = dataLen;
        RecTime=System.currentTimeMillis();
    }

    public LogInfo(int srcID, int dataType, byte[] data, int dataLen, long recTime) {
        SrcID = srcID;
        DataType = dataType;
        this.data = data;
        this.dataLen = dataLen;
        RecTime = recTime;
        if(dataType==1){
            paseInfo();
        }else if(dataType==255){
            //test
        }
    }

    @Generated(hash = 264707627)
    public LogInfo(Long id, int SrcID, int DataType, byte[] data, int dataLen,
            int dwChkOpt, int dwChkSubState, int dwErrCode, byte fgChkEndFlag,
            int dwCurAppVersion, int dwUbootVersion, int dwKernelVersion,
            int dwRootfsVersion, long RecTime) {
        this.id = id;
        this.SrcID = SrcID;
        this.DataType = DataType;
        this.data = data;
        this.dataLen = dataLen;
        this.dwChkOpt = dwChkOpt;
        this.dwChkSubState = dwChkSubState;
        this.dwErrCode = dwErrCode;
        this.fgChkEndFlag = fgChkEndFlag;
        this.dwCurAppVersion = dwCurAppVersion;
        this.dwUbootVersion = dwUbootVersion;
        this.dwKernelVersion = dwKernelVersion;
        this.dwRootfsVersion = dwRootfsVersion;
        this.RecTime = RecTime;
    }

    public int getSrcID() {
        return SrcID;
    }

    public void setSrcID(int srcID) {
        SrcID = srcID;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public int getDataType() {
        return DataType;
    }

    public void setDataType(int dataType) {
        DataType = dataType;
    }

    public long getRecTime() {
        return RecTime;
    }

    public void setRecTime(long recTime) {
        RecTime = recTime;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "SrcID=" + SrcID +
                "\n data=" + Arrays.toString(data) +
                "\n dataLen=" + dataLen +
                '}';
    }

    public String getContStr(){
        if(DataType==255){
            return SrcID+":   Link OK";
        }
        return " dwChkOpt=" + dwChkOpt +
                "\n                  dwChkSubState=" + dwChkSubState +
                "\n                  dwErrCode=" + dwErrCode +getError()+
                "\n                  fgChkEndFlag=" + fgChkEndFlag +
                "\n                  dwCurAppVersion=" + dwCurAppVersion +
                "\n                  dwUbootVersion=" + dwUbootVersion +
                "\n                  dwKernelVersion=" + dwKernelVersion +
                "\n                  dwRootfsVersion=" + dwRootfsVersion +
                "\n";
    }

    private void paseInfo(){
        if(data!=null&&data.length>=29){
            dwChkOpt= MyUtils.bytesToInt(data,0);
            dwChkSubState= MyUtils.bytesToInt(data,4);
            dwErrCode= MyUtils.bytesToInt(data,8);
            fgChkEndFlag= data[12];
            dwCurAppVersion= MyUtils.bytesToInt(data,13);
            dwUbootVersion= MyUtils.bytesToInt(data,17);
            dwKernelVersion= MyUtils.bytesToInt(data,21);
            dwRootfsVersion= MyUtils.bytesToInt(data,25);
        }
    }

    private String getError(){
        return "  ("+ErrorCodeUtils.getErrorCode(dwErrCode)+")";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDwChkOpt() {
        return this.dwChkOpt;
    }

    public void setDwChkOpt(int dwChkOpt) {
        this.dwChkOpt = dwChkOpt;
    }

    public int getDwChkSubState() {
        return this.dwChkSubState;
    }

    public void setDwChkSubState(int dwChkSubState) {
        this.dwChkSubState = dwChkSubState;
    }

    public int getDwErrCode() {
        return this.dwErrCode;
    }

    public void setDwErrCode(int dwErrCode) {
        this.dwErrCode = dwErrCode;
    }

    public byte getFgChkEndFlag() {
        return this.fgChkEndFlag;
    }

    public void setFgChkEndFlag(byte fgChkEndFlag) {
        this.fgChkEndFlag = fgChkEndFlag;
    }

    public int getDwCurAppVersion() {
        return this.dwCurAppVersion;
    }

    public void setDwCurAppVersion(int dwCurAppVersion) {
        this.dwCurAppVersion = dwCurAppVersion;
    }

    public int getDwUbootVersion() {
        return this.dwUbootVersion;
    }

    public void setDwUbootVersion(int dwUbootVersion) {
        this.dwUbootVersion = dwUbootVersion;
    }

    public int getDwKernelVersion() {
        return this.dwKernelVersion;
    }

    public void setDwKernelVersion(int dwKernelVersion) {
        this.dwKernelVersion = dwKernelVersion;
    }

    public int getDwRootfsVersion() {
        return this.dwRootfsVersion;
    }

    public void setDwRootfsVersion(int dwRootfsVersion) {
        this.dwRootfsVersion = dwRootfsVersion;
    }
}
