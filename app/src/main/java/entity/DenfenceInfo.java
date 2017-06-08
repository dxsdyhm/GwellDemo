package entity;

/**
 * Created by xiyingzhu on 2017/6/6.
 */
public class DenfenceInfo {

    private String contactId;
    private int state;

    public DenfenceInfo(String contactId, int state) {
        this.contactId = contactId;
        this.state = state;
    }

    public DenfenceInfo() {
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DenfenceInfo{" +
                "contactId='" + contactId + '\'' +
                ", state=" + state +
                '}';
    }
}
