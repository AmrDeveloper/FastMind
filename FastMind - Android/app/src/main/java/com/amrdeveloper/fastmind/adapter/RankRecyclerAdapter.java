package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amrdeveloper.fastmind.activities.ProfileActivity;
import com.amrdeveloper.fastmind.objects.Avatar;
import com.amrdeveloper.fastmind.objects.Player;
import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.socket.Game;

import java.util.ArrayList;
import java.util.List;

public class RankRecyclerAdapter extends RecyclerView.Adapter<RankRecyclerAdapter.RankViewHolder> implements Filterable {

    private Context mContext;
    private List<Player> rankedPlayerList;
    private List<Player> rankPlayerFiltered;

    private static final String USERNAME_FORMAT = "Username : %s";
    private static final String SCORE_FORMAT = "Score : %d";
    private static final String STATE_FORMAT = "State : %s";

    public RankRecyclerAdapter(Context context) {
        mContext = context;
        this.rankedPlayerList = new ArrayList<>();
        this.rankPlayerFiltered = new ArrayList<>();
    }

    public RankRecyclerAdapter(Context context,List<Player> rankedPlayerList) {
        mContext = context;
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


    public void updateRecyclerData(List<Player> playerList) {
        if (playerList != null) {
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
                        if (player.getUsername().contains(keyword) || player.getEmail().contains(keyword)) {
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

    class RankViewHolder extends RecyclerView.ViewHolder {

        private TextView mRankUserName;
        private TextView mRankUserScore;
        private TextView mRankStateTxt;
        private ImageView mRankAvatarImg;

        private RankViewHolder(View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(onClickListener);
        }

        private void initViews(View view) {
            mRankUserName = view.findViewById(R.id.rankUserTxt);
            mRankUserScore = view.findViewById(R.id.rankScoreTxt);
            mRankStateTxt = view.findViewById(R.id.rankStateTxt);
            mRankAvatarImg = view.findViewById(R.id.playerAvatar);
        }

        private void bindView(Player player) {
            String username = String.format(USERNAME_FORMAT, player.getUsername());
            String score = String.format(SCORE_FORMAT, player.getScore());
            String state = String.format(STATE_FORMAT, player.getState());
            int avatarIndex = player.getAvatarID();

            mRankUserName.setText(username);
            mRankUserName.setTag(player.getUsername());
            mRankUserScore.setText(score);
            mRankStateTxt.setText(state);

            if (avatarIndex != 0) {
                int avatarId = Avatar.AVATARS[avatarIndex];
                mRankAvatarImg.setImageResource(avatarId);
            }

            if(player.getStateInt() == 0){
                mRankStateTxt.setTextColor(Color.RED);
            }else{
                mRankStateTxt.setTextColor(Color.GREEN);
            }
        }

        private final View.OnClickListener onClickListener = view -> {
            final String username = mRankUserName.getTag().toString();
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(Game.USERNAME,username);
            mContext.startActivity(intent);
        };
    }
}
