package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campusconnect.R;

/**
 * Created by RK on 23-09-2015.
 */
public class Signup_4Activity extends AppCompatActivity {

    RecyclerView group_list_at_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_4);

        group_list_at_sign_up = (RecyclerView) findViewById(R.id.rv_college_list);

        group_list_at_sign_up.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_list_at_sign_up.setLayoutManager(llm);
        group_list_at_sign_up.setItemAnimator(new DefaultItemAnimator());
        //group_list_at_sign_up.addItemDecoration(new DividerItemDecoration_college_list(this, LinearLayoutManager.VERTICAL));

    }
}
