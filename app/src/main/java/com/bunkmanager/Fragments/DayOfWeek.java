package com.bunkmanager.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.DBHelper;
import com.bunkmanager.R;
import com.bunkmanager.adapters.TTRecyclerAdapter;
import com.bunkmanager.entity.*;
import com.melnykov.fab.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Pavan on 5/24/2016.
 */
public class DayOfWeek extends Fragment {
    private RecyclerView mRecycler;
    private TTRecyclerAdapter mAdapter;
    private String hour;
    private FloatingActionButton FAB;
    private TextView intro;
    private DBHelper dbHelper;
    private ViewPager mPager;

    public DayOfWeek() {

    }

    public static DayOfWeek newInstance(String position) {
        DayOfWeek fragment = new DayOfWeek();
        Bundle args =new Bundle();
        args.putString("position",position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        dbHelper = new DBHelper(getActivity());
        try {
            dbHelper.open();
            hour = dbHelper.getLectureCount(getArguments().getString("position"));
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
            hour = "";
        }

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final
    Bundle savedInstanceState) {
        mPager = (ViewPager) getActivity().findViewById(R.id.view5);
        super.onViewCreated(view, savedInstanceState);
        final String day = getArguments().getString("position");
        intro=(TextView)view.findViewById(R.id.lec_intro);
        mRecycler = (RecyclerView) view.findViewById(R.id.view);
        FAB=(FloatingActionButton)view.findViewById(R.id.fab1);
        FAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Add new lecture", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        FAB.setOnClickListener(new View.OnClickListener() {
            AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());

            @Override
            public void onClick(View v) {
                ArrayList<com.bunkmanager.entity.Subjects> subs =new ArrayList<>();
                try {
                    dbHelper.open();
                    subs = (ArrayList<com.bunkmanager.entity.Subjects>)dbHelper.getSubjects().clone();
                    dbHelper.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ArrayList<String> subNames = new ArrayList<String>();
                for(int i = 0; i < subs.size(); i++) {
                    subNames.add(subs.get(i).getName());
                }
                final CharSequence[] subjects = subNames.toArray(new CharSequence[subs.size()]);
                if(subs.size()<1){
                    Toast.makeText(getActivity(),"Add some subjects at tab, 'Subjects'",Toast.LENGTH_SHORT).show();
                } else {
                    final ArrayList<com.bunkmanager.entity.Subjects> finalSubs = (ArrayList<com.bunkmanager.entity.Subjects>)subs.clone();
                    dialog.setItems(subjects, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dbHelper.open();
                                long id = dbHelper.addLecture(day, finalSubs.get(which).getId());
                                mAdapter.addLecture(dbHelper.getLecture((int) id));
                                dbHelper.close();
                                /*Intent bIntent= new Intent(getActivity(),MainActivity.class);
                                bIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(bIntent);*/
                                //mPager.getAdapter().notifyDataSetChanged();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    subs.clear();
                    dialog.show();
                }
            }
        });
        mAdapter = new TTRecyclerAdapter(getActivity(), day);
        try {
            dbHelper.open();
            mAdapter.setTimeTables(dbHelper.getLectures(day));
            for(int i = 0; i < Integer.parseInt(hour); i++) {
                mAdapter.addAttended(dbHelper.getAttendanceCount(mAdapter.getLecture(i), 1), i);
                mAdapter.addMissed(dbHelper.getAttendanceCount(mAdapter.getLecture(i), 0), i);
            }
            dbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
