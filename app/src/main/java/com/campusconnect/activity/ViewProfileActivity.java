package com.campusconnect.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.adapter.ProfilePageAdapterActivity;
import com.campusconnect.adapter.ViewProfilePageAdapterActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class ViewProfileActivity extends ActionBarActivity {

    RecyclerView groups_joined;
    DatabaseHandler db;
    TextView profile_title;
    Typeface r_reg, r_med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profile_title = (TextView) findViewById(R.id.tv_profile_text);
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");
        profile_title.setTypeface(r_med);

        groups_joined = (RecyclerView)findViewById(R.id.recycler_groups);
        LinearLayoutManager llm = new LinearLayoutManager(ViewProfileActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        groups_joined.setLayoutManager(llm);
        groups_joined.setHasFixedSize(true);
        groups_joined.setItemAnimator(new DefaultItemAnimator());

        List<GroupBean> GroupList;
        db = new DatabaseHandler(ViewProfileActivity.this);

        GroupList = db.getFollowingClubData();
        for(GroupBean gb: GroupList){
            Log.e("LOG A", "A " + gb.getAbb());

        }
        if(GroupList == null || GroupList.size() == 0){
            GroupList = new ArrayList<GroupBean>();
        }
        if(GroupList.size()==0) {
            GroupBean gb = new GroupBean();
            GroupList.add(0,gb);
        }
        else {
            //Adding to 1st entry to GroupList again,as the first entry doesnt seem to come up
            GroupList.add(GroupList.size(), GroupList.get(0));
        }
        ViewProfilePageAdapterActivity gj = new ViewProfilePageAdapterActivity(
                GroupList,ViewProfileActivity.this);
        groups_joined.setAdapter(gj);



    }


}
