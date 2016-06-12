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
    BroadcastReceiver feedTaskUpdated;
    SwipeRefreshLayout mSwipeRefresh;
    int type;
    public static final String TAG = "FragmentNewsFeed";
    public static final int FEED = 460;
    public static final int FOLLOWING = 430;
    public static final int PROFILE = 806;
    public FragmentNewsFeed(){

       taskFeedList= new ArrayList<>(Reptile.mOwnTasks.values());
        feedTaskUpdated = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                taskFeedList = new ArrayList<>(Reptile.mOwnTasks.values());
                Log.d("FragmentNews","Broadcast reciever called size ="+taskFeedList.size());
                feedAdapter.Tasks = taskFeedList;
                myTaskFeedAdapter.Tasks = taskFeedList;
                feedAdapter.notifyDataSetChanged();
                myTaskFeedAdapter.notifyDataSetChanged();
            }
        };
    }
    public static FragmentNewsFeed newInstance(int type)
    {

        if(type!=FEED&&type!=FOLLOWING&&type!=PROFILE)
        {
            throw new AssertionError("Invalid Type");
        }
        FragmentNewsFeed newFrag = new FragmentNewsFeed();
       newFrag.type = type;
      newFrag.taskFeedList = new ArrayList<>();
        return  newFrag;

    }

    @Override
    public void onResume() {
        super.onResume();
    if(type==FEED)
       LocalBroadcastManager.getInstance(getContext()).registerReceiver(feedTaskUpdated,new IntentFilter(QuickPreferences.tasksUpdated));
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


            myTaskFeedAdapter = new MyTasksAdapter(taskFeedList,getContext());

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
        if (title.equals("Profile")) {
            list.setAdapter(myTaskFeedAdapter);
        } else {
            list.setAdapter(feedAdapter);

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
