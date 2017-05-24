package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.user.logserver.LogServer;
import com.example.user.logserver.LogServerUtils;
import com.gwelldemo.R;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.p2p.core.utils.MyUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import Utils.ToastUtils;
import Utils.UDPClient;
import Utils.Util;
import adapter.LogInfoProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.Device;
import entity.LogInfo;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dansesshou on 17/2/17.
 */

public class DeviceActivity extends BaseActivity {
    @BindView(R.id.tx_deviceinfo)
    TextView txDeviceinfo;
    @BindView(R.id.btn_sendlog)
    Button btnSendlog;
    @BindView(R.id.ry_content)
    RecyclerView ryContent;
    @BindView(R.id.tx_getport)
    TextView txGetport;
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.btn_stopsendlog)
    Button btnStopsendlog;
    @BindView(R.id.tx_log)
    TextView txLog;
    @BindView(R.id.tx_uertips)
    TextView txUertips;
    private Context mContext;
    private Device device;
    //设备与P2P通信端口
    private int LogPort = 12345;
    private final static String surpprot_email = "support@gwell.cc";
    private final static String testEnd = "Test End";
    private final static String Format_tips = "result:%d--logIp:%s--logPort:%d--numbers:%d";
    private final static String usertips = "After 2 minutes later,please select all the contents and copy them then send to %s or customer service people";


    private MultiTypeAdapter adapter;
    private Items items;

    private String IPDefault = "192.168.1.238";
    //日志服务器端口
    private int Server_port = 6566;
    //日志服务器域名
    private String NetAddressDefault = "debug.yoosee.mobi";

    private int LogServerInitResult = -1;
    private boolean isInitLogServer = false;
    //日志服务器IP
    private int logIP = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        ButterKnife.bind(this);
        mContext = this;
        device = (Device) getIntent().getSerializableExtra("device");
        initUI();
        initServer();
        UDPClient.getInstance().setHandler(handler);
        UDPClient.getInstance().startListner(8899);
    }

    private void initUI() {
        txDeviceinfo.setText(device.toString());
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        adapter.register(LogInfo.class, new LogInfoProvider());
        ryContent.setLayoutManager(new LinearLayoutManager(this));
        ryContent.setAdapter(adapter);
        etIp.setText(IPDefault);
        etPort.setText(String.valueOf(Server_port));
        ryContent.setVisibility(View.GONE);
        //setTitle(getString(R.string.wait_label));
        String tips = getString(R.string.wait_label);
        txUertips.setText(tips);
        txUertips.append("\n");
        txUertips.append(String.format(getString(R.string.tips2), surpprot_email));
        Linkify.addLinks(txUertips, Linkify.WEB_URLS | Linkify.EMAIL_ADDRESSES);
    }

    private void UserInitData() {
        logIP = (int) LogServerUtils.ipToLong(IPDefault);
        Log.e("dxstest", "IPDefault:" + IPDefault + "logIP:" + logIP);
        LogServerInitResult = LogServer.init(logIP, Server_port);
        LogPort = LogServer.fgGetLogCollectTcpForwardPort();
        String inf = String.format(Format_tips, LogServerInitResult, IPDefault, LogPort, items.size());
        txGetport.setText(inf);
        txLog.append(Util.getLogStr(inf));
        if (LogServerInitResult == 0) {
            ToastUtils.ShowSuccess(mContext, getString(R.string.test_log), 2000, true);
            txLog.append(Util.getLogStr(getString(R.string.test_log)));
        } else {
            ToastUtils.ShowError(mContext, "error:" + LogServerInitResult, 2000, true);
            txLog.append(Util.getLogStr("error:" + LogServerInitResult));
            txLog.append(Util.getLogStr(testEnd));
        }
    }

    private void initServer() {
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String ip = Util.IPPasser(NetAddressDefault);
                if (TextUtils.isEmpty(ip)) {
                    subscriber.onError(new Exception("net error"));
                } else {
                    subscriber.onNext(ip);
                }
                subscriber.onCompleted();
            }
        });
        observable.subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        UserInitData();
                        sendLogPort();
                    }

                    @Override
                    public void onError(Throwable e) {
                        txLog.append(Util.getLogStr(getString(R.string.netaddress_error)));
                        txLog.append(Util.getLogStr(testEnd));
                    }

                    @Override
                    public void onNext(String s) {
                        IPDefault = s;
                    }
                });
    }

    @OnClick(R.id.btn_sendlog)
    public void sendLogPort() {
        //initData();
        Log.e("dxsTest", "LogPort:" + LogPort + "logIP:" + logIP + "Server_port:" + Server_port);
        sendUdp(getSendLogData(LogPort, (byte) 0, logIP, Server_port), 8899, device.getIP());
    }

    @OnClick(R.id.btn_stopsendlog)
    public void StopSendLog() {
        sendUdp(getSendLogData(LogPort, (byte) 1, logIP, Server_port), 8899, device.getIP());
    }

    private void ExiteLogServer() {
        LogServer.LogCollectExit();
    }

    @Subscribe
    public void getLogInfo(LogInfo info) {
        if (items != null) {
            //items.add(0, info);
            //adapter.notifyItemRangeInserted(0, 1);
            txGetport.setText(String.format(Format_tips, LogServerInitResult, IPDefault, LogPort, items.size()));
        }
        txLog.append(Util.getLogStr(String.valueOf(info.getSrcID())));
        txLog.append(Util.getLogStr(info.getContStr()));
    }

    private void sendUdp(final byte[] data, final int port, String ip) {
        try {
            UDPClient.getInstance().send(data, port, ip, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //option 0:开始设备体检 1：关闭设备体检
    private byte[] getSendData(int port, byte option) {
        byte[] data = new byte[36];
        byte[] cmd = MyUtils.intToByte4(50);
        System.arraycopy(cmd, 0, data, 0, 4);
        data[4] = option;
        String phone = NetworkUtils.getIPAddress(true);
        String[] phoneIps = phone.split("\\.");
        byte[] ips = new byte[4];
        for (int i = 0; i < phoneIps.length; i++) {
            ips[i] = (byte) Integer.parseInt(phoneIps[i]);
        }
        data[16] = 2;
        System.arraycopy(ips, 0, data, 20, ips.length);
        //byte[] ports = MyUtils.intToBytes2(port);
        //data[24] = ports[2];
        //data[25] = ports[3];
        byte[] ports = MyUtils.intToByte4(port);
        System.arraycopy(ports, 0, data, 24, 4);
        Log.e("leleTest", Arrays.toString(data));
        return data;
    }

    //0 开始发送日志  1 停止发送日志
    private byte[] getSendLogData(int port, byte option, int serverIP, int Serverport) {
        byte[] data = new byte[44];
        byte[] cmd = MyUtils.intToByte4(50);
        System.arraycopy(cmd, 0, data, 0, 4);
        data[4] = option;
        String phone = NetworkUtils.getIPAddress(true);
        Log.e("dxsTest", "phone:" + phone);
        String[] phoneIps = phone.split("\\.");
        byte[] ips = new byte[4];
        for (int i = 0; i < phoneIps.length; i++) {
            ips[i] = (byte) Integer.parseInt(phoneIps[i]);
        }
        byte[] mesageVersion = MyUtils.intToByte4(1);
        System.arraycopy(mesageVersion, 0, data, 12, mesageVersion.length);
        data[16] = 2;
        System.arraycopy(ips, 0, data, 20, ips.length);
        //byte[] ports = MyUtils.intToBytes2(port);
        //data[24] = ports[2];
        //data[25] = ports[3];
        byte[] ports = MyUtils.intToByte4(port);
        System.arraycopy(ports, 0, data, 24, 4);
        byte[] servIP = MyUtils.intToByte4(serverIP);
        byte[] servPort = MyUtils.intToByte4(Serverport);
        System.arraycopy(servIP, 0, data, 36, 4);
        System.arraycopy(servPort, 0, data, 40, 4);
        Log.e("leleTest", Arrays.toString(data));
        return data;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            final byte[] data = bundle.getByteArray("data");
            int result = MyUtils.bytesToInt(data, 4);
            Log.e("leleTest", "result=" + result + "data=" + Arrays.toString(data));
            int cmd = msg.what;
            if (cmd == 51) {
                if (result == 0) {
                    ToastUtils.ShowSuccess(mContext, getString(R.string.device_send_ack), 2000, true);
                } else if (result == 20) {
                    ToastUtils.ShowError(mContext, getString(R.string.busy), 2000, true);
                } else if (result == 19) {
                    ToastUtils.ShowError(mContext, getString(R.string.parems_error), 2000, true);
                } else if (result == 255) {
                    ToastUtils.ShowError(mContext, getString(R.string.not_surpport), 2000, true);
                }
            } else {
                ToastUtils.ShowError(mContext, "cmd:" + cmd, 2000, true);
            }
        }
    };

    @Override
    protected void onDestroy() {
        ExiteLogServer();
        StopSendLog();
        UDPClient.getInstance().kill();
        UDPClient.getInstance().StopListen();
        super.onDestroy();
    }

    //产生模拟数据
    private void creatData() {
        final byte[] data = new byte[50];
        Observable.interval(1, 10, TimeUnit.SECONDS)
                .map(new Func1<Long, LogInfo>() {
                    @Override
                    public LogInfo call(Long aLong) {
                        long time = System.currentTimeMillis();
                        return new LogInfo(12345, 1, data, 5, time);
                    }
                })
                .subscribe(new Subscriber<LogInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LogInfo logInfo) {
                        RxBus.get().post(logInfo);
                    }
                });
    }
}
