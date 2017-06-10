package shake;

import entity.Device;

/**
 * 搜索结果监听器
 * Created by Administrator on 2017/4/11.
 */

public abstract class ShakeListener {
    /**
     * 搜索开始的时候回调
     */
    public void onStart() {
    }

    /**
     * 搜索发生错误的时候开始回调
     */
    public void onError(Throwable throwable) {
    }

    /**
     * 每搜索到一台设备就回调一次
     *
     * @param device
     */
    public abstract void onNext(Device device);

    /**
     * 搜索结束的时候回调
     */
    public void onCompleted() {
    }
}
