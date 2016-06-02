package com.reptile.nomad.reptile;

import android.app.Activity;
import android.os.Bundle;

import com.reptile.nomad.reptile.Models.User;

import java.util.List;

public class EditGroup extends Activity {
    public String selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        Bundle extras = getIntent().getExtras();
        selectedGroup = extras.getString("group");
        List<User> groupUsers = (List<User>) Reptile.knownUsers.get(selectedGroup);

    }
}
