package com.amrdeveloper.fastmind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goToSigninActivity(View view){
        Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity(View view){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
