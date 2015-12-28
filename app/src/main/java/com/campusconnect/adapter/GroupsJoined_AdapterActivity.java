package com.campusconnect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.supportClasses.GroupsJoined_infoActivity;
import com.campusconnect.R;
import java.util.List;

/**
 * Created by RK on 11-09-2015.
 */
public class GroupsJoined_AdapterActivity extends
        RecyclerView.Adapter<GroupsJoined_AdapterActivity.GroupsViewHolder> {

    private List<GroupsJoined_infoActivity> GroupsJoinedList;

    public GroupsJoined_AdapterActivity(List<GroupsJoined_infoActivity> GroupsJoinedList) {
        this.GroupsJoinedList = GroupsJoinedList;
    }

    @Override
    public int getItemCount() {
        return GroupsJoinedList.size();
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder groupViewHolder, int i) {
        GroupsJoined_infoActivity gi = GroupsJoinedList.get(i);
        groupViewHolder.group_joinedName.setText(gi.group_name);

    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_groups_joined, viewGroup, false);

        return new GroupsViewHolder(itemView);
    }

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {

        TextView group_joinedName;
        public GroupsViewHolder(View v) {
            super(v);

            group_joinedName = (TextView)v.findViewById(R.id.tv_group_joined_name);



        }

    }
}
