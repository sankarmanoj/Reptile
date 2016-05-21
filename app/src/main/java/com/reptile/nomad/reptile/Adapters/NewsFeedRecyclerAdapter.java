package com.reptile.nomad.reptile.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.reptile.nomad.reptile.DetailedViewActivity;
import com.reptile.nomad.reptile.Fragments.FragmentNewsFeed;
import com.reptile.nomad.reptile.MainActivity;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by sankarmanoj on 17/05/16.
 */
public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.TaskViewHolder> {

    public static final String TAG = "NewsFeedRecyclerAdapter";
    public List<Task> Tasks;
    public Context context;
    public NewsFeedRecyclerAdapter(List<Task> Tasks, Context context) {
        this.context = context;
        this.Tasks = Tasks;
        if(Tasks==null)
        {
            throw  new RuntimeException("Task is null");
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public Task currentTask;
       public    TextView NameTextView;
       public    ImageView ProfilePictureImageView;
       public  ImageView SendCommentImageView;
        public TextView TaskTextView;
       public   EditText CommentEntryEditText;
       public TaskViewHolder(View itemView) {
            super(itemView);
           view = itemView;
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, DetailedViewActivity.class);
                   intent.putExtra("taskId",currentTask.id);
                   context.startActivity(intent);
               }
           });
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
        holder.currentTask = Tasks.get(position);
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
