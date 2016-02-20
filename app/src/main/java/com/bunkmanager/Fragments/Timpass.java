package com.bunkmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bunkmanager.R;
import com.bunkmanager.SimpleDividerItemDecoration;
import com.bunkmanager.adapters.InfoRecyclerAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pavan on 30/04/2015.
 */
public class Timpass extends Fragment {
    private View view;
    private RecyclerView mRecycler;
    private InfoRecyclerAdapter mAdapter;
    public  Timpass(){

    }
    public static Timpass newInstance(int position){
        Timpass fragment =new Timpass();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.timpass1,null);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler=(RecyclerView)view.findViewById(R.id.infoRecycler);
        mAdapter=new InfoRecyclerAdapter(getActivity());
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
