package com.reptile.nomad.reptile.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Adapters.NewsFeedRecyclerAdapter;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;


import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment {

    private RecyclerView list = null;
    private List<Task> taskFeedList = null;

    String title = "";



    public FragmentNewsFeed(){

    }
    public static FragmentNewsFeed newInstance(String title, List<Task> taskList)
    {
        FragmentNewsFeed newFrag = new FragmentNewsFeed();
      newFrag.taskFeedList = taskList;
        newFrag.title = title;
        return  newFrag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_news_feed,container,false);
        TextView Title = (TextView) view.findViewById(R.id.feedTitle);
        Title.setText(title);
        NewsFeedRecyclerAdapter feedAdapter = new NewsFeedRecyclerAdapter(taskFeedList);
        list = (RecyclerView)view.findViewById(R.id.newsFeedRV);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(feedAdapter);
        // bind the recycler view to the news feed RV adapter.
        return view;
    }

}
