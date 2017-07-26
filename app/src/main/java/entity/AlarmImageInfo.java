package entity;

/**
 * Created by USER on 2017/4/1.
 */

public class AlarmImageInfo {
    private int SrcID;
    private String Path;
    private int ErrorCode;

    public AlarmImageInfo() {
    }

    public AlarmImageInfo(int srcID, String path, int errorCode) {
        SrcID = srcID;
        Path = path;
        ErrorCode = errorCode;
    }

    public int getSrcID() {
        return SrcID;
    }

    public void setSrcID(int srcID) {
        SrcID = srcID;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    @Override
    public String toString() {
        return "AlarmImageInfo{" +
                "SrcID=" + SrcID +
                ", Path='" + Path + '\'' +
                ", ErrorCode=" + ErrorCode +
                '}';
    }
}
