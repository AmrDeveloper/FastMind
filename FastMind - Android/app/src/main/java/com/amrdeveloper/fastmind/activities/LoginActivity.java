package com.amrdeveloper.fastmind.activities;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.ActivityLoginBinding;
import com.amrdeveloper.fastmind.datasource.DataException;
import com.amrdeveloper.fastmind.datasource.auth.AuthDataSource;
import com.amrdeveloper.fastmind.datasource.auth.AuthDataSourceImpl;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.State;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.utils.DataValidation;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Disposable disposable = null;
    private String playerEmail, playerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        isPlayerLogined();
        binding.loginButton.setOnClickListener(onLoginClickListener);
    }

    private void isPlayerLogined() {
        PlayerPreferences preferences = new PlayerPreferences(this);
        if (preferences.isPlayerLogined()) {
            goToMainActivity();
            finish();
        }
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener onLoginClickListener = view -> {
        playerEmail = binding.emailEditText.getText().toString().trim();
        playerPassword = binding.passWordEditText.getText().toString().trim();

        boolean emailValid = DataValidation.isEmailValid(playerEmail);
        boolean passValid = DataValidation.isPasswordValid(playerPassword);
        boolean isLoginValid = emailValid && passValid;

        if (isLoginValid) {
            disposable = login(playerEmail, playerPassword);
        } else {
            if (!emailValid)
                binding.emailInputLayout.setError(getString(R.string.invalid_Email));
            if (!passValid)
                binding.passWordInputLayout.setError(getString(R.string.invalid_password));
        }
    };

    private DisposableObserver<Player> login(String playerEmail, String playerPassword) {
        return new AuthDataSourceImpl()
                .login(playerEmail, playerPassword)
                .toObservable()
                .onErrorReturn(Player::error)
                .subscribeOn(Schedulers.io())
                .startWith(Player.progress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Player>() {

                    @Override
                    public void onNext(Player player) {
                        handleData(player);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void handleData(Player player) {

        if (player.getProgress() == State.SUCCESS) {
            //Save Current Player Session
            Session session = new Session(getApplicationContext());
            session.playerLogIn(player);

            //Go to Main Activity
            goToMainActivity();

            //Destroy this activity after end
            finish();

        } else if (player.getProgress() == State.PROGRESS) {

            Toast.makeText(this, "Progress Loading", Toast.LENGTH_SHORT).show();

        } else if (player.getProgress() == State.ERROR) {
            if (player.getThrowable() != null) {
                if (player.getThrowable() instanceof DataException) {
                    DataException dataException = (DataException) player.getThrowable();

                    if (dataException.shouldRetry() && dataException.getMessage() != null) {
                        Snackbar.make(findViewById(android.R.id.content), dataException.getMessage(), Snackbar.LENGTH_LONG)
                                .setAction("Retry", v -> disposable = login(playerEmail, playerPassword)).show();
                    }

                } else {
                    Toast.makeText(this, player.getThrowable().getMessage(), Toast.LENGTH_SHORT).show();

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
