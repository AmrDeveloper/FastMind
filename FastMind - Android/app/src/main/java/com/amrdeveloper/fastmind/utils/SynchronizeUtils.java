package com.amrdeveloper.fastmind.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.Session;
import com.amrdeveloper.fastmind.socket.Game;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class SynchronizeUtils {

    private Context mContext;
    private RequestQueue queue;
    private static final Gson gson = new Gson();

    public SynchronizeUtils(Context context){
        mContext = context;
        queue = Volley.newRequestQueue(mContext);
    }

    /**
     * Push current Player information to server
     * @param player : Current Player
     */
    public void syncToServer(Player player){
        String requestURL = generateRequestURL(player);
        StringRequest request = new StringRequest(
                Request.Method.PUT,
                requestURL,
                response -> {
                    if(response.isEmpty() || response.equals("failure")){
                        Toast.makeText(mContext, "Invalid Sync", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "Valid Sync", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    //TODO : Invalid Sync
                }
        );
        queue.add(request);
    }

    /**
     *  Get up to date player information from server
     * @param username : current player username
     */
    public void syncFromServer(String username){
        String requestURL = generateRequestURL(username);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                requestURL,
                response -> {
                    if (response.length() == 0) {
                        Toast.makeText(mContext, "Invalid Update", Toast.LENGTH_SHORT).show();
                    }else {
                        Player player = gson.fromJson(response,Player.class);
                        Session session = new Session(mContext);
                        session.playerSync(player);
                    }
                },
                error -> {
                      //TODO : Invalid Update Action
                });
        queue.add(request);
    }

    /**
     * Generate Query one player link
     * @param username : Current Player username
     * @return : return API Get One player information request link
     */
    private String generateRequestURL(String username){
        String router = "/api/player";
        String requestUrl = mContext.getString(R.string.LOCALHOST) + mContext.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(Game.USERNAME, username);
        return uriBuilder.toString();
    }

    /**
     * Generate sync one player link
     * @param player : Current Player
     * @return : return Sync Player information request url
     */
    private String generateRequestURL(Player player){
        String router = "/api/player/sync";
        String requestUrl = mContext.getString(R.string.LOCALHOST) + mContext.getString(R.string.PORT) + router;
        Uri baseUri = Uri.parse(requestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(Game.USERNAME, player.getUsername());
        uriBuilder.appendQueryParameter("score", String.valueOf(player.getScore()));
        uriBuilder.appendQueryParameter("level", String.valueOf(player.getLevel()));
        return uriBuilder.toString();
    }
}
