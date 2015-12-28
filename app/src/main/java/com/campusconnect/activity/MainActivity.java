package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.campusconnect.R;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.fragment.CalenderFragment;
import com.campusconnect.fragment.HomeFragment;
import com.campusconnect.fragment.NotificationFragment;
import com.campusconnect.fragment.ProfilePageFragment;
import com.campusconnect.fragment.SearchFragment;
import com.campusconnect.utility.Analytics;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.flurry.android.FlurryAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by canopus on 24/11/15.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    ImageButton search;
    FrameLayout frame;
    ImageButton calender;
    ImageButton profile;
    ImageButton home;
    ImageButton notification;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LinearLayout searchLine, calLine, notificationLine, profileLine, homeLine;

    public static boolean isLaunch = true;

    int fragment;
    public static String frag_loc="";

    public MainActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlurryAgent.setLogLevel(Log.VERBOSE);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogEnabled(true);
        //FlurryAgent.setLocation(float latitude, float longitude);
        FlurryAgent.init(this,AppConstants.MY_FLURRY_APIKEY);

        isLaunch = true;
        search = (ImageButton) findViewById(R.id.ib_search);
        calender = (ImageButton) findViewById(R.id.ib_calendar);
        home = (ImageButton) findViewById(R.id.ib_home);
        profile = (ImageButton) findViewById(R.id.ib_profile);
        notification = (ImageButton) findViewById(R.id.ib_notification);
/*        first,second,third,fourth,fifth;*/
        searchLine = (LinearLayout) findViewById(R.id.lnr_search_line);
        calLine = (LinearLayout) findViewById(R.id.lnr_cal_line);
        notificationLine = (LinearLayout) findViewById(R.id.lnr_notification_line);
        profileLine = (LinearLayout) findViewById(R.id.lnr_profile_line);
        homeLine = (LinearLayout) findViewById(R.id.lnr_home_line);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
            fragment=bundle.getInt("FRAGMENT");
        else if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            HomeFragment fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            frag_loc="home";
            fragmentTransaction.commit();
        }

        search.setOnClickListener(this);
        calender.setOnClickListener(this);
        home.setOnClickListener(this);
        profile.setOnClickListener(this);
        notification.setOnClickListener(this);

        if(fragment==1) {
            search.performClick();
        }
        else if(fragment==2) {
            calender.performClick();
        }
        else if(fragment==3) {
            home.performClick();
        }
        else if(fragment==4) {
            notification.performClick();
        }
        else if(fragment==5) {
            profile.performClick();
        }
    }

    //TODO  changes in the listner
    //TODO changes in the bottom bar
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_profile:

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.pushfront, R.anim.centertoright);
                ProfilePageFragment profileFragment = new ProfilePageFragment();
                fragmentTransaction.replace(R.id.frame_container, profileFragment);
                fragmentTransaction.commit();

                UpdateUi("profile");
                // Intent intent_profile = new Intent(v.getContext(), ProfilePageFragment.class);
                // startActivity(intent_profile);
                // finish();
                break;
            case R.id.ib_home:

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.righttocenter, R.anim.pushback);
                HomeFragment homeFragment = new HomeFragment();
                fragmentTransaction.replace(R.id.frame_container, homeFragment);
                fragmentTransaction.commit();
                UpdateUi("home");
             /*   Intent intent_home = new Intent(v.getContext(), HomeFragment.class);
                startActivity(intent_home);
                overridePendingTransition(R.anim.pushfront, R.anim.centertoright);
                finish();*/
                break;
            case R.id.ib_notification:
              /*  Intent intent_noti = new Intent(v.getContext(), NotificationFragment.class);
                startActivity(intent_noti);
                finish();*/
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.pushfront, R.anim.centertoright);
                NotificationFragment notificationFragment = new NotificationFragment();
                fragmentTransaction.replace(R.id.frame_container, notificationFragment);
                fragmentTransaction.commit();

                UpdateUi("notification");
                break;
            case R.id.ib_search:


                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.righttocenter, R.anim.pushback);
                SearchFragment serchFragment = new SearchFragment();
                fragmentTransaction.replace(R.id.frame_container, serchFragment);
                fragmentTransaction.commit();
                UpdateUi("search");
             /*
                Intent intent_cal = new Intent(v.getContext(), SearchFragment.class);
                startActivity(intent_cal);
                finish();*/
                break;
            case R.id.ib_calendar:

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.righttocenter, R.anim.pushback);
                CalenderFragment calender = new CalenderFragment();
                fragmentTransaction.replace(R.id.frame_container, calender);
                fragmentTransaction.commit();
                UpdateUi("calender");
                break;
        }
    }

    public void UpdateUi(String str) {

        if(fragment!=1 || fragment!=2 || fragment!=3 || fragment!=4 || fragment!=5)
            frag_loc = str;

        if (str.equals("home")) {
            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home_selected);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.yello));

        } else if (str.equals("profile")) {

            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile_selected);
            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.yello));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("search")) {

            search.setImageResource(R.mipmap.search_selected);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.yello));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("calender")) {


            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar_selected);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.yello));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("notification")) {

            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification_selected);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.yello));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
        }


    }

    public void onStart() {
        super.onStart();
        HashMap<String,String> flurryParams=new HashMap<>();
        flurryParams.put("User",SharedpreferenceUtility.getInstance(MainActivity.this).getString(AppConstants.EMAIL_KEY));
        flurryParams.put("EventName","LogIn");
        Analytics.startSession(this,flurryParams);
    }

    public void onStop() {
        super.onStop();
        HashMap<String,String> flurryParams=new HashMap<>();
        flurryParams.put("EventName","LogIn");
        Analytics.stopSession(this,flurryParams);
    }


}
