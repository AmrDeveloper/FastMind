package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.amrdeveloper.fastmind.R;

public class SigninActivity extends AppCompatActivity {

    private TextInputLayout mUsernameInputLayout;
    private TextInputEditText mUsernameEditText;

    private TextInputLayout mEmailInputLayout;
    private TextInputEditText mEmailEditText;

    private TextInputLayout mPassWordInputLayout;
    private TextInputEditText mPassWordEditText;

    private ProgressBar mLoginProgressBar;

    private Button mSigninButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initializeViews();
    }

    private void initializeViews() {
        mUsernameInputLayout = findViewById(R.id.usernameInputLayout);
        mUsernameEditText = findViewById(R.id.usernameEditText);

        mEmailInputLayout = findViewById(R.id.emailInputLayout);
        mEmailEditText = findViewById(R.id.emailEditText);

        mPassWordInputLayout = findViewById(R.id.passWordInputLayout);
        mPassWordEditText = findViewById(R.id.passWordEditText);

        mLoginProgressBar = findViewById(R.id.loginProgressBar);

        mSigninButton = findViewById(R.id.signinButton);
        mSigninButton.setOnClickListener(onSigninClickListener);
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private final View.OnClickListener onSigninClickListener = view -> {

    };
}
