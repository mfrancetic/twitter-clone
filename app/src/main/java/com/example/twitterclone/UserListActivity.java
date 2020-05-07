package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private UserListAdapter userListAdapter;
    private Context context;
    private ArrayList<String> users;
    private ArrayList<String> followedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle(getString(R.string.user_list));
        context = UserListActivity.this;
        findViews();

        getUsers();
        getFollowedUsers();
    }

    private void findViews() {
        userRecyclerView = findViewById(R.id.users_recycler_view);
    }

    private void getUsers() {
        users = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo(getString(R.string.username_key), ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder(getString(R.string.username_key));
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null && parseUsers.size() > 0) {
                    for (ParseUser user : parseUsers) {
                        users.add(user.getUsername());
                    }
                    setupRecyclerView();
                }
            }
        });
    }

    private void getFollowedUsers() {
        followedUsers = new ArrayList<>();
        List<String> followedUserNames = ParseUser.getCurrentUser().getList(getString(R.string.followed_users_key));
        if (followedUserNames != null && followedUserNames.size() > 0) {
            followedUsers.addAll(followedUserNames);
        }
    }

    private void setupRecyclerView() {
        userListAdapter = new UserListAdapter(users, new UserListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                followUnfollowUser(users.get(position));
            }
        }, followedUsers);
        userRecyclerView.setAdapter(userListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        userRecyclerView.addItemDecoration(decoration);
    }

    private void followUnfollowUser(String user) {
        if (followedUsers.contains(user)) {
            unfollowUser(user);
        } else {
            followUser(user);
        }
    }

    private void unfollowUser(String user) {
        followedUsers.remove(user);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(getString(R.string.followed_users_key), followedUsers);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ToastUtils.showToast(context, getString(R.string.user_unfollowed));
                } else {
                    ToastUtils.showToast(context, e.getMessage());
                }
            }
        });
        userListAdapter.notifyDataSetChanged();
    }

    private void followUser(String user) {
        followedUsers.add(user);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(getString(R.string.followed_users_key), followedUsers);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ToastUtils.showToast(context, getString(R.string.user_followed));
                } else {
                    ToastUtils.showToast(context, e.getMessage());
                }
            }
        });
        userListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.tweet:
                sendTweet();
                return true;
            case R.id.your_feed:
                goToUserFeed(followedUsers);
                return true;
            case R.id.your_tweets:
                goToYourFeed();
                return true;
            case R.id.logout:
                logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToYourFeed() {
        Intent intent = new Intent(context, YourFeedActivity.class);
        startActivity(intent);
    }

    private void sendTweet() {
        final EditText tweetEditText = new EditText(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        tweetEditText.setLayoutParams(layoutParams);

        new AlertDialog.Builder(context)
                .setTitle(getString(R.string.send_tweet))
                .setView(tweetEditText)
                .setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String tweet = tweetEditText.getText().toString();
                        String username = ParseUser.getCurrentUser().getUsername();
                        ParseObject newTweet = new ParseObject(getString(R.string.tweet_table_key));
                        newTweet.put(getString(R.string.username_key), username);
                        newTweet.put(getString(R.string.tweet_key), tweet);
                        newTweet.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ToastUtils.showToast(context, getString(R.string.tweet_sent));
                                } else {
                                    ToastUtils.showToast(context, getString(R.string.tweet_not_sent));
                                }
                            }
                        });
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

    private void goToUserFeed(ArrayList<String> followedUsers) {
        Intent goToUserFeedIntent = new Intent(context, UserFeedActivity.class);
        goToUserFeedIntent.putStringArrayListExtra(getString(R.string.users_key), followedUsers);
        startActivity(goToUserFeedIntent);
    }

    private void logoutUser() {
        ParseUser.logOut();
        onBackPressed();
    }
}