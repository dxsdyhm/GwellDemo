package com.example.dansesshou.jcentertest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.util.ArrayList;

import Utils.RxBUSAction;
import adapter.SensorProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.DefenceAreaInfo;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class SensorActivity extends BaseActivity {

    @BindView(R.id.tx_device_id)
    EditText txDeviceId;
    @BindView(R.id.tx_device_pwd)
    EditText txDevicePwd;
    @BindView(R.id.tx_ok)
    Button txOk;
    @BindView(R.id.rc_sensor)
    RecyclerView rcSensor;
    @BindView(R.id.tv_explain)
    TextView tvExplain;

    private String deviceId, devicePass;
    private String TAG = "zxy";
    private Items items;
    private MultiTypeAdapter adapter;

    public static final int ACK_PWD_ERROR = 9999;
    public static final int ACK_NET_ERROR = 9998;
    public static final int ACK_SUCCESS = 9997;
    public static final int ACK_INSUFFICIENT_PERMISSIONS = 9996;

    private int currentPositon = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        ButterKnife.bind(this);
        initRC();
        setExplain();
    }

    private void setExplain() {
        String str = "遥控  传感器  特殊传感器  已学习";
        int romStart = str.indexOf("遥控");
        int romEend = romStart + "遥控".length();
        int chStart = str.indexOf("传感器");
        int chEend = chStart + "传感器".length();
        int spStart = str.indexOf("特殊传感器");
        int spEend = spStart + "特殊传感器".length();
        int selStart = str.indexOf("已学习");
        int selEend = selStart + "已学习".length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new BackgroundColorSpan(Color.CYAN), romStart, romEend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new BackgroundColorSpan(Color.GREEN), chStart, chEend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new BackgroundColorSpan(Color.YELLOW), spStart, spEend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new BackgroundColorSpan(Color.RED), selStart, selEend, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvExplain.setText(style);
    }

    private void initRC() {
        rcSensor.setLayoutManager(new GridLayoutManager(this, 8));
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        SensorProvider provider = new SensorProvider();
        adapter.register(Integer.class, provider);
        provider.setOnItemClickListner(new SensorProvider.OnItemClickListner() {
            @Override
            public void onItemClick(int position, Integer integer) {
               if (integer == 1){
                //学习配对
                P2PHandler.getInstance().setDefenceAreaState(deviceId, devicePass, position / 8, position % 8, 0);
                   Toast.makeText(SensorActivity.this, R.string.start_pair, Toast.LENGTH_SHORT).show();
                currentPositon = position;

               }
            }
        });
        rcSensor.setAdapter(adapter);
    }

    private void retAck(int result) {
        if (result == ACK_PWD_ERROR) {
            Toast.makeText(SensorActivity.this, R.string.password_wrong, Toast.LENGTH_SHORT).show();
        } else if (result == ACK_NET_ERROR) {
            Toast.makeText(SensorActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
        } else if (result == ACK_INSUFFICIENT_PERMISSIONS) {
            Toast.makeText(SensorActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
        } else if (result == ACK_SUCCESS) {
            Log.d(TAG, "SENSOR_ACK_SUCCESS");
        }
    }

    @OnClick(R.id.tx_ok)
    public void onClick() {
        deviceId = txDeviceId.getText().toString();
        devicePass = txDevicePwd.getText().toString();
        devicePass = P2PHandler.getInstance().EntryPassword(devicePass);//经过转换后的设备密码
        if (!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(deviceId)) {
            items.clear();
            adapter.notifyDataSetChanged();
            P2PHandler.getInstance().getDefenceArea(deviceId,
                    devicePass);
        }
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_ACK_RET_GET_DEFENCE_AREA),
                    @Tag(RxBUSAction.EVENT_ACK_RET_SET_DEFENCE_AREA)
            }
    )
    public void ackGetDefenceArea(Integer result) {
        retAck(result);
    }


    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_GET_DEFENCE_AREA)
            }
    )
    public void getDefenceArea(DefenceAreaInfo food) {
        ArrayList<int[]> data = food.getData();
        for (int i = 0; i < data.size(); i++) {
            int[] group = data.get(i);
            for (int j : group) {
                items.add(j);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_SET_DEFENCE_AREA)
            }
    )
    public void setDefenceArea(DefenceAreaInfo food) {
        retSetDefenceArea(food);
    }

    private void retSetDefenceArea(DefenceAreaInfo food) {
        int result =  food.getResult();
        if (result == 0) {
            Toast.makeText(SensorActivity.this, R.string.learn_success, Toast.LENGTH_SHORT).show();
            if (currentPositon != -1) {
                items.set(currentPositon, 0);
                adapter.notifyItemChanged(currentPositon);
                currentPositon = -1;
            }
        } else if (result == 30) {
            Toast.makeText(SensorActivity.this, R.string.clear_success, Toast.LENGTH_SHORT).show();
        } else if (result == 32) {
            Toast.makeText(SensorActivity.this, R.string.already_added, Toast.LENGTH_SHORT).show();
        } else if (result == 41) {
            Toast.makeText(SensorActivity.this, R.string.device_not_support, Toast.LENGTH_SHORT).show();
        } else if (result == 24) {
            Toast.makeText(SensorActivity.this, R.string.channel_learned, Toast.LENGTH_SHORT).show();
        } else if (result == 25) {
            Toast.makeText(SensorActivity.this, R.string.learning, Toast.LENGTH_SHORT).show();
        } else if (result == 26) {
            Toast.makeText(SensorActivity.this, R.string.learn_out_time, Toast.LENGTH_SHORT).show();
        } else if (result == 37) {
            Toast.makeText(SensorActivity.this, R.string.code_invalid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SensorActivity.this, R.string.operation_failure, Toast.LENGTH_SHORT).show();
        }
    }
}
