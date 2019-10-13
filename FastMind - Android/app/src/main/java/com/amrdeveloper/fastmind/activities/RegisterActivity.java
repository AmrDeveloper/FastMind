package com.amrdeveloper.fastmind.activities;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityRegisterBinding;
import com.amrdeveloper.fastmind.datasource.DataException;
import com.amrdeveloper.fastmind.datasource.auth.AuthDataSourceImpl;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.RegisterResponse;
import com.amrdeveloper.fastmind.objects.State;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.utils.DataValidation;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Disposable disposable = null;
    private String username, email, password;

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

    private final View.OnClickListener onSignInClickListener = view -> {
        username = Objects.requireNonNull(binding.usernameEditText.getText()).toString().trim();
        email = Objects.requireNonNull(binding.emailEditText.getText()).toString().trim();
        password = Objects.requireNonNull(binding.passWordEditText.getText()).toString().trim();

        boolean isNameValid = DataValidation.isUsernameValid(username);
        boolean isEmailValid = DataValidation.isEmailValid(email);
        boolean isPasswordValid = DataValidation.isPasswordValid(password);
        boolean isSignInValid = isNameValid && isEmailValid && isPasswordValid;

        if (isSignInValid) {
            disposable = register();
        } else {
            if (!isNameValid)
                binding.usernameInputLayout.setError(getString(R.string.invalid_username));
            if (!isEmailValid)
                binding.emailInputLayout.setError(getString(R.string.invalid_Email));
            if (!isPasswordValid)
                binding.passWordInputLayout.setError(getString(R.string.invalid_password));
        }
    };

    private DisposableObserver<RegisterResponse> register() {
        return new AuthDataSourceImpl()
                .register(username, email, password)
                .toObservable()
                .onErrorReturn(RegisterResponse::error)
                .subscribeOn(Schedulers.io())
                .startWith(RegisterResponse.progress("On Going"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RegisterResponse>() {
                    @Override
                    public void onNext(RegisterResponse registerResponse) {
                        Toast.makeText(RegisterActivity.this, registerResponse.getStatus(), Toast.LENGTH_SHORT).show();
                        handleData(registerResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void handleData(RegisterResponse registerResponse) {
        if (registerResponse.getState() == State.SUCCESS && registerResponse.getStatus().equals("success")) {
            binding.registerProgressBar.setVisibility(View.GONE);
            Player player = new Player(username, email, password, 1, 0);

            //Save New Player Session
            Session session = new Session(getApplicationContext());
            session.playerLogIn(player);

            //Go to Main Activity
            goToMainActivity();

            //Destroy this activity after end
            finish();

        } else if (registerResponse.getState() == State.PROGRESS) {
            binding.registerProgressBar.setVisibility(View.VISIBLE);
        } else if (registerResponse.getState() == State.ERROR) {
            binding.registerProgressBar.setVisibility(View.GONE);

            if (registerResponse.getThrowable() != null) {

                if (registerResponse.getThrowable() instanceof DataException) {
                    DataException dataException = (DataException) registerResponse.getThrowable();

                    if (dataException.shouldRetry() && dataException.getMessage() != null) {
                        Snackbar.make(findViewById(android.R.id.content), dataException.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Retry", v -> {
                                    disposable = register();
                                }).show();
                    }else{
                        Toast.makeText(this, dataException.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, registerResponse.getThrowable().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

    }
}
