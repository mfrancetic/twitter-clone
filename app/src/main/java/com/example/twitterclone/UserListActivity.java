package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView userRecyclerView;
    private UserListAdapter userListAdapter;
    private Context context;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle(getString(R.string.user_list));
        context = UserListActivity.this;

        getUsers();
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
        userRecyclerView = findViewById(R.id.users_recycler_view);
        userListAdapter = new UserListAdapter(users, new UserListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Intent openUserFeedActivityIntent = new Intent(context, UserFeedActivity.class);
//                openUserFeedActivityIntent.putExtra(getString(R.string.username_key), users.get(position).getUsername());
//                startActivity(openUserFeedActivityIntent);
            }
        });
        userRecyclerView.setAdapter(userListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        userRecyclerView.addItemDecoration(decoration);
    }
}