package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.campusconnect.R;
import com.campusconnect.constant.AppConstants;
/**
 * Created by RK on 26-09-2015.
 */
public class CategoryActivity extends AppCompatActivity {

    Button student,alumini;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        student = (Button)findViewById(R.id.b_student_category);
        alumini = (Button)findViewById(R.id.b_alumini_category);

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedpreferences.edit();
                edit.putString(AppConstants.PROFILE_CATEGORY, AppConstants.STUDENT);
                edit.commit();
                Intent intent_temp = new Intent(v.getContext(), GetProfileDetailsActivity.class);
                startActivity(intent_temp);

            }
        });

        alumini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sharedpreferences.edit();
                edit.putString(AppConstants.PROFILE_CATEGORY, AppConstants.ALUMNI);
                edit.commit();
                Intent intent_temp = new Intent(v.getContext(), GetProfileDetailsActivity.class);
                startActivity(intent_temp);
            }
        });
    }
}