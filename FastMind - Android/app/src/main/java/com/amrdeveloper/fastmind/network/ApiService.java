package com.amrdeveloper.fastmind.network;

import com.amrdeveloper.fastmind.objects.Player;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET(ApiConstants.LOGIN)
    Call<Player> login(@Query("email") String email,@Query("password") String password);

    @POST(ApiConstants.REGISTER)
    Call<Void> register(@Query("username") String username,@Query("email") String email,@Query("password") String password);



}
