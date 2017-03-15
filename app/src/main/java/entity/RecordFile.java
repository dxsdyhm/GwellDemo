package entity;

/**
 * Created by xiyingzhu on 2017/3/14.
 */
public class RecordFile {
    public String id;
    public String name;

    public RecordFile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

        RecordFile that = (RecordFile) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RecordFile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
