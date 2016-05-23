package com.bunkmanager.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.CustomDrawerLayout;

import com.bunkmanager.MainActivity;
import com.bunkmanager.R;


import java.util.ArrayList;

/**
 * Created by Pavan on 29/05/2015.
 */
public class RecyclerDrawerAdapter extends RecyclerView.Adapter<RecyclerDrawerAdapter.Holder> {
    private LayoutInflater mLayoutInflater;
    private CustomDrawerLayout mDrawerLayout;
    public Activity activity;
    private ArrayList<String> items=new ArrayList<String>();
    public RecyclerDrawerAdapter(Activity act){
        this.activity=act;
        mLayoutInflater= LayoutInflater.from(act);
        /*mDrawerLayout=(CustomDrawerLayout) ((MainActivity)act).findViewById(R.id.drawer_layout);*/
    }

    @Override
    public RecyclerDrawerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.drawer_recycler,parent,false);
        items.add("Get Started");
        items.add("Subjects");
        items.add("Time Table");
        items.add("Notifications");
        Holder holder=new Holder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerDrawerAdapter.Holder holder, final int position) {
        holder.title.setText(items.get(position));


    }



    @Override
    public int getItemCount() {
        return 4;
    }
    public static class Holder extends RecyclerView.ViewHolder {
        int i = getAdapterPosition();
        TextView title;
        RelativeLayout layout;
        public Holder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.textView15);
            layout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout3);
        }
    }
}
