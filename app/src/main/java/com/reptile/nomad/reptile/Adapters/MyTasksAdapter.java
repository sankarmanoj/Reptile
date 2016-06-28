package com.reptile.nomad.reptile.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.reptile.nomad.reptile.DetailedViewActivity;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;
import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import io.socket.emitter.Emitter;

public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.TaskViewHolder> {

    public static final String TAG = "MyTasksAdapter";
    public List<Task> Tasks;
    public Activity mActivity;
    public MyTasksAdapter(List<Task> Tasks, Activity mActivity) {
        this.mActivity = mActivity;
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
        public ImageButton doneButton;
        public ImageButton deleteButton;
        public ImageView statusImaveView;
        public RoundCornerProgressBar taskProgressBar;
//        public    ImageView ProfilePictureImageView;
        public TextView TaskTextView;
        public TaskViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DetailedViewActivity.class);
                    intent.putExtra("taskID",currentTask.id);
                    mActivity.startActivity(intent);
                }
            });
//            NameTextView = (TextView)itemView.findViewById(R.id.feedNameTextView);
//            ProfilePictureImageView = (ImageView)itemView.findViewById(R.id.feedProfileImageView);
            TaskTextView = (TextView)itemView.findViewById(R.id.feedTaskTextView);
            doneButton = (ImageButton)itemView.findViewById(R.id.imageButtonDone);
            deleteButton = (ImageButton)itemView.findViewById(R.id.imageButtonDelete);
            taskProgressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.progressBar);

        }

    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        final Task currentTask = Tasks.get(position);
//        String userName = currentTask.creator.getUserName();
//        holder.NameTextView.setText(userName);
        try {
            holder.TaskTextView.setText(currentTask.getTaskString());
            holder.currentTask = Tasks.get(position);
            holder.taskProgressBar.setMax(1);

            //TODO:Shit is happening here

            Calendar nowTime = Calendar.getInstance();
            long timeGapNow = nowTime.getTimeInMillis()-currentTask.getCreated().getTimeInMillis();
            Double gap = new Double(timeGapNow/currentTask.getMaxTimeGap());
            float progress = gap.floatValue();
            holder.taskProgressBar.setProgress((float)timeGapNow/currentTask.getMaxTimeGap());
            Log.d("gap",gap.toString());


            holder.doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Inform backend + datasetchanged
                    JSONObject sendToServer = new JSONObject();
                    try {
                        sendToServer.put("taskID", currentTask.id);
                        sendToServer.put("status", "done");
                        Reptile.mSocket.emit("taskCompleted", sendToServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Reptile.mSocket.on("taskCompleted", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            String reply = (String)args[0];
                            Log.d(TAG,"Reply From Server = "+reply);
                            switch (reply)
                            {
                                case "success":
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Tasks.remove(position);
                                           notifyDataSetChanged();
                                        }
                                    });
                                    Reptile.mSocket.emit("addtasks");
                                    Reptile.mSocket.off("taskCompleted");
                                    break;
                                case "error":


                                    break;
                            }
                        }
                    });
                }
            });
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Inform backend + datasetchanged
                    JSONObject sendToServer = new JSONObject();
                    try {
                        sendToServer.put("taskID", currentTask.id);
                        sendToServer.put("status", "deleted");
                        Reptile.mSocket.emit("taskDeleted", sendToServer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Reptile.mSocket.on("taskDeleted", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            String reply = (String)args[0];
                            Log.d(TAG,"Reply From Server = "+reply);
                            switch (reply)
                            {
                                case "success":
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Tasks.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    });
                                //    Reptile.mSocket.emit("addtasks");
                                    Reptile.mSocket.off("taskDeleted");
                                    break;
                                case "error":


                                    break;
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Tasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_task_card,parent,false);
        return new TaskViewHolder(v);
    }
}
