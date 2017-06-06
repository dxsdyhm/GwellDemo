package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.p2p.core.P2PHandler;

import java.io.File;

import Utils.RxBUSAction;
import Utils.ToastUtils;
import adapter.AlarmInfoProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import entity.AlarmImageInfo;
import entity.AlarmInfo;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by dansesshou on 17/4/24.
 * 提供一个测试Activity,开发人员请忽略此页面
 */

public class AllarmImageListActivity extends BaseActivity {
    private final static String TextFormat = "报警共%d条";
    @BindView(R.id.tx_alarmsum)
    TextView txAlarmsum;
    @BindView(R.id.rc_allarm)
    RecyclerView rcAllarm;
    @BindView(R.id.et_pas)
    EditText etPas;

    private MultiTypeAdapter adapter;
    private Items items;

    private Context mContext;
    private boolean isGetProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allarmlist);
        mContext = this;
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        creatFile();
        AlarmInfoProvider p = new AlarmInfoProvider();
        p.setOnItemClickListner(listner);
        adapter.register(AlarmInfo.class, p);
        rcAllarm.setLayoutManager(new LinearLayoutManager(this));
        rcAllarm.setAdapter(adapter);
    }

    private void creatFile() {
        String fileTemp = String.format(AlarmInfoProvider.localPathFormat, 1);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + fileTemp);
        File paren = file.getParentFile();
        Log.e("dxsTest", "file.getParentFile:" + paren.getPath() + "isD" + paren.isDirectory());
        File test = new File("/storage/emulated/0/test");
        if (!test.exists()) {
            boolean result = test.mkdirs();
            Log.e("dxsTest", "result:" + result);
        }
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_ALARM)
            }
    )
    public void initUI(AlarmInfo info) {
        if (items != null) {
            items.add(0, info);
            String text = String.format(TextFormat, items.size());
            txAlarmsum.setText(text);
            adapter.notifyItemRangeInserted(0, 1);
        }
    }

    @Subscribe(
            tags = {
                    @Tag(RxBUSAction.EVENT_RET_ALARMIMAGE)
            }
    )
    public void GetAlarmImageInfo(AlarmImageInfo info) {
        Log.e("dxsTest", "info:" + info.toString());
        adapter.notifyDataSetChanged();
    }

    public void GetAlarmImage(AlarmInfo info) {
        String userpwd = etPas.getText().toString().trim();
        if (TextUtils.isEmpty(userpwd)) {
            userpwd = "qwe123";
        }
        if (info != null) {
            String pwd = P2PHandler.getInstance().EntryPassword(userpwd);
            String imagpath = getImagePath(info);
            String LocalPath = getLocalImagePath(info);
            Log.e("dxsTest", "imagpath:" + imagpath + "LocalPath:" + LocalPath);
            P2PHandler.getInstance().GetAllarmImage(info.getSrcId(), pwd, imagpath, LocalPath);
            //如果没有UI上展示进度的需求，获取下载进度的代码可不写
        } else {
            ToastUtils.ShowError(this, getString(R.string.no_alarm), Toast.LENGTH_SHORT, true);
        }
    }

    //报警时设备传的路径
    private String getImagePath(AlarmInfo info) {
        return info.getAlarmCapDir() + "01.jpg";
    }

    //手机本地保存路径
    private String getLocalImagePath(AlarmInfo info) {
        String path = String.format(AlarmInfoProvider.localPathFormat, info.getId());
        return Environment.getExternalStorageDirectory().getPath() + path;
    }

    private AlarmInfoProvider.OnItemClickListner listner = new AlarmInfoProvider.OnItemClickListner() {
        @Override
        public void onItemClick(int position, AlarmInfo info) {
            GetAlarmImage(info);
        }
    };
}
