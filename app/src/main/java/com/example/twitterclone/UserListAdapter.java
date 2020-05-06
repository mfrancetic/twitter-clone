package com.example.twitterclone;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<String> users;
    private RecyclerViewClickListener listener;
    private List<String> followedUsers;

    public UserListAdapter(List<String> users, RecyclerViewClickListener listener, List<String> followedUsers) {
        this.users = users;
        this.listener = listener;
        this.followedUsers = followedUsers;
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View userView = layoutInflater.inflate(R.layout.user_list_item, parent, false);

        return new ViewHolder(userView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {
        String user = users.get(position);

        TextView usernameTextView = holder.userUsernameTextView;
        usernameTextView.setText(user);
        ImageView imageView = holder.followUnfollowImage;

        if (followedUsers.contains(user)) {
            imageView.setImageResource(R.mipmap.ic_follow);
        } else {
            imageView.setImageResource(R.mipmap.ic_unfollow);
        }
    }

    @Override
    public int getItemCount() {
        if (users != null) {
            return users.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userUsernameTextView;
        private ImageView followUnfollowImage;

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);

            listener = clickListener;
            itemView.setOnClickListener(this);

            userUsernameTextView = itemView.findViewById(R.id.user_username_text_view);
            followUnfollowImage = itemView.findViewById(R.id.follow_unfollow_image);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}