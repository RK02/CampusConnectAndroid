package com.campusconnect.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.bean.GroupMemberBean;
import com.campusconnect.utility.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class GroupMembersByGroupAdapterActivity extends
        RecyclerView.Adapter<GroupMembersByGroupAdapterActivity.GroupMembersByGroupViewHolder> {

    private List<GroupMemberBean> GroupMembersByGroupList;
    private Context context;

    public GroupMembersByGroupAdapterActivity(List<GroupMemberBean> GroupMembersByGroupList, Context context) {
        this.GroupMembersByGroupList = GroupMembersByGroupList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return GroupMembersByGroupList.size();
    }

    @Override
    public void onBindViewHolder(GroupMembersByGroupViewHolder group_membersbygroupViewHolder, int i) {
        GroupMemberBean ci = GroupMembersByGroupList.get(i);
        group_membersbygroupViewHolder.member_batch.setText(ci.getBatch());
        group_membersbygroupViewHolder.member_branch.setText(ci.getBranch());
        group_membersbygroupViewHolder.member_name.setText(ci.getName());

        try {


            Picasso.with(context).load(ci.getPhotoUrl()).into(group_membersbygroupViewHolder.member_icon);

        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(group_membersbygroupViewHolder.member_icon);
        }
    }

    @Override
    public GroupMembersByGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_member, viewGroup, false);

        return new GroupMembersByGroupViewHolder(itemView);
    }

    public static class GroupMembersByGroupViewHolder extends RecyclerView.ViewHolder {

        TextView member_name, member_batch, member_branch;
        CircularImageView member_icon;

        public GroupMembersByGroupViewHolder(View v) {
            super(v);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");
            Typeface r_lig = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Light.ttf");

            member_name = (TextView) v.findViewById(R.id.tv_member_name);
            member_branch = (TextView) v.findViewById(R.id.tv_member_branch);
            member_batch = (TextView) v.findViewById(R.id.tv_member_batch);
            member_icon = (CircularImageView) v.findViewById(R.id.group_icon_notification);
            member_name.setTypeface(r_reg);
            member_batch.setTypeface(r_lig);
            member_branch.setTypeface(r_lig);

        }

    }
}

