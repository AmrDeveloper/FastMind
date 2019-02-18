package com.amrdeveloper.fastmind.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amrdeveloper.fastmind.utils.NetworkUtils;

public class NetworkReceiver extends BroadcastReceiver {

    private OnConnectListener mListener;

    public NetworkReceiver(OnConnectListener listener){
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean isConnected = NetworkUtils.isNetworkAvailable(context);
            if (isConnected) mListener.onConnected();
            else mListener.onDisConnected();
        }
    }
}
