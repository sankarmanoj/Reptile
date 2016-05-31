package com.reptile.nomad.reptile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.User;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by sankarmanoj on 25/05/16.
 */
public class SearchUserRecyclerAdapter extends RecyclerView.Adapter<SearchUserRecyclerAdapter.SearchUserViewHolder> {
    public List<User> userList;
    public SearchUserRecyclerAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
       // Log.d("Search Adapter",String.valueOf(userList.size()));
        return userList.size();
    }

    @Override
    public SearchUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_card,null);
        return new SearchUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchUserViewHolder holder, int position) {
        holder.nameTextView.setText(userList.get(position).userName);
    }

    public class SearchUserViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTextView;
        View thisView;
        public SearchUserViewHolder(View itemView) {
            super(itemView);
            thisView = itemView;
            nameTextView = (TextView)itemView.findViewById(R.id.searchUserNameTextView);
        }
    }
}
