package com.reptile.nomad.reptile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.reptile.nomad.reptile.Models.Task;

import org.json.JSONObject;
import org.w3c.dom.Text;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.socket.emitter.Emitter;


public class CreateTaskActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    SegmentedGroup deadlineOptions;
    RadioButton custom;
    Toolbar toolbar ;
    Activity mActivity;
    @Bind(R.id.createTaskButton)
    ImageButton createTaskButton;
    @Bind(R.id.createTaskStringEditText)
    EditText createTaskStringEditText;
    @Bind(R.id.createTaskDeadlineTimeTextView)
    TextView createTaskDeadlineTimeTextView;
    @Bind(R.id.createTaskDeadlineDateTextView)
    TextView createTaskDeadlineDateTextView;
    Calendar deadline;
    public static final String TAG ="CreateTaskActivity";
    Boolean m24HourView = true;
    CharSequence groups[] = new CharSequence[]{
      "Family", "BFFs", "Classroom" , "Chicks"
    };
    String selectedGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        deadlineOptions = (SegmentedGroup)findViewById(R.id.segmented2);
        deadlineOptions.setOnCheckedChangeListener(this);
        mActivity = this;
        deadline = Calendar.getInstance();
        custom = (RadioButton)findViewById(R.id.radioButton3);
        toolbar = (Toolbar) findViewById(R.id.createTaskToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Calendar now = Calendar.getInstance();
        createTaskDeadlineDateTextView.setText(new SimpleDateFormat("E , d MMM yy", Locale.UK).format(deadline.getTime()));
        deadline.add(Calendar.HOUR,2);
        createTaskDeadlineTimeTextView.setText(new SimpleDateFormat("h:m a",Locale.UK).format(deadline.getTime()));
        createTaskDeadlineTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker = new TimePickerDialog(mActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        deadline.set(Calendar.MINUTE,minute);
                        deadline.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        createTaskDeadlineTimeTextView.setText(new SimpleDateFormat("h:m a",Locale.UK).format(deadline.getTime()));
                    }
                },deadline.get(Calendar.HOUR),deadline.get(Calendar.MINUTE),m24HourView);
                mTimePicker.show();
            }
        });
        createTaskDeadlineDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        deadline.set(Calendar.YEAR,year);
                        deadline.set(Calendar.MONTH,monthOfYear);
                        deadline.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        createTaskDeadlineDateTextView.setText(new SimpleDateFormat("E , d MMM yy", Locale.UK).format(deadline.getTime()));
                    }
                },deadline.get(Calendar.YEAR),deadline.get(Calendar.MONTH),deadline.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
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
                String TaskString = createTaskStringEditText.getText().toString();
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

        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                CreateTaskActivity.this);
                builder.setTitle("Select a group :");
//                builder.setMessage("Groups can be created in the drawer activity");
                builder.setItems(groups, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedGroup = groups[which].toString();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Reptile.mSocket.connected())
        {
            Reptile.mSocket.connect();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.fiveMinutes:
                deadline.add(Calendar.MINUTE,5);
                break;
            case R.id.fifteenMinutes:
                deadline.add(Calendar.MINUTE,15);
                break;
            case R.id.fortyfiveMinutes:
                deadline.add(Calendar.MINUTE,45);
                break;
            case R.id.twoHours:
                deadline.add(Calendar.HOUR,2);
                break;
            case R.id.oneDay:
                deadline.add(Calendar.DATE,1);
                break;

        }

    }
}
