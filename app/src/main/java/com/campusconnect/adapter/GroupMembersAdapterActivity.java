package com.campusconnect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class GroupMembersAdapterActivity extends
        RecyclerView.Adapter<GroupMembersAdapterActivity.GroupMembersViewHolder> {

    private List<GroupMembersinfoActivity> GroupMembersList;
    CharSequence members[]={"Rishab Doshi","Siddharth Ramakrishnan","Aditi Chalisgaonkar"};

    public GroupMembersAdapterActivity(List<GroupMembersinfoActivity> GroupMembersList) {
        this.GroupMembersList = GroupMembersList;
    }

    @Override
    public int getItemCount() {
        return GroupMembersList.size();
    }

    @Override
    public void onBindViewHolder(GroupMembersViewHolder group_membersViewHolder, int i) {
        GroupMembersinfoActivity ci = GroupMembersList.get(i);
        group_membersViewHolder.g_members.setText(members[i]);
    }

    @Override
    public GroupMembersViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_group_members, viewGroup, false);

        return new GroupMembersViewHolder(itemView);
    }

    public static class GroupMembersViewHolder extends RecyclerView.ViewHolder {

        TextView g_members;
        public GroupMembersViewHolder(View v) {
            super(v);
            g_members = (TextView)v.findViewById(R.id.tv_group_members);
            
        }

    }
}

