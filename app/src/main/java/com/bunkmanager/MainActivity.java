package com.bunkmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;
import com.bunkmanager.adapters.SubjectRecyclerAdapter;
import com.bunkmanager.adapters.ViewPagerAdapter;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bunkmanager.entity.Subjects;


public class MainActivity extends AppCompatActivity {
    public static final String TAG_FAB="FAB called";
    private RecyclerView mRecycler;
    private EditText subject;
    private ViewPager mPager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout mTabHost;
    private EditText Percent;
    private SubjectRecyclerAdapter mAdapter;
    private Toolbar mToolbar;
    private CustomDrawerLayout mDrawerLayout;
    private DBHelper dbHelper;



    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View mView = getLayoutInflater().inflate(R.layout.fragment_layout,null);
        /*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
        /*StringBuffer stringBuffer =new StringBuffer();*/
        ArrayList<Subjects> subs =new ArrayList<>();
        /*ArrayList<String> hour = new ArrayList<>();*/
        dbHelper = new DBHelper(this);
        try {
           /* BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                    openFileInput("Subjects")));
            int inputChar;
            while ((inputChar = inputReader.read()) != -1) {
                if ((char) inputChar == '~') {
                    subs.add(stringBuffer.toString());
                    stringBuffer.delete(0, stringBuffer.length());
                } else {
                    stringBuffer.append((char) inputChar);
                }
            }*/
            dbHelper.open();
            subs = (ArrayList<Subjects>)dbHelper.getSubjects().clone();
            dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*StringBuffer stringBuffer1=new StringBuffer();
        for(int i=1;i<=6;i++) {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        openFileInput("hours"+String.valueOf(i))));
                String input;
                while ((input = inputReader.readLine()) != null) {
                    hour.add(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        mToolbar = (Toolbar)findViewById(R.id.view3);
        mRecycler=(RecyclerView)mView.findViewById(R.id.view);
        mDrawerLayout=(CustomDrawerLayout)findViewById(R.id.drawer_layout);
        mAdapter=new SubjectRecyclerAdapter(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTabHost=(SlidingTabLayout)findViewById(R.id.view4);
        mPager=(ViewPager)findViewById(R.id.view5);
        adapter=new ViewPagerAdapter(mToolbar,mTabHost,getSupportFragmentManager(),this);
        mPager.setAdapter(adapter);
      //  mPager.setOffscreenPageLimit(0);
        if(subs.size()<1){
            mPager.setCurrentItem(1);
        } else{
            mPager.setCurrentItem(2);
            Date date =new Date();
            SimpleDateFormat dateFormat =new SimpleDateFormat("E");

            if(dateFormat.format(date).equals("Sun")){
                mPager.setCurrentItem(0);
                Toast.makeText(getBaseContext(),"Sit back and review attendance,its Sunday!",Toast.LENGTH_LONG).show();
            }  else if(dateFormat.format(date).equals("Mon")){
                mPager.setCurrentItem(2);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else if(dateFormat.format(date).equals("Tue")){
                mPager.setCurrentItem(3);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else if(dateFormat.format(date).equals("Wed")){
                mPager.setCurrentItem(4);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else if(dateFormat.format(date).equals("Thu")){
                mPager.setCurrentItem(5);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else if(dateFormat.format(date).equals("Fri")){
                mPager.setCurrentItem(6);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else if(dateFormat.format(date).equals("Sat")){
                mPager.setCurrentItem(7);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            } else{
                mPager.setCurrentItem(1);
                Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            }
        }
        mTabHost.setDistributeEvenly(true);
        mTabHost.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.white);
            }
        });
        mTabHost.setViewPager(mPager);
        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean hide=false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(hide){
                    mToolbar.animate().translationY(-mToolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                } else{
                   mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>8){
                    hide=true;
                } else if(dy<-5){
                    hide=false;
                }
            }
        });
        NavigationDrawerFragment drawerFragment =(NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(mPager,R.id.fragment_navigation_drawer,(CustomDrawerLayout)findViewById(R.id.drawer_layout),mToolbar);



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==R.id.reset){
            AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
            alert.setMessage("All data will be lost, do you wish to continue?");
            alert.setPositiveButton("CONTINUE",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        dbHelper.open();
                        dbHelper.deleteAllData();
                        dbHelper.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Intent bIntent= new Intent(getBaseContext(),MainActivity.class);
                    bIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(bIntent);
                }
            });
            alert.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(),"Canceled, no data lost",Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();

        } /*else if(id==R.id.notification){
            Intent intent =new Intent(getBaseContext(),notifications.class);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }




    public static boolean isNotNumeric(String str)
    {
        try
        {
            int d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return true;
        }
        return false;
    }




}

