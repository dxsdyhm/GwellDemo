package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import Utils.RxBUSAction;
import adapter.SerialAppProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


public class SerialAppActivity extends BaseActivity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_cmd_string)
    EditText etCmdString;
    @BindView(R.id.send_string)
    Button sendString;
    @BindView(R.id.et_cmd_bytes)
    EditText etCmdBytes;
    @BindView(R.id.send_bytes)
    Button sendBytes;
    private RecyclerView rcSerialApp;
    private TextView txt_num;
    private Items items;
    private MultiTypeAdapter adapter;
    private int count = 0;
    private String deviceId, devicePwd, cmdBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_app);
        ButterKnife.bind(this);
        initRC();
        //send();
    }

    private void initRC() {
        txt_num = (TextView) findViewById(R.id.txt_num);
        rcSerialApp = (RecyclerView) findViewById(R.id.rc_serialapp);
        rcSerialApp.setLayoutManager(new LinearLayoutManager(this));
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        SerialAppProvider provider = new SerialAppProvider();
        adapter.register(String.class, provider);
        rcSerialApp.setAdapter(adapter);
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_CUSTOM_CMD)
            }
    )
    public void getDefenceArea(String info) {
        count++;
        info = count + ".  " + info;
        items.add(0, info);
        adapter.notifyItemInserted(0);
        txt_num.setText("获取数：" + count);
    }

    private final static byte[] cmd = new byte[]{1, 2, 0, 4, 5, 2, 1};

    private void send() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(  new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String info = Arrays.toString(cmd);
                        RxBus.get().post(RxBUSAction.EVENT_RET_CUSTOM_CMD, info);
                    }
                });
    }

    private void getInfo() {
        deviceId = etId.getText().toString().trim();
        devicePwd = etPwd.getText().toString().trim();

        if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(devicePwd)) {
            Toast.makeText(SerialAppActivity.this, R.string.not_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        devicePwd = P2PHandler.getInstance().EntryPassword(devicePwd);//经过转换后的设备密码
    }

    @OnClick({R.id.send_string, R.id.send_bytes})
    public void onClick(View view) {
        getInfo();
        switch (view.getId()) {
            case R.id.send_string:
                cmdBody = etCmdString.getText().toString().trim();
                if (TextUtils.isEmpty(cmdBody)){
                    Toast.makeText(SerialAppActivity.this, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                P2PHandler.getInstance().vSendDataToURAT(deviceId,
                        devicePwd, cmdBody.getBytes(), cmdBody.getBytes().length, true);
                break;
            case R.id.send_bytes:
                cmdBody = etCmdBytes.getText().toString().trim();
                if (TextUtils.isEmpty(cmdBody)){
                    Toast.makeText(SerialAppActivity.this, R.string.not_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cmdBody.contains(",")) {
                    String[] s = cmdBody.split(",");
                    byte[] byteBuffer = new byte[s.length];
                    for (int i = 0; i < s.length; i++) {
                        if (s[i].startsWith("0x")) {
                            byteBuffer[i] = Byte.decode("0x" + s[i].substring(2));
                        } else {
                            byteBuffer[i] = Byte.parseByte(s[i]);
                        }

                    }
                    P2PHandler.getInstance().vSendDataToURAT(deviceId,
                            devicePwd, byteBuffer, byteBuffer.length, true);
                }else {
                    Toast.makeText(SerialAppActivity.this, R.string.input_true_format, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
