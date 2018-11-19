package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.R;

import java.util.ArrayList;
import java.util.List;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.RankViewHolder>{

    private List<Player> rankedPlayerList;

    public RankRecyclerAdapter(List<Player> rankedPlayerList){
        this.rankedPlayerList = rankedPlayerList;
    }

    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rank_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new RankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        Player currentPlayer = rankedPlayerList.get(position);
        holder.bindView(currentPlayer);
    }

    @Override
    public int getItemCount() {
        return rankedPlayerList.size();
    }

    private void playersFilter(String keyword){
        List<Player> filterResult = new ArrayList<>();
        for(Player p : rankedPlayerList)
            if(p.getUsername().contains(keyword) || p.getEmail().contains(keyword))
                filterResult.add(p);
        updateRecyclerData(filterResult);
    }

    private void updateRecyclerData(List<Player> playerList){
        if(playerList != null){
            rankedPlayerList.clear();
            rankedPlayerList.addAll(playerList);
            notifyDataSetChanged();
        }
    }

    class RankViewHolder extends RecyclerView.ViewHolder{

        private TextView mRankUserName;
        private TextView mRankUserScore;

        public RankViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view){
            mRankUserName = view.findViewById(R.id.rankUserTxt);
            mRankUserScore = view.findViewById(R.id.rankScoreTxt);
        }

        private void bindView(Player player){
            mRankUserName.setText(player.getUsername());
            mRankUserScore.setText(String.valueOf(player.getScore()));
        }
    }
}
