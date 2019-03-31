package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.databinding.FeedListItemBinding;
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
        int layoutID = R.layout.feed_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        FeedListItemBinding binding = DataBindingUtil.inflate(inflater,layoutID,parent,shouldAttachToParentImmediately);
        return new FeedViewHolder(binding);
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

    public void updateFeeds(List<Feed> feedList) {
        if (feedList != null) {
            mFeedBackList = feedList;
            mFilteredFeedList = feedList;
            notifyDataSetChanged();
        }
    }

    class FeedViewHolder extends RecyclerView.ViewHolder {

        private FeedListItemBinding binding;

        private FeedViewHolder(FeedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView(Feed feedBack) {
            String gameScore = "Score : " + feedBack.getGameScore();

            binding.firstPlayerTxt.setText(feedBack.getFirstPlayer());
            binding.secondPlayerTxt.setText(feedBack.getSecondPlayer());
            binding.gameScore.setText(gameScore);
            final int gameResult = feedBack.getGameResult();
            switch (gameResult) {
                case Result.NO_ONE_WINNER:
                    binding.firstPlayerTxt.setTextColor(Color.RED);
                    binding.secondPlayerTxt.setTextColor(Color.RED);
                    break;
                case Result.FIRST_ONE_WINNER:
                    binding.firstPlayerTxt.setTextColor(Color.GREEN);
                    binding.secondPlayerTxt.setTextColor(Color.RED);
                    break;
                case Result.SECOND_ONE_WINNER:
                    binding.firstPlayerTxt.setTextColor(Color.RED);
                    binding.secondPlayerTxt.setTextColor(Color.GREEN);
                    break;
            }
        }
    }
}
