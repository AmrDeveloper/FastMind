package com.amrdeveloper.fastmind.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.amrdeveloper.fastmind.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerUpdateUtils {

    private Context context;
    private RequestQueue queue;

    public PlayerUpdateUtils(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }

    public void updateAvatar(String email, int avatarId) {
        String requestUrl = generateUpdateAvatarUrl(email, avatarId);
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                requestUrl,
                response -> {
                    if (response.contains("success")) {
                        //TODO : Make Action for Update Success
                    } else {
                        //TODO : Make Action for Update Un Success
                    }
                },
                error -> {
                    //TODO : Make Action for Update Un Success
                }
        );
        queue.add(request);
    }

    public void updateUsername(String email, String username) {
        String requestUrl = generateUpdateUsernameUrl(email, username);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                requestUrl,
                response -> {
                    if (response.contains("success")) {
                        //TODO : Make Action for Update Success
                    } else {
                        //TODO : Make Action for Update Un Success
                    }
                },
                error -> {
                    //TODO : Make Action for Update Un Success
                }
        );
        queue.add(request);
    }

    public void updateEmail(String username, String email) {
        String requestUrl = generateUpdateEmailUrl(username, email);
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                requestUrl,
                response -> {
                    if (response.contains("success")) {
                        //TODO : Make Action for Update Success
                    } else {
                        //TODO : Make Action for Update Un Success
                    }
                },
                error -> {
                    //TODO : Make Action for Update Un Success
                }
        );
        queue.add(request);
    }

    public void updatePassword(String email, String pass) {
        String requestUrl = generateUpdatePassUrl(email, pass);
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                requestUrl,
                response -> {
                    if (response.contains("success")) {
                        //TODO : Make Action for Update Success
                    } else {
                        //TODO : Make Action for Update Un Success
                    }
                },
                error -> {
                    //TODO : Make Action for Update Un Success
                }
        );
        queue.add(request);
    }

    public void deleteAccount(String email) {
        String requestUrl = generateDeleteAccUrl(email);
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                requestUrl,
                response -> {
                    if (response.contains("success")) {
                        //TODO : Make Action for Update Success
                    } else {
                        //TODO : Make Action for Update Un Success
                    }
                },
                error -> {
                    //TODO : Make Action for Update Un Success
                }
        );
        queue.add(request);
    }

    //Router : /api/player/update/avatar
    @NonNull
    private String generateUpdateAvatarUrl(String email, int avatarId) {
        String router = "/api/player/update/avatar";
        String requestUrl = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("avatar", String.valueOf(avatarId));
        return uriBuilder.toString();
    }

    //Router : /api/player/update/username
    @NonNull
    private String generateUpdateUsernameUrl(String email, String username) {
        String router = "/api/player/update/username";
        String requestUrl = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("username", username);
        return uriBuilder.toString();
    }

    //Router : /api/player/update/email
    @NonNull
    private String generateUpdateEmailUrl(String username, String email) {
        String router = "/api/player/update/email";
        String requestUrl = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("username", username);
        return uriBuilder.toString();
    }

    //Router : /api/player/update/pass
    @NonNull
    private String generateUpdatePassUrl(String email, String pass) {
        String router = "/api/player/update/pass";
        String requestUrl = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        uriBuilder.appendQueryParameter("password", pass);
        return uriBuilder.toString();
    }

    //Router : /api/player/delete
    @NonNull
    private String generateDeleteAccUrl(String email) {
        String router = "/api/player/delete";
        String requestUrl = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("email", email);
        return uriBuilder.toString();
    }
}
