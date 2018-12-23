package com.amrdeveloper.fastmind.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class ChallengeRecyclerAdapter extends RecyclerView.Adapter<ChallengeRecyclerAdapter.ChallengeViewHolder> {

    private List<Player> mPlayerList;

    public ChallengeRecyclerAdapter(List<Player> players) {
        mPlayerList = players;
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
        Player player = mPlayerList.get(position);
        holder.bingView(player);
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    private void playersFilter(String keyword) {
        List<Player> filterResult = new ArrayList<>();
        for (Player p : mPlayerList)
            if (p.getUsername().contains(keyword) || p.getEmail().contains(keyword))
                filterResult.add(p);
        updateRecyclerData(filterResult);
    }

    private void updateRecyclerData(List<Player> playerList) {
        if (playerList != null) {
            mPlayerList.clear();
            mPlayerList.addAll(playerList);
            notifyDataSetChanged();
        }
    }

    class ChallengeViewHolder extends RecyclerView.ViewHolder {

        private TextView mUsernameTxt;
        private TextView mUserScoreTxt;
        private ImageButton mChallengeAction;

        public ChallengeViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            mUsernameTxt = view.findViewById(R.id.usernameTxt);
            mUserScoreTxt = view.findViewById(R.id.userScoreTxt);
            mChallengeAction = view.findViewById(R.id.challengeImgButton);
        }

        private void bingView(Player player) {
            mUsernameTxt.setText(player.getUsername());
            mUserScoreTxt.setText(player.getState());
        }
    }
}
