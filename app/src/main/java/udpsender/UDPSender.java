package udpsender;

import android.os.Handler;
import android.os.Message;


import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * UDP发送器
 * <p> -不建议直接在使用此类来发送UDP，而是通过{@link UDPManger}来发送，如后续业务有调整只需修改UDPManger，
 * 而不用找到所有调用UDPSender的地方修改逻辑（只需修改UDPManger即可），便于后期维护 </p>
 */
public class UDPSender {
    private static final String TAG = "UDPSender";
    private static UDPSender udpSender;

    private UDPSender() {
    }

    public static UDPSender getInstance() {
        if (udpSender == null) {
            synchronized (UDPSender.class) {
                udpSender = new UDPSender();
            }
        }
        return udpSender;
    }

    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 8899;
    /**
     * 开始发送
     */
    private static final int WHAT_SENDER_START = 1001;
    /**
     * 拿到一个设备
     */
    private static final int WHAT_SENDER_NEXT = 1002;
    /**
     * 结束
     */
    private static final int WHAT_SENDER_FINISHED = 1003;
    /**
     * 错误
     */
    private static final int WHAT_SENDER_ERROR = 1004;

    /**
     * 请求的端口，默认为8899
     */
    private int port = DEFAULT_PORT;
    /**
     * 接收超时时间,
     * <p>当UDPSender接收数据时，如果超过了shakeTimeOut还是没有响应，那么一般就是已经搜索完成了，这次可根据这个值来判断是否要结束搜索 </p>
     * <p>默认8s，5是时间因子</p>
     */
    private int receiveTimeOut = 8 * 5;
    /**
     * 接收数据时的标记,默认为2
     */
    private int receiveCmdFlag = 2;


    /**
     * 是否正在运行，次字段用于结束任务/判断是否正在执行
     */
    private boolean isRunning = false;

    /**
     * 指定数组（字节）
     */
    private byte[] instructions;
    /**
     * UDPsocket
     */
    private DatagramSocket serverSocket;
    /**
     * UDP广播socket
     */
    private DatagramSocket broadcastSocket;
    /**
     * 通道选择器
     */
    private Selector selector;
    /**
     * udp通道
     */
    private DatagramChannel channel;
    /**
     * 绑定网络地址
     */
    private static InetSocketAddress inetSocketAddress;
    /**
     * 结果回调对象
     */
    private UDPResultCallback callback;
    /**
     * 用于主线程于子线程之间通讯
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SENDER_START:
                    callback.onStart();
                    break;
                case WHAT_SENDER_NEXT:
                    UDPResult result = (UDPResult) msg.obj;
                    callback.onNext(result);
                    break;
                case WHAT_SENDER_FINISHED:
                    callback.onCompleted();
                    break;
                case WHAT_SENDER_ERROR:
                    Throwable throwable = (Throwable) msg.obj;
                    callback.onError(throwable);
                    break;
            }
        }
    };

    /**
     * 设置接收数据时的标记，默认为2
     *
     * @param receiveCmdFlag
     * @return
     */
    public UDPSender setReceiveCmdFlag(int receiveCmdFlag) {
        this.receiveCmdFlag = receiveCmdFlag;
        return this;
    }

    /**
     * 获取运行状态
     *
     * @return
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * 设置搜索超时时间，时间的毫秒值，eg：1000（即1s）
     *
     * @param receiveTimeOut
     */
    public UDPSender setReceiveTimeOut(int receiveTimeOut) {
        this.receiveTimeOut = (receiveTimeOut / 1000) * 5;//5是时间因子
        return this;
    }

    /**
     * 设置请求端口
     *
     * @param port 请求端口号，默认为8899，范围是1024-65535
     * @return 当前发送器对象
     */
    public UDPSender setPort(int port) {
        if (port < 1024 & port > 65535) {
            this.port = DEFAULT_PORT;
            return this;
        }
        this.port = port;
        return this;
    }

    /**
     * 初始化通道及选择器
     *
     * @throws IOException
     */
    private boolean initServer() {
        try {
            selector = Selector.open();// 打开选择器
            channel = DatagramChannel.open();// 打开UDP包通道
            channel.configureBlocking(false);// 设置非堵塞
            if (serverSocket != null && serverSocket.isBound()) {//判断之前是否绑定，绑定了就解绑，防止bind异常
                serverSocket.close();
            }
            serverSocket = channel.socket();// 通过通道拿到UDP数据包的Socket
            inetSocketAddress = new InetSocketAddress(port);
            serverSocket.bind(inetSocketAddress);// 绑定端口号
            channel.register(selector, SelectionKey.OP_READ);// 注册通道到选择器，并加上读取操作的选择键/事件

        } catch (BindException e) {
            handlerError(e);
            return false;
        } catch (SocketException e) {
            handlerError(e);//防止被多次绑定，造成端口占用
            return false;
        } catch (ClosedChannelException e) {
            handlerError(e);//防止被多次绑定，造成端口占用
            return false;
        } catch (IOException e) {
            handlerError(e);//防止被多次绑定，造成端口占用
            return false;
        } catch (Exception e) {
            handlerError(e);//防止被多次绑定，造成端口占用
            return false;
        }
        return true;
    }

    /**
     * 设置指令
     *
     * @param instructions 指令字节数组
     * @return
     */
    public UDPSender setInstructions(byte[] instructions) {
        if (instructions != null && instructions.length > 0) {
            this.instructions = instructions;
        } else {
            try {
                throw new Throwable("instructions is null");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 开始发送
     */
    public void send(UDPResultCallback callback) {
        this.callback = callback;
        isRunning = true;//每次开始前需要确保其能正常运行（多次调用时isRunning=false--->复位）
        new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_SENDER_START);//开始任务了
                try {
                    ByteBuffer receiveBuffer = ByteBuffer.allocate(256);// 用于接收响应的buffer
                    if (initServer()) {// 初始化通道及选择器,成功了才发数据
                        if (sendBroadcast(instructions)) {//发送广播,发送成功了才接收数据
                            receiveData(receiveBuffer);//接收数据
                        }
                    }
                } catch (IOException e) {
                    handlerError(e);
                } catch (InterruptedException e) {
                    handlerError(e);
                } finally {
                    closeTask();
                }
            }
        }.start();
    }

    /**
     * 关闭任务，释放资源
     */
    private void closeTask() {
        try {
            //都需要先判断是不是空并且是不是打开的
            if (selector.isOpen()) {
                selector.wakeup();
                selector.close();
            }
            if (channel.isOpen()) {
                channel.close();
            }
            if (serverSocket.isConnected()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送错误，通知回调
     *
     * @param throwable 错误信息
     */
    private void handlerError(Throwable throwable) {

        isRunning = false;//设置为不在运行状态
        closeTask();//还要关闭通道哦

        Message msg = handler.obtainMessage();
        msg.obj = throwable;
        msg.what = WHAT_SENDER_ERROR;
        handler.sendMessage(msg);//通知错误信息
        handler.sendEmptyMessage(WHAT_SENDER_FINISHED);//错误的同时，需要结束任务
    }

    /**
     * 接收数据
     *
     * @param receiveBuffer
     * @throws IOException
     * @throws InterruptedException
     */
    private void receiveData(ByteBuffer receiveBuffer) throws IOException, InterruptedException {
        int count = 0;//同于统计有多少次没有接收到结果
        while (isRunning) {
            int readyCount = 0;
            try {
                readyCount = selector.select(100);// 获取已经准备好的通道数
            } catch (RuntimeException e) {
                handlerError(e);
                e.printStackTrace();
            }
            count++;//统计接收的
            if (count > receiveTimeOut) {//如果超过了指定的时间，还是没有接收到数据，那么就停止搜索
                isRunning = false;// 结束搜索
                handler.sendEmptyMessage(WHAT_SENDER_FINISHED);
            }
            if (readyCount > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys()
                        .iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();// 拿到当前循环的key
                    iterator.remove();// 移除本次循环
                    if (key.isReadable()) {// 可读
                        DatagramChannel clientChannel = (DatagramChannel) key.channel();// 拿到udp通道（客户端）
                        InetSocketAddress clientAddress = (InetSocketAddress) clientChannel.receive(receiveBuffer);
                        key.interestOps(SelectionKey.OP_READ);//设置感兴趣的事件
                        receiveBuffer.flip();// 切换一下状态(写---->读)
                        receiveBuffer.limit(200);
                        // 拿到数据之后就开始处理
                        if (receiveBuffer.getInt(0) == receiveCmdFlag) {//根据数据接收标记
                            count = 0;// 复位
                            Message msg = handler.obtainMessage();
                            msg.what = WHAT_SENDER_NEXT;
                            String ip = clientAddress.getAddress().getHostAddress();
                            msg.obj = new UDPResult(ip, receiveBuffer.array());
                            handler.sendMessage(msg);
                        }
                        receiveBuffer.clear();// 读取完了就清空数据，以便下次使用
                    }
                }
            }
            Thread.sleep(100);// 停0.1s再发送请求，防止过快调用callback.onNext造成数据异常
        }
    }

    /**
     * 发送广播
     *
     * @param datas
     * @throws IOException
     */
    private boolean sendBroadcast(byte[] datas) throws IOException {
        if (datas == null || datas.length == 0) {
            handlerError(new Throwable("instructions is null"));
            return false;
        }
        broadcastSocket = new DatagramSocket();// 创建UDP数据报套接字
        broadcastSocket.setBroadcast(true);// 设置为广播模式
        DatagramPacket packet = new DatagramPacket(datas, datas.length,
                InetAddress.getByName("255.255.255.255"), port);// 构造UDP数据包
        broadcastSocket.send(packet);// 发送
        return true;
    }
}
