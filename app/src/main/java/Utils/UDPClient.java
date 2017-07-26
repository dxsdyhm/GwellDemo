package Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.p2p.core.utils.MyUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class UDPClient {
    private static UDPClient UDPc = null;
    DatagramSocket datagramSocket = null;
    Handler handler;
    boolean isRecieve = true;
    private boolean isStartSuccess = false;
    private int port;
    private String ip;
    byte[] data;
    int count;
    SendThread sendThread;

    private UDPClient() {
    }

    ;

    public synchronized static UDPClient getInstance() {
        if (null == UDPc) {
            synchronized (UDPClient.class) {
                UDPc = new UDPClient();
            }
        }
        return UDPc;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void send(byte[] messgae, int port, String ip,int count) throws Exception {
        this.port =port;
        this.data = messgae;
        this.ip = ip;
        this.count=count;
        openThread();
    }
    class SendThread extends Thread{
        @Override
        public void run() {
            DatagramSocket s = null;
            try {
                s = new DatagramSocket();
                InetAddress local = null;
                try {
                    local = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                // 换成服务器端IP
                DatagramPacket p = new DatagramPacket(data, data.length, local, port);
                try {
                    while (count>0){
                        try {
                            if (s != null) {
                                s.send(p);
                                Log.e("leleTest","count="+count);
                            }
                            count--;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    public void startListner(final int port) {
        new Thread() {
            @Override
            public void run() {
                isStartSuccess = false;
                while (!isStartSuccess) {
                    try {
                        listner(port);
                        try {
                            Log.e("leleTest","listner");
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                        isStartSuccess=false;
                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            }
        }.start();
    }

    private void listner(int port)throws Exception {
        // UDP服务器监听的端口
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        byte[] message = new byte[1024];
        try {
            // 建立Socket连接
            datagramSocket = new DatagramSocket(port);
            DatagramPacket datagramPacket = new DatagramPacket(message,
                    message.length);
            try {
                isRecieve = true;
                isStartSuccess = true;
                while (isRecieve) {
                    // 准备接收数据
                    datagramSocket.receive(datagramPacket);
                    String rIpAdrress = datagramPacket.getAddress().getHostAddress();
                    int rPort = datagramPacket.getPort();
                   // Log.e("leleTest", "ip=" + datagramPacket.getAddress().getHostAddress() + "port=" + datagramPacket.getPort());
                    int cmd = MyUtils.bytesToInt(message, 0);
                    if (cmd == 51 || cmd == 0) {
                        if (handler != null) {
                            Message ms = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("data", message);
                            ms.what = cmd;
                            ms.setData(bundle);
                            handler.sendMessage(ms);
                            Log.e("dxsUDP", "data-->" + Arrays.toString(message));
                        }
                    }
                    if (cmd == 0) {
                        byte[] rData = new byte[8];
                        rData[0] = 1;
                        System.arraycopy(message, 4, rData, 4, 4);
                        DatagramPacket p = new DatagramPacket(rData, rData.length, InetAddress.getByName(rIpAdrress), rPort);
                        if (datagramSocket != null) {
                            datagramSocket.send(p);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("leleTest", "IOException");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e("leleTest", "SocketException");
        } finally {
            StopListen();
        }
    }


    public void StopListen() {
        isRecieve = false;
        isStartSuccess = true;
        if (null != datagramSocket) {
            datagramSocket.close();
            datagramSocket = null;
        }
    }
    public void openThread() {
        if (null == sendThread || !sendThread.isAlive()) {
            sendThread = new SendThread();
            sendThread.start();
        }
    }

    public void kill() {
        count=0;
        sendThread = null;
    }

}
