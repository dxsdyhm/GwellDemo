package entity;

import java.util.ArrayList;

/**
 * Created by xiyingzhu on 2017/4/20.
 */
public class DefenceAreaInfo {
    private int result;
    private ArrayList<int[]> data;
    private int group;
    private int item;

    public DefenceAreaInfo(int result, ArrayList<int[]> data, int group, int item) {
        this.result = result;
        this.data = data;
        this.group = group;
        this.item = item;
    }

    public DefenceAreaInfo() {
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ArrayList<int[]> getData() {
        return data;
    }

    public void setData(ArrayList<int[]> data) {
        this.data = data;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "DefenceAreaInfo{" +
                "result=" + result +
                ", data=" + data +
                ", group=" + group +
                ", item=" + item +
                '}';
    }
}
