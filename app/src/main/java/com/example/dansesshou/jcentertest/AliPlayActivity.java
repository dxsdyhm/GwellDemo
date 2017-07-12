package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gwelldemo.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by USER on 2017/6/16.
 */

public class AliPlayActivity extends BaseActivity {
    private final static String mVideoPath = "http://live-streaming-test.oss-cn-shenzhen.aliyuncs.com/npc_test_rtmp_live/npc_test.m3u8";
    private final static String mVideoPath1 = "http://cdn-fms.rbs.com.br/vod/hls_sample1_manifest.m3u8";
    @BindView(R.id.ijkplayer)
    SurfaceView ijkplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aliplay);
        ButterKnife.bind(this);
        initUI();
    }

    private void initUI() {

    }


}
