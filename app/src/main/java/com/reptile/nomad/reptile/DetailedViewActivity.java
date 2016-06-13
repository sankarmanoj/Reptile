package com.reptile.nomad.reptile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reptile.nomad.reptile.Adapters.CommentsRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Comment;
import com.reptile.nomad.reptile.Models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.socket.emitter.Emitter;

public class DetailedViewActivity extends Activity {

    public TextView NameTextViewDetailed;
    public TextView TaskTextViewDetailed;
    public TextView DeadlineTextViewDetailed;
    private ProgressBar ProgressBarDetailed;
    public RecyclerView CommentsRecyclerViewDetailed;
    public ImageView DPimageViewDetailed;
    public EditText writeComment;
    public ImageButton postComment;

    public String taskID;
    public Task thisTask;

  @Override
  public void onBackPressed()
  {
      super.onBackPressed();
      finish();
  }

    public Calendar deadline;
    public String deadlineString;
    public Calendar createdDate;
    public String createdDateString;
    public Date today;
    public HashMap<String,Comment> comments;

    private CommentsRecyclerAdapter commentsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        comments = new HashMap<>();

        NameTextViewDetailed = (TextView)findViewById(R.id.NameTextViewDetailed);
        TaskTextViewDetailed = (TextView)findViewById(R.id.TaskTextViewDetailed);
        writeComment  = (EditText)findViewById(R.id.DetailedViewCommentEntryEditText);
        postComment = (ImageButton)findViewById(R.id.detailedSubmitCommentImageView);
        ProgressBarDetailed = (ProgressBar)findViewById(R.id.progressBarDetailed);
        DPimageViewDetailed = (ImageView)findViewById(R.id.DPimageView);
        DeadlineTextViewDetailed = (TextView)findViewById(R.id.DeadlineTextViewDetailed);
        CommentsRecyclerViewDetailed = (RecyclerView)findViewById(R.id.CommentsRecyclerView);

        Bundle extras = getIntent().getExtras();
        taskID = extras.getString("taskID");
        thisTask = Reptile.mAllTasks.get(taskID); // causing null object error

        TaskTextViewDetailed.setText(thisTask.getTaskString());
        NameTextViewDetailed.setText(thisTask.creator.getUserName());
        deadline = thisTask.getDeadline();
        createdDate = thisTask.getCreated();
        deadlineString = new SimpleDateFormat("E , d MMM yy", Locale.UK).format(deadline.getTime());
        createdDateString = new SimpleDateFormat("E , d MMM yy", Locale.UK).format(createdDate.getTime());
        DeadlineTextViewDetailed.setText(deadlineString);
        today = new Date();
        ProgressBarDetailed.setMax((int)getTimeDifference(deadline,createdDate) );
        ProgressBarDetailed.setProgress((int)getTimeDifference(Calendar.getInstance(),createdDate));
        postComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String newComment = writeComment.getText().toString();
                if(newComment.replace(" ","").length()<3)
                {
                    Toast.makeText(getApplicationContext(),"Comment is too small",Toast.LENGTH_LONG).show();
                    return;
                }
                Reptile.mSocket.emit("createcomment",new Comment(newComment,Reptile.mUser,thisTask).getCreationJSON());
                postComment.setEnabled(false);
                Reptile.mSocket.on("createcomment", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String reply = (String)args[0];
                        switch (reply)
                        {
                            case "success":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Successfully Created Comment",Toast.LENGTH_SHORT).show();
                                        postComment.setEnabled(true);
                                        writeComment.setText("");
                                    }
                                });
                                loadComments();
                                break;
                            case "error":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Error Creating Comment",Toast.LENGTH_SHORT).show();
                                        postComment.setEnabled(true);
                                    }
                                });
                                break;
                        }
                    }
                });

            }
        });
        loadComments();
        commentsAdapter = new CommentsRecyclerAdapter(new ArrayList<>(comments.values()));
        Collections.sort(commentsAdapter.taskComments, new Comparator<Comment>() {
            @Override
            public int compare(Comment lhs, Comment rhs) {
                if(lhs.created.after(rhs.created))
                {
                    return 1;
                }
                else
                {
                    return -1;
                }
            }
        });
        CommentsRecyclerViewDetailed.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // not required ?
        CommentsRecyclerViewDetailed.setAdapter(commentsAdapter);


    }

    public long getTimeDifference(Calendar t2, Calendar t1){
        return t2.getTimeInMillis() - t1.getTimeInMillis();
    }
    public void loadComments()
    {
        Reptile.mSocket.emit("loadcomments",thisTask.id);
        Reptile.mSocket.on("loadcomments", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONArray commentsJSON = new JSONArray((String) args[0]);
                    comments.clear();
                    for(int i = 0;i<commentsJSON.length();i++)
                    {
                        JSONObject commentJSON = commentsJSON.getJSONObject(i);
                        Comment newComment = Comment.generateComment(commentJSON);

                        comments.put(newComment.id,newComment);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentsAdapter.taskComments=new ArrayList<Comment>(comments.values());
                            Collections.sort(commentsAdapter.taskComments, new Comparator<Comment>() {
                                @Override
                                public int compare(Comment lhs, Comment rhs) {
                                    if(lhs.created.after(rhs.created))
                                    {
                                        return 1;
                                    }
                                    else
                                    {
                                        return -1;
                                    }
                                }
                            });
                            commentsAdapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Reptile.mSocket.off("loadcomments");
            }
        });

    }


}
