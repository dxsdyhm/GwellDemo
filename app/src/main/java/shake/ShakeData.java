package shake;

import com.hdl.udpsenderlib.UDPResult;

import java.nio.ByteBuffer;

import entity.Device;

public class ShakeData {
    private int cmd;// 指令类型
    private int error_code;// 错误码
    private int leftlength;// 结构体大小
    private int rightCount;// 结构体版本信息
    private int id;// 设备的id
    private int type;// 设备的类型
    private int flag;// 设备标记是否有密码---1有，0没有
    private int subType;// 设备子类型

    /**
     * 局域网搜索设备的指令类
     */
    public static class Cmd {
        public static final int CMD_SHAKE_DEVICE = 1;//搜索的命令
        public static final int CMD_RECEIVE_DEVICE = 2;//接收设备的命令
        public static final int CMD_SHAKE_DEVICE_DEFAULT_PORT = 8899;//搜索设备的端口号
    }

    /**
     * 将ShakeData转换为字节数组输出
     *
     * @param data shakedata对象
     * @return 字节数组
     */
    public static byte[] getBytes(ShakeData data) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(data.getCmd());
        buffer.putInt(data.getError_code());
        buffer.putInt(data.getLeftlength());
        buffer.putInt(data.getId());
        buffer.putInt(data.getType());
        buffer.putInt(data.getFlag());
        buffer.putInt(data.getSubType());
        byte[] array = buffer.array();
        buffer.clear();// 清空缓冲区
        return array;
    }

    /**
     * 通过dupresult获取设备
     *
     * @param result 需要转换的结果
     * @return
     */
    public static Device getDevice(UDPResult result) {
        ShakeData shakeDataResult = getShakeDataResult(result.getResultData());
        Device device = new Device();
        device.setIP(result.getIp());
        device.setRtspflag(shakeDataResult.getRightCount());//设备固件信息
        int curVersion = (shakeDataResult.getFlag() >> 4) & 0x1;//固件版本
        device.setVersion(curVersion);
        device.setId(shakeDataResult.getId() + "");
        device.setType(shakeDataResult.getType());
        device.setSubType(shakeDataResult.getSubType());
        device.setFlag(shakeDataResult.getFlag());
        return device;
    }

    /**
     * 通过字节数组获取ShakeData（解析数据）
     *
     * @param buf
     * @return
     */
    public static ShakeData getShakeDataResult(byte[] buf) {
        ShakeData data = new ShakeData();
        ByteBuffer buffer = ByteBuffer.allocate(buf.length);
        buffer.put(buf);
        data.setCmd(buffer.getInt(0));
        data.setError_code(buffer.getInt(4));
        data.setLeftlength(buffer.getInt(8));
        data.setRightCount(buffer.getInt(12));
        data.setId(buffer.getInt(16));
        data.setType(buffer.getInt(20));
        data.setFlag(buffer.getInt(24));
        data.setSubType(buffer.getInt(80));
        buffer.clear();
        return data;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getLeftlength() {
        return leftlength;
    }

    public void setLeftlength(int leftlength) {
        this.leftlength = leftlength;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "ShakeData{" +
                "cmd=" + cmd +
                ", error_code=" + error_code +
                ", leftlength=" + leftlength +
                ", rightCount=" + rightCount +
                ", id=" + id +
                ", type=" + type +
                ", flag=" + flag +
                ", subType=" + subType +
                '}';
    }
}
