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
import com.amrdeveloper.fastmind.utils.DataValidation;


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
        mSigninButton.setOnClickListener(onSignInClickListener);
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(SigninActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private final View.OnClickListener onSignInClickListener = view -> {
        String username = mUsernameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String password = mPassWordEditText.getText().toString().trim();

        boolean isNameValid = DataValidation.isUsernameValid(username);
        boolean isEmailValid = DataValidation.isEmailValid(email);
        boolean isPasswordValid = DataValidation.isPasswordValid(password);
        boolean isSignInValid = isNameValid && isEmailValid && isPasswordValid;

        if (isSignInValid) {
            goToMainActivity();
        } else {
            if (!isNameValid)
                mUsernameEditText.setError(getString(R.string.invalid_username));
            if (!isEmailValid)
                mEmailInputLayout.setError(getString(R.string.invalid_Email));
            if (!isPasswordValid)
                mPassWordInputLayout.setError(getString(R.string.invalid_password));
        }
    };
}
