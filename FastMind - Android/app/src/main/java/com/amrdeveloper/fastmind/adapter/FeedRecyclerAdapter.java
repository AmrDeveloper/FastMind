package com.amrdeveloper.fastmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amrdeveloper.fastmind.R;
import com.amrdeveloper.fastmind.objects.Feed;
import com.amrdeveloper.fastmind.socket.Result;

import java.util.List;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder> {

    private List<Feed> mFeedBackList;

    public FeedRecyclerAdapter(List<Feed> feedList) {
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


    class FeedViewHolder extends RecyclerView.ViewHolder {

        private TextView mFirstPlayer;
        private TextView mSecondPlayer;
        private TextView mgameScore;

        FeedViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            mFirstPlayer = view.findViewById(R.id.firstPlayerTxt);
            mSecondPlayer = view.findViewById(R.id.secondPlayerTxt);
            mgameScore = view.findViewById(R.id.gameScore);
        }

        private void bindView(Feed feedBack) {
            mFirstPlayer.setText(feedBack.getFirstPlayer());
            mSecondPlayer.setText(feedBack.getSecondPlayer());
            mgameScore.setText(feedBack.getGameScore());
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
