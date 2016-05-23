package com.bunkmanager.adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bunkmanager.DBHelper;
import com.bunkmanager.R;
import com.bunkmanager.entity.TimeTable;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Pavan on 5/22/2016.
 */
public class TTRecyclerAdapter extends RecyclerView.Adapter<TTRecyclerAdapter.Holder> {
    public Activity activity;
    private LayoutInflater mLayoutInflater;
    private String day;
    private ArrayList<TimeTable> timeTables = new ArrayList<>();
    private ArrayList<Integer> attended = new ArrayList<>();
    private ArrayList<Integer> missed = new ArrayList<>();
    private DBHelper dbHelper;

    public TTRecyclerAdapter (Activity act) {
        this.activity = act;
        mLayoutInflater= LayoutInflater.from(act);
    }

    public TTRecyclerAdapter(Activity act, String day){
        this.day = day;
        this.activity=act;
        dbHelper = new DBHelper(activity);
        mLayoutInflater=LayoutInflater.from(act);
    }

    @Override
    public TTRecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.recycler_layout1, parent, false);
        Holder holder = new Holder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TTRecyclerAdapter.Holder holder, final int position) {
        holder.sub_name.setText(timeTables.get(position).getSubject().getName());
        holder.attended.setText(attended.get(position).toString());
        holder.missed.setText(missed.get(position).toString());
        holder.number.setText(String.format("%6d",position+1)+". ");
        holder.attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(holder.attended.getText().toString());
                i++;
                try {
                    dbHelper.open();
                    dbHelper.addAttendance(timeTables.get(position).getId(), 1, timeTables.get(position).getSubject().getId());
                    dbHelper.close();
                    attended.set(position, i);
                    holder.attended.setText(String.valueOf(i));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(holder.missed.getText().toString());
                i++;
                try {
                    dbHelper.open();
                    dbHelper.addAttendance(timeTables.get(position).getId(), 0, timeTables.get(position).getSubject().getId());
                    dbHelper.close();
                    missed.set(position, i);
                    holder.missed.setText(String.valueOf(i));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.attended.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int i = Integer.parseInt(holder.attended.getText().toString());
                if(i > 0) {
                    i--;
                    try {
                        dbHelper.open();
                        dbHelper.deleteAttendance(timeTables.get(position).getId(), 1);
                        dbHelper.close();
                        attended.set(position, i);
                        holder.attended.setText(String.valueOf(i));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        holder.missed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int i = Integer.parseInt(holder.missed.getText().toString());
                if(i>0) {
                    i--;
                    try {
                        dbHelper.open();
                        dbHelper.deleteAttendance(timeTables.get(position).getId(), 0);
                        dbHelper.close();
                        missed.set(position, i);
                        holder.missed.setText(String.valueOf(i));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return attended.size();
    }

    public void setTimeTables(ArrayList<TimeTable> timeTables) {
        this.timeTables = (ArrayList<TimeTable>)timeTables.clone();
    }

    public void addLecture(TimeTable timeTable) {
        timeTables.add(timeTable);
        attended.add(0);
        missed.add(0);
        notifyDataSetChanged();
    }

    public void addAttended(int count, int position) {
        attended.add(position, count);
    }

    public void addMissed(int count, int position) {
        missed.add(position, count);
        notifyDataSetChanged();
    }

    public int getLecture(int position) {
        return timeTables.get(position).getId();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView number;
        TextView sub_name;
        TextView status;
        Button attended;
        Button missed;
        public Holder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.subjectCard);
            number = (TextView) itemView.findViewById(R.id.textView4);
            sub_name = (TextView) itemView.findViewById(R.id.textView);
            attended = (Button) itemView.findViewById(R.id.button);
            missed = (Button) itemView.findViewById(R.id.button2);
            status = (TextView)itemView.findViewById(R.id.textView6);
        }
    }
}
