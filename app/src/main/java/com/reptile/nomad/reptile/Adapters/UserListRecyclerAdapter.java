package com.reptile.nomad.reptile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.User;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by sankarmanoj on 04/06/16.
 */
public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.UserListViewHolder> {
    List<User> users;
    @Override
    public int getItemCount() {
        return users.size();
    }

    public UserListRecyclerAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_card,null);
        return new UserListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.nameTextView.setText(users.get(position).userName);
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder
    {     TextView nameTextView;
        View thisView;
        public UserListViewHolder(View itemView) {
            super(itemView);
            thisView = itemView;
            nameTextView = (TextView)itemView.findViewById(R.id.searchUserNameTextView);
        }
    }
}
