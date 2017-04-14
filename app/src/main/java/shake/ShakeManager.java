package shake;

import entity.Device;
import udpsender.UDPManger;
import udpsender.UDPResult;
import udpsender.UDPResultCallback;
import udpsender.UDPSender;

/**
 * 扫描管理器
 * Created by Administrator on 2017/4/11.
 */

public class ShakeManager {
    /**
     * 搜索管理器，保持单例
     */
    private static ShakeManager manager = null;
    /**
     * 结果回调handler
     */
    public ShakeListener listener;
    /**
     * 搜索时间
     */
    private int searchTime = 10000;

    private ShakeManager() {
    }

    public static ShakeManager getInstance() {
        if (null == manager) {
            synchronized (ShakeManager.class) {
                manager = new ShakeManager();
            }
        }
        return manager;
    }

    /**
     * 设置搜索超时时间
     *
     * @param time 搜索时间  时间的毫秒值（eg:1000）
     * @return 当前对象
     */
    public ShakeManager setSearchTime(int time) {
        this.searchTime = time;
        return this;
    }


    /**
     * 开始扫描
     *
     * @param listener
     */
    public void shaking(final ShakeListener listener) {
        this.listener = listener;
        ShakeData data = new ShakeData();
        data.setCmd(ShakeData.Cmd.CMD_SHAKE_DEVICE);
        UDPManger.getInstance()
                .setInstructions(ShakeData.getBytes(data))//设置请求指令
                .setReceiveTimeOut(searchTime)//设置接收超时时间
                .setPort(ShakeData.Cmd.CMD_SHAKE_DEVICE_DEFAULT_PORT)//设置端口
                .setReceiveCmdFlag(ShakeData.Cmd.CMD_RECEIVE_DEVICE)//设置接收数据标记
                .send(new UDPResultCallback() {
                    /**
                     * 请求开始的时候回调
                     */
                    @Override
                    public void onStart() {
                        listener.onStart();
                    }
                    /**
                     * 每拿到一个结果的时候就回调
                     *
                     * @param result 请求的结果
                     */
                    @Override
                    public void onNext(UDPResult result) {
                        Device device = ShakeData.getDevice(result);
                        listener.onNext(device);
                    }
                    /**
                     * 当发生错误的时候回调
                     *
                     * @param throwable 错误信息
                     */
                    @Override
                    public void onError(Throwable throwable) {
                        listener.onError(throwable);
                    }
                    /**
                     * 请求结束的时候回调
                     */
                    @Override
                    public void onCompleted() {
                        listener.onCompleted();
                    }
                });
    }


    /**
     * 是否正在搜索
     *
     * @return 搜索状态
     */
    public boolean isShaking() {
        return UDPSender.getInstance().isRunning();
    }
}
