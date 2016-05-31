package com.reptile.nomad.reptile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.reptile.nomad.reptile.Models.Comment;
import com.reptile.nomad.reptile.Models.Preference;
import com.reptile.nomad.reptile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nomad on 30/5/16.
 */
public class preferenceAdapter extends RecyclerView.Adapter<preferenceAdapter.prefViewHolder> {

    public List<Preference> listPreferences;
    public Preference thisPreference;
    public Boolean[] stats;
    List<String> prefList;
    List<Boolean> prefStat;


    public preferenceAdapter(List<Preference> listPreferences) {
        this.listPreferences = listPreferences;
    }




    @Override
    public prefViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preference_element,parent,false);


        return new prefViewHolder(v);
    }

    @Override
    public void onBindViewHolder(prefViewHolder holder, int position) {
        thisPreference = listPreferences.get(position);
        holder.checkedTextView.setText(thisPreference.getTagName());
        holder.checkedTextView.setChecked(thisPreference.getStatus());

    }

    @Override
    public int getItemCount() {
        return prefList.size();

    }

    public class prefViewHolder extends RecyclerView.ViewHolder {
        CheckedTextView checkedTextView;
        Boolean status;
        public prefViewHolder(View itemView) {
            super(itemView);
            checkedTextView = (CheckedTextView) itemView.findViewById(R.id.checkedTextView1);

        }
    }
}