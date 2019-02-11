package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.utils.DataValidation;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;


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

        isPlayerLogined();
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

    private void isPlayerLogined() {
        PlayerPreferences preferences = new PlayerPreferences(this);
        if (preferences.isPlayerLogined()) {
            goToMainActivity();
            finish();
        }
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

    private String generateLoginUrl(String email, String pass) {
        String router = "/api/player/login";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("password", pass);
        return uriBuilder.toString();
    }

    private void sendLoginRequest(String requestUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                requestUrl,
                response -> {
                    if (response.length() == 0) {
                        Toast.makeText(this, "Invalid Information", Toast.LENGTH_SHORT).show();
                    } else {
                        //Get Player Information From Server
                        Gson gson = new Gson();
                        Player player = gson.fromJson(response, Player.class);

                        //Save Current Player Session
                        Session session = new Session(getApplicationContext());
                        session.playerLogIn(player);

                        //Go to Main Activity
                        goToMainActivity();

                        //Destroy this activity after end
                        finish();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show()) {
        };
        queue.add(stringRequest);
    }

    private View.OnClickListener onLoginClickListener = view -> {
        String playerEmail = mEmailEditText.getText().toString().trim();
        String playerPassword = mPassWordEditText.getText().toString().trim();

        boolean emailValid = DataValidation.isEmailValid(playerEmail);
        boolean passValid = DataValidation.isPasswordValid(playerPassword);
        boolean isLoginValid = emailValid && passValid;

        if (isLoginValid) {
            String requestUrl = generateLoginUrl(playerEmail, playerPassword);
            sendLoginRequest(requestUrl);
        } else {
            if (!emailValid)
                mEmailInputLayout.setError(getString(R.string.invalid_Email));
            if (!passValid)
                mPassWordInputLayout.setError(getString(R.string.invalid_password));
        }

    };
}
