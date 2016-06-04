package com.reptile.nomad.reptile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.Group;
import com.reptile.nomad.reptile.Models.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditGroup extends Activity {
    public Group selectedGroup;
    @Bind(R.id.groupNameTextView)
    TextView groupNameTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        selectedGroup = (Group) extras.get("group");
        groupNameTextView.setText(selectedGroup.name);


    }
}
