package com.example.dansesshou.jcentertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.p2p.core.P2PHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Utils.OnLoadMoreListener;
import adapter.RecordFileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import entity.RecordFile;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class RecordFilesActivity extends AppCompatActivity {

    @BindView(R.id.tx_device_id)
    EditText txDeviceId;
    @BindView(R.id.tx_device_pwd)
    EditText txDevicePwd;
    @BindView(R.id.tx_ok)
    Button txOk;
    @BindView(R.id.tx_loading)
    TextView txLoading;
    @BindView(R.id.rc_recordfiles)
    RecyclerView rcRecordfiles;
    private MultiTypeAdapter adapter;
    private Items items;
    private String deviceId;
    private String devicePwd;
    private boolean firstLoad = true;
    private int count = 0;
    private LinearLayoutManager mLayoutManager;
    Date startDate = new Date(0);
    public static final String RECORDFILES = "com.yoosee.RECORDFILES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordfiles);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        txLoading.setVisibility(View.GONE);
        mLayoutManager = new LinearLayoutManager(this);
        rcRecordfiles.setLayoutManager(new LinearLayoutManager(this));
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //加载更多
        rcRecordfiles.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //获取上一次获取的数据中最后一位
                RecordFile file = (RecordFile) items.get(OnLoadMoreListener.itemCount - 1);
                String lastTime = file.getName().substring(6, 22);
                lastTime = lastTime.replace("_", " ");
                Date nextEndTime = null;
                try {
                    nextEndTime = sdf.parse(lastTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //因获取到数据是按照时间从近到远顺序排序
                //所以加载更多时startDate不用变,改变nextEndTime即可
                P2PHandler.getInstance().getRecordFiles(deviceId, devicePwd, startDate, nextEndTime);
            }
        });
        regFilter();
        deviceId = getIntent().getStringExtra("callID");
        devicePwd = getIntent().getStringExtra("callPwd");
        if (!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(devicePwd)) {
            getRecordFiles();
        }
    }

    private void regFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RECORDFILES);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RECORDFILES)) {
                //获取到的录像文件名字
                // 该数组中的文件名按照录像时间从近到远开始排序 且 每次获取到的nams的长度不大于64
                String[] names = (String[]) intent.getCharSequenceArrayExtra("recordList");
                byte option = intent.getByteExtra("option1", (byte) -1);
                if (option == 82) {
                    txLoading.setText(R.string.sd_notexist);
                }
                if (names.length > 0) {
                    updateAdapter(names);
                }
            }
        }
    };

    private void updateAdapter(String[] names) {
        txLoading.setVisibility(View.GONE);
        //因加载更多时，之前的最后一个文件和加载后第一个文件重复，故仅第一次加载时加载第一个文件
        if (firstLoad) {
            items.add(new RecordFile(count, names[0]));
            firstLoad = false;
        }
        for (int i = 1; i < names.length; i++) {
            items.add(new RecordFile(++count, names[i]));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            txLoading.setVisibility(View.VISIBLE);
            txLoading.setText(R.string.failed);
        }
    };

    @OnClick(R.id.tx_ok)
    public void onClick() {
        deviceId = txDeviceId.getText().toString().trim();
        devicePwd = txDevicePwd.getText().toString().trim();
        if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(devicePwd)) {
            Toast.makeText(RecordFilesActivity.this, R.string.notnull, Toast.LENGTH_SHORT).show();
        } else {
            getRecordFiles();
        }
    }

    private void getRecordFiles() {
        txLoading.setVisibility(View.VISIBLE);
        txLoading.setText(R.string.loading);
        devicePwd = P2PHandler.getInstance().EntryPassword(devicePwd);//经过转换后的设备密码
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        Date endDate = new Date(System.currentTimeMillis());
        RecordFileProvider recordFileProvider = new RecordFileProvider();
        adapter.register(RecordFile.class, recordFileProvider);
        recordFileProvider.setOnItemClickListner(new RecordFileProvider.OnItemClickListner() {
            @Override
            public void onItemClick(int position, RecordFile recordFile) {
                Intent intent = new Intent(RecordFilesActivity.this, PlayBackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("file", recordFile);
                intent.putExtra("recordFile", bundle);
                intent.putExtra("deviceId", deviceId);
                intent.putExtra("devicePwd", devicePwd);
                startActivity(intent);
            }
        });
        //获取录像列表
        P2PHandler.getInstance().getRecordFiles(deviceId, devicePwd, startDate, endDate);
        rcRecordfiles.setAdapter(adapter);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (items.size() == 0) {
                    hander.sendEmptyMessage(0);
                }
            }
        }, 8000);
    }


}
