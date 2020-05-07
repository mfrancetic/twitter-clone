package com.example.twitterclone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.twitterclone.DateTimeUtils.formatDateToString;

public class YourFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private YourFeedAdapter adapter;
    private ImageView emptyImageView;
    private TextView emptyTextView;
    private List<Tweet> tweets;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_feed);
        setTitle(getString(R.string.your_feed));

        findViews();
        setupRecyclerView();
        getTweets();
    }

    private void getTweets() {
        final String username = ParseUser.getCurrentUser().getUsername();
        ParseQuery<ParseObject> query = new ParseQuery<>(getString(R.string.tweet_table_key));
        query.whereEqualTo(getString(R.string.username_key), username);
        query.orderByDescending(getString(R.string.created_at_key));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0 && e == null) {
                    for (ParseObject object : objects) {
                        String tweet = object.getString(getString(R.string.tweet_key));
                        Date createdAtDate = object.getCreatedAt();
                        String createdAt = formatDateToString(createdAtDate);
                        String objectId = object.getObjectId();
                        tweets.add(new Tweet(username, tweet, createdAt, objectId));
                    }
                    removeEmptyView();
                    adapter.notifyDataSetChanged();
                } else {
                    showEmptyView();
                }
            }
        });
    }

    private void setupRecyclerView() {
        tweets = new ArrayList<>();
        adapter = new YourFeedAdapter(tweets, new YourFeedAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        }, new YourFeedAdapter.RecyclerViewOnLongClickListener() {
            @Override
            public void onClick(View view, int position) {
                displayDeleteTweetDialog(tweets.get(position));
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);
    }

    private void displayDeleteTweetDialog(final Tweet tweet) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_tweet))
                .setIcon(android.R.drawable.ic_menu_delete)
                .setMessage(getString(R.string.delete_tweet_message))
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTweet(tweet);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void deleteTweet(final Tweet tweet) {
        ParseQuery<ParseObject> query = new ParseQuery<>(getString(R.string.tweet_table_key));
        query.whereEqualTo(getString(R.string.object_id_key), tweet.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && e == null) {
                    for (ParseObject object : objects) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    tweets.remove(tweet);
                                    ToastUtils.showToast(context, getString(R.string.tweet_deleted));
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ToastUtils.showToast(context, e.getMessage());
                                }
                            }
                        });
                    }
                } else {
                    ToastUtils.showToast(context, e.getMessage());
                }
            }
        });
    }

    private void findViews() {
        context = YourFeedActivity.this;
        recyclerView = findViewById(R.id.your_feed_recycler_view);
        emptyImageView = findViewById(R.id.empty_tweet_image_view);
        emptyTextView = findViewById(R.id.empty_tweet_text_view);
    }

    private void removeEmptyView() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
        emptyImageView.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyImageView.setVisibility(View.VISIBLE);
    }
}