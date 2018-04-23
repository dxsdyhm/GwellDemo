package sdk;

import android.content.Intent;
import android.util.Log;

import com.example.dansesshou.jcentertest.MonitoerActivity;
import com.hwangjr.rxbus.RxBus;
import com.p2p.core.P2PInterface.IP2P;
import com.p2p.core.pano.Decoder;

import Utils.DBManager;
import Utils.RxBUSAction;
import entity.AlarmInfo;

/**
 * Created by dxs on 2016/6/13.
 * p2p监控时的回调接口
 */
public class P2PListener implements IP2P {
    /**
     * 被设备呼叫时的回调
     * @param isOutCall
     * @param threeNumber ID号
     * @param type
     */
    @Override
    public void vCalling(boolean isOutCall, String threeNumber, int type) {

    }

    /**
     * 设备端挂断时的回调
     * @param deviceId 设备挂断
     * @param reason_code 挂断原因
     */
    @Override
    public void vReject(String deviceId, int reason_code, int exCode1, int exCode2) {
        Intent intent = new Intent();
        intent.setAction(MonitoerActivity.P2P_REJECT);
        intent.putExtra("reason_code", reason_code);
        intent.putExtra("exCode1", exCode1);
        intent.putExtra("exCode2", exCode2);
        MyApp.app.sendBroadcast(intent);
    }

    /**
     * 设备端接听时的回调,参数需要传给P2PView
     * @param type 视频宽高比例参数
     * @param state 视频宽高比例参数
     */
    @Override
    public void vAccept(int type, int state) {
        Intent accept = new Intent();
        accept.setAction(MonitoerActivity.P2P_ACCEPT);
        accept.putExtra("type", new int[]{type, state});
        MyApp.app.sendBroadcast(accept);
    }

    /**
     * 准备好传输音视频,一次监控只会被调用一次
     */
    @Override
    public void vConnectReady() {
        Intent intent = new Intent();
        intent.setAction(MonitoerActivity.P2P_READY);
        MyApp.app.sendBroadcast(intent);
    }

    @Override
    public void vAllarming(String srcId, int type, boolean isSupportExternAlarm, int iGroup, int iItem, boolean isSurpportDelete) {
        //老版设备报警时的回调,可忽略,新版报警回调见下面方法,应用不在前台或者后台时不会响应
    }

    @Override
    public void vChangeVideoMask(int state) {


    }

    @Override
    public void vRetPlayBackPos(int length, int currentPos) {
        //回放进度回调  length是总时长 currentPos当前播放时间点
    }

    @Override
    public void vRetPlayBackStatus(int state) {
        //回放状态回调
    }

    @Override
    public void vGXNotifyFlag(int flag) {

    }

    /**
     * 设备视频解析出来的宽高,可根据宽来判断清晰度
     * 1280 1920 高清  640 标清  320  流畅
     * @param iWidth 宽
     * @param iHeight 高
     */
    @Override
    public void vRetPlaySize(int iWidth, int iHeight) {
        Decoder.getInstance().setWidthHeight(iWidth,iHeight);
    }

    /**
     * 当前观看人数,人数改变时回调一次
     *
     * @param iNumber 人数
     * @param data
     */
    @Override
    public void vRetPlayNumber(int iNumber, int[] data) {

    }

    /**
     * 设备传过来的,解码前的音视频数据,已停止回调
     * @param AudioBuffer 音频数据
     * @param AudioLen    音频长度
     * @param AudioFrames 音频帧
     * @param AudioPTS    音频PTS
     * @param VideoBuffer 视频数据
     * @param VideoLen    视频长度
     * @param VideoPTS    视频PTS
     */
    @Override
    public void vRecvAudioVideoData(byte[] AudioBuffer, int AudioLen, int AudioFrames, long AudioPTS, byte[] VideoBuffer, int VideoLen, long VideoPTS) {

    }

    /**
     * 新版报警回调,注意查看硬件接口说明
     * @param srcId 报警设备ID
     * @param type 报警类型
     * @param option 功能参数
     * @param iGroup 防区
     * @param iItem 通道
     * @param imagecounts 图片数量 以下功能需要设备支持,支持情况会在option反应
     * @param imagePath   图片在设备端的路径
     * @param alarmCapDir 报警路径
     * @param VideoPath   报警视频路径 暂不支持
     * @param sensorName  传感器名字 需要设备支持
     * @param deviceType  设备类型 高16位是子类型 低16位是主类型
     */
    @Override
    public void vAllarmingWitghTime(String srcId, int type, int option, int iGroup, int iItem, int imagecounts, String imagePath, String alarmCapDir, String VideoPath, String sensorName, int deviceType) {
        AlarmInfo info=new AlarmInfo(srcId,type,option,iGroup,iItem,imagecounts,imagePath,alarmCapDir,VideoPath,sensorName,deviceType);
        DBManager.getInstance(MyApp.app).insertAlarmInfo(info);
        RxBus.get().post(RxBUSAction.EVENT_ALARM,info);
        Log.e("dxsTest","vAllarmingWitghTime.srcId:"+srcId);
    }

    /**
     * 新的系统消息
     *
     * @param iSystemMessageType  消息类型
     * @param iSystemMessageIndex 消息索引标记
     */
    @Override
    public void vRetNewSystemMessage(int iSystemMessageType, int iSystemMessageIndex) {

    }

    @Override
    public void vRetRTSPNotify(int arg2, String msg) {
        //暂时无用
    }

    /**
     * 底层视屏渲染通知消息出口,监控时会被回调多次
     *
     * @param what   消息主标记 为10时是视屏渲染到屏幕
     * @param iDesID
     * @param arg1
     * @param arg2
     * @param msgStr
     */
    @Override
    public void vRetPostFromeNative(int what, int iDesID, int arg1, int arg2, String msgStr) {

    }

    /**
     * 这个方法用于拦截是否是TF卡录像状态的推送
     *
     * @param cmd    命令头
     * @param option 操作
     * @param data   数据
     */
    @Override
    public void vRetUserData(byte cmd, byte option, int[] data) {

    }

}
