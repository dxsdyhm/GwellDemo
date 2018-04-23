package com.example.dansesshou.jcentertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.gwelldemo.R;

import Utils.ToastUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactInfoActivity extends AppCompatActivity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra(LoginActivity.USERID);

    }

    String callID,CallPwd,userId;

    @OnClick(R.id.btn)
    public void onViewClicked() {
        callID = etId.getText().toString().trim();//设备号
        CallPwd = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(callID) || TextUtils.isEmpty(CallPwd)) {
            ToastUtils.ShowError(this, getString(R.string.tips_idpwd), 2000, true);
            return;
        }
        Intent i = new Intent(ContactInfoActivity.this,PanoramaActivity.class);
        i.putExtra("callID",callID);
        i.putExtra("CallPwd",CallPwd);
        i.putExtra(LoginActivity.USERID,userId);
        startActivity(i);
        finish();
    }
}
