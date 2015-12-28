package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupMembersAdapterActivity;
import com.campusconnect.adapter.GroupMembersinfoActivity;
import com.campusconnect.utility.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class GroupMembersPage_InAdminActivity extends AppCompatActivity {

    RecyclerView group_members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_in_admin);

        group_members = (RecyclerView) findViewById(R.id.rv_group_members);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_members.setLayoutManager(llm);
        group_members.setHasFixedSize(true);
        group_members.setItemAnimator(new DefaultItemAnimator());
        group_members.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        GroupMembersAdapterActivity gm = new GroupMembersAdapterActivity(
                createList_group_members(3));

        group_members.setAdapter(gm);
    }

    private List<GroupMembersinfoActivity> createList_group_members(int size) {

        List<GroupMembersinfoActivity> result = new ArrayList<GroupMembersinfoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupMembersinfoActivity ci = new GroupMembersinfoActivity();
            result.add(ci);

        }

        return result;
    }
}
