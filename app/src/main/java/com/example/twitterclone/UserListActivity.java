package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private UserListAdapter userListAdapter;
    private List<User> followedUsers;
    private Context context;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle(getString(R.string.user_list));
        context = UserListActivity.this;
        followedUsers = new ArrayList<>();
        findViews();

        getUsers();
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
                        users.add(new User(user.getUsername()));
                    }
                    setupRecyclerView();
                }
            }
        });
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

    private void followUnfollowUser(User user) {
        if (followedUsers.contains(user)) {
            unfollowUser(user);
        } else {
            followUser(user);
        }
    }

    private void unfollowUser(User user) {
        followedUsers.remove(user);
        ToastUtils.showToast(context, getString(R.string.user_unfollowed));
        userListAdapter.notifyDataSetChanged();
    }

    private void followUser(User user) {
        followedUsers.add(user);
        ToastUtils.showToast(context, getString(R.string.user_followed));
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
                return true;
            case R.id.your_feed:
                goToUserFeed(followedUsers);
                return true;
            case R.id.logout:
                logoutUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToUserFeed(List<User> users) {
        Intent goToUserFeedIntent = new Intent(context, UserFeedActivity.class);
        ArrayList<String> usersUsernames = new ArrayList<>();
        for (User user : users) {
            usersUsernames.add(user.getUsername());
        }
        goToUserFeedIntent.putStringArrayListExtra(getString(R.string.users_key), usersUsernames);
        startActivity(goToUserFeedIntent);
    }

    private void logoutUser() {
        ParseUser.logOut();
        onBackPressed();
    }
}