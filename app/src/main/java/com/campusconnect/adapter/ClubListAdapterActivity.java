package com.campusconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.activity.RequestsPage_InAdminActivity;
import com.campusconnect.bean.GroupBean;
import java.util.List;

/**
 * Created by rkd on 28/12/15.
 */
public class ClubListAdapterActivity extends
        RecyclerView.Adapter<ClubListAdapterActivity.ClubListViewHolder> {
    //private static List<CollegeListInfoBean> CollegeList;
    private static List<GroupBean> groupList;

    static int pos;
    static Context context;

    public ClubListAdapterActivity(List<GroupBean> groupList,Context context) {
        this.groupList = groupList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    @Override
    public void onBindViewHolder(ClubListViewHolder group_listViewHolder, int i) {
        try {

            GroupBean ci = groupList.get(i);


            group_listViewHolder.club_name.setText(ci.getAbb());
            if (i == 0) {
                group_listViewHolder.club_name.setTypeface(null, Typeface.BOLD);
                group_listViewHolder.club_name.setGravity(Gravity.CENTER);
                group_listViewHolder.i_divider.setVisibility(View.GONE);
                group_listViewHolder.t_divider.setVisibility(View.VISIBLE);
                group_listViewHolder.club_list.setClickable(false);
            } else {
                group_listViewHolder.club_name.setTypeface(null, Typeface.NORMAL);
                group_listViewHolder.club_name.setGravity(Gravity.LEFT);
                group_listViewHolder.i_divider.setVisibility(View.VISIBLE);
                group_listViewHolder.t_divider.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ClubListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_club_list, viewGroup, false);

        return new ClubListViewHolder(itemView);
    }

    public static class ClubListViewHolder extends RecyclerView.ViewHolder {

        CardView club_list;
        TextView club_name;
        View t_divider, i_divider;

        public ClubListViewHolder(View v) {
            super(v);

            club_list = (CardView) v.findViewById(R.id.club_list);
            club_name = (TextView) v.findViewById(R.id.tv_club_name);
            t_divider = (View) v.findViewById(R.id.title_divider);
            i_divider = (View) v.findViewById(R.id.item_divider);

            club_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    Intent intent_temp = new Intent(v.getContext(), RequestsPage_InAdminActivity.class);
                    intent_temp.putExtra("club_id", groupList.get(pos).getClubId());
                    v.getContext().startActivity(intent_temp);
                    ((Activity)context).finish();
                }
            });
        }
    }
}