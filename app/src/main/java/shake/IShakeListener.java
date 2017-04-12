package shake;

import entity.Device;

/**
 * 搜索结果监听器
 * Created by Administrator on 2017/4/11.
 */

public interface IShakeListener {
    /**
     * 搜索开始的时候回调
     */
    void onStart();


    /**
     * 每搜索到一台设备就回调一次
     *
     * @param device
     */
    void onNext(Device device);

    /**
     * 搜索结束的时候回调
     */
    void onCompleted();
}
