package com.reptile.nomad.reptile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ManageGroups extends Activity {
    public ListView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        groupListView = (ListView)findViewById(R.id.groupList);
        List<String> groups = new ArrayList<>(Reptile.knownUsers.keySet());
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,R.id.groupList,groups);
        groupListView.setAdapter(listAdapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemVal = (String)groupListView.getItemAtPosition(position);
                Intent intent = new Intent(ManageGroups.this,MainActivity.class);
                intent.putExtra("Group",itemVal);
                startActivity(intent);
            }
        });
    }
}
