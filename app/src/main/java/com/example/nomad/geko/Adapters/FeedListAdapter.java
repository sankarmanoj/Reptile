package com.example.nomad.geko.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nomad.geko.Models.Task;
import com.example.nomad.geko.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by nomad on 12/5/16.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ListViewHolder>  {
    List<Task> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public FeedListAdapter(Context context){
        inflater = LayoutInflater.from(context);

    }

//    @Override
//    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = inflater.inflate(R.layout.custom_lv_element,parent,false);
//        ListViewHolder viewHolder = new ListViewHolder(view);
//        return viewHolder;
//    }View view = mInflater.inflate(R.layout.custom_movie_box_office, parent, false);

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.task_card,parent,false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        Task currentView =  data.get(position);
        holder.profilePicture.setImageResource(currentView.get IMAGE HERE); /// profile picture is just a marker , add all fields here


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;
        TextView taskString;
        TextView created_at;
        TextView due_date;
        TextView likes;
        TextView comments;

        public ListViewHolder(View itemView) {
            super(itemView);
            profilePicture = (ImageView)itemView.findViewById(R.id.person_photo);
            taskString = (TextView)itemView.findViewById(R.id.tv_taskString);
            username = (TextView)itemView.findViewById(R.id.person_name);
            due_date = (TextView)itemView.findViewById(R.id.due_date);
            likes = (TextView)itemView.findViewById(R.id.likes);
            comments = (TextView)itemView.findViewById(R.id.comments);
        }
    }
}
