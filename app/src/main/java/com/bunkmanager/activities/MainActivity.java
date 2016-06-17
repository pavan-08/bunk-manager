package com.bunkmanager.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bunkmanager.helpers.CustomDrawerLayout;
import com.bunkmanager.helpers.DBHelper;
import com.bunkmanager.R;
import com.bunkmanager.adapters.ViewPagerAdapter;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bunkmanager.entity.Subjects;


public class MainActivity extends AppCompatActivity {
    public static final String TAG_FAB="FAB called";
    private EditText subject;
    private ViewPager mPager;
    private ViewPagerAdapter adapter;
    private TabLayout mTabHost;
    private EditText Percent;
    private Toolbar mToolbar;
    private CustomDrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DBHelper dbHelper;
    private RelativeLayout relativeLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean isDrawerLocked = false;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(isTablet()) {
            setTheme(R.style.AppTheme_tablet);
        }*/
        setContentView(R.layout.activity_main);
        ArrayList<Subjects> subs =new ArrayList<>();
        dbHelper = new DBHelper(this);
        try {
            dbHelper.open();
            subs = (ArrayList<Subjects>)dbHelper.getSubjects().clone();
            dbHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mToolbar = (Toolbar)findViewById(R.id.view3);
        drawerLayout=(CustomDrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.fragment_navigation_drawer);
        relativeLayout = (RelativeLayout) findViewById(R.id.findLayout);
        setSupportActionBar(mToolbar);
        setTitle("");
        mTabHost=(TabLayout) findViewById(R.id.view4);
        mPager=(ViewPager)findViewById(R.id.view5);
        adapter=new ViewPagerAdapter(mToolbar,getSupportFragmentManager(),this);
        mPager.setAdapter(adapter);
        if(subs.size()<1){
            mPager.setCurrentItem(1);
            navigationView.setCheckedItem(R.id.getStarted);
        } else{
            int page = getDayInt();
            mPager.setCurrentItem(page);
            navigationView.setCheckedItem(page == 0 ? R.id.subjects : R.id.timeTable);
        }
        if (relativeLayout.getPaddingLeft() > 0) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, navigationView);
            drawerLayout.setScrimColor(getResources().getColor(R.color.noShadow));
            isDrawerLocked = true;
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, navigationView);
            isDrawerLocked = false;
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setUpNavigationDrawer();
        mTabHost.setupWithViewPager(mPager);
        mTabHost.setTabTextColors(ContextCompat.getColorStateList(this, R.color.selector));
        mTabHost.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.white));
        mTabHost.setTabMode(TabLayout.MODE_SCROLLABLE);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    navigationView.setCheckedItem(R.id.subjects);
                } else if(position == 1) {
                    navigationView.setCheckedItem(R.id.getStarted);
                } else {
                    navigationView.setCheckedItem(R.id.timeTable);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpNavigationDrawer() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                if(!isDrawerLocked) {
                    drawerLayout.closeDrawers();
                }
                switch (menuItem.getItemId()){
                    case R.id.getStarted:
                        mPager.setCurrentItem(1);
                        return true;
                    case R.id.subjects:
                        mPager.setCurrentItem(0);
                        return true;
                    case R.id.timeTable:
                        mPager.setCurrentItem(getDayInt());
                        return true;
                    case R.id.notificationSettings:
                        Intent intent1 = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent1);
                        return true;
                    case R.id.helpAFriend:
                        Intent helpAFriend = new Intent(MainActivity.this, HelpAFriend.class);
                        startActivity(helpAFriend);
                        return true;
                    case R.id.rateMe:
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                            return true;
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                            return true;
                        }

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        if(!isDrawerLocked) {
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        }

    }

    private int getDayInt() {
        Date date =new Date();
        SimpleDateFormat dateFormat =new SimpleDateFormat("E");

        if(dateFormat.format(date).equals("Sun")){
            Toast.makeText(getBaseContext(),"Sit back and review attendance,its Sunday!",Toast.LENGTH_LONG).show();
            return 0;
        }  else if(dateFormat.format(date).equals("Mon")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 2;
        } else if(dateFormat.format(date).equals("Tue")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 3;
        } else if(dateFormat.format(date).equals("Wed")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 4;
        } else if(dateFormat.format(date).equals("Thu")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 5;
        } else if(dateFormat.format(date).equals("Fri")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 6;
        } else if(dateFormat.format(date).equals("Sat")){
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 7;
        } else{
            Toast.makeText(getBaseContext(),"Record attendance now",Toast.LENGTH_SHORT).show();
            return 1;
        }
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

    /*public boolean isTablet() {
        try {
            // Compute screen size
            DisplayMetrics dm = getResources().getDisplayMetrics();
            float screenWidth  = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2) +
                    Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 7;
        } catch(Throwable t) {
            return false;
        }

    }
*/


}

