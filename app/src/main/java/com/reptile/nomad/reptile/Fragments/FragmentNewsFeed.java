package com.reptile.nomad.reptile.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Adapters.MyTasksAdapter;
import com.reptile.nomad.reptile.Adapters.NewsFeedRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.QuickPreferences;
import com.reptile.nomad.reptile.R;
import com.reptile.nomad.reptile.Reptile;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment {

    private RecyclerView list = null;
    private List<Task> taskFeedList = null;
    NewsFeedRecyclerAdapter feedAdapter;
    MyTasksAdapter myTaskFeedAdapter;
    public String title = "";
    BroadcastReceiver taskUpdated;
    SwipeRefreshLayout mSwipeRefresh;
    TextView feedTitle;
    public static final String TAG = "FragmentNewsFeed";


    public FragmentNewsFeed(){

       taskFeedList= new ArrayList<>(Reptile.mOwnTasks.values());
        taskUpdated = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("FragmentNews","Broadcast reciever called");
                taskFeedList = new ArrayList<>(Reptile.mOwnTasks.values());
                feedAdapter.Tasks = taskFeedList;
                feedAdapter.notifyDataSetChanged();
            }
        };
    }
    public static FragmentNewsFeed newInstance(String title, List<Task> taskList)
    {
        FragmentNewsFeed newFrag = new FragmentNewsFeed();
      newFrag.taskFeedList = taskList;
        if(taskList==null)
        {
            throw new RuntimeException("TaskList is null");
        }
//        newFrag.title = title;
        return  newFrag;

    }

    @Override
    public void onResume() {
        super.onResume();

       LocalBroadcastManager.getInstance(getContext()).registerReceiver(taskUpdated,new IntentFilter(QuickPreferences.tasksUpdated));
    }

    @Override
    public void onPause() {
        super.onPause();
       // LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(taskUpdated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_news_feed,container,false);

        if (title == "Profile") {
            myTaskFeedAdapter = new MyTasksAdapter(taskFeedList,getContext());
        } else {
            feedAdapter = new NewsFeedRecyclerAdapter(taskFeedList, getContext());
        }
        mSwipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Reptile.mSocket.connected()==false)
                {
                    Reptile.mSocket.connect();
                }
                Reptile.mSocket.emit("addtasks");
                Reptile.mSocket.emit("addusers");
                mSwipeRefresh.setRefreshing(false);
            }
        });
        list = (RecyclerView)view.findViewById(R.id.newsFeedRV);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        if (title == "Profile") {
            list.setAdapter(myTaskFeedAdapter);
        } else {
            list.setAdapter(feedAdapter);

        }
//        feedTitle = (TextView)view.findViewById(R.id.feedTitle);
//        feedTitle.setText(title);

        // bind the recycler view to the news feed RV adapter.
        return view;
    }

}
