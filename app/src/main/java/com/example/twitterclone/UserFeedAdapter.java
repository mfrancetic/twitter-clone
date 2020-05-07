package com.example.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserFeedAdapter extends RecyclerView.Adapter<com.example.twitterclone.UserFeedAdapter.ViewHolder> {

    private UserFeedAdapter.RecyclerViewClickListener listener;
    private List<Tweet> tweets;

    public UserFeedAdapter(List<Tweet> tweets, UserFeedAdapter.RecyclerViewClickListener listener) {
        this.tweets = tweets;
        this.listener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public UserFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View userView = layoutInflater.inflate(R.layout.user_feed_list_item, parent, false);

        return new UserFeedAdapter.ViewHolder(userView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFeedAdapter.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        TextView usernameTextView = holder.userUsernameTextView;
        usernameTextView.setText(tweet.getUsername());
        TextView tweetTextView = holder.userTweetTextView;
        tweetTextView.setText(tweet.getTweet());
        TextView createdAtTextView = holder.userCreatedAtTextView;
        createdAtTextView.setText(tweet.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        if (tweets != null) {
            return tweets.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userUsernameTextView;
        private TextView userTweetTextView;
        private TextView userCreatedAtTextView;

        public ViewHolder(@NonNull View itemView, UserFeedAdapter.RecyclerViewClickListener clickListener) {
            super(itemView);

            listener = clickListener;
            itemView.setOnClickListener(this);

            userUsernameTextView = itemView.findViewById(R.id.user_username_text_view);
            userTweetTextView = itemView.findViewById(R.id.user_tweet_text_view);
            userCreatedAtTextView = itemView.findViewById(R.id.user_created_at_text_view);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}