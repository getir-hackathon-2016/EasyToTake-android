package com.easytotake.activity;

import android.content.Intent;
import android.os.Bundle;

import com.easytotake.BaseActivity;
import com.easytotake.R;

import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.btn_sing_up)
    void onSingUpClick() {
        Intent nextIntent = new Intent(LoginActivity.this, SingUpActivity.class);
        LoginActivity.this.startActivity(nextIntent);
    }

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(nextIntent);
        this.finish();
    }
}
