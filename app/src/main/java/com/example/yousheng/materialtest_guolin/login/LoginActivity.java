package com.example.yousheng.materialtest_guolin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username)
    EditText editUsername;
    @BindView(R.id.password)
    EditText editPassword;
    @BindView(R.id.username_login_button)
    Button buttonLogin;
    @BindView(R.id.username_register_button)
    Button buttonRegister;
    @BindView(R.id.login_progress)
    ProgressBar progressBar;
    @BindView(R.id.email_login_form)
    View mLoginFormView;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setToolbar();
        setIMEPassword();
        setButtonClicked();
    }

    //设置登陆和注册按钮的点击事件
    private void setButtonClicked() {
        //按钮半透明度
        buttonLogin.getBackground().setAlpha(80);
        buttonRegister.getBackground().setAlpha(80);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.newInstance(LoginActivity.this);
                LoginActivity.this.finish();
            }
        });
    }

    //密码框键盘右下角变为"登陆"按钮，且可以直接登陆
    //用户名键盘右下角变为"下一项"按钮，可以focus到下一个框
    private void setIMEPassword() {
        editUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    editPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //actionId默认为xml中imeOptions的值，只有xml中设定的actionId为@integer时候，下面的actionId才会变成我们设定的值
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    //尝试登陆
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptLogin() {
        editUsername.setError(null);
        editPassword.setError(null);
        final String username = editUsername.getText().toString();
        final String password = editPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editPassword.setError("密码必须大于四位");
            focusView = editPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("用户名不能为空");
            focusView = editUsername;
            cancel = true;
        }

        //若账号或者密码有问题，则焦点聚焦到问题上
        if (cancel) {
            focusView.requestFocus();
        } else {
            //显示进度条方法
            showProgress(true);

            //leancloud的sdk的登陆逻辑
            AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        TastyToast.makeText(LoginActivity.this,"登录成功！",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        LoginActivity.this.finish();
                    } else {
                        showProgress(false);
                        TastyToast.makeText(LoginActivity.this,"登录错误："+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
            });
        }

    }

    //显示进度条，并且隐藏登陆填写的表单
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        //安卓3.2以上我们有 ViewPropertyAnimator APIs，可以允许简单的动画效果。
        // 如果3.2以上，我们就给进度条用淡入淡出的效果
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

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            //api过低，直接使用显示隐藏的效果
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("登陆");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
