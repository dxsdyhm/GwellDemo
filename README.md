# GwellDemo [![Build Status](https://travis-ci.org/dxsdyhm/GwellDemo.svg?branch=master)](https://travis-ci.org/dxsdyhm/GwellDemo)  [![Download][download_version_1]][download_version_2]

### 1.Demo function List
> 可开发除配网外所有Yoosee已开发的功能，配网在下面说明中单独列出，对于Demo中的内容，会根据实际情况增加;  
> 以下功能已经完成

* [x] 登陆(Login)  
* [x] 监控(Moniter)
* [x] 回放(Playback)
* [x] 注册(registered)
* [x] 获取报警图片(Get the alarm picture)
* [x] 发现局域网设备
* [ ] 局域网设备体检
* [x] 传感器(Sensor)

### 2.APK下载地址
* [Gwell][fir]

# P2P-Core 使用说明
### 1.版本记录

#####  0.3.8 (2017.04.14)
* 【修复】本地录像异常

#####  0.3.7 (2017.04.11)
* 【修复】ThirdPartLogin接口的返回的JavaBean与服务器命名不同，导致无法JSON解析，【注意】引用http里面的Result对象

#####  0.3.6 (2017.04.10)
* 【修复】getAccountByPhoneNO与ThirdPartLogin接口的返回类型报错(can't cast Class)

#####  0.3.3 (2017.03.31)
* 【新增】getAccountByPhoneNO接口返回实体类getAccountByPhoneNoResult,参见[HttpSend][HttpSend]

#####  0.3.1 (2017.03.29)
* 【修复】获取普通设置项的数据异常
* 【修复】部分Android 7.0设备在全景摄像头监控完成后退出崩溃
* 【新增】门铃离线消息与消息接收结束接口

##### [更多历史版本... ][old_version]

### 2.集成

> 不提供Eclipse的集成方式，如有需要可自行寻找解决办法([参考][Eclipse_massage],未经实践，如有成功的小伙伴，可发总结交流文章给作者)

``` Groovy
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    ......
    compile 'com.p2p.core:p2p-core:0.3.8'
}
```    
### 3.使用([Web接口][HttpSend]、[P2P接口][P2PHandler]、[完整Doc文档][p2p-core doc])
> 在自定义Application中初始化  

```java
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
    }
```  

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
	//所有的ACK回调都会有四个状态result:9996（权限不足（访客））9997（指令发送成功）9998（指令发送失败）9999（密码错误）
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
 
### 4.备注
*  代码顺序：init-->login-->connect-->...P2PHandler里面的其他指令...-->Disconnect
*  MyAPP里的三个参数`APPID`,`APPTOKEN`,`APPVersion`需要提供包名分配
*  `APPVersion`建议保证每个新版本都不相同，但前两位保持一致，这样易于排查问题与将来新功能的加入，例如:`00.23.00.01`-->`00.23.00.02`-->`00.23.00.03`-->...
*  设备端的交互接口参见[硬件接口说明]()
*  P2PView外层必须被`RelativeLayout`包裹,且不要在子类中声明(父类已存在)
*  监控页挂断之后必须finish，其他页面想返回必须重新startActivity
*  删除原来APP层的[Mediaplayer.so][mediaplayer.so download]、[SDL.so][SDL]与[mp4v2.so][mp4v2](如果有的话)删除(P2P-Core已包含)
*  [NDK 暂时仅支持ARM 32位,更多支持还在开发中,大部分手机已够用]()
*  Web接口(与服务器交互)方法都在[HttpSend][HttpSend]中。
*  P2P功能(与设备交互)方法都在[P2PHandler][P2PHandler]中,使用前确保设备在线，并且P2P初始化正常，后续会加强代码注释
*  代码与接口有少许修改，如果之前已经在使用Module的，改用Gradle之后，有其他使用上的问题，可询问技术支持
*  P2P消息简易流程图如下

![P2P消息简易流程图][p2p_ACK_JPG]
### 5.关于图像
*  监控结束时会保存一张图片到SD卡,路径:~/screenshot/tempHead/{$UserID}/{$deviceid}.jpg
*  手动截图路径:~/screenshot/{$deviceid}_{$time}.jpg  

### 6.技术支持  
*  对库有任何疑问可在[issues][issuses]
*  也可询问人工技术支持  

### 7.配网(让摄像头链接网络)  
>  配网是是添加摄像头的前期必要步骤，但已联网的设备不需此步骤。配网代码流程相似，基本都是将WiFi信息通过某种方式发给设备，设备连接成功之后通过UDP告知APP自身信息  

*  智能联机示例[SmartLinke][SmartLinke]
*  声波配网示例[soundwave][soundwave]

### 8.WebAPI反馈码说明

* 1.由于服务器使用了新的反馈码，旧代码中的定义基本废除
所以，每一份与webAPI通信的代码都需要经历一次改造
* 新反馈码是16进制字符串，之前代码中大量存在Integer.paser(error_code)的情况
使用新webAPI之后这部分会崩溃
* 新反馈码与旧码不是一一对应的，所以旧版中发现新版没有对应时可删除对应反馈码的逻辑处理代码
* 新反馈码含义参见下图,代码中可通过```HttpErrorCode```引用
* [新反馈码 附录1](http://7xp6ld.com1.z0.glb.clouddn.com/0001.jpg)  
  [新反馈码 附录2](http://7xp6ld.com1.z0.glb.clouddn.com/0002.jpg)

### 9.包含的三方库
> C/C++

* [x264][x264](未知)
* [SDL][SDL](未知)
* [faac][faac](1.18 beta)
* [mp4v2][mp4v2]([2.0.0][mp4v2_download])
* [FFmpeg][FFmpeg](3.2.4)
    * avcodec
    * avformat
    * swscale
    * swresample
    * avutil

### TODO  
*  Web API与 P2P-Core的API 分开，可单独使用
*  增加配网部分单独依赖

<h3 id="old_version">更多历史版本</h3>

#####  0.2.8 (2017.03.23)
* 【修复】局域网呼叫失败
* 【新增】微信登陆接口，参见[HttpSend][HttpSend]的WeChatLogin

#####  0.2.7 (2017.03.22)
* 【修复】获取报警图片大概率获取失败
* 【新增】支持targetSdkVersion 23+

#####  0.2.6 (2017.03.21)
* 【修复】新版WebAPI下服务器返回2个新错误码标记更换服务器地址
* 【修复】解决依然耗电（占用CPU）的情况（10%--->5%以内）
* 【新增】支持配置P2P入口地址
* 【新增】SDK内部Activity最终继承父类改为AppCompatActivity
* 【新增】指纹锁代码
* 【新增】AP重启代码
* 【新增】门铃群组消息接口集合
* 【新增】普通设备设置项注释、回放注释等，在implement SDK内接口时记得带上javaDoc,便于理解

#####  0.2.3 (Yoosee 15)
* P2PListener的reject回调增加两个exCode1与exCode2(将之前的实现转移到新回调并删除旧回调即可)
* SettingListener新增三个回调,是定制功能,可空实现
* 优化P2P连接
* 修复Index服务器一些BUG(稳定版,建议使用)

#####  0.2.2 (Yoosee 14)
* 增加Index服务器支持
* 修复本地录像部分BUG
* 增加视频暂停
* 老版网络请求过时,换新请求(Web SDK),方法集合是[HttpSend][HttpSend]类

[p2p-core doc]:http://doc.cloud-links.net/SDK/Android/p2p-core 'Doc文档'
[mediaplayer.so download]:http://olcizsy23.bkt.clouddn.com/libmediaplayer.so 'libmediaplayer.so下载'
[Mediaplayer.java download]:http://olcizsy23.bkt.clouddn.com/MediaPlayer.java 'MediaPlayer.java下载'
[SmartLinke]:https://github.com/jwkj/SmartlinkDemo '智能联机Demo'
[download]:http://olcizsy23.bkt.clouddn.com/DOC.rar '文档下载'
[P2PHandler]:http://doc.cloud-links.net/SDK/Android/p2p-core/com/p2p/core/P2PHandler.html 'P2PHandler页'
[p2p_ACK_JPG]:http://7xp6ld.com1.z0.glb.clouddn.com/p2p.png 'P2P消息 简易流程图'
[faac]:www.audiocoding.com 'faac主页地址'
[mp4v2]:https://github.com/TechSmith/mp4v2 'mp4v2主页地址'
[mp4v2_download]:http://olcizsy23.bkt.clouddn.com/libmp4v2.so 'mp4v2下载地址'
[FFmpeg]:https:[F:\UserWorkCode\DIYSmart\GwellDemo\README](F:%5CUserWorkCode%5CDIYSmart%5CGwellDemo%5CREADME.md)//ffmpeg.org/ 'FFmpeg官网'
[SDL]:http://www.libsdl.org/ 'SDL官网'
[x264]:http://www.videolan.org/developers/x264.html 'x264官网'
[HttpSend]:http://doc.cloud-links.net/SDK/Android/p2p-core/com/p2p/core/P2PSpecial/HttpSend.html 'HttpSend'
[old_version]:#old_version '更多版本记录'
[issuses]:https://github.com/dxsdyhm/GwellDemo/issues/new 'GitHub issuses'
[download_version_1]:https://api.bintray.com/packages/dxsdyhm/maven/p2p-core/images/download.svg '版本标记图'
[download_version_2]:https://bintray.com/dxsdyhm/maven/p2p-core/_latestVersion '版本标记链接'
[Eclipse_massage]:http://www.cnblogs.com/shortboy/p/4424944.html 'Eclipse使用参考'
[soundwave]:https://github.com/jwkj/SoundwaveDemo '声波配网示例'
[fir]:https://fir.im/smya 'APK下载地址'