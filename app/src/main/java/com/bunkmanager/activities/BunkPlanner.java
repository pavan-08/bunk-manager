package com.bunkmanager.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bunkmanager.R;
import com.bunkmanager.adapters.PlannerRecyclerAdapter;

import java.util.Calendar;

public class BunkPlanner extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView textView;
    private View.OnClickListener planBunk;
    private static final String message = "Bunk Planner will help you bunk a day previously planned.\n" +
            "It guides daily about which lectures to attend to be able to bunk on said date.\n" +
            "It will also point out if a planned bunk is impossible.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunk_planner);
        toolbar = (Toolbar) findViewById(R.id.planner_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.planner_fab);
        recyclerView = (RecyclerView) findViewById(R.id.planner_recycler_view);
        textView = (TextView) findViewById(R.id.planner_textview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new PlannerRecyclerAdapter(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        planBunk = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(BunkPlanner.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        //TODO: store in database
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        };
        fab.setOnClickListener(planBunk);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Plan new bunk.", Snackbar.LENGTH_LONG)
                        .setAction("Plan", planBunk).show();
                return true;
            }
        });

        textView.setText(message);
        if(recyclerView.getChildCount() > 0) {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bunk_planner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.planner_info:
                AlertDialog.Builder info = new AlertDialog.Builder(BunkPlanner.this);
                info.setTitle("Bunk Planner");
                info.setIcon(android.R.drawable.ic_menu_info_details);
                info.setMessage(message);
                info.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                info.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
