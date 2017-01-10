# P2P-Core 使用说明
### 1.集成
* 支持Gradle`compile 'com.p2p.core:p2p-core:0.1.4'`
 
``` Groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    ......
    compile 'com.p2p.core:p2p-core:0.1.4'
}
```  
### 2.使用  
> 在自定义Application中初始化  

* ```java
	public class MyApp extends Application {
    	public static MyApp app;
    	//这三个参数需要服务器分配，连接时需要传入，下面的数据仅供测试使用
    	//three parems come frome Gwell , the value below just test
    	public final static String APPID="1e9a2c3ead108413e8218a639c540e44";
    	public final static String APPToken="7db7b2bff80a025a3dad546a4d5a6c3ee545568d4e0ce9609c0585c71c287d08";
    	public final static String APPVersion="00.00.00.01";
    	@Override
    	public void onCreate() {
        	super.onCreate();
        	app = this;
        	initP2P(app);
    	}

        private void initP2P(MyApp app) {
        	//这两个Listner是两个接口，分别接收P2P数据与设置项数据的统一返回
        	P2PSpecial.getInstance().init(app,APPID,APPToken,APPVersion);
    	}
}```

* p2p初始化的代码MainService可不做修改,此Demo仅做示例
   
* P2PListener与SettingListener

* 两个监听分别实现的是P2P-Core的`IP2P`与`ISetting`  

 ```java
public class P2PListener implements IP2P {
		...
		...
    	@Override
    	public void vCalling(boolean isOutCall, String threeNumber, int type) {
			//手机被动呼叫
    	}

    	@Override
    	public void vReject(String deviceId, int reason_code) {
    		//监控挂断时回调
        	Intent intent = new Intent();
        	intent.setAction(MonitoerActivity.P2P_REJECT);
        	intent.putExtra("reason_code", reason_code);
       		MyApp.app.sendBroadcast(intent);
    	}
    	...
		...
}
```  
 ```java
public class SettingListener implements ISetting {
		...
		...
    	@Override
    	public void ACK_vRetSetDeviceTime(int msgId, int result) {
			//设置设备时间命令的ACK回调
    	}

    	@Override
    	public void ACK_vRetGetDeviceTime(int msgId, int result) {
			//获取设备时间命令的ACK回调
    	}
    	...
		...
}
``` 
 
### 3.备注  
*  MyAPP里的三个参数`APPID`,`APPTOKEN`,`APPVersion`需要提供包名分配
*  `APPVersion`建议保证每个新版本都不相同，但前两位保持一致，这样易于排查问题与将来新功能的加入，例如:`00.23.00.01`-->`00.23.00.02`-->`00.23.00.03`-->...
*  WebAPI接口的回调结果,新版网络部分单独的SDK在计划并实施中
*  设备端的交互接口参见[硬件接口说明]()
*  P2PView外层必须被`RelativeLayout`包裹
*  暂时仅支持Android API 22 即：`targetSdkVersion 22`
*  删除原来APP层的Mediaplayer.so SDL.so mp4v2.so(如果有的话)删除(P2P-Core已包含)
*  NDK 暂时仅支持[ARM 32位](),更多支持还在开发中,大部分手机已够用
*  大部分功能方法都在P2PHandler中,已导出Doc,后续会加强代码注释
*  代码与接口有少许修改，如果之前已经在使用Module的，改用Gradle之后，有其他使用上的问题，可询问技术支持

### WebAPI反馈码说明

* 1.由于服务器使用了新的反馈码，旧代码中的定义基本废除
所以，每一份与webAPI通信的代码都需要经历一次改造
* 新反馈码是16进制字符串，之前代码中大量存在Integer.paser(error_code)的情况
使用新webAPI之后这部分会崩溃
* 新反馈码与旧码不是一一对应的，所以旧版中发现新版没有对应时可删除对应反馈码的逻辑处理代码
* 新反馈码含义参见下图,代码中可通过```HttpErrorCode```引用
* ![新反馈码1](http://7xp6ld.com1.z0.glb.clouddn.com/0001.jpg)
![新反馈码2](http://7xp6ld.com1.z0.glb.clouddn.com/0002.jpg)
