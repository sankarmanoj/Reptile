package com.reptile.nomad.reptile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reptile.nomad.reptile.Adapters.GroupListRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Group;

import java.util.ArrayList;
import java.util.List;

public class ManageGroups extends Activity {
    public RecyclerView groupListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        groupListView = (RecyclerView) findViewById(R.id.groupList);
        List<Group> groups = Reptile.mUserGroups;
        GroupListRecyclerAdapter GroupAdapter = new GroupListRecyclerAdapter(groups);
        groupListView.setAdapter(GroupAdapter);
        groupListView.setLayoutManager(new LinearLayoutManager(this));

    }
}
