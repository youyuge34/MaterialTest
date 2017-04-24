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
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.username)
    EditText editUsername;
    @BindView(R.id.password)
    EditText editPassword;
    @BindView(R.id.email)
    EditText editEmail;
    @BindView(R.id.button_register)
    Button buttonRegister;
    @BindView(R.id.login_progress)
    ProgressBar progressBar;
    @BindView(R.id.email_login_form)
    View mLoginFormView;

    //正则检验email需要
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern=Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setToolbar();
        setIMERegister();
        setButtonRegister();
    }

    //用户名、邮箱框键盘右下角变为"下一项"按钮
    //密码框键盘右下角变为"注册"按钮，且可以直接注册
    private void setIMERegister() {
        editUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL){
                    editEmail.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL){
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
                    //尝试注册
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
    }

    private void setButtonRegister() {
        buttonRegister.getBackground().setAlpha(80);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        editUsername.setError(null);
        editPassword.setError(null);

        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();
        String email=editEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editPassword.setError("密码必须大于4位");
            focusView = editPassword;
            cancel = true;
        }

        if(!TextUtils.isEmpty(email)&&!isEmailValid(email)) {
            editEmail.setError("邮箱格式不正确");
            focusView = editEmail;
            cancel=true;
        }

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("用户名不能为空");
            focusView = editUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            AVUser user = new AVUser();// 新建 AVUser 对象实例
            user.setUsername(username);// 设置用户名
            user.setPassword(password);// 设置密码
            user.setEmail(email);//设置邮箱
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        TastyToast.makeText(RegisterActivity.this,"注册成功！",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                        RegisterActivity.this.finish();
                    } else {
                        // 失败的原因可能有多种，常见的是用户名已经存在。
                        showProgress(false);
                        TastyToast.makeText(RegisterActivity.this,"注册失败："+e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    //使用正则表达式检验email
    private boolean isEmailValid(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

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

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("注册");
        }
    }
}
