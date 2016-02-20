package com.bunkmanager;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.bunkmanager.adapters.RecyclerDrawerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {
    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
    //InterstitialAd mInterstitialAd;
    private static RecyclerView mRecycler;
    private static RecyclerDrawerAdapter mAdapter;
    private ImageView img;
    private ActionBarDrawerToggle mDrawerToggle;
    private CustomDrawerLayout mDrawerLayout;


    private View containerView;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private ViewPager mPager;
    public NavigationDrawerFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState!=null){
            mFromSavedInstanceState=true;
        }
      /* mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getActivity().getResources().getString(R.string.interstitial_ad_unit_id));
        requestNewInterstitial();*/
    }
    /*private void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler=(RecyclerView)view.findViewById(R.id.view9);
        //img=(ImageView)view.findViewById(R.id.nav_back);
       // img.setScaleType(ImageView.ScaleType.FIT_XY);
        final ArrayList<String> hour = new ArrayList<>();
        for(int i=1;i<=6;i++) {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                        getActivity().openFileInput("hours" + String.valueOf(i))));
                String input;
                while ((input = inputReader.readLine()) != null) {
                    hour.add(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mAdapter=new RecyclerDrawerAdapter(getActivity());
        mRecycler.setAdapter(mAdapter);
        mRecycler.isClickable();
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecycler.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {


                switch (position) {
                    case 0:

                        if (mPager != null) {
                            mPager.setCurrentItem(1);
                        } else {
                            Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:

                        if (mPager != null) {
                            mPager.setCurrentItem(0);
                        } else {
                            Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:

                        if (mPager != null) {
                            if (hour.size() < 1) {
                                mPager.setCurrentItem(2);
                            } else {
                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("E");
                                if (dateFormat.format(date).equals("Sun")) {
                                    mPager.setCurrentItem(0);
                                    Toast.makeText(getActivity(), "Sit back and review attendance,its Sunday!", Toast.LENGTH_LONG).show();
                                } else if (dateFormat.format(date).equals("Mon")) {
                                    mPager.setCurrentItem(2);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else if (dateFormat.format(date).equals("Tue")) {
                                    mPager.setCurrentItem(3);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else if (dateFormat.format(date).equals("Wed")) {
                                    mPager.setCurrentItem(4);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else if (dateFormat.format(date).equals("Thu")) {
                                    mPager.setCurrentItem(5);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else if (dateFormat.format(date).equals("Fri")) {
                                    mPager.setCurrentItem(6);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else if (dateFormat.format(date).equals("Sat")) {
                                    mPager.setCurrentItem(7);
                                    Toast.makeText(getActivity(), "Record attendance now", Toast.LENGTH_SHORT).show();
                                } else {
                                    mPager.setCurrentItem(1);
                                    Toast.makeText(getActivity(), dateFormat.format(date), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            Toast.makeText(getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                       /*if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {*/
                            Intent intent1 = new Intent(getActivity(), Settings.class);
                            getActivity().startActivity(intent1);
                        /*}*/
                        break;
                }
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
       /*mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent1 = new Intent(getActivity(), Settings.class);
                getActivity().startActivity(intent1);
            }
        });*/
    }

    public void setUp(ViewPager viewPager, int fragmentId,CustomDrawerLayout drawerLayout, Toolbar toolbar) {
        containerView=getActivity().findViewById(fragmentId);
        mPager=viewPager;
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,String.valueOf(mUserLearnedDrawer));
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();

            }
        });
    }

    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }


}
