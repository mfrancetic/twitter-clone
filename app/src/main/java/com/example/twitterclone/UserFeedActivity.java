package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.twitterclone.DateTimeUtils.formatDateToString;

public class UserFeedActivity extends AppCompatActivity {

    private RecyclerView userFeedRecyclerView;
    private UserFeedAdapter userFeedAdapter;
    private ArrayList<Tweet> tweets;
    private TextView emptyTextView;
    private ImageView emptyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        setTitle(getString(R.string.user_feed));
        findViews();
        setupRecyclerView();
        getUsersAndTweets();
    }

    private void findViews() {
        emptyImageView = findViewById(R.id.empty_image_view);
        emptyTextView = findViewById(R.id.empty_text_view);
        userFeedRecyclerView = findViewById(R.id.user_feed_recycler_view);
    }

    private void setupRecyclerView() {
        tweets = new ArrayList<>();
        userFeedAdapter = new UserFeedAdapter(tweets, new UserFeedAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        userFeedRecyclerView.setAdapter(userFeedAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userFeedRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        userFeedRecyclerView.addItemDecoration(decoration);
    }

    private void getUsersAndTweets() {
        Intent intent = getIntent();
        final List<String> usernames = intent.getStringArrayListExtra(getString(R.string.users_key));
        if (usernames != null) {
            ParseQuery<ParseObject> query = new ParseQuery<>(getString(R.string.tweet_table_key));
            query.whereContainedIn(getString(R.string.username_key), usernames);
            query.orderByDescending(getString(R.string.created_at_key));
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects != null && objects.size() > 0 && e == null) {
                        for (ParseObject object : objects) {
                            String username = object.getString(getString(R.string.username_key));
                            String tweet = object.getString(getString(R.string.tweet_key));
                            Date createdAtDate = object.getCreatedAt();
                            String createdAt = formatDateToString(createdAtDate);
                            tweets.add(new Tweet(username, tweet, createdAt));
                        }
                        removeEmptyView();
                        userFeedAdapter.notifyDataSetChanged();
                    } else {
                        showEmptyView();
                    }
                }
            });
        }
    }

    private void removeEmptyView() {
        userFeedRecyclerView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        emptyImageView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        userFeedRecyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyImageView.setVisibility(View.VISIBLE);
    }
}