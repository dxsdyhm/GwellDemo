package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gwelldemo.R;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import Utils.RxBUSAction;
import adapter.SerialAppProvider;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import rx.Observable;
import rx.functions.Action1;

public class SerialAppActivity extends BaseActivity {

    private RecyclerView rcSerialApp;
    private TextView txt_num;
    private Items items;
    private MultiTypeAdapter adapter;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_app);
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
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        String info = Arrays.toString(cmd);
                        RxBus.get().post(RxBUSAction.EVENT_RET_CUSTOM_CMD, info);
                    }
                });
    }
}
