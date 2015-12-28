package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campusconnect.R;

/**
 * Created by RK on 05/11/2015.
 */
public class SettingsActivity extends ActionBarActivity {
    LinearLayout close;
    TextView about_text, group_info;
    Typeface r_reg, r_med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        about_text = (TextView) findViewById(R.id.tv_about);
        group_info = (TextView) findViewById(R.id.tv_about_group_info);

        about_text.setTypeface(r_med);
        group_info.setTypeface(r_reg);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}
