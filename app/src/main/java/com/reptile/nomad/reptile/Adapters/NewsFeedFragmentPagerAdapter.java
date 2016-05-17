package com.reptile.nomad.reptile.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by sankarmanoj on 17/05/16.
 */
public class NewsFeedFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    public NewsFeedFragmentPagerAdapter(android.support.v4.app.FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        if(fragments.size()!=3)
        {
            throw new RuntimeException("Only Three Supported Panes");
        }
    }

    @Override
    public int getCount() {
        return  fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
