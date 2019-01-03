package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.R;

import java.util.ArrayList;
import java.util.List;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.RankViewHolder> implements Filterable{

    private List<Player> rankedPlayerList;
    private List<Player> rankPlayerFiltered;

    public RankRecyclerAdapter(){
        this.rankedPlayerList = new ArrayList<>();
        this.rankPlayerFiltered = new ArrayList<>();
    }

    public RankRecyclerAdapter(List<Player> rankedPlayerList){
        this.rankedPlayerList = rankedPlayerList;
        this.rankPlayerFiltered = rankedPlayerList;
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
        Player currentPlayer = rankPlayerFiltered.get(position);
        holder.bindView(currentPlayer);
    }

    @Override
    public int getItemCount() {
        return rankPlayerFiltered.size();
    }


    public void updateRecyclerData(List<Player> playerList){
        if(playerList != null){
            rankedPlayerList = playerList;
            rankPlayerFiltered = playerList;
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String keyword = constraint.toString();
                if (keyword.isEmpty()) {
                    rankPlayerFiltered = rankedPlayerList;
                } else {
                    List<Player> filteredList = new ArrayList<>();
                    for (Player player : rankedPlayerList) {
                        if(player.getUsername().contains(keyword) || player.getEmail().contains(keyword)){
                            filteredList.add(player);
                        }
                    }
                    rankPlayerFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = rankPlayerFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                rankPlayerFiltered = (ArrayList<Player>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class RankViewHolder extends RecyclerView.ViewHolder{

        private TextView mRankUserName;
        private TextView mRankUserScore;
        private TextView mRankStateTxt;

        private RankViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View view){
            mRankUserName = view.findViewById(R.id.rankUserTxt);
            mRankUserScore = view.findViewById(R.id.rankScoreTxt);
            mRankStateTxt = view.findViewById(R.id.rankStateTxt);
        }

        private void bindView(Player player){
            mRankUserName.setText(player.getUsername());
            mRankUserScore.setText(String.valueOf(player.getScore()));
            mRankStateTxt.setText(player.getState());
        }
    }
}
