package com.reptile.nomad.reptile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CreateTaskActivity extends AppCompatActivity {
    Toolbar toolbar ;
    Activity mActivity;
    @Bind(R.id.createTaskButton)
    Button createTaskButton;
    @Bind(R.id.createTaskStringEditText)
    EditText createTaskStringEditText;
    @Bind(R.id.createTaskDeadlineTimeTextView)
    TextView createTaskDeadlineTimeTextView;
    @Bind(R.id.createTaskDeadlineDateTextView)
    TextView createTaskDeadlineDateTextView;
    Calendar deadline;
    Boolean m24HourView = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        ButterKnife.bind(this);
        mActivity = this;
        deadline = Calendar.getInstance();
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
        final Calendar initialDeadLine = Calendar.getInstance();
        createTaskDeadlineDateTextView.setText(new SimpleDateFormat("E , d MMM yy", Locale.UK).format(initialDeadLine.getTime()));
        initialDeadLine.add(Calendar.HOUR,2);
        createTaskDeadlineTimeTextView.setText(new SimpleDateFormat("h:m a",Locale.UK).format(initialDeadLine.getTime()));
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
                },initialDeadLine.get(Calendar.HOUR),initialDeadLine.get(Calendar.MINUTE),m24HourView);
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
                },initialDeadLine.get(Calendar.YEAR),initialDeadLine.get(Calendar.MONTH),initialDeadLine.get(Calendar.DAY_OF_MONTH));
                mDatePicker.show();
            }
        });

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
