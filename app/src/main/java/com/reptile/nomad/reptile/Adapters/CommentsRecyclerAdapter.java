package com.reptile.nomad.reptile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.Comment;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by nomad on 22/5/16.
 */
public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder> {

    public List<Comment> taskComments;

    public CommentsRecyclerAdapter(List<Comment> comments) {
        try {
            taskComments = comments;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card,parent,false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment thisComment = taskComments.get(position);
        String userName = thisComment.getAuthorname();
        holder.name.setText(userName);
        holder.comment.setText(thisComment.getComment());

    }


    @Override
    public int getItemCount() {
        return taskComments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.commentNameTextView);
            comment = (TextView)itemView.findViewById(R.id.commentTextView);
        }
    }
}
