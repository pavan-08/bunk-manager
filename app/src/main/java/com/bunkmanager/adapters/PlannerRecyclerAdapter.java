package com.bunkmanager.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.R;
import com.bunkmanager.entity.BunkPlanner;
import com.bunkmanager.entity.PlannerSubjects;
import com.bunkmanager.entity.Subjects;
import com.bunkmanager.entity.TimeTable;
import com.bunkmanager.helpers.DBHelper;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Pavan on 6/17/2016.
 */
public class PlannerRecyclerAdapter extends RecyclerView.Adapter<PlannerRecyclerAdapter.Holder> {

    private Activity activity;
    private LayoutInflater layoutInflater;
    private ArrayList<BunkPlanner> bunkPlanners = new ArrayList<BunkPlanner>();
    private ArrayList<PlannerSubjects> plannerSubjects = new ArrayList<PlannerSubjects>();
    private DBHelper dbHelper;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdfParser;

    public PlannerRecyclerAdapter(Activity activity) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        dbHelper = new DBHelper(activity);
        sdf = new SimpleDateFormat("EEE, MMM d, ''yy", Locale.getDefault());
        sdfParser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    @Override
    public PlannerRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.planner_recycler_layout, parent, false);
        return new PlannerRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(final PlannerRecyclerAdapter.Holder holder, int position) {
        try {
            holder.date.setText(sdf.format(sdfParser.parse(bunkPlanners.get(position).getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.date.setText(bunkPlanners.get(position).getDate());
        }
        if(bunkPlanners.get(position).isExpanded()) {
            holder.toggleContent.setImageResource(R.drawable.ic_expand_less_black_24dp);
            holder.moreContent.setText(getFormattedSubjects(holder.getAdapterPosition()));
        } else {
            holder.toggleContent.setImageResource(R.drawable.ic_expand_more_black_24dp);
            holder.moreContent.setText("");
        }
        holder.toggleContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunkPlanners.get(holder.getAdapterPosition()).toggleExpanded();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        ((GradientDrawable)holder.circle.getBackground()).setColor(ContextCompat.getColor(activity, computeColor(holder.getAdapterPosition())));
        ((GradientDrawable)holder.lower.getBackground()).setColor(ContextCompat.getColor(activity, computeColor(holder.getAdapterPosition())));
        ((GradientDrawable)holder.upper.getBackground()).setColor(ContextCompat.getColor(activity, computeColor(holder.getAdapterPosition() - 1)));
    }

    private int computeColor(int adapterPosition) {
        adapterPosition = adapterPosition > -1 ? adapterPosition : 0;
        switch (bunkPlanners.get(adapterPosition).getStatus()) {
            case 0:
                return R.color.material_indigo;
            case 1:
                return R.color.material_green;
            case -1:
                return R.color.material_red;
        }
        return 0;
    }

    private String getFormattedSubjects(int adapterPosition) {
        String formattedString = "";
        for (int i = 0; i < plannerSubjects.get(adapterPosition).getSubjects().size(); i++) {
            formattedString += String.valueOf(i+1) + ". " + plannerSubjects.get(adapterPosition).getSubjects().get(i).getName() + "\n";
        }
        if(plannerSubjects.get(adapterPosition).getSubjects().size() == 0) {
            formattedString = "Seems you have no lectures this day, you may easily bunk.";
        }
        return formattedString;
    }

    @Override
    public int getItemCount() {
        return bunkPlanners.size();
    }

    public void setItems() {
        bunkPlanners.clear();
        try {
            dbHelper.open();
            bunkPlanners = dbHelper.getBunkPlans();
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        fetchSubjects();
    }

    private void fetchSubjects() {
        plannerSubjects.clear();
        for (BunkPlanner bunkPlan:
             bunkPlanners) {
            PlannerSubjects plannerSubject = new PlannerSubjects();
            ArrayList<Subjects> subjectList = new ArrayList<Subjects>();
            ArrayList<TimeTable> lectures = new ArrayList<TimeTable>();
            plannerSubject.setBp_id(bunkPlan.get_id());
            try {
                String day = new SimpleDateFormat("EEEE", Locale.getDefault()).format(sdfParser.parse(bunkPlan.getDate()));
                dbHelper.open();
                lectures = dbHelper.getLectures(day);
                for (TimeTable lecture :
                        lectures) {
                    subjectList.add(lecture.getSubject());
                }
                dbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                plannerSubject.setSubjects(subjectList);
                plannerSubjects.add(plannerSubject);
                notifyItemRangeChanged(0, bunkPlanners.size());
            }
        }
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public TextView date, moreContent;
        public AppCompatImageButton toggleContent;
        public ImageView circle, upper, lower;
        public CardView cardView;

        public Holder(final View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.planner_recycler_date);
            moreContent = (TextView) itemView.findViewById(R.id.more_text_view);
            toggleContent = (AppCompatImageButton) itemView.findViewById(R.id.show_more_less);
            circle = (ImageView) itemView.findViewById(R.id.planner_circle);
            upper = (ImageView) itemView.findViewById(R.id.planner_upper_line);
            lower = (ImageView) itemView.findViewById(R.id.planner_lower_line);
            cardView = (CardView) itemView.findViewById(R.id.planner_cardview);
        }
    }
}
