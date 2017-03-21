package com.example.dansesshou.jcentertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hwangjr.rxbus.RxBus;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by USER on 2017/3/21.
 */

public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }
}
