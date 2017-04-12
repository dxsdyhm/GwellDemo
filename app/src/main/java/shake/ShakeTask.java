package shake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;


/**
 * 扫描任务
 * Created by Administrator on 2017/4/11.
 */

public class ShakeTask extends Thread {
    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 8899;
    /**
     * 指令发送时间
     */
    public int SEND_TIMES;
    /**
     * 端口
     */
    private int port;
    /**
     * 是否执行
     */
    private boolean isRun;
    /**
     * 数据包socket
     */
    private DatagramSocket server;
    /**
     * 广播socket
     */
    private DatagramSocket broadcast;
    /**
     * 通道选择器
     */
    private Selector selector;
    /**
     * udp包
     */
    private DatagramChannel channel;

    private Handler handler;

    public ShakeTask(Handler handler) {
        this.handler = handler;
        this.port = DEFAULT_PORT;
        this.SEND_TIMES = 10;
    }

    public void setSearchTime(long time) {
        SEND_TIMES = (int) (time / 1000);
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(ShakeManager.HANDLE_ID_SEARCH_START);//开始扫描了
        isRun = true;
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.configureBlocking(false);//设置非堵塞
            server = channel.socket();
            server.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer receiveBuffer = ByteBuffer.allocate(256);

            new Thread() {
                @Override
                public void run() {
                    try {
                        int times = 0;
                        broadcast = new DatagramSocket();
                        broadcast.setBroadcast(true);
                        Log.e("leleshaking", "isRun=" + isRun + "--" + "times=" + times);
                        while (times < SEND_TIMES) {
                            if (!isRun) {
                                return;
                            }
                            times++;
                            Log.e("my", "shake thread send broadcast.");

                            ShakeData data = new ShakeData();
                            data.setCmd(ShakeData.Cmd.GET_DEVICE_LIST);

                            DatagramPacket packet = new DatagramPacket(
                                    ShakeData.getBytes(data), ShakeData.getBytes(data).length,
                                    InetAddress.getByName("255.255.255.255"),
                                    port);
                            broadcast.send(packet);
                            Thread.sleep(1000);
                        }

                        Log.e("my", "shake thread broadcast end.");
                    } catch (Exception e) {
                        Log.e("leleshaking", e.toString());
                        e.printStackTrace();
                    } finally {
                        try {
                            broadcast.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ShakeManager.getInstance().stopShaking();
                    }
                }
            }.start();
            while (isRun) {
                int n = selector.select(100);
                if (n > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (SelectionKey key : keys) {
                        keys.remove(key);
                        if (key.isReadable()) {
                            DatagramChannel dc = (DatagramChannel) key.channel();
                            InetSocketAddress client = (InetSocketAddress) dc.receive(receiveBuffer);
                            key.interestOps(SelectionKey.OP_READ);
                            receiveBuffer.flip();
                            ShakeData data = ShakeData.getShakeData(receiveBuffer);

                            switch (data.getCmd()) {
                                case ShakeData.Cmd.RECEIVE_DEVICE_LIST:

                                    if (data.getError_code() == 1) {
                                        Message msg = new Message();
                                        msg.obj = data;
                                        msg.what = ShakeManager.HANDLE_ID_RECEIVE_DEVICE_INFO;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ip", client.getAddress().getHostAddress() + "");
                                        msg.setData(bundle);
                                        handler.sendMessage(msg);
                                        break;
                                    } else {
                                    }
                                    receiveBuffer.clear();
                            }
                        }
                    }

                }
            }
            Log.e("my", "shake thread end.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("leleshaking", e.toString());
            e.printStackTrace();
        } finally {
            ShakeManager.getInstance().stopShaking();

            if (null != handler) {
                handler.sendEmptyMessage(ShakeManager.HANDLE_ID_APDEVICE_END);
            }

            try {
                server.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void killThread() {
        if (isRun) {
            selector.wakeup();
            isRun = false;
        }
    }
}
