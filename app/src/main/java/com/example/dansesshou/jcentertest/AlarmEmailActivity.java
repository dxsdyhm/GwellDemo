package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gwelldemo.R;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import Utils.RxBUSAction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmEmailActivity extends BaseActivity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_receiver)
    EditText etReceiver;
    @BindView(R.id.et_sender)
    EditText etSender;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_smtp)
    EditText etSmtp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.et_encryption)
    EditText etEncryption;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tx_info)
    TextView txInfo;

    private int CheckedEmailTimes = 0;
    private String TAG = "zxy";
    private String maddressee;
    String msmtpAddress;
    String msender;
    String mport_number;
    String mEncryption;
    String pwd;
    String deviceId;
    String devicePsw;
    byte encryptType;
    boolean isSurportSMTP = false;
    boolean isEmailLegal = true;
    boolean isEmailChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_email);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_save)
    public void onClick() {
        saveManualSetEmail();
        CheckedEmailTimes = 0;

        txInfo.setText("");
    }

    public void saveManualSetEmail() {
        deviceId = etId.getText().toString().trim();
        devicePsw = etPwd.getText().toString().trim();
        devicePsw = P2PHandler.getInstance().EntryPassword(devicePsw);//经过转换后的设备密码
        maddressee = etReceiver.getText().toString().trim()
                .replaceAll(" ", "");
        msmtpAddress = etSmtp.getText().toString().trim()
                .replaceAll(" ", "");
        msender = etSender.getText().toString().trim()
                .replaceAll(" ", "");
        pwd = etPassword.getText().toString().trim();
        mport_number = etPort.getText().toString().trim();
        mEncryption = etEncryption.getText().toString().trim();
//        encryptType = parseEncryption(mEncryption);
        int port = 0;
        if (maddressee == null || maddressee.equals("")) {
            txInfo.append("不能为空\n");
            return;
        }
        if (maddressee.charAt(maddressee.length() - 1) == ','
                || maddressee.charAt(maddressee.length() - 1) == '，') {
            maddressee = maddressee.substring(0, maddressee.length() - 1);
        }
        String[] addressees = maddressee.split(",|，");
        if (addressees.length > 3) {
            txInfo.append("接收邮箱过多\n");
            return;
        }
        if (!isEmail(maddressee)) {
            txInfo.append("邮箱格式不正确\n");
            return;
        }
        if (msender == null || msender.equals("")) {
            txInfo.append("发送邮箱不能为空\n");
            return;
        }
        if (msender.charAt(msender.length() - 1) == ','
                || msender.charAt(msender.length() - 1) == '，') {
            msender = msender.substring(0, msender.length() - 1);
        }
        String[] senders = msender.split(",|，");
        if (senders.length > 1) {
            txInfo.append("发送邮箱只能唯一\n");
            return;
        }
        if (!isEmail(msender)) {
            txInfo.append("邮箱格式错误\n");
            return;
        }
        if (pwd == null || pwd.equals("")) {
            txInfo.append("密码或SMTP授权码不能为空\n");
            return;
        }
        if (msmtpAddress == null || msmtpAddress.equals("")) {
            txInfo.append("SMTP地址不能为空\n");
            return;
        }
        if (msmtpAddress.charAt(msmtpAddress.length() - 1) == ','
                || msmtpAddress.charAt(msmtpAddress.length() - 1) == '，') {
            msmtpAddress = msmtpAddress.substring(0, msmtpAddress.length() - 1);
        }
        String[] smtps = msmtpAddress.split(",");
        if (smtps.length > 5) {
            txInfo.append("SMTP地址过多\n");
            return;
        }
        if (!checkSmtpAddress(msmtpAddress)) {
            txInfo.append("SMTP地址错误\n");
            return;
        }
        if (mport_number != null && !mport_number.equals("")) {
            port = Integer.parseInt(mport_number);
        } else {
            txInfo.append("断开号不能为空\n");
            return;
        }
        if (port < 1 || port > 65535) {
            txInfo.append("端口号在0~65535之间>端口号在0~65535之间\n");
            return;
        }
        try {
            int timeOut = 10000 + (smtps.length - 1) * 4000;
            String subject = new String("zxy  Hello Attention: alarm".getBytes(), "UTF-8");
            String countent = new String("zxy  Hello\n Please check the attached picture for more information.".getBytes(), "UTF-8");

            Log.e("sendmanual", "addressee=" + maddressee + "--" + "port="
                    + port + "--" + "smtpAddress=" + msmtpAddress + "sender="
                    + msender + "--" + msmtpAddress + "--" + "memailPwd="
                    + pwd + "--" + "subject=" + subject + "--"
                    + "countent=" + countent + "--" + "encryptType="
                    + encryptType);
            P2PHandler.getInstance().setAlarmEmailWithSMTP(deviceId,
                    devicePsw, (byte) 3, maddressee, port,
                    msmtpAddress, msender, pwd, subject, countent,
                    encryptType, (byte) 0, 0);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean isEmail(String maddressee) {
        String[] emails = maddressee.split(",|，");
        for (int i = 0; i < emails.length; i++) {
            if (!(emails[i].contains("@") && emails[i].contains("."))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSmtpAddress(String msmtpAddress) {
        String[] smtps = msmtpAddress.split(",|，");
        for (int i = 0; i < smtps.length; i++) {
            String[] ips = smtps[i].split("\\.");
            boolean isIp = true;
            for (int j = 0; j < ips.length; j++) {
                if (!isNumeric(ips[j])) {
                    isIp = false;
                    break;
                }
            }
            if (isIp) {
                if (!checkIP(smtps[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    // 判断ip地址是否合法
    public boolean checkIP(String str) {
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private byte parseEncryption(String mEncryption) {
        if ("非加密".equals(mEncryption)) {
            encryptType = 0;
        } else if ("SSL".equals(mEncryption)) {
            encryptType = 1;
        } else if ("STL".equals(mEncryption)) {
            encryptType = 2;
        } else {
            encryptType = 3;
        }
        return encryptType;
    }

    private void getSMTPMessage(int result) {
        if ((byte) ((result >> 1) & (0x1)) == 0) {
            isSurportSMTP = false;
            txInfo.append("isSurportSMTP = false\n");
            txInfo.append("ok");
            getAlarmEmailHandler.removeCallbacks(runable);
        } else {
            isSurportSMTP = true;
            txInfo.append("isSurportSMTP = true\n");
            if ((byte) ((result >> 4) & (0x1)) == 0) {// 验证通过
                isEmailChecked = true;
                txInfo.append("isSurportSMTP = true\n");
                if ((byte) ((result >> 2) & (0x1)) == 0) {
                    isEmailLegal = false;
                    txInfo.append("isEmailLegal = false\n");
                } else {
                    if ((byte) ((result >> 3) & (0x1)) == 0) {
                        isEmailLegal = true;
                        txInfo.append("isEmailLegal = true\n");
                        txInfo.append("ok");
                        getAlarmEmailHandler.removeCallbacks(runable);
                    } else {// 格式错误
                        txInfo.append("邮箱格式错误\n");
                    }
                }
            } else {
                isEmailChecked = false;
                if (CheckedEmailTimes < 5) {
                    CheckedEmailTimes++;
                    delayGetAlarmEmial();
                } else {
                    txInfo.append("邮箱未验证,请确保邮箱已开启SMTP服务!\n");
                }
            }

        }
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_ACK_RET_SET_ALARM_EMAIL)
            }
    )
    public void ackSetAlarmEmail(Integer result) {
        if (result == 9999) {
            txInfo.append("设备密码错误\n");
        } else if (result == 9998) {
            txInfo.append("网络异常\n");
        }
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_SET_ALARM_EMAIL)
            }
    )
    public void setAlarmEmail(Integer result) {
        Log.d(TAG, "setAlarmEmail: " + result);
        if ((result & (1 << 0)) == 0) {
            txInfo.append("setAlarmEmail成功！\n");
            CheckedEmailTimes++;
            delayGetAlarmEmial();
        } else if ((result & (1 << 0)) == 1) {
            txInfo.append("getAlarmEmail成功！\n");
            getSMTPMessage(result);
        }
    }

    private void delayGetAlarmEmial() {
        getAlarmEmailHandler.postDelayed(runable, 3000);
        txInfo.append("第" + CheckedEmailTimes + "次请求\n");
    }

    private Handler getAlarmEmailHandler = new Handler();
    private Runnable runable = new Runnable() {
        public void run() {
            P2PHandler.getInstance().getAlarmEmail(deviceId,
                    devicePsw);
        }
    };
}
