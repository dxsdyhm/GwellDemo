# P2P-Core 使用说明
### 1.集成
 
``` Groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    ......
    compile 'com.p2p.core:p2p-core:0.2.3'
}
```    
### 2.使用([Doc文档][p2p-core doc],[文档下载][download])
> 在自定义Application中初始化  

* ```java
	public class MyApp extends Application {
    	public static MyApp app;
    	//这三个参数需要服务器分配(ID token与版本(在同一版本)是固定的,可硬编码在代码中。版本迭代时需要修改版本)
    	//连接时需要传入，下面的数据仅供测试使用
    	//three parems come frome Gwell , the value below just test
    	public final static String APPID="1e9a2c3ead108413e8218a639c540e44";
    	public final static String APPToken="7db7b2bff80a025a3dad546a4d5a6c3ee545568d4e0ce9609c0585c71c287d08";
    	//前两位是客户APP唯一编号(00.00 由技威分配),后两位是APP版本号(客户自定义),此参数不可省略
    	public final static String APPVersion="00.00.00.01";
    	@Override
    	public void onCreate() {
        	super.onCreate();
        	app = this;
        	initP2P(app);
    	}

        private void initP2P(MyApp app) {
        	//ID、TOKEN与APPVersion需要在服务器申请登记
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
*  设备端的交互接口参见[硬件接口说明]()
*  P2PView外层必须被`RelativeLayout`包裹
*  **暂时仅支持Android API 22 即：`targetSdkVersion 22`**
*  删除原来APP层的Mediaplayer.so SDL.so mp4v2.so(如果有的话)删除(P2P-Core已包含)
*  **NDK 暂时仅支持[ARM 32位](),更多支持还在开发中,大部分手机已够用**
*  大部分功能方法都在P2PHandler中,已导出[Doc][p2p-core doc],后续会加强代码注释
*  代码与接口有少许修改，如果之前已经在使用Module的，改用Gradle之后，有其他使用上的问题，可询问技术支持

### 4.关于图像
*  监控结束时会保存一张图片到SD卡,路径:~/screenshot/tempHead/{$appID}/{$deviceid}.jpg
*  手动截图路径:~/screenshot/{$deviceid}_{$time}.jpg  

### 5.技术支持  
*  对库有任何疑问可在[issues](https://github.com/dxsdyhm/GwellDemo/issues/1)
*  也可询问人工技术支持  
*  对Mediaplayer.so有特殊需求的可在[这里][mediaplayer.so download]单独下载,与Gradle版本一致.附[Mediaplayer.java][Mediaplayer.java download]

### 6.配网(让摄像头链接网络)  
>  配网是是添加摄像头的前期必要步骤，但已联网的设备不需此步骤。配网代码流程相似，基本都是将WiFi信息通过某种方式发给设备，设备连接成功之后通过UDP告知APP自身信息  

*  智能联机示例[SmartLinke][SmartLinke]

### 7.版本记录

#####  0.2.3 (Yoosee 15)
* P2PListener的reject回调增加两个exCode1与exCode2(将之前的实现转移到新回调并删除旧回调即可)
* SettingListener新增三个回调,是定制功能,可空实现
* 优化P2P连接
* 修复Index服务器一些BUG(稳定版,建议使用)

#####  0.2.2 (Yoosee 14)
* 增加Index服务器支持
* 修复本地录像部分BUG
* 增加视频暂停
* 老版网络请求过时,换新请求(Web SDK),方法集合是HttpSend类

### WebAPI反馈码说明

* 1.由于服务器使用了新的反馈码，旧代码中的定义基本废除
所以，每一份与webAPI通信的代码都需要经历一次改造
* 新反馈码是16进制字符串，之前代码中大量存在Integer.paser(error_code)的情况
使用新webAPI之后这部分会崩溃
* 新反馈码与旧码不是一一对应的，所以旧版中发现新版没有对应时可删除对应反馈码的逻辑处理代码
* 新反馈码含义参见下图,代码中可通过```HttpErrorCode```引用
* [新反馈码 附录1](http://7xp6ld.com1.z0.glb.clouddn.com/0001.jpg)  
  [新反馈码 附录2](http://7xp6ld.com1.z0.glb.clouddn.com/0002.jpg)

### TODO  
*  Web API与 P2P-Core的API 分开，可单独使用
*  增加配网部分单独依赖
*  支持`targetSdkVersion` >=23

[p2p-core doc]:http://doc.cloud-links.net/SDK/Android/SDK/Android/P2P-core/
[mediaplayer.so download]:http://olcizsy23.bkt.clouddn.com/libmediaplayer.so
[Mediaplayer.java download]:http://olcizsy23.bkt.clouddn.com/MediaPlayer.java
[SmartLinke]:https://github.com/jwkj/SmartlinkDemo
[download]:http://olcizsy23.bkt.clouddn.com/DOC.rar
