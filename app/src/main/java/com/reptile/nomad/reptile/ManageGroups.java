package com.reptile.nomad.reptile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.reptile.nomad.reptile.Adapters.GroupListRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Group;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ManageGroups extends Activity {
    public RecyclerView groupListView;
    @Bind(R.id.addGroupButton)
    Button AddGroupButton;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_groups);
        ButterKnife.bind(this);
        mActivity = this;
        groupListView = (RecyclerView) findViewById(R.id.groupList);
        List<Group> groups = Reptile.mUserGroups;
        final GroupListRecyclerAdapter GroupAdapter = new GroupListRecyclerAdapter(groups,this);
        groupListView.setAdapter(GroupAdapter);
        groupListView.setLayoutManager(new LinearLayoutManager(this));
        AddGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                final View createGroupView =mActivity.getLayoutInflater().inflate(R.layout.create_group_alert,null);
                builder.setView(createGroupView);
                final AlertDialog dialog = builder.create();
                final EditText GroupNameEditText = (EditText) createGroupView.findViewById(R.id.createGroupEditText);
                createGroupView.findViewById(R.id.createGroupButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Group newGroup = new Group(GroupNameEditText.getText().toString());
                        int count = GroupAdapter.groups.size()-1;
                        Reptile.mUserGroups.add(newGroup);
                        GroupAdapter.groups = Reptile.mUserGroups;
                        GroupAdapter.notifyDataSetChanged();
                        GroupAdapter.notifyItemChanged(count);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

    }
}
