package entity;

import java.io.Serializable;

/**
 * Created by xiyingzhu on 2017/3/14.
 */
public class RecordFile implements Serializable {
    public int position;
    public String name;

    public RecordFile(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordFile)) return false;
        return this.name.equals(((RecordFile) o).getName());

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "RecordFile{" +
                "position=" + position +
                ", name='" + name + '\'' +
                '}';
    }
}
