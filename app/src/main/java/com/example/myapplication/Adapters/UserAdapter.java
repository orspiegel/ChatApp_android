package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entites.User;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userHolder> {
    private List<User> users = new ArrayList<>();
    @NonNull
    @Override
    public userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO: user_item XML layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new userHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull userHolder holder, int position) {
        User currentUser = users.get(position);
        holder.displayName.setText(currentUser.getDisplayName());
        holder.profilePic.setText(currentUser.getProfilePic());
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
    class userHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView displayName;
        private TextView password;
        private TextView profilePic;
        private TextView token;

        public userHolder(@NonNull View itemView) {
            super(itemView);
            //TODO: add the XML fields
            displayName = itemView.findViewById(R.id.speakerName);
            profilePic = itemView.findViewById(R.id.avatar);
        }
    }
}
