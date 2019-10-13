package com.amrdeveloper.fastmind.datasource.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.amrdeveloper.fastmind.datasource.DataException;
import com.amrdeveloper.fastmind.datasource.ErrorMessage;
import com.amrdeveloper.fastmind.datasource.Issue;
import com.amrdeveloper.fastmind.network.RetrofitClient;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.objects.RegisterResponse;

import java.io.IOException;

import io.reactivex.Single;
import retrofit2.Response;

public class AuthDataSourceImpl implements AuthDataSource {


    @Override
    public Single<Player> login(@NonNull String email, @NonNull String password) {
        return Single.defer(() -> {
            try {
                Response<Player> response = RetrofitClient.create().login(email, password).execute();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Player player = response.body();
                    return Single.just(Player.success(player));
                } else {
                    return Single.error(new DataException(Issue.API, ErrorMessage.API));
                }

            } catch (IOException error) {
                return Single.error(new DataException(Issue.NETWORK, ErrorMessage.NETWORK));

            }
        });
    }

    @Override
    public Single<RegisterResponse> register(@NonNull String username, @NonNull String email, @NonNull String password) {
        return Single.defer(() -> {
            try {
                Response response = RetrofitClient.create().register(username, email, password).execute();

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    return Single.just(RegisterResponse.success("success"));
                } else {
                    return Single.error(new DataException(Issue.API, ErrorMessage.API));
                }

            } catch (IOException error) {
                return Single.error(new DataException(Issue.NETWORK, ErrorMessage.NETWORK));

            }
        });
    }
}
