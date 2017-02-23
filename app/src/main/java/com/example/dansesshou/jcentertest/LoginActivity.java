package com.example.dansesshou.jcentertest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwelldemo.R;
import com.libhttp.subscribers.SubscriberListener;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PSpecial.HttpErrorCode;
import com.p2p.core.P2PSpecial.HttpSend;

import static com.p2p.core.MediaPlayer.mContext;


/**
 * A login screen that offers login via email/password.
 * 此页面是Android studio 自动生成的具体登陆页需用户自己实现
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView = (EditText) findViewById(R.id.password);
        if(mPasswordView==null){
            Log.e("dxsTest","mPasswordView==null");
        }
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //*************************技威代码插入**********************************
            SubscriberListener<com.libhttp.entity.LoginResult> subscriberListener=new SubscriberListener<com.libhttp.entity.LoginResult>(){

                @Override
                public void onStart() {
                    showProgress(true);
                }

                @Override
                public void onNext(com.libhttp.entity.LoginResult loginResult) {
                    showProgress(false);
                    //error code 全部改为了新版,如果没有老版对应 的反馈码则可忽略此错误
                    //如果不可以忽略,则反馈给技术支持即可
                    switch (loginResult.getError_code()){
                        case HttpErrorCode.ERROR_0:
                            //成功的逻辑不需要改成下面这样,以下仅演示过程
                            //原有的这部分代码可以不修改
                            showProgress(false);
                            //code1与code2是p2p连接的鉴权码,只有在帐号异地登录或者服务器强制刷新(一般不会干这件事)时才会改变
                            //所以可以将code1与code2保存起来,只需在下次登录时刷新即可
                            int code1=Integer.parseInt(loginResult.getP2PVerifyCode1());
                            int code2=Integer.parseInt(loginResult.getP2PVerifyCode2());
                            //p2pconnect方法在APP一次生命周期中只需调用一次,退出后台结束时配对调用disconnect一次
                            boolean connect=P2PHandler.getInstance().p2pConnect(loginResult.getUserID(),code1,code2);
                            if(connect){
                                P2PHandler.getInstance().p2pInit(mContext,new P2PListener(),new SettingListener());
                                Intent callIntent=new Intent(MyApp.app,MainActivity.class);
                                callIntent.putExtra("LoginID",loginResult.getUserID());
                                startActivity(callIntent);
                                finish();
                            }else{
                                //这里只是demo的写法,可加入自己的逻辑
                                //为false时p2p的功能不可用
                                Toast.makeText(MyApp.app,""+connect,Toast.LENGTH_LONG).show();
                            }
                            break;
                        case HttpErrorCode.ERROR_10902011:
                            showProgress(false);
                            Toast.makeText(MyApp.app,"用户不存在",Toast.LENGTH_LONG).show();
                            break;
                        case HttpErrorCode.ERROR_10902003:
                            showProgress(false);
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                            mPasswordView.requestFocus();
                            break;
                        default:
                            //其它错误码需要用户自己实现
                            showProgress(false);
                            String msg=String.format("登录失败测试版(%s)",loginResult.getError_code());
                            Toast.makeText(MyApp.app,msg,Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                @Override
                public void onError(String error_code, Throwable throwable) {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            };
            //支持邮箱,手机号码(必须带国码 eg:86-18922222222),用户ID
            HttpSend.getInstance().login(email, password,subscriberListener);
            //*************************技威代码插入**********************************
        }
    }

    private boolean isEmailValid(String email) {
        return true;
        //return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

