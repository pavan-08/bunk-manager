package com.bunkmanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.bunkmanager.Fragments.DayOfWeek;
import com.bunkmanager.Fragments.Subjects;

import com.bunkmanager.Fragments.Timpass;

import com.bunkmanager.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pavan on 01/05/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Toolbar mToolbar;
    private SlidingTabLayout mTabHost;
    private Map<Integer, String> mFragmentTags;
    private ViewPager mPager;
    private Context mContext;
    FragmentManager fragmentManager;
    String[] tabs={"Subjects","Get Started","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public ViewPagerAdapter(Toolbar toolbar,SlidingTabLayout tabLayout,FragmentManager fm, Context context) {
        super(fm);
        mToolbar=toolbar;
        mTabHost=tabLayout;
        fragmentManager = fm;
        mFragmentTags=new HashMap<Integer,String>();
        mContext=context;
    }

    public Fragment getItem(int num) {
        if(num==1){
            Timpass fragment=Timpass.newInstance(num);
            return fragment;
        }
        else if(num == 0){
            Subjects fragment = Subjects.newInstance(num,mToolbar,mTabHost);
            return fragment;
        } else {
            DayOfWeek fragment = DayOfWeek.newInstance(tabs[num]);
            return fragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabs[position];
    }


    @Override
    public int getCount() {
        return 8;
    }

}
