package com.reptile.nomad.reptile;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;


import com.reptile.nomad.reptile.Adapters.DateExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CreateTaskActivity extends AppCompatActivity {
    Toolbar toolbar ;
    ExpandableListView listView;

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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = (ExpandableListView) findViewById(R.id.date_list);

        ArrayList<String> values=new ArrayList<String>();
        values.add("Set a date");
        values.add("Set a time");
        HashMap<String, String> listDataChild = new HashMap<String, String>();
        listDataChild.put(values.get(0), "Date picker");
        listDataChild.put(values.get(1), "TIme picker");
        DateExpandableAdapter dateExpandableAdapter=new DateExpandableAdapter(this,values,listDataChild);

        listView.setAdapter(dateExpandableAdapter);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
