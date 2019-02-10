package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.socket.Challenge;
import com.amrdeveloper.fastmind.socket.GameSocket;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecyclerAdapter
        extends RecyclerView.Adapter<ChallengeRecyclerAdapter.ChallengeViewHolder>
        implements Filterable {

    private List<Player> mPlayerList;
    private List<Player> mFilteredPlayerList;

    private Socket mGameSocket;

    private Player mCurrentPlayer;

    public ChallengeRecyclerAdapter(Context context) {
        mPlayerList = new ArrayList<>();
        mFilteredPlayerList = new ArrayList<>();
        connectToServer(context);
    }

    public ChallengeRecyclerAdapter(Context context, List<Player> players) {
        mPlayerList = players;
        mFilteredPlayerList = players;
        connectToServer(context);
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        int layoutIdForListItem = R.layout.challenge_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ChallengeRecyclerAdapter.ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Player player = mFilteredPlayerList.get(position);
        holder.bingView(player);
    }

    @Override
    public int getItemCount() {
        return mFilteredPlayerList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String keyword = constraint.toString();
                if (keyword.isEmpty()) {
                    mFilteredPlayerList = mPlayerList;
                } else {
                    List<Player> filteredList = new ArrayList<>();
                    for (Player player : mPlayerList) {
                        if (player.getUsername().contains(keyword) || player.getEmail().contains(keyword)) {
                            filteredList.add(player);
                        }
                    }
                    mFilteredPlayerList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredPlayerList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredPlayerList = (ArrayList<Player>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void updateRecyclerData(List<Player> playerList) {
        if (playerList != null) {
            mPlayerList = playerList;
            mFilteredPlayerList = playerList;
            notifyDataSetChanged();
        }
    }

    private void connectToServer(Context context) {
        mGameSocket = GameSocket.getSocket(context);
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsernameTxt;
        private TextView mUserScoreTxt;
        private ImageButton mChallengeAction;

        private ChallengeViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(onClickListener);
        }

        private void initView(View view) {
            mUsernameTxt = view.findViewById(R.id.usernameTxt);
            mUserScoreTxt = view.findViewById(R.id.userScoreTxt);
            mChallengeAction = view.findViewById(R.id.challengeImgButton);
            mChallengeAction.setOnClickListener(onChallengeRequest);
        }

        private void bingView(Player player) {
            mUsernameTxt.setText(player.getUsername());
            mUserScoreTxt.setText(player.getState());
        }

        private View.OnClickListener onChallengeRequest = view -> {
            if (!mGameSocket.connected()) {
                mGameSocket.connect();
            }
            mGameSocket.emit(Challenge.REQUEST, Challenge.SEND, mUsernameTxt.getText().toString());
        };

        private final View.OnClickListener onClickListener = view -> {
            //TODO : Open Profile Activity for this username
            final String username = mUsernameTxt.getText().toString();
        };
    }
}
