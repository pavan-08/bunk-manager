package com.bunkmanager.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.MainActivity;
import com.bunkmanager.R;
import com.bunkmanager.SimpleDividerItemDecoration;
import com.bunkmanager.SlidingTabLayout;
import com.bunkmanager.adapters.RecyclerAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pavan on 30/05/2015.
 */
public class Subjects extends Fragment {
    private static Toolbar mToolbar;
    private static SlidingTabLayout mTabHost;
    private static RecyclerView mRecycler;
    private static RecyclerAdapter mAdapter;
    private static FloatingActionButton FAB;
    private TextView intro;
    private static View view;
    public Subjects(){

    }
    public static Subjects newInstance(int position,Toolbar toolbar,SlidingTabLayout tabLayout) {
        Subjects fragment = new Subjects();
        Bundle args =new Bundle();
        args.putInt("position",position);
        mToolbar=toolbar;
        mTabHost=tabLayout;
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int num =getArguments().getInt("position");
        ArrayList<String> subs= new ArrayList<>();


        intro=(TextView)view.findViewById(R.id.add_intro_text);
        mRecycler = (RecyclerView) view.findViewById(R.id.view);
        mAdapter = new RecyclerAdapter(getActivity(),num);
        FAB=(FloatingActionButton)view.findViewById(R.id.fab);
        FAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Add Subject", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        if(mAdapter.getItemCount()>0){
            intro.setVisibility(View.INVISIBLE);
        } else{
            intro.setVisibility(View.VISIBLE);
        }
        mRecycler.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                intro.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if(mAdapter.getItemCount()==0){
                    intro.setVisibility(View.VISIBLE);
                }

            }
        });
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hours;
                AlertDialog.Builder add=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater =getLayoutInflater(savedInstanceState);
                final View layout =inflater.inflate(R.layout.add_subject,null);
                add.setView(layout);
                add.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText sub = (EditText) layout.findViewById(R.id.editText);
                        EditText per = (EditText) layout.findViewById(R.id.editText2);
                        String subject = sub.getText().toString();
                        String percent = per.getText().toString();
                        if (subject.equals("")) {
                            Toast.makeText(getActivity(), "Empty Subject Inputs", Toast.LENGTH_SHORT).show();
                        } else if (percent.equals("")) {
                            Toast.makeText(getActivity(), "Empty Percent Inputs", Toast.LENGTH_SHORT).show();
                        } else if (MainActivity.isNotNumeric(percent)) {
                            Toast.makeText(getActivity(), percent + "is not a number", Toast.LENGTH_LONG).show();
                        } else if (Integer.parseInt(percent) > 100 || Integer.parseInt(percent)<0) {
                            Toast.makeText(getActivity(), "invalid percent limit", Toast.LENGTH_SHORT).show();
                        } else {
                            FileOutputStream fos;
                            try {
                                fos = getActivity().openFileOutput(subject, Context.MODE_PRIVATE);
                                fos.write((subject + "~" + percent + "`").getBytes());
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fos = getActivity().openFileOutput("Subjects", Context.MODE_APPEND);
                                fos.write((subject + "~").getBytes());
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fos = getActivity().openFileOutput("a" + subject, Context.MODE_PRIVATE);
                                fos.write("0".getBytes());
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                fos = getActivity().openFileOutput("m" + subject, Context.MODE_PRIVATE);
                                fos.write("0".getBytes());
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            mAdapter.addPercent(percent);
                            mAdapter.addAttended("0");
                            mAdapter.addMissed("0");
                            mAdapter.addItem(subject);
                            mRecycler.setAdapter(mAdapter);
                            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                        }


                    }
                });

                add.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                add.show();
            }
        });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean hide = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hide) {

                    FAB.animate().rotation(315);
                    FAB.hide();


                } else {

                    FAB.animate().rotation(0);
                    FAB.show();

                }

            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 5) {
                    hide = true;
                } else if (dy < -3) {
                    hide = false;
                }
            }
        });



        StringBuffer stringBuffer3 = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    getActivity().openFileInput("Subjects")));
            int inputChar;
            while ((inputChar = inputReader.read()) != -1) {
                if ((char) inputChar == '~') {

                    subs.add(stringBuffer3.toString());
                    stringBuffer3.delete(0, stringBuffer3.length());
                } else {
                    stringBuffer3.append((char) inputChar);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<subs.size();i++) {
            StringBuffer stringBuffer = new StringBuffer();
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        getActivity().openFileInput(subs.get(i))));
                int inputChar;
                while ((inputChar = inputReader.read()) != -1) {
                    if ((char) inputChar == '~') {

                        stringBuffer.delete(0, stringBuffer.length());
                    } else if ((char) inputChar == '`') {
                        mAdapter.addPercent(stringBuffer.toString());
                        stringBuffer.delete(0, stringBuffer.length());
                    } else {
                        stringBuffer.append((char) inputChar);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuffer stringBuffer1 = new StringBuffer();

            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        getActivity().openFileInput("a"+subs.get(i))));
                String inputChar;
                while ((inputChar = inputReader.readLine()) != null) {


                    mAdapter.addAttended(inputChar);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }




            StringBuffer stringBuffer2 = new StringBuffer();

            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        getActivity().openFileInput("m"+subs.get(i))));
                String inputChar;
                while ((inputChar = inputReader.readLine()) != null) {
                    mAdapter.addMissed(inputChar);



                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            mAdapter.addItem(subs.get(i));
        }




        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
