package com.example.dansesshou.jcentertest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gwelldemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnIn;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }
    private void initUI() {
        btnIn= (Button) findViewById(R.id.btn_in);
        btnIn.setOnClickListener(this);
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_in:
                break;
        }
    }
}
