package udpsender;

/**
 * 结果接收回调
 */
public abstract class UDPResultCallback {
    /**
     * 请求开始的时候回调
     */
    public void onStart() {

    }

    /**
     * 每拿到一个结果的时候就回调
     *
     * @param result 请求的结果
     */
    public abstract void onNext(UDPResult result);

    /**
     * 请求结束的时候回调
     */
    public void onCompleted() {

    }

    /**
     * 当发生错误的时候回调
     *
     * @param throwable 错误信息
     */
    public void onError(Throwable throwable) {

    }
}
