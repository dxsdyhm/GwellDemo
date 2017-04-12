package Utils;

/**
 * Created by Administrator on 2017/4/11.
 */

public interface BaseView {
    /**
     * 显示进度
     */
    void showProgress();

    /**
     * 隐藏进度
     */
    void hideProgress();

    /**
     * 显示消息
     *
     * @param msg 消息内容
     */
    void showMsg(String msg);
}
