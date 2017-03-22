package com.example.dansesshou.jcentertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.libhttp.entity.HttpResult;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_email)
    AutoCompleteTextView etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.register_progress)
    ProgressBar registerProgress;
    @BindView(R.id.register_form)
    ScrollView registerForm;

    private String email;
    private String password;
    private String repassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        if (checkData()) {
            //*************************技威代码插入**********************************
            SubscriberListener<HttpResult> subscriberListener = new SubscriberListener<HttpResult>() {
                @Override
                public void onStart() {

                    showProgress(true);
                }

                @Override
                public void onNext(HttpResult registerResult) {
                    Log.d("zxy", "onNext" + registerResult.getError_code());
                    showProgress(false);
                    switch (registerResult.getError_code()) {
                        case HttpErrorCode.ERROR_0:
                            //注册成功
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                            Intent intent = getIntent();
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            setResult(2,intent);
                            finish();
                            break;
                        default:
                            //其它错误码需要用户自己实现
                            String msg = String.format("注册测试版(%s)", registerResult.getError_code());
                            Toast.makeText(MyApp.app, msg, Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                @Override
                public void onError(String error_code, Throwable throwable) {
                    Log.d("zxy", "onError");
                    showProgress(false);
                    Toast.makeText(MyApp.app, "onError:" + error_code, Toast.LENGTH_LONG).show();
                }
            };
            //邮箱注册
            HttpSend.getInstance().register("1", email, "", "", password,
                    repassword, "", "1", subscriberListener);
            //*************************技威代码插入**********************************
        }
    }

    private void showProgress(boolean b) {

        if (b){
            registerProgress.setVisibility(View.VISIBLE);
            registerForm.setVisibility(View.GONE);
        }else {
            registerProgress.setVisibility(View.GONE);
            registerForm.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkData() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        repassword = etRepassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
            Toast.makeText(RegisterActivity.this, R.string.error_field_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.contains("@")) {
            etEmail.setError(getString(R.string.error_invalid_email));
            return false;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(RegisterActivity.this, "两次输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
