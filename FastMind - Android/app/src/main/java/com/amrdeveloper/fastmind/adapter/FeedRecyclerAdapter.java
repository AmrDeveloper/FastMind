package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Feed;

import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder>{

    private List<Feed> mFeedBackList;

    public FeedRecyclerAdapter(List<Feed> feedList){
        mFeedBackList = feedList;
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
        Feed feedBack = mFeedBackList.get(position);
        holder.bindView(feedBack);

    }

    @Override
    public int getItemCount() {
        return mFeedBackList.size();
    }


    class FeedViewHolder extends RecyclerView.ViewHolder{

        private TextView mWinnerUser;
        private TextView mLoserUser;
        private TextView mGameLevel;

        public FeedViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view){
            mWinnerUser = view.findViewById(R.id.winnerTextView);
            mLoserUser = view.findViewById(R.id.loserTextView);
            mGameLevel = view.findViewById(R.id.gameLevel);
        }

        private void bindView(Feed feedBack){
            mWinnerUser.setText(feedBack.getWinnerName());
            mLoserUser.setText(feedBack.getLoserName());
            mGameLevel.setText(feedBack.getGamelevel());
        }
    }
}
