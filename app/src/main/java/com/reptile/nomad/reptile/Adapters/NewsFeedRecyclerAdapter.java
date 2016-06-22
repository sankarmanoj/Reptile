package com.reptile.nomad.reptile.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.like.LikeButton;
import com.like.OnLikeListener;

import com.reptile.nomad.reptile.DetailedViewActivity;
import com.reptile.nomad.reptile.FeedImageView;
import com.reptile.nomad.reptile.Fragments.FragmentNewsFeed;
import com.reptile.nomad.reptile.MainActivity;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;
import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.socket.emitter.Emitter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
/**
 * Created by sankarmanoj on 17/05/16.
 */
public class NewsFeedRecyclerAdapter extends RecyclerView.Adapter<NewsFeedRecyclerAdapter.TaskViewHolder> {

    public static final String TAG = "NewsFeedRecyclerAdapter";
    public List<Task> Tasks;
    String number;
    public Context context;
    ImageLoader imageLoader = Reptile.getInstance().getImageLoader();
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
        public LikeButton likeButtonCool;
        public ImageButton likeButton;
        public ImageButton commentButton;
        public TextView likeCount;
        public TextView commentCount;
        public Task currentTask;
        public NetworkImageView taskCreatorProfilePictureView;
       public    TextView NameTextView;
        ImageLoader imageLoader = Reptile.getInstance().getImageLoader();
//       public    ImageView ProfilePictureImageView;
        public TextView TaskTextView;
       public TaskViewHolder(View itemView) {
            super(itemView);
           view = itemView;
           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, DetailedViewActivity.class);
                   intent.putExtra("taskID",currentTask.id);
                   context.startActivity(intent);
               }
           });
            NameTextView = (TextView)itemView.findViewById(R.id.feedNameTextView);
//            ProfilePictureImageView = (ImageView)itemView.findViewById(R.id.feedProfileImageView);
            TaskTextView = (TextView)itemView.findViewById(R.id.feedTaskTextView);
           commentCount = (TextView)itemView.findViewById(R.id.TaskCommentCount);
           taskCreatorProfilePictureView = (NetworkImageView) itemView.findViewById(R.id.feedProfileImageView);
           commentButton = (ImageButton)itemView.findViewById(R.id.commentOnTaskButton);
           commentButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, DetailedViewActivity.class);
                   intent.putExtra("taskID",currentTask.id);
                   context.startActivity(intent);
               }
           });
           likeCount = (TextView)itemView.findViewById(R.id.TaskLikeCount);
//           likeButton = (ImageButton)itemView.findViewById(R.id.taskLikeButton);
           likeButtonCool = (LikeButton)itemView.findViewById(R.id.taskLikeButton);
           likeButtonCool.setOnLikeListener(new OnLikeListener() {
               @Override
               public void liked(LikeButton likeButton) {
                   likeButtonCool.setLiked(true);
                   JSONObject sendLikes = new JSONObject();
                   try {
                       sendLikes.put("taskID",currentTask.id);
                       sendLikes.put("liker",Reptile.mUser.id);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   Reptile.mSocket.emit("likeAction",sendLikes);
                   Reptile.mSocket.on("likeAction", new Emitter.Listener() {
                       @Override
                       public void call(Object... args) {
                           String response = (String) args[0];
                           likeCount.setText(response);
                           Reptile.mSocket.off("likeAction");
                       }
                   });
                   Reptile.mSocket.emit("addtasks");
               }

               @Override
               public void unLiked(LikeButton likeButton) {

               }
           });
//           likeButton.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View v) {
//                   JSONObject sendLikes = new JSONObject();
//                   try {
//                       sendLikes.put("taskID",currentTask.id);
//                       sendLikes.put("liker",Reptile.mUser.id);
//                   } catch (JSONException e) {
//                       e.printStackTrace();
//                   }
//                   Reptile.mSocket.emit("likeAction",sendLikes);
//                   Reptile.mSocket.on("likeAction", new Emitter.Listener() {
//                       @Override
//                       public void call(Object... args) {
//                           String response = (String) args[0];
//                           likeCount.setText(response);
//                           Reptile.mSocket.off("likeAction");
//                       }
//                   });
//                   Reptile.mSocket.emit("addtasks");
//               }
//           });

        }

    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task currentTask = Tasks.get(position);
        String userName = currentTask.creator.getUserName();
        holder.NameTextView.setText(userName);
        holder.TaskTextView.setText(currentTask.getTaskString());
        holder.likeCount.setText(currentTask.getLikes());
        holder.commentCount.setText(getCommentCount(currentTask));
        holder.currentTask = Tasks.get(position);
        if (currentTask.likedByCurrentUser()) {
            holder.likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        String imageURL = currentTask.creator.imageURI;
//        ImageLoader imageLoader = ImageLoader.getInstance().;
//        imageLoader.setImage(imageURL,holder.taskCreatorProfilePictureView);
        if(imageLoader == null)
            imageLoader = Reptile.getInstance().getImageLoader();
        holder.taskCreatorProfilePictureView.setImageUrl(imageURL,imageLoader);

    }

    @Override
    public int getItemCount() {
         return Tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card2,parent,false);
        return new TaskViewHolder(v);
    }

    public String getCommentCount(Task task){
        Reptile.mSocket.emit("commentCount",task.id);
        Reptile.mSocket.on("commentCount", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    number = (String)args[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Reptile.mSocket.off("commentCount");

            }
        });
        return number;
    }
}
