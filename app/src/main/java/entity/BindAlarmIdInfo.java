package entity;

import java.util.Arrays;

/**
 * Created by xiyingzhu on 2017/6/5.
 */
public class BindAlarmIdInfo {

    private int srcID;
    private int result;
    private int maxCount;
    private String[] data;

    public BindAlarmIdInfo(int srcID, int result, int maxCount, String[] data) {
        this.srcID = srcID;
        this.result = result;
        this.maxCount = maxCount;
        this.data = data;
    }

    public BindAlarmIdInfo() {
    }

    public int getSrcID() {
        return srcID;
    }

    public void setSrcID(int srcID) {
        this.srcID = srcID;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BindAlarmIdInfo{" +
                "srcID=" + srcID +
                ", result=" + result +
                ", maxCount=" + maxCount +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
