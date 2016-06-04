package com.reptile.nomad.reptile;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.reptile.nomad.reptile.Adapters.SearchUserRecyclerAdapter;
import com.reptile.nomad.reptile.Adapters.UserListRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Group;
import com.reptile.nomad.reptile.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.socket.emitter.Emitter;

public class EditGroup extends Activity {
    public Group selectedGroup;
    Activity mActivity;
    @Bind(R.id.groupNameTextView)
    TextView groupNameTextView;
    @Bind(R.id.usersInGroupView)
    RecyclerView usersListRecyclerView;
    @Bind(R.id.addUserToGroupButton)
    Button addUserButton;
    Timer searchUserTimer;
    HashMap<String,User> searchedUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        ButterKnife.bind(this);
        mActivity = this;
        Bundle extras = getIntent().getExtras();
        selectedGroup =  Reptile.mUserGroups.get(extras.getInt("group"));
        groupNameTextView.setText(selectedGroup.name);
        final UserListRecyclerAdapter userListRecyclerAdapter = new UserListRecyclerAdapter(selectedGroup.members);
        usersListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersListRecyclerView.setAdapter(userListRecyclerAdapter);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                final View dialogView = mActivity.getLayoutInflater().inflate(R.layout.search_dialog,null);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Reptile.mSocket.off("user-search");
                        searchedUsers = null;

                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        searchedUsers=null;
                        Reptile.mSocket.off("user-search");
                    }
                });
                final EditText searchEditText = (EditText)dialogView.findViewById(R.id.searchEditText);

                final RecyclerView userListRecycler = (RecyclerView)dialogView.findViewById(R.id.userRecyclerVeiw);
                searchedUsers = new HashMap<>();
                final SearchUserRecyclerAdapter searchUserRecyclerAdapter = new SearchUserRecyclerAdapter(new ArrayList<>(searchedUsers.values()), new SearchUserRecyclerAdapter.ItemClickEvent() {
                    @Override
                    public void onItemClick(User selectedUser) {

                        selectedGroup.members.add(selectedUser);
                        userListRecyclerAdapter.notifyDataSetChanged();
                    }
                });
                userListRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                userListRecycler.setAdapter(searchUserRecyclerAdapter);
                Reptile.mSocket.on("user-search", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        try {
                            JSONArray inputArray = new JSONArray((String) args[0]);

                            for(int i = 0; i<inputArray.length();i++)
                            {
                                JSONObject input = inputArray.getJSONObject(i);
                                User.addUserToHashMap(input,searchedUsers);
                            }

                            searchUserRecyclerAdapter.userList = new ArrayList<User>(searchedUsers.values());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchUserRecyclerAdapter.notifyDataSetChanged();
                                }
                            });


                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                searchUserTimer = new Timer();
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        searchUserTimer.purge();
                        searchUserTimer.cancel();
                        searchUserTimer = new Timer();
                        searchUserTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                Reptile.mSocket.emit("user-search", searchEditText.getText().toString());
                                // Log.d(TAG, searchEditText.getText().toString());

                            }
                        },250);

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                final AlertDialog dialog = builder.create();

                dialog.show();
            }
        });

    }
}
