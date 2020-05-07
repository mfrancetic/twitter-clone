package com.example.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class YourFeedAdapter extends RecyclerView.Adapter<YourFeedAdapter.ViewHolder> {

    private YourFeedAdapter.RecyclerViewClickListener listener;
    private List<Tweet> tweets;

    public YourFeedAdapter(List<Tweet> tweets, YourFeedAdapter.RecyclerViewClickListener listener) {
        this.tweets = tweets;
        this.listener = listener;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public YourFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View userView = layoutInflater.inflate(R.layout.your_feed_list_item, parent, false);

        return new YourFeedAdapter.ViewHolder(userView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull YourFeedAdapter.ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        TextView tweetTextView = holder.tweetTextView;
        tweetTextView.setText(tweet.getTweet());
        TextView createdAtTextView = holder.createdAtTextView;
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

        private TextView tweetTextView;
        private TextView createdAtTextView;

        public ViewHolder(@NonNull View itemView, YourFeedAdapter.RecyclerViewClickListener clickListener) {
            super(itemView);

            listener = clickListener;
            itemView.setOnClickListener(this);

            tweetTextView = itemView.findViewById(R.id.tweet_text_view);
            createdAtTextView = itemView.findViewById(R.id.created_at_text_view);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}