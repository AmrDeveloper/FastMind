package com.amrdeveloper.fastmind.datasource.auth;

import androidx.annotation.NonNull;

import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.RegisterResponse;

import io.reactivex.Single;

public interface AuthDataSource {

    Single<Player> login(@NonNull String email, @NonNull String password);

    Single<RegisterResponse>  register(@NonNull String username, @NonNull String email, @NonNull String password);
}
