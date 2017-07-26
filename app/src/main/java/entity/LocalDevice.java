package entity;

import java.io.Serializable;

/**
 * Created by dansesshou on 17/2/17.
 */

public class LocalDevice implements Serializable{
    public String ID;
    public String IP;
    public String version;

    public LocalDevice(String ID, String IP, String version) {
        this.ID = ID;
        this.IP = IP;
        this.version = version;
    }


    public String getID() {
        return "ID:"+ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIP() {
        return "IP"+IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getVersion() {
        return "version:"+version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LocalDevice){
            return ((LocalDevice) obj).getID().equals(ID);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "LocalDevice{" +
                "ID='" + ID + '\'' +
                ", IP='" + IP + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
