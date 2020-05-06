package com.example.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<User> users;
    private RecyclerViewClickListener listener;

    public UserListAdapter(List<User> users, RecyclerViewClickListener listener) {
        this.users = users;
        this.listener = listener;
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
        User user = users.get(position);

        TextView usernameTextView = holder.userUsernameTextView;
        usernameTextView.setText(user.getUsername());
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

        public ViewHolder(@NonNull View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);

            listener = clickListener;
            itemView.setOnClickListener(this);

            userUsernameTextView = itemView.findViewById(R.id.user_username_text_view);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}