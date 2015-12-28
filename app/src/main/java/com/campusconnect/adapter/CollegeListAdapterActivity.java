package com.campusconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.campusconnect.R;
import com.campusconnect.activity.Signup_2Activity;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;

import java.util.List;


/**
 * Created by RK on 21-09-2015.
 */
public class CollegeListAdapterActivity extends
        RecyclerView.Adapter<CollegeListAdapterActivity.CollegeListViewHolder> {

    private static List<CollegeListInfoBean> CollegeList;
    static int pos;
  static  Context context;

    public CollegeListAdapterActivity(List<CollegeListInfoBean> CollegeList,Context context) {
        this.CollegeList = CollegeList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return CollegeList.size();
    }

    @Override
    public void onBindViewHolder(CollegeListViewHolder group_listViewHolder, int i) {
        try {

            CollegeListInfoBean ci = CollegeList.get(i);


            group_listViewHolder.college_name.setText(ci.getName());
            if (i == 0) {



                group_listViewHolder.college_name.setTypeface(null, Typeface.BOLD);
                group_listViewHolder.college_name.setGravity(Gravity.CENTER);
                group_listViewHolder.i_divider.setVisibility(View.GONE);
                group_listViewHolder.t_divider.setVisibility(View.VISIBLE);
                group_listViewHolder.college_list.setClickable(false);
            } else {
                group_listViewHolder.college_name.setTypeface(null, Typeface.NORMAL);
                group_listViewHolder.college_name.setGravity(Gravity.LEFT);
                group_listViewHolder.i_divider.setVisibility(View.VISIBLE);
                group_listViewHolder.t_divider.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CollegeListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_college_list, viewGroup, false);

        return new CollegeListViewHolder(itemView);
    }

    public static class CollegeListViewHolder extends RecyclerView.ViewHolder {

        CardView college_list;
        TextView college_name;
        View t_divider, i_divider;

        public CollegeListViewHolder(View v) {
            super(v);

            college_list = (CardView) v.findViewById(R.id.college_list);
            college_name = (TextView) v.findViewById(R.id.tv_college_name);
            t_divider = (View) v.findViewById(R.id.title_divider);
            i_divider = (View) v.findViewById(R.id.item_divider);

            college_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    SharedpreferenceUtility.getInstance(v.getContext()).putString(AppConstants.COLLEGE_NAME, CollegeList.get(pos).getName());
                    SharedpreferenceUtility.getInstance(v.getContext()).putString(AppConstants.COLLEGE_LOCATION, CollegeList.get(pos).getLocation());
                    SharedpreferenceUtility.getInstance(v.getContext()).putString(AppConstants.COLLEGE_ID, CollegeList.get(pos).getCollegeId());
                    SharedPreferences sharedpreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.putString(AppConstants.COLLEGE_NAME, CollegeList.get(pos).getName());
                    edit.putString(AppConstants.COLLEGE_LOCATION, CollegeList.get(pos).getLocation());
                    edit.putString(AppConstants.COLLEGE_ID, CollegeList.get(pos).getCollegeId());
                    edit.commit();

                    Intent intent_temp = new Intent(v.getContext(), Signup_2Activity.class);
                    v.getContext().startActivity(intent_temp);
                    ((Activity)context).finish();

                }
            });

        }

    }
}