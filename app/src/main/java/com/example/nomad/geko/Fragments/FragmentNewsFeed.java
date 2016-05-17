package com.example.nomad.geko.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomad.geko.Adapters.FeedListAdapter;
import com.example.nomad.geko.Models.Task;
import com.example.nomad.geko.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment {

    private RecyclerView list = null;
    private List<Task> taskFeedList;
    FeedListAdapter feedAdapter;


    public FragmentNewsFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_news_feed,container,false);
        feedAdapter = new FeedListAdapter(getActivity());
        list = (RecyclerView)view.findViewById(R.id.newsFeedRV);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(feedAdapter);
        // bind the recycler view to the news feed RV adapter.
        return view;
    }

}
