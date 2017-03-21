package entity;

/**
 * 报警消息
 * String srcId, int type, int option, int iGroup, int iItem, int imagecounts, String imagePath, String alarmCapDir, String VideoPath, String sensorName, int deviceType
 * Created by USER on 2017/3/21.
 */

public class AlarmInfo {
    private String srcId;
    private int type;
    private int option;
    private int iGroup;
    private int iItem;
    private int imagecounts;
    private String imagePath;
    private String alarmCapDir;
    private String VideoPath;
    private String sensorName;
    private int deviceType;

    public AlarmInfo() {
    }

    public AlarmInfo(String srcId, int type, int option, int iGroup, int iItem, int imagecounts, String imagePath, String alarmCapDir, String videoPath, String sensorName, int deviceType) {
        this.srcId = srcId;
        this.type = type;
        this.option = option;
        this.iGroup = iGroup;
        this.iItem = iItem;
        this.imagecounts = imagecounts;
        this.imagePath = imagePath;
        this.alarmCapDir = alarmCapDir;
        VideoPath = videoPath;
        this.sensorName = sensorName;
        this.deviceType = deviceType;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getiGroup() {
        return iGroup;
    }

    public void setiGroup(int iGroup) {
        this.iGroup = iGroup;
    }

    public int getiItem() {
        return iItem;
    }

    public void setiItem(int iItem) {
        this.iItem = iItem;
    }

    public int getImagecounts() {
        return imagecounts;
    }

    public void setImagecounts(int imagecounts) {
        this.imagecounts = imagecounts;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAlarmCapDir() {
        return alarmCapDir;
    }

    public void setAlarmCapDir(String alarmCapDir) {
        this.alarmCapDir = alarmCapDir;
    }

    public String getVideoPath() {
        return VideoPath;
    }

    public void setVideoPath(String videoPath) {
        VideoPath = videoPath;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                "srcId='" + srcId + '\'' +
                ", type=" + type +
                ", option=" + option +
                ", iGroup=" + iGroup +
                ", iItem=" + iItem +
                ", imagecounts=" + imagecounts +
                ", imagePath='" + imagePath + '\'' +
                ", alarmCapDir='" + alarmCapDir + '\'' +
                ", VideoPath='" + VideoPath + '\'' +
                ", sensorName='" + sensorName + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
