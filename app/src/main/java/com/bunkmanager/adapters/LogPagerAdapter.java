package com.bunkmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bunkmanager.Fragments.LogFragment;

/**
 * Created by Pavan on 6/5/2016.
 */
public class LogPagerAdapter extends FragmentStatePagerAdapter {

    String[] tabs={"Bunked","Attended"};
    private int subjectID;

    public LogPagerAdapter(FragmentManager fm, int subjectID) {
        super(fm);
        this.subjectID = subjectID;
    }

    @Override
    public Fragment getItem(int position) {
        return LogFragment.newInstance(subjectID, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
