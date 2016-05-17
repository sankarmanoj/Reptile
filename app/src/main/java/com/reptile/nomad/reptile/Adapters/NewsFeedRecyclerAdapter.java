package com.reptile.nomad.reptile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by sankarmanoj on 17/05/16.
 */
public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.TaskViewHolder> {

    List<Task> Tasks;
    public NewsFeedRecyclerAdapter(List<Task> Tasks) {
        assert (Tasks!=null);
        this.Tasks = Tasks;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder
    {
       public    TextView NameTextView;
       public    ImageView ProfilePictureImageView;
       public  ImageView SendCommentImageView;
        public TextView TaskTextView;
       public   EditText CommentEntryEditText;
       public TaskViewHolder(View itemView) {
            super(itemView);
            NameTextView = (TextView)itemView.findViewById(R.id.feedNameTextView);
            ProfilePictureImageView = (ImageView)itemView.findViewById(R.id.feedProfileImageView);
            CommentEntryEditText = (EditText)itemView.findViewById(R.id.feedCommentEntryEditText);
            SendCommentImageView = (ImageView)itemView.findViewById(R.id.feedSubmitCommentImageView);
            TaskTextView = (TextView)itemView.findViewById(R.id.feedTaskTextView);
            SendCommentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentEntryEditText.setText("");
                }
            });
        }

    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task currentTask = Tasks.get(position);
        String userName = currentTask.creator.getUserName();
        holder.NameTextView.setText(userName);
        holder.TaskTextView.setText(currentTask.getTaskString());
    }

    @Override
    public int getItemCount() {
         return Tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card,parent,false);
        return new TaskViewHolder(v);
    }
}
