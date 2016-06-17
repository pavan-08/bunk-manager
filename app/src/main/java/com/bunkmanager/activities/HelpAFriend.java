package com.bunkmanager.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.bunkmanager.R;
import com.bunkmanager.helpers.Screenshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HelpAFriend extends AppCompatActivity {

    private FloatingActionButton fabShare;
    private AppCompatButton calculate;
    private TextInputEditText total, attended, percent;
    private TextInputLayout totalLayout, attendedLayout, percentLayout;
    private AppCompatTextView summary;
    private RelativeLayout ssFrame;
    private Screenshot screenshot;
    private boolean canShare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_afriend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabShare = (FloatingActionButton) findViewById(R.id.fab);
        calculate = (AppCompatButton) findViewById(R.id.help_calculate);
        totalLayout = (TextInputLayout) findViewById(R.id.total_lectures_layout);
        total = (TextInputEditText) findViewById(R.id.total_lectures_input);
        attendedLayout = (TextInputLayout) findViewById(R.id.attended_lectures_layout);
        attended = (TextInputEditText) findViewById(R.id.attended_lectures_input);
        percentLayout = (TextInputLayout) findViewById(R.id.required_lectures_layout);
        percent = (TextInputEditText) findViewById(R.id.required_lectures_input);
        summary = (AppCompatTextView) findViewById(R.id.help_summary);
        ssFrame = (RelativeLayout) findViewById(R.id.help_relative_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canShare = false;
                if(total.getText().toString().isEmpty()) {
                    totalLayout.setErrorEnabled(true);
                    attendedLayout.setErrorEnabled(false);
                    percentLayout.setErrorEnabled(false);
                    totalLayout.setError("Enter value");
                } else if(attended.getText().toString().isEmpty()) {
                    totalLayout.setErrorEnabled(false);
                    attendedLayout.setErrorEnabled(true);
                    percentLayout.setErrorEnabled(false);
                    attendedLayout.setError("Enter value");
                } else if(percent.getText().toString().isEmpty()) {
                    totalLayout.setErrorEnabled(false);
                    attendedLayout.setErrorEnabled(false);
                    percentLayout.setErrorEnabled(true);
                    percentLayout.setError("Enter value");
                } else if(MainActivity.isNotNumeric(total.getText().toString())) {
                    totalLayout.setErrorEnabled(true);
                    attendedLayout.setErrorEnabled(false);
                    percentLayout.setErrorEnabled(false);
                    totalLayout.setError("Only numbers allowed");
                } else if(MainActivity.isNotNumeric(attended.getText().toString())) {
                    totalLayout.setErrorEnabled(false);
                    attendedLayout.setErrorEnabled(true);
                    percentLayout.setErrorEnabled(false);
                    attendedLayout.setError("Only numbers allowed");
                } else if(MainActivity.isNotNumeric(percent.getText().toString())) {
                    totalLayout.setErrorEnabled(false);
                    attendedLayout.setErrorEnabled(false);
                    percentLayout.setErrorEnabled(true);
                    percentLayout.setError("Only numbers allowed");
                } else {
                    float t = Float.parseFloat(total.getText().toString());
                    float a = Float.parseFloat(attended.getText().toString());
                    float p = Float.parseFloat(percent.getText().toString());
                    percentLayout.setErrorEnabled(false);
                    attendedLayout.setErrorEnabled(false);
                    totalLayout.setErrorEnabled(false);
                    if(a <= t) {
                        canShare = true;
                        float cp = a / t * 100;
                        float b = a / p * 100;
                        float m = (t - a) / (100 - p) * 100 - t;
                        if (b - t >= 0) {
                            summary.setText(String.format("You have %.2f percent attendance.\nYou can bunk %d lectures.", cp, (int) Math.floor(b - t)));
                        } else {
                            summary.setText(String.format("You have %.2f percent attendance.\nYou need to attend %d lectures.", cp, (int) Math.ceil(m)));
                        }
                    } else {
                        attendedLayout.setErrorEnabled(true);
                        attendedLayout.setError("Cannot be greater than total lectures");
                    }
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canShare) {
                    screenshot = new Screenshot(ssFrame);
                    Bitmap bitmap = screenshot.snap();
                    //save in cache
                    try {

                        File cachePath = new File(getCacheDir(), "images");
                        cachePath.mkdirs(); // don't forget to make the directory
                        FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //share saved image
                    File imagePath = new File(getCacheDir(), "images");
                    File newFile = new File(imagePath, "image.png");
                    Uri contentUri = FileProvider.getUriForFile(getBaseContext(), "com.bunkmanager.fileprovider", newFile);

                    if (contentUri != null) {

                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                        shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                "Hi, here is your attendance summary. Download Bunk Manager " +
                                "to enjoy all features for free, no ads. Download via "+
                                "http://play.google.com/store/apps/details?id=" + getPackageName());
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        startActivity(Intent.createChooser(shareIntent, "Choose an app"));

                    }
                } else {
                    Snackbar.make(view, "Nothing to share", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        fabShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(view, "Share results to friend", Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}
