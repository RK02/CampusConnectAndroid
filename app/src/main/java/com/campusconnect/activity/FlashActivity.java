package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.campusconnect.R;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.flurry.android.FlurryAgent;

/**
 * Created by rkd on 3/12/15.
 */
public class FlashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashpage);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(4000);
                    SharedpreferenceUtility.getInstance(FlashActivity.this).putInt("AdminStatus",0);
                    Boolean loggedIn = SharedpreferenceUtility.getInstance(FlashActivity.this).getBoolean(AppConstants.LOG_IN_STATUS);
                    SharedPreferences prefs = FlashActivity.this.getSharedPreferences("AllPersonalFeeds", Context.MODE_PRIVATE);
//save the user list to preference
                    SharedPreferences.Editor editor = prefs.edit();

                    prefs.edit().remove(AppConstants.PERSONAL_FEED_ARRAYLIST).commit();
                    if (loggedIn) {
                        Intent next = new Intent(FlashActivity.this, MainActivity.class);
                        startActivity(next);
                        finish();
                    } else {
                        Intent next = new Intent(FlashActivity.this, SelectCollegeActivity.class);
                        startActivity(next);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();


    }
}
