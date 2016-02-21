package com.easytotake.activity;

import android.content.Intent;
import android.os.Bundle;

import com.easytotake.BaseActivity;
import com.easytotake.R;

import butterknife.OnClick;

public class SelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        if (isLoggedIn()) {
            Intent nextIntent = new Intent(SelectActivity.this, MainActivity.class);
            SelectActivity.this.startActivity(nextIntent);
            this.finish();
        }
    }

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        Intent nextIntent = new Intent(SelectActivity.this, LoginActivity.class);
        SelectActivity.this.startActivity(nextIntent);
    }

    @OnClick(R.id.btn_sing_up)
    void onSingUpClick() {
        Intent nextIntent = new Intent(SelectActivity.this, SingUpActivity.class);
        SelectActivity.this.startActivity(nextIntent);
    }


    private boolean isLoggedIn() {
        return true;
    }
}
