package shake;

import android.os.Handler;
import android.os.Message;

import entity.Device;

/**
 * 扫描管理器
 * Created by Administrator on 2017/4/11.
 */

public class ShakeManager {
    /**
     * 扫描开始
     */
    public static final int HANDLE_ID_SEARCH_START = 0x10;
    /**
     * 扫描结束
     */
    public static final int HANDLE_ID_SEARCH_END = 0x11;
    /**
     * 接收设备信息（每找到一个就调用一次）
     */
    public static final int HANDLE_ID_RECEIVE_DEVICE_INFO = 0x12;
    /**
     * 搜索Ap设备结束
     */
    public static final int HANDLE_ID_APDEVICE_END = 0x13;
    public static final int HANDLE_ID_ONEAPDEVICE = 0x14;
    /**
     * 搜索管理器，保持单例
     */
    private static ShakeManager manager = null;
    /**
     * 搜索线程/任务
     */
    private ShakeTask shakeTask;
    /**
     * 结果回调handler
     */
    public IShakeListener listener;
    /**
     * 搜索时间
     */
    private long searchTime = 10000;

    private ShakeManager() {
    }

    public synchronized static ShakeManager getInstance() {
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
    public ShakeManager setSearchTime(long time) {
        this.searchTime = time;
        return this;
    }

    /**
     * 在此处过滤/转换数据，减少调用处逻辑；
     * <p>此处主要的作用是接收广播（网络广播）请求的结果的回调，用于通知调用者调用状态，用于主/子线程的切换</p>
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ShakeManager.HANDLE_ID_SEARCH_START:
                    listener.onStart();
                    break;
                case ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO:
                    ShakeData data = (ShakeData) msg.obj;
                    Device device = new Device();
                    device.setId(data.getId() + "");
                    device.setName(data.getName() + "");
                    device.setIP(msg.getData().getString("ip") + "");
                    device.setFlag(data.getFlag());
                    device.setRtspflag(data.getRightCount());
                    device.setSubType(data.getSubType());
                    device.setType(data.getType());
                    listener.onNext(device);
                    break;
                case ShakeManager.HANDLE_ID_APDEVICE_END:
                    listener.onCompleted();
                    break;
            }
        }
    };

    /**
     * 开始搜索
     *
     * @param listener 结果回调对象
     * @return
     */
    public ShakeManager shaking(IShakeListener listener) {
        this.listener = listener;
        if (null == shakeTask) {
            shakeTask = new ShakeTask(handler);
            shakeTask.setSearchTime(searchTime);
            shakeTask.start();
        }
        return this;
    }

    /**
     * 停止搜索
     */
    public void stopShaking() {
        if (null != shakeTask) {
            shakeTask.killThread();
            shakeTask = null;
        }
    }

    /**
     * 是否正在搜索
     *
     * @return 搜索状态
     */
    public boolean isShaking() {
        if (null != shakeTask) {
            return true;
        } else {
            return false;
        }
    }
}
