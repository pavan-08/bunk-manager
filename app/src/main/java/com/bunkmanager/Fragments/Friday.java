package com.bunkmanager.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.MainActivity;
import com.bunkmanager.R;
import com.bunkmanager.SimpleDividerItemDecoration;
import com.bunkmanager.adapters.RecyclerAdapter5;
import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Pavan on 28/04/2015.
 */
public class Friday extends Fragment {
    private static RecyclerView mRecycler;
    private static RecyclerAdapter5 mAdapter;
    private static String hour;
    private FloatingActionButton FAB;
    private TextView intro;
    public Friday() {

    }

    public static Friday newInstance(int position) {
        Friday fragment = new Friday();
        Bundle args =new Bundle();
        args.putInt("position",position);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        StringBuffer stringBuffer =new StringBuffer();
        try{
            BufferedReader inputReader =new BufferedReader(new InputStreamReader(getActivity().openFileInput("hours"+String.valueOf(5))));
            String input;
            while((input=inputReader.readLine())!=null){
                stringBuffer.append(input);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hour=stringBuffer.toString();

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final
    Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int num =getArguments().getInt("position");
        intro=(TextView)view.findViewById(R.id.lec_intro);
        mRecycler = (RecyclerView) view.findViewById(R.id.view);
        FAB=(FloatingActionButton)view.findViewById(R.id.fab1);
        if(hour.equals("")){
            FAB.setVisibility(View.VISIBLE);
        } else{
            FAB.setVisibility(View.INVISIBLE);
        }
        FAB.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), "Add lecture count", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hour.equals("")) {
                    AlertDialog.Builder add_hour = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater(savedInstanceState);
                    View hour_layout = inflater.inflate(R.layout.add_hour, null);
                    add_hour.setView(hour_layout);
                    final EditText mHour1 = (EditText) hour_layout.findViewById(R.id.editText3);
                    final EditText mHour2 = (EditText) hour_layout.findViewById(R.id.editText4);
                    final EditText mHour3 = (EditText) hour_layout.findViewById(R.id.editText5);
                    final EditText mHour4 = (EditText) hour_layout.findViewById(R.id.editText6);
                    final EditText mHour5 = (EditText) hour_layout.findViewById(R.id.editText7);
                    final EditText mHour6 = (EditText) hour_layout.findViewById(R.id.editText8);


                    add_hour.setPositiveButton("SET", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (MainActivity.isNotNumeric(mHour1.getText().toString()) || MainActivity.isNotNumeric(mHour2.getText().toString()) || MainActivity.isNotNumeric(mHour3.getText().toString()) || MainActivity.isNotNumeric(mHour4.getText().toString()) || MainActivity.isNotNumeric(mHour5.getText().toString()) || MainActivity.isNotNumeric(mHour6.getText().toString())) {
                                Toast.makeText(getActivity(), "Invalid Entry", Toast.LENGTH_SHORT).show();

                            } else {
                                save("hours" + String.valueOf(1), mHour1.getText().toString(), Context.MODE_PRIVATE);
                                save("hours" + String.valueOf(2), mHour2.getText().toString(), Context.MODE_PRIVATE);
                                save("hours" + String.valueOf(3), mHour3.getText().toString(), Context.MODE_PRIVATE);
                                save("hours" + String.valueOf(4), mHour4.getText().toString(), Context.MODE_PRIVATE);
                                save("hours" + String.valueOf(5), mHour5.getText().toString(), Context.MODE_PRIVATE);
                                save("hours" + String.valueOf(6), mHour6.getText().toString(), Context.MODE_PRIVATE);
                                Intent bIntent= new Intent(getActivity(),MainActivity.class);
                                bIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(bIntent);
                                FAB.hide();
                            }
                        }
                    });
                    add_hour.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();

                        }
                    });
                    add_hour.show();
                }
                else{
                    Toast.makeText(getActivity(),"Add Subjects now",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(hour.equals("")){
            mAdapter = new RecyclerAdapter5(getActivity(),num,String.valueOf(0));
            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        else {
            mAdapter = new RecyclerAdapter5(getActivity(), num, hour);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < Integer.parseInt(hour); i++) {
                mAdapter.setdata();
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader
                            (getActivity().openFileInput(String.valueOf(num) + String.valueOf(i))));
                    String input;
                    while ((input = inputReader.readLine()) != null) {
                        mAdapter.addItem(input, i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader
                            (getActivity().openFileInput("a" + String.valueOf(num) + String.valueOf(i))));
                    String input;
                    while ((input = inputReader.readLine()) != null) {
                        mAdapter.addAttended(input, i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader
                            (getActivity().openFileInput("m" + String.valueOf(num) + String.valueOf(i))));
                    String input;
                    while ((input = inputReader.readLine()) != null) {
                        mAdapter.addMissed(input, i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            mRecycler.setAdapter(mAdapter);
            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

            if (mAdapter.getItemCount() > 0) {
                intro.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_name2, 0, 0, 0);
                intro.setText(": click this to add number of lectures");
                intro.setTextSize(17);
            } else {
                intro.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                intro.setText("Enjoy the holiday! :D");
                intro.setTextSize(22);
            }
        }

    }

    public  void save(String file, String data, int mode){
        FileOutputStream fos;
        try{
            fos=getActivity().openFileOutput(file,mode);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

