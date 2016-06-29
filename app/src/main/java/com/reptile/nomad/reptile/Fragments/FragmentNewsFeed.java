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
import android.widget.LinearLayout;

import com.reptile.nomad.reptile.Adapters.MyTasksAdapter;
import com.reptile.nomad.reptile.Adapters.NewsFeedRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.QuickPreferences;
import com.reptile.nomad.reptile.R;
import com.reptile.nomad.reptile.Reptile;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    BroadcastReceiver taskUpdatedBroadcastReceiver;
    SwipeRefreshLayout mSwipeRefresh;
    int type;
    public static final String TAG = "FragmentNewsFeed";
    public static final int FEED = 460;
    public static final int FOLLOWING = 430;
    public static final int PROFILE = 806;

    public void setBroadcastReceiver()
    {
        if(type==FEED) {
            taskUpdatedBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    taskFeedList = new ArrayList<>(Reptile.mAllTasks.values());
                    try {
                        Collections.sort(taskFeedList,Task.createdComparator);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    feedAdapter.Tasks = taskFeedList;
                    feedAdapter.notifyDataSetChanged();
                }
            };
        }
        else if (type==PROFILE) {
            {
                taskUpdatedBroadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        taskFeedList = new ArrayList<>(Reptile.ownTasks.values());
                        try {
                            Collections.sort(taskFeedList,Task.createdComparator);
                            Collections.sort(taskFeedList,Task.statusComparator);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        myTaskFeedAdapter.Tasks = taskFeedList;
                        myTaskFeedAdapter.notifyDataSetChanged();
                    }
                };
            }
        }
    }
    public static FragmentNewsFeed newInstance(int type)
    {

        if(type!=FEED&&type!=PROFILE)
        {
            throw new AssertionError("Invalid Type");
        }
        FragmentNewsFeed newFrag = new FragmentNewsFeed();
       newFrag.type = type;
        newFrag.setBroadcastReceiver();
      newFrag.taskFeedList = new ArrayList<>();
        return  newFrag;

    }

    @Override
    public void onResume() {
        super.onResume();
    LocalBroadcastManager.getInstance(getContext()).registerReceiver(taskUpdatedBroadcastReceiver,new IntentFilter(QuickPreferences.tasksUpdated));
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                feedAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
       // LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(feedTaskUpdated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_news_feed,container,false);


            myTaskFeedAdapter = new MyTasksAdapter(taskFeedList,getActivity());

            feedAdapter = new NewsFeedRecyclerAdapter(taskFeedList, getContext());

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
        if(type==FEED)
        {
            list.setAdapter(feedAdapter);
        }
        else if(type==PROFILE)
        {
            list.setAdapter(myTaskFeedAdapter);
        }
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                feedAdapter.notifyDataSetChanged();
            }
        });
//        feedTitle = (TextView)view.findViewById(R.id.feedTitle);
//        feedTitle.setText(title)

        // bind the recycler view to the news feed RV adapter.
        return view;
    }

}
