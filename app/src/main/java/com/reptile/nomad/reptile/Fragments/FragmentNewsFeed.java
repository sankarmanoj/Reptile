package com.reptile.nomad.reptile.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.reptile.nomad.reptile.Adapters.NewsFeedRecyclerAdapter;
import com.reptile.nomad.reptile.MainActivity;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.R;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.reptile.nomad.reptile.Reptile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewsFeed extends Fragment {

    private RecyclerView list = null;
    private List<Task> taskFeedList = new ArrayList<>();
    NewsFeedRecyclerAdapter feedAdapter;

    public String title = "";
    String URL_FEED = "http://www.mocky.io/v2/573ce8513700006b034dcc90";

    private static final String TAG = MainActivity.class.getSimpleName();




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

        list = (RecyclerView)view.findViewById(R.id.newsFeedRV);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        // bind the recycler view to the news feed RV adapter.
        feedAdapter = new NewsFeedRecyclerAdapter(taskFeedList);
        list.setAdapter(feedAdapter);



        Cache cache = Reptile.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                        Toast.makeText(getActivity(),response.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            Reptile.getInstance().addToRequestQueue(jsonReq);
        }





        return view;


    }

    // json parsing
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Task item = new Task();
                item.setId(feedObj.getInt("taskId"));
                item.setId(feedObj.getInt("userId"));
                String firstName = feedObj.getString("firstName");
                String lastName = feedObj.getString("lastName");
                String creator = firstName + " " + lastName;
                item.setUsername(feedObj.getString(creator));
                item.setTaskString(feedObj.getString("taskString"));
                item.setCreated(feedObj.getString("created"));
                item.setDeadline(feedObj.getString("deadline"));
                item.setLikes(feedObj.getInt("likes"));
                item.setComments(feedObj.getInt("comments"));

                taskFeedList.add(item);
            }

            // notify data changes to list adapater
            feedAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


