package com.amrdeveloper.fastmind.socket;

import android.content.Context;
import android.util.Log;

import com.amrdeveloper.fastmind.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class GameSocket {

    private static Socket mSocket;

    private GameSocket(){}

    synchronized public static Socket getSocket(Context context){
        if (mSocket == null) {
            try {
                final String SERVER = context.getString(R.string.LOCALHOST) + context.getString(R.string.PORT);
                mSocket = IO.socket(SERVER);
            } catch (URISyntaxException e) {
                Log.e("SOCKET",e.getMessage());
            }
        }
        return mSocket;
    }
}
