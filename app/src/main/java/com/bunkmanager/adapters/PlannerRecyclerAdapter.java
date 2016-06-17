package com.bunkmanager.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Pavan on 6/17/2016.
 */
public class PlannerRecyclerAdapter extends RecyclerView.Adapter<PlannerRecyclerAdapter.Holder> {

    private Activity activity;

    public PlannerRecyclerAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public PlannerRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PlannerRecyclerAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
