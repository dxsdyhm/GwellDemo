package udpsender;


/**
 * UDP管理器，隔离UDP实现层逻辑
 * Created by Administrator on 2017/4/14.
 */

public class UDPManger {
    private static final String TAG = "UDPManger";
    private static UDPManger udpManger;

    private UDPManger() {
    }

    public static UDPManger getInstance() {
        if (udpManger == null) {
            synchronized (UDPManger.class) {
                udpManger = new UDPManger();
            }
        }
        return udpManger;
    }

    /**
     * 接收数据时的标记,默认为2
     */
    private int receiveCmdFlag = 2;
    /**
     * 接收数据超时时间
     */
    private int receiveTimeOut = 8 * 1000;//默认8s

    /**
     * 指定数组（字节）
     */
    private byte[] instructions;
    /**
     * 请求的端口，默认为8899
     */
    private int port = UDPSender.DEFAULT_PORT;

    /**
     * 设置接收超时时间
     *
     * @param receiveTimeOut 超时时间
     * @return
     */
    public UDPManger setReceiveTimeOut(int receiveTimeOut) {
        this.receiveTimeOut = receiveTimeOut;
        return this;
    }

    /**
     * 设置接收数据时的标记，默认为2
     *
     * @param receiveCmdFlag
     * @return
     */
    public UDPManger setReceiveCmdFlag(int receiveCmdFlag) {
        this.receiveCmdFlag = receiveCmdFlag;
        return this;
    }

    /**
     * 设置指令
     *
     * @param instructions 指令字节数组
     * @return
     */
    public UDPManger setInstructions(byte[] instructions) {
        this.instructions = instructions;
        return this;
    }

    /**
     * 设置请求端口
     *
     * @param port 请求端口号，默认为8899，范围是1024-65535
     * @return 当前发送器对象
     */
    public UDPManger setPort(int port) {
        this.port = port;
        return this;
    }

    /**
     * 发送UDP广播
     *
     * @param callback 结果回调
     */
    public synchronized void send(UDPResultCallback callback)  {
        if (isRunning()) {
            callback.onError(new Throwable("Task running"));//任务执行中
            callback.onCompleted();
        } else {
            UDPSender.getInstance()
                    .setInstructions(instructions)//设置请求指令
                    .setPort(port)//设置搜索设备的端口
                    .setReceiveCmdFlag(receiveCmdFlag)//设置接收设备的指令标记
                    .setReceiveTimeOut(2000)//设置搜索超时时间
                    .send(callback);
        }
    }

    public boolean isRunning() {
            return UDPSender.getInstance().isRunning();
    }
}
