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
import com.amrdeveloper.fastmind.utils.DataValidation;

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

    private void initializeViews() {
        mEmailInputLayout = findViewById(R.id.emailInputLayout);
        mEmailEditText = findViewById(R.id.emailEditText);

        mPassWordInputLayout = findViewById(R.id.passWordInputLayout);
        mPassWordEditText = findViewById(R.id.passWordEditText);

        mLoginButton = findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(onLoginClickListener);
    }

    public void goToSigninActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener onLoginClickListener = view -> {
        String playerEmail = mEmailEditText.getText().toString().trim();
        String playerPassword = mPassWordEditText.getText().toString().trim();

        boolean emailValid = DataValidation.isEmailValid(playerEmail);
        boolean passValid = DataValidation.isPasswordValid(playerPassword);
        boolean isLoginValid = emailValid && passValid;

        if (isLoginValid) {
            goToMainActivity();
        } else {
            if (!emailValid)
                mEmailInputLayout.setError(getString(R.string.invalid_Email));
            if (!passValid)
                mPassWordInputLayout.setError(getString(R.string.invalid_password));
        }
    };
}
