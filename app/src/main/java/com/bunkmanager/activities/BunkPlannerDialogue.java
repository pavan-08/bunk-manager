package com.bunkmanager.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bunkmanager.R;

public class BunkPlannerDialogue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunk_planner_dialogue);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Bunk Predictor");
        dialog.setMessage(getIntent().getStringExtra("summary"));
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BunkPlannerDialogue.this.finish();
            }
        });
        dialog.show();
    }
}
