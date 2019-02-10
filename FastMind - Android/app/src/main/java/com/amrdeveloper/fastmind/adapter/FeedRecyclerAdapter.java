package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Feed;
import com.amrdeveloper.fastmind.socket.Result;

import java.util.ArrayList;
import java.util.List;

public class FeedRecyclerAdapter
        extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder>
        implements Filterable {

    private List<Feed> mFeedBackList;
    private List<Feed> mFilteredFeedList;

    public FeedRecyclerAdapter() {
        mFeedBackList = new ArrayList<>();
        mFilteredFeedList = new ArrayList<>();
    }

    public FeedRecyclerAdapter(List<Feed> feedList) {
        mFeedBackList = feedList;
        mFilteredFeedList = feedList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        int layoutIdForListItem = R.layout.feed_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed feedBack = mFilteredFeedList.get(position);
        holder.bindView(feedBack);

    }

    @Override
    public int getItemCount() {
        return mFilteredFeedList.size();
    }

    public void updateFeeds(List<Feed> feedList) {
        if (feedList != null) {
            mFeedBackList = feedList;
            mFilteredFeedList = feedList;
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
                    mFilteredFeedList = mFeedBackList;
                } else {
                    List<Feed> filteredList = new ArrayList<>();
                    for (Feed feed : mFeedBackList) {
                        if (feed.getFirstPlayer().equalsIgnoreCase(keyword)
                                || feed.getSecondPlayer().equalsIgnoreCase(keyword)) {
                            filteredList.add(feed);
                        }
                    }
                    mFilteredFeedList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredFeedList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredFeedList = (ArrayList<Feed>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView mFirstPlayer;
        private TextView mSecondPlayer;
        private TextView mGameScore;

        FeedViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            mFirstPlayer = view.findViewById(R.id.firstPlayerTxt);
            mSecondPlayer = view.findViewById(R.id.secondPlayerTxt);
            mGameScore = view.findViewById(R.id.gameScore);
        }

        private void bindView(Feed feedBack) {
            String gameScore = "Score : " + feedBack.getGameScore();

            mFirstPlayer.setText(feedBack.getFirstPlayer());
            mSecondPlayer.setText(feedBack.getSecondPlayer());
            mGameScore.setText(gameScore);
            final int gameResult = feedBack.getGameResult();
            switch (gameResult) {
                case Result.NO_ONE_WINNER:
                    mFirstPlayer.setTextColor(Color.RED);
                    mSecondPlayer.setTextColor(Color.RED);
                    break;
                case Result.FIRST_ONE_WINNER:
                    mFirstPlayer.setTextColor(Color.GREEN);
                    mSecondPlayer.setTextColor(Color.RED);
                    break;
                case Result.SECOND_ONE_WINNER:
                    mFirstPlayer.setTextColor(Color.RED);
                    mSecondPlayer.setTextColor(Color.GREEN);
                    break;
            }

        }
    }
}
