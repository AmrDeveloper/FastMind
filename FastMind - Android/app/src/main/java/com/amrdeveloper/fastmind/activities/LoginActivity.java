package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amrdeveloper.fastmind.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmailInputLayout;
    private EditText mEmailEditText;

    private TextInputLayout mPassWordInputLayout;
    private TextInputEditText mPassWordEditText;

    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
    }

    private void initializeViews(){
        mEmailInputLayout = findViewById(R.id.emailInputLayout);
        mEmailEditText = findViewById(R.id.emailEditText);

        mPassWordInputLayout = findViewById(R.id.passWordInputLayout);
        mPassWordEditText = findViewById(R.id.passWordEditText);

        mLoginButton = findViewById(R.id.loginButton);
    }

    public void goToSigninActivity(View view){
        Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity(View view){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
