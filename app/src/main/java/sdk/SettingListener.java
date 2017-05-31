package sdk;

import android.content.Intent;
import android.util.Log;

import com.example.dansesshou.jcentertest.RecordFilesActivity;
import com.hwangjr.rxbus.RxBus;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PInterface.ISetting;
import com.p2p.core.global.P2PConstants;

import java.util.ArrayList;
import java.util.Arrays;

import Utils.RxBUSAction;
import entity.AlarmImageInfo;
import entity.DefenceAreaInfo;

/**
 * Created by dansesshou on 16/11/30.
 * 设备设置返回结果回调,分为ACK回调和结果回调
 * ACK回调可以判断这条命令是否正确发送到设备端(回调方法名前有ACK前缀)
 * 结果回调则是业务数据,根据硬件接口说明解析即可
 */

public class SettingListener implements ISetting {


    @Override
    public void ACK_vRetSetDeviceTime(int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetDeviceTime(int msgId, int result) {
        Log.e("dxsTest","msgId:"+msgId+"--result:"+result);
    }

    @Override
    public void ACK_vRetGetNpcSettings(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetRemoteDefence(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetRemoteRecord(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsVideoFormat(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsVideoVolume(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsBuzzer(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsMotion(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsRecordType(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsRecordTime(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsRecordPlanTime(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsNetType(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetAlarmEmail(int msgId, int result) {
        Integer i = result;
        RxBus.get().post(RxBUSAction.EVENT_ACK_RET_SET_ALARM_EMAIL,i);
    }

    @Override
    public void ACK_vRetGetAlarmEmail(int msgId, int result) {
        Integer i = result;
        RxBus.get().post(RxBUSAction.EVENT_ACK_RET_GET_ALARM_EMAIL,i);
    }

    @Override
    public void ACK_vRetSetAlarmBindId(int srcID, int result) {

    }

    @Override
    public void ACK_vRetGetAlarmBindId(int srcID, int result) {

    }

    @Override
    public void ACK_vRetSetInitPassword(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetDevicePassword(int msgId, int result) {

    }

    @Override
    public void ACK_vRetCheckDevicePassword(int msgId, int result, String deviceId) {
        Log.e("dxsTest","msgId:"+msgId+"result:"+result+"deviceId:"+deviceId);
    }

    @Override
    public void ACK_vRetSetWifi(int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetWifiList(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetDefenceArea(int msgId, int result) {
        Integer i = result;
        RxBus.get().post(RxBUSAction.EVENT_ACK_RET_SET_DEFENCE_AREA,i);
    }

    @Override
    public void ACK_vRetGetDefenceArea(int msgId, int result) {
        Integer i = result;
        RxBus.get().post(RxBUSAction.EVENT_ACK_RET_GET_DEFENCE_AREA,i);
    }

    @Override
    public void ACK_vRetGetRecordFileList(int msgId, int result) {

    }

    @Override
    public void ACK_vRetMessage(int msgId, int result) {

    }

    @Override
    public void ACK_vRetCustomCmd(int msgId, int result) {
        Log.e("dxsTest","ACK_vRetCustomCmd:"+msgId+"result:"+result);
    }

    @Override
    public void ACK_vRetGetDeviceVersion(int msgId, int result) {

    }

    @Override
    public void ACK_vRetCheckDeviceUpdate(int msgId, int result) {

    }

    @Override
    public void ACK_vRetDoDeviceUpdate(int msgId, int result) {

    }

    @Override
    public void ACK_vRetCancelDeviceUpdate(int msgId, int result) {

    }

    @Override
    public void ACK_vRetClearDefenceAreaState(int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetDefenceStates(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetImageReverse(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetInfraredSwitch(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetWiredAlarmInput(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetWiredAlarmOut(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetAutomaticUpgrade(int msgId, int state) {

    }

    @Override
    public void ACK_VRetSetVisitorDevicePassword(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetTimeZone(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetSDCard(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSdFormat(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetGPIO(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetGPIO1_0(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetPreRecord(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetSensorSwitchs(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetSensorSwitchs(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetAlarmCenter(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetAlarmCenter(int msgId, int state) {

    }

    @Override
    public void ACK_VRetGetNvrIpcList(int msgId, int state) {

    }

    @Override
    public void ACK_VRetGetNvrInfo(String deviceId, int msgId, int state) {

    }

    @Override
    public void ACK_OpenDoor(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetFTPInfo(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetPIRLight(int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetPIRLight(int msgId, int state) {

    }

    @Override
    public void ACK_vRetGetDefenceWorkGroup(int msgId, int state) {

    }

    @Override
    public void ACK_VRetGetPresetPos(int msgId, int state) {

    }

    @Override
    public void ACK_VRetSetKeepClient(String contactId, int msgId, int state) {

    }

    @Override
    public void ACK_VRetGetLed(String contactId, int msgId, int state) {

    }

    @Override
    public void ACK_VRetSetLed(String contactId, int msgId, int state) {

    }

    @Override
    public void ACK_vRetSetNpcSettingsMotionSens(int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetVideoQuality(int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetVideoQuality(int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetApIsWifiSetting(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetApStaWifiInfo(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetLockInfo(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetGetLockInfo(String contactId, int msgId, int result) {

    }

    /**
     * 设置夜视LED状态回调
     *
     * @param contactId 设备ID
     * @param msgId     临时请求标记
     * @param result    ACK结果
     * @see P2PConstants.ACK_RET_TYPE
     */
    @Override
    public void ACK_vRetSetLEDStatus(String contactId, int msgId, int result) {

    }

    @Override
    public void ACK_vRetSetApStart(String contactId, int msgId, int result) {

    }

    @Override
    public void vRetGetRemoteDefenceResult(String contactId, int state) {

    }

    /**
     * 远程录像
     *
     * @param state 0:off  1:on
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetRemoteRecordResult(int state) {

    }

    /**
     * 获取蜂鸣器状态返回
     *
     * @param state OFF:0
     *              开启蜂鸣： 1：一分钟  2： 两分钟  3： 三分钟
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetBuzzerResult(int state) {

    }

    /**
     * 获取设备移动侦测状态
     *
     * @param state 1：开启 0：关闭 255：不支持
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetMotionResult(int state) {

    }

    /**
     * 获取视频格式返回
     *
     * @param type 0:NTSC   1:PAL
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetVideoFormatResult(int type) {

    }

    /**
     * 获取录像类型返回
     *
     * @param type 0：Manual   1:alarm   2:schedule
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetRecordTypeResult(int type) {

    }

    /**
     * 报警录像时间
     *
     * @param time 时间
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetRecordTimeResult(int time) {

    }

    /**
     * 获取设备网络连接类型返回
     *
     * @param type 0:有线  1：WiFi
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetNetTypeResult(int type) {

    }

    /**
     * 获取设备音量返回
     *
     * @param value 设备音量 0-9
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetVideoVolumeResult(int value) {

    }

    /**
     * 获取录像定时时间返回
     *
     * @param time 时间
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetRecordPlanTimeResult(String time) {

    }

    /**
     * 获取图像倒转返回
     *
     * @param type 0:off   1:on
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetImageReverseResult(int type) {

    }

    /**
     * 获取人体红外开关状态
     *
     * @param state 0：关闭    1：开启
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetInfraredSwitch(int state) {

    }

    /**
     * 获取设备有线报警输入开关状态
     *
     * @param state 1：开启 0：关闭
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetWiredAlarmInput(int state) {

    }

    /**
     * 获取设备有线报警输出开关状态
     *
     * @param state 0：关闭   1：开启
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetWiredAlarmOut(int state) {

    }

    /**
     * 自动升级开关
     *
     * @param state 0:off    1:on
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetAutomaticUpgrade(int state) {

    }

    /**
     * 获取时区返回
     *
     * @param state 时区 0-24（24：东 5.5）
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetTimeZone(int state) {

    }

    /**
     * 获取音频设备型号
     *
     * @param type 1
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetAudioDeviceType(int type) {

    }

    /**
     * 预录像开关
     *
     * @param state 0:off   1:on
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetPreRecord(int state) {

    }

    @Override
    public void vRetGetSensorSwitchs(int result, ArrayList<int[]> data) {

    }

    @Override
    public void vRetSetRemoteDefenceResult(String contactId, int result) {

    }

    @Override
    public void vRetSetRemoteRecordResult(int result) {

    }

    /**
     * 设置蜂鸣器
     *
     * @param result 状态码：0：设置成功 1：设置失败
     */
    @Override
    public void vRetSetBuzzerResult(int result) {

    }

    /**
     * 设置设备移动侦测状态
     *
     * @param result 0:设置成功  255：不支持  其他：操作失败
     */
    @Override
    public void vRetSetMotionResult(int result) {

    }

    @Override
    public void vRetSetVideoFormatResult(int result) {

    }

    @Override
    public void vRetSetRecordTypeResult(int result) {

    }

    @Override
    public void vRetSetRecordTimeResult(int result) {

    }

    /**
     * 设置网络类型
     *
     * @param result 状态码 0：成功 非0：失败
     */
    @Override
    public void vRetSetNetTypeResult(int result) {

    }

    @Override
    public void vRetSetVolumeResult(int result) {

    }

    @Override
    public void vRetSetRecordPlanTimeResult(int result) {

    }

    @Override
    public void vRetSetDeviceTimeResult(int result) {

    }

    @Override
    public void vRetGetDeviceTimeResult(String time) {
        Log.e("dxsTest","vRetGetDeviceTimeResult:"+time);
    }

    @Override
    public void vRetAlarmEmailResult(int result, String email) {
        Log.d("zxy", "vRetAlarmEmailResult: ");
    }

    /**
     * 利用SMTP协议获取报警邮箱信息
     *
     * @param result      状态码 0：设置成功 -1：设置失败
     *                    二进制表示：第1位  0：不支持SMTP  1：支持SMTP
     *                    第4位  0：验证通过    1：验证未通过
     *                    第2位  0：email不合法 1：email合法
     *                    第3为  0：格式正确    1：格式错误
     * @param email       email
     * @param smtpport    SMTP服务器端口
     * @param Entry       加密类型
     * @param SmptMessage SMTP信息  第0位：SMTP地址 第1位：email发送者 第2位：email密码
     * @param reserve     是否支持人工操作  1：支持 非1：不支持
     */
    @Override
    public void vRetAlarmEmailResultWithSMTP(int result, String email, int smtpport, byte Entry, String[] SmptMessage, byte reserve) {
        Log.d("zxy", "vRetAlarmEmailResultWithSMTP: "+result+","+
                email+","+ smtpport+","+SmptMessage+","+(int) Entry+","+(int) reserve);
        RxBus.get().post(RxBUSAction.EVENT_RET_SET_ALARM_EMAIL,new Integer(result));
    }

    /**
     * 获取设备周围的wifi信息
     *
     * @param result    状态码 1：get方法返回的  非1：set方法返回的
     * @param currentId 若为有线连接状态，则为上一次无线连接到的wifi，在wifi数组中的下标。
     *                  若为无线连接状态，则为当前连接wifi在wifi数组中的下标
     * @param count     设备周围能获取到的wifi个数
     * @param types     wifi加密类型 非0：加密 0：公开（设置wifi时要把对应type传进去）
     * @param strengths wifi信号
     * @param names     wifi名字
     * @see P2PHandler#setWifi(String, String, int, String, String)
     */
    @Override
    public void vRetWifiResult(int result, int currentId, int count, int[] types, int[] strengths, String[] names) {

    }

    @Override
    public void vRetDefenceAreaResult(int result, ArrayList<int[]> data, int group, int item) {
        DefenceAreaInfo defenceAreaInfo = new DefenceAreaInfo(result,data,group,item);
        if (result == 1){
            RxBus.get().post(RxBUSAction.EVENT_RET_GET_DEFENCE_AREA,defenceAreaInfo);
        }else {
            RxBus.get().post(RxBUSAction.EVENT_RET_SET_DEFENCE_AREA,defenceAreaInfo);
        }
    }

    /**
     * 获取绑定警报账号结果
     *
     * @param srcID    设备ID
     * @param result   1:由getBindAlarmId获取到的结果
     * @param maxCount 支持的最大报警推送账号个数
     * @param data     绑定的报警推送账号
     * @see P2PHandler#getBindAlarmId(String, String)
     * 非1：由setBindAlarmId获取到的结果   0：设置成功 其他：设置失败
     * @see P2PHandler#setBindAlarmId(String, String, int, String[])
     */
    @Override
    public void vRetBindAlarmIdResult(int srcID, int result, int maxCount, String[] data) {

    }

    @Override
    public void vRetSetInitPasswordResult(int result) {

    }

    @Override
    public void vRetSetDevicePasswordResult(int result) {

    }

    @Override
    public void vRetGetFriendStatus(int count, String[] contactIds, int[] status, int[] types, boolean isFinish) {

    }

    /**
     * 获取设备中存储卡的录像列表
     * @param names 录像文件名：如：disc1/2017-03-15_02:42:14_A.av(25S)
     *              第0-第5位：表示录像文件在设备存储卡里的路径
     *              第6为-第24位：表示该文件录制开始的时间（指设备的时间）。格式：yyyy-MM-dd_HH:mm:ss
     *              第25位-第26位：_A:表示报警录像 _M:表示手动录像 _S:表示定时录像
     *              第27位-第29位：视频格式
     *              第30位-最后一位：表示录像的时间长度
     *              （下标从0开始）
     * @param option0 标识符 1：获取成功
     * @param option1  获取状态 0：获取成功  82：存储卡不存在
     *
     */
    @Override
    public void vRetGetRecordFiles(String[] names, byte option0, byte option1) {
        Intent i = new Intent();
        i.setAction(RecordFilesActivity.RECORDFILES);
        i.putExtra("recordList", names);
        i.putExtra("option0", option0);
        i.putExtra("option1", option1);
        MyApp.app.sendBroadcast(i);
    }

    @Override
    public void vRetMessage(String contactId, String msg) {

    }

    @Override
    public void vRetSysMessage(String msg) {

    }

    @Override
    public void vRetCustomCmd(int contactId, int len, byte[] cmd) {
        Log.e("dxsTest","ACK_vRetCustomCmd:"+contactId+"cmd:"+ Arrays.toString(cmd));
        String info = Arrays.toString(cmd);
        RxBus.get().post(RxBUSAction.EVENT_RET_CUSTOM_CMD,info);
    }

    @Override
    public void vRetGetDeviceVersion(int result, String cur_version, int iUbootVersion, int iKernelVersion, int iRootfsVersion) {

    }

    @Override
    public void vRetCheckDeviceUpdate(String contactId, int result, String cur_version, String upg_version) {

    }

    @Override
    public void vRetDoDeviceUpdate(String contactId, int result, int value) {

    }

    @Override
    public void vRetCancelDeviceUpdate(int result) {

    }

    @Override
    public void vRetDeviceNotSupport() {

    }

    @Override
    public void vRetClearDefenceAreaState(int result) {

    }

    @Override
    public void vRetSetImageReverse(int result) {

    }

    @Override
    public void vRetSetInfraredSwitch(int result) {

    }

    @Override
    public void vRetSetWiredAlarmInput(int state) {

    }

    @Override
    public void vRetSetWiredAlarmOut(int state) {

    }

    @Override
    public void vRetSetAutomaticUpgrade(int state) {

    }

    @Override
    public void vRetSetVisitorDevicePassword(int result) {

    }

    @Override
    public void vRetSetTimeZone(int result) {

    }

    @Override
    public void vRetGetSdCard(int result1, int result2, int SDcardID, int state) {

    }

    @Override
    public void VRetGetUsb(int result1, int result2, int SDcardID, int state) {

    }

    /**
     * SD卡格式化
     *
     * @param result 80：SD卡格式化成功  81:SD卡格式化失败 82：存储卡不存在  103:正在录像，不允许格式化
     */
    @Override
    public void vRetSdFormat(int result) {

    }

    @Override
    public void vRetSetGPIO(int result) {

    }

    @Override
    public void vRetSetPreRecord(int result) {

    }

    @Override
    public void vRetSetSensorSwitchs(int result) {

    }

    @Override
    public void vRecvSetLAMPStatus(String deviceId, int result) {

    }

    @Override
    public void vACK_RecvSetLAMPStatus(int result, int value) {

    }

    /**
     * 获取灯光控制返回
     *
     * @param deviceId IP或设备ID  如果是局域网用IP，不是局域网用设备ID
     * @param result   0：off   1:on   -1:获取失败
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRecvGetLAMPStatus(String deviceId, int result) {

    }

    @Override
    public void vRetPresetMotorPos(byte[] result) {

    }

    @Override
    public void vRetDefenceSwitchStatus(int result) {

    }

    @Override
    public void vRetDefenceSwitchStatusResult(byte[] result) {

    }

    @Override
    public void vRetAlarmPresetMotorPos(byte[] result) {

    }

    @Override
    public void vRetIpConfig(byte[] result) {

    }

    @Override
    public void vRetGetAlarmCenter(int result, int state, String ipdress, int port, String userId) {

    }

    @Override
    public void vRetSetAlarmCenter(int result) {

    }

    @Override
    public void vRetDeviceNotSupportAlarmCenter() {

    }

    /**
     * 获取用户密码返回
     *
     * @param pwd 用户密码
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetNPCVistorPwd(int pwd) {

    }

    @Override
    public void vRetDeleteDeviceAlarmID(String deviceId, int result, int result1) {

    }

    @Override
    public void vRetDeviceLanguege(int result, int languegecount, int curlanguege, int[] langueges) {

    }

    /**
     * 焦距缩放
     *
     * @param deviceId IP或设备ID  如果是局域网用IP，不是局域网用设备ID
     * @param result   2：改变焦点   3：改变焦距缩放  其他：不支持
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetFocusZoom(String deviceId, int result) {

    }

    @Override
    public void vRetGetAllarmImage(int id, String filename, int errorCode) {
        AlarmImageInfo info=new AlarmImageInfo(id,filename,errorCode);
        RxBus.get().post(RxBUSAction.EVENT_RET_ALARMIMAGE,info);
    }

    @Override
    public void vRetFishEyeData(int iSrcID, byte[] data, int datasize) {

    }

    @Override
    public void vRetGetNvrIpcList(String contactId, String[] date, int number) {

    }

    @Override
    public void vRetSetWifiMode(String id, int result) {

    }

    /**
     * 获取对ap模式的支持
     *
     * @param id     IP或设备ID  如果是局域网用IP，不是局域网用设备ID
     * @param result 2：支持AP模式（目前为ap）  1：支持AP模式,不处于AP模式  0：不支持AP模式
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetAPModeSurpport(String id, int result) {

    }

    /**
     * 获取设备类型
     *
     * @param id       IP或设备ID  如果是局域网用IP，不是局域网用设备ID
     * @param mainType 联系人类型
     * @param subType  子类型
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetDeviceType(String id, int mainType, int subType) {

    }

    @Override
    public void vRetNVRInfo(int iSrcID, byte[] data, int datasize) {

    }

    @Override
    public void vRetGetFocusZoom(String deviceId, int result, int value) {

    }

    @Override
    public void vRetSetFocusZoom(String deviceId, int result, int value) {

    }

    @Override
    public void vRetSetGPIO(String contactid, int result) {

    }

    @Override
    public void vRetGetGPIO(String contactid, int result, int bValueNs) {

    }

    @Override
    public void vRetGetDefenceWorkGroup(String contactid, byte[] data) {

    }

    @Override
    public void vRetSetDefenceWorkGroup(String contactid, byte[] data) {

    }

    @Override
    public void vRetFTPConfigInfo(String contactid, byte[] data) {

    }

    @Override
    public void vRetGPIOStatus(String contactid, byte[] Level) {

    }

    @Override
    public void vRecvSetGPIOStatus(String contactid, byte[] data) {

    }

    /**
     * 带名字防区开关
     *
     * @param value
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetDefenceSwitch(int value) {

    }

    @Override
    public void vRetSetDefenceSwitch(int result) {

    }

    @Override
    public void vRetDefenceAreaName(String contactid, byte[] data) {

    }

    /**
     * PIP灯光控制
     *
     * @param value 1：支持PIP灯光控制   非1：不支持PIP灯光控制
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetPIRLightControl(int value) {

    }

    @Override
    public void vRetFishInfo(String contactid, byte[] data) {

    }

    /**
     * 十分钟自动拍照
     *
     * @param value
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetAutoSnapshotSwitch(int value) {

    }

    @Override
    public void vRetSetAutoSnapshotSwitch(int result) {

    }

    /**
     * 是否支持记忆点
     *
     * @param deviceId IP或设备ID  如果是局域网用IP，不是局域网用设备ID
     * @param result   1:支持  0：不支持
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRecvGetPrepointSurpporte(String deviceId, int result) {

    }

    /**
     * 获取移动侦测灵敏度
     *
     * @param value 值的范围 0-6(越高越迟钝)
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetMotionSensResult(int value) {

    }

    /**
     * 移动侦测灵敏度设置ret回调
     *
     * @param iResult 0:设置成功
     */
    @Override
    public void vRetSetMotionSensResult(int iResult) {

    }

    @Override
    public void vRetVideoQuality(String contactId, byte[] data) {

    }


    @Override
    public void vRetGetIndexFriendStatus(int count, String[] contactIds, int[] IdProtery, int[] status, int[] DevTypes,int[] SubType, int[] DefenceState, byte bRequestResult) {
        Log.e("dxsTest","count:"+count+"ids"+Arrays.toString(contactIds));
    }

    /**
     * 红外LED关闭
     *
     * @param value 1:不用红外灯补光   0:自动根据环境亮度进行补光
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetIRLEDResult(int value) {

    }

    @Override
    public void vRetSetIRLEDResult(String contactId, int iResult) {

    }

    @Override
    public void vRetGetApIsWifiSetting(String contactId, byte[] data) {

    }

    @Override
    public void vRetSetApStaWifiInfo(String contactId, byte[] data) {

    }

    /**
     * 获取夜视LED状态
     *
     * @param contactId 设备ID
     * @param iResult
     * @see P2PHandler#getNpcSettings(String, String)
     */
    @Override
    public void vRetGetLEDResult(String contactId, int iResult) {

    }

    /**
     * 设置夜视LED状态
     *
     * @param contactId
     * @param iResult
     */
    @Override
    public void vRetSetLEDResult(String contactId, int iResult) {

    }

    @Override
    public void vRetSetApStart(String contactId, int iResult) {

    }

    @Override
    public void vRetLockInfo(String contactId, byte[] data) {

    }

    /**
     * 群组消息回调
     *
     * @param groupName  群组名
     * @param srcId      用户ID
     * @param ReciveTime 接收时间
     * @param msg        消息
     * @param msgSize    消息大小
     * @param MesgType   消息类型
     */
    @Override
    public void vRetGroupMessage(String groupName, int srcId, int ReciveTime, byte[] msg, int msgSize, int MesgType) {

    }

    /**
     * 群组消息ACK回调
     *
     * @param groupName 群组名
     * @param srcId     临时请求标记
     * @param Error     错误编号
     */
    @Override
    public void vRetGroupMessageAck(String groupName, int srcId, int Error) {

    }

    /**
     * 离线群组消息回调
     *
     * @param groupName  群组名
     * @param srcId      用户ID
     * @param ReciveTime 接收时间
     * @param msg        消息
     * @param msgSize    消息大小
     * @param MesgType   消息类型
     */
    @Override
    public void vRetOfflineGroupMessage(String groupName, int srcId, int ReciveTime, byte[] msg, int msgSize, int MesgType) {

    }

    /**
     * 群组消息发送结束
     */
    @Override
    public void vRetGroupMessageOver() {

    }


}
