package com.reptile.nomad.reptile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.Group;
import com.reptile.nomad.reptile.R;

import java.util.List;

/**
 * Created by sankarmanoj on 04/06/16.
 */
public class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.GroupListViewHolder> {
    public List<Group> groups;

    public GroupListRecyclerAdapter(List<Group> groups) {
        this.groups = groups;
    }

    public class GroupListViewHolder extends RecyclerView.ViewHolder
    {
        public TextView Name;
        public View Line;
        public GroupListViewHolder(View itemView) {
            super(itemView);
            Name = (TextView)itemView.findViewById(R.id.groupNameListTextView);
            Line = itemView.findViewById(R.id.groupSeperatingLine);
            if(Name==null)
            {
                throw new AssertionError("Name can't be null");
            }
        }
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card,parent,false);
        return new GroupListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder holder, int position) {
     holder.Name.setText(groups.get(position).name);
        if(position==groups.size()-1)
        {
            holder.Line.setVisibility(View.INVISIBLE);
        }
    }
}
