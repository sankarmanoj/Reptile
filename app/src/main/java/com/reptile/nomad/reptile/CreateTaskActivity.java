package com.reptile.nomad.reptile;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.reptile.nomad.reptile.Adapters.DateExpandableAdapter;
import com.reptile.nomad.reptile.Models.Task;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.emitter.Emitter;


public class CreateTaskActivity extends AppCompatActivity {
    Toolbar toolbar ;
    ListView listView;
    Calendar deadline;
    Boolean m24HourView = true;
    Activity mActivity;
    Button createTaskButton;
    EditText taskText;
    public static final String TAG ="CreateTaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        toolbar = (Toolbar) findViewById(R.id.createTaskToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("                   Add new task");
        toolbar.setTitleTextColor(-1);
        mActivity=this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.date_list);
        deadline = Calendar.getInstance();
        final Calendar now = Calendar.getInstance();
        final ArrayList<String> values=new ArrayList<String>();
        values.add("Set a date");
        values.add("Set a time");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, values);


        listView.setAdapter(adapter);
        createTaskButton=(Button) findViewById(R.id.createTaskButton);
        taskText=(EditText)findViewById(R.id.taskText);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if(values.get(position)=="Set a date")
                {
                    TimePickerDialog mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            deadline.set(Calendar.MINUTE,minute);
                            deadline.set(Calendar.HOUR_OF_DAY,hourOfDay);

                        }
                    },deadline.get(Calendar.HOUR),deadline.get(Calendar.MINUTE),m24HourView);
                    mTimePicker.show();
                }
                else
                {
                    TimePickerDialog mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            deadline.set(Calendar.MINUTE,minute);
                            deadline.set(Calendar.HOUR_OF_DAY,hourOfDay);

                        }
                    },deadline.get(Calendar.HOUR),deadline.get(Calendar.MINUTE),m24HourView);
                    mTimePicker.show();
                }
            }
        });
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Reptile.mSocket.connected())
                {
                    AlertDialog notConnectedDialog = new AlertDialog.Builder(mActivity)
                            .setTitle("Unable to Connected to Server")
                            .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            }).create();
                    notConnectedDialog.show();
                }
                String TaskString = taskText.getText().toString();
                if(TaskString.replace(" ","") .length()<3)
                {
                    Toast.makeText(getApplicationContext(),"Empty Task String",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(deadline.before(now))
                {
                    Toast.makeText(getApplicationContext(),"Please enable time travelling to create deadlines in the past",Toast.LENGTH_LONG).show();
                    return;
                }
                Task newTask = new Task(Reptile.mUser,TaskString,now,deadline);
                JSONObject taskJson = newTask.getJSON();

                Reptile.mSocket.emit("createtask",taskJson);
                createTaskButton.setEnabled(false);
                Reptile.mSocket.on("createtask", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String reply = (String)args[0];
                        Log.d(TAG,"Reply From Server = "+reply);
                        switch (reply)
                        {
                            case "success":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Task Created Successfully",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Reptile.mSocket.emit("addtasks");
                                TimerTask finishActivity = new TimerTask() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(finishActivity,1000);
                                Reptile.mSocket.off("createtask");
                                break;
                            case "error":
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Error Creating Task",Toast.LENGTH_LONG).show();
                                        createTaskButton.setEnabled(true);
                                    }
                                });


                                break;
                        }
                    }
                });
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
