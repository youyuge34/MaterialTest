package com.example.yousheng.materialtest_guolin.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.yousheng.materialtest_guolin.R;
import com.example.yousheng.materialtest_guolin.view.BaseActivity;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity{

    public static void newInstance(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }
}
