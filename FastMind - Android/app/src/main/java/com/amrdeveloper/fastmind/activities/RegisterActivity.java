package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityRegisterBinding;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.utils.DataValidation;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        binding.registerButton.setOnClickListener(onSignInClickListener);
    }

    public void goToLoginActivity(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String generateUrlRequest(String username, String email, String pass) {
        String router = "/api/player/insert";
        String requestUrl = getString(R.string.LOCALHOST) + getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("username", username);
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("password", pass);
        return uriBuilder.toString();
    }

    private void sendRegisterRequest(String username, String email, String pass) {
        binding.registerProgressBar.setVisibility(View.VISIBLE);
        String requestUrl = generateUrlRequest(username, email, pass);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                requestUrl,
                response -> {
                    if (response.length() == 0 || response.equals("failure")) {
                        Toast.makeText(this, "Invalid Information", Toast.LENGTH_SHORT).show();
                    } else if (response.equals("success")) {
                        binding.registerProgressBar.setVisibility(View.GONE);
                        //Create New Player
                        Player player = new Player(username, email, pass, 1, 0);

                        //Save New Player Session
                        Session session = new Session(getApplicationContext());
                        session.playerLogIn(player);

                        //Go to Main Activity
                        goToMainActivity();

                        //Destroy this activity after end
                        finish();
                    }
                },
                error -> {
                    binding.registerProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Invalid Request", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(stringRequest);
    }

    private final View.OnClickListener onSignInClickListener = view -> {
        String username = Objects.requireNonNull(binding.usernameEditText.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passWordEditText.getText()).toString().trim();

        boolean isNameValid = DataValidation.isUsernameValid(username);
        boolean isEmailValid = DataValidation.isEmailValid(email);
        boolean isPasswordValid = DataValidation.isPasswordValid(password);
        boolean isSignInValid = isNameValid && isEmailValid && isPasswordValid;

        if (isSignInValid) {
            sendRegisterRequest(username, email, password);
        } else {
            if (!isNameValid)
                binding.usernameInputLayout.setError(getString(R.string.invalid_username));
            if (!isEmailValid)
                binding.emailInputLayout.setError(getString(R.string.invalid_Email));
            if (!isPasswordValid)
                binding.passWordInputLayout.setError(getString(R.string.invalid_password));
        }
    };
}
