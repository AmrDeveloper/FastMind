package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.activities.ProfileActivity;
import com.amrdeveloper.fastmind.databinding.ChallengeListItemBinding;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.preferences.PlayerPreferences;
import com.amrdeveloper.fastmind.socket.Challenge;
import com.amrdeveloper.fastmind.socket.Game;
import com.amrdeveloper.fastmind.socket.GameSocket;
import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecyclerAdapter
        extends RecyclerView.Adapter<ChallengeRecyclerAdapter.ChallengeViewHolder>
        implements Filterable {

    private List<Player> mPlayerList;
    private List<Player> mFilteredPlayerList;

    private Context mContext;
    private Socket mGameSocket;

    public ChallengeRecyclerAdapter(Context context) {
        mPlayerList = new ArrayList<>();
        mFilteredPlayerList = new ArrayList<>();
        mContext = context;
        connectToServer(context);
    }

    public ChallengeRecyclerAdapter(Context context, List<Player> players) {
        mPlayerList = players;
        mFilteredPlayerList = players;
        mContext = context;
        removeCurrentPlayer();
        connectToServer(context);
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        int layoutID = R.layout.challenge_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        ChallengeListItemBinding binding = DataBindingUtil.inflate(inflater, layoutID, parent, shouldAttachToParentImmediately);
        return new ChallengeRecyclerAdapter.ChallengeViewHolder(binding);
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

    private void removeCurrentPlayer() {
        PlayerPreferences preferences = new PlayerPreferences(mContext);
        Player currentPlayer = preferences.queryPlayerInformation();
        mPlayerList.remove(currentPlayer);
        mFilteredPlayerList.remove(currentPlayer);
    }

    public void updateRecyclerData(List<Player> playerList) {
        if (playerList != null) {
            mPlayerList = playerList;
            mFilteredPlayerList = playerList;
            removeCurrentPlayer();
            notifyDataSetChanged();
        }
    }

    private void connectToServer(Context context) {
        mGameSocket = GameSocket.getSocket(context);
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder {

        private ChallengeListItemBinding binding;

        private ChallengeViewHolder(ChallengeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.challengeCardView.setOnClickListener(onClickListener);
            binding.challengeImgButton.setOnClickListener(onChallengeRequest);
        }

        private void bingView(Player player) {
            binding.usernameTxt.setText(player.getUsername());
            String userScore = String.format("Score : %d", player.getScore());
            binding.userScoreTxt.setText(userScore);

            int avatarIndex = player.getAvatarID();
            if (avatarIndex != 0) {
                int avatarResId = Avatar.AVATARS[avatarIndex];
                binding.playerAvatar.setImageResource(avatarResId);
            }
        }

        private View.OnClickListener onChallengeRequest = view -> {
            if (!mGameSocket.connected()) {
                mGameSocket.connect();
            }
            mGameSocket.emit(Challenge.REQUEST, Challenge.SEND, binding.usernameTxt.getText().toString());
        };

        private final View.OnClickListener onClickListener = view -> {
            final String username = binding.usernameTxt.getText().toString();
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(Game.USERNAME, username);
            mContext.startActivity(intent);
        };
    }
}
