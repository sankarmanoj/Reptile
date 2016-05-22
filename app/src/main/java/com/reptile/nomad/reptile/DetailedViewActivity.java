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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    public Calendar deadline;
    public String deadlineString;
    public Calendar createdDate;
    public String createdDateString;
    public Date today;
    public List<Comment> comments;

    private CommentsRecyclerAdapter commentsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
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
        thisTask = Reptile.mOwnTasks.get(taskID); // causing null object error

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
                Toast.makeText(getApplicationContext(),newComment,Toast.LENGTH_LONG).show();

                //TODO : Handle Comments posted
            }
        });
        commentsAdapter = new CommentsRecyclerAdapter(thisTask);
        CommentsRecyclerViewDetailed.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // not required ?
        CommentsRecyclerViewDetailed.setAdapter(commentsAdapter);

    }

    public long getTimeDifference(Calendar t2, Calendar t1){
        return t2.getTimeInMillis() - t1.getTimeInMillis();
    }


}
