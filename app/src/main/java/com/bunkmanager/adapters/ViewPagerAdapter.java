package com.bunkmanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;

import com.bunkmanager.Fragments.Friday;
import com.bunkmanager.Fragments.Monday;
import com.bunkmanager.Fragments.Saturday;
import com.bunkmanager.Fragments.Subjects;
import com.bunkmanager.Fragments.Thursday;

import com.bunkmanager.Fragments.Timpass;
import com.bunkmanager.Fragments.Tuesday;
import com.bunkmanager.Fragments.Wednesday;

import com.bunkmanager.SlidingTabLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pavan on 01/05/2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Toolbar mToolbar;
    private SlidingTabLayout mTabHost;
    private Map<Integer, String> mFragmentTags;
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


        if (num == 2) {
            Monday fragment = Monday.newInstance(num);
            return fragment;
        } else if (num == 3) {
            Tuesday fragment = Tuesday.newInstance(num);
            return fragment;
        } else if (num == 4) {
            Wednesday fragment = Wednesday.newInstance(num);
            return fragment;
        } else if (num == 5) {
            Thursday fragment = Thursday.newInstance(num);
            return fragment;
        } else if (num == 6) {
            Friday fragment = Friday.newInstance(num);
            return fragment;
        } else if (num == 7) {
            Saturday fragment = Saturday.newInstance(num);
            return fragment;
        }
        else if(num==1){
            Timpass fragment=Timpass.newInstance(num);
            return fragment;
        }
        else {
            Subjects fragment = Subjects.newInstance(num,mToolbar,mTabHost);
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
