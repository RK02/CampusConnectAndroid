package com.campusconnect.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.campusconnect.R;
import com.campusconnect.activity.AboutGroupActivity;
import com.campusconnect.activity.GroupMembersByGroupActivity;
import com.campusconnect.activity.NewsPostsByGroupActivity;
import com.campusconnect.activity.UpcomingEventsActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 07-10-2015.
 */
public class GroupPageAdapterActivity extends
        RecyclerView.Adapter<GroupPageAdapterActivity.GroupPageHolder> {

    private List<GroupPage_infoActivity> GroupPageList;
    String g_name, g_icon;
    Integer m_count, f_count;
    int follow_click_count = 0, member_click_count = 0;
    static int followClick = 0;
    Context context;
    String Imageurl = "";
    GroupBean groupBean;
    int call_web_api;

    int yes_dialog_box_click_count = 0;
    public TextView dialog_info;

    GroupInfoViewHolder holder1;
    DatabaseHandler db;
    ExtraGroupInfoListHolder holder2;
    CharSequence GroupInfoAttributes[] = {"About", "Members", "Events", "News Posts",};


    public GroupPageAdapterActivity(List<GroupPage_infoActivity> GroupsJoinedList) {
        this.GroupPageList = GroupsJoinedList;
    }

    public GroupPageAdapterActivity(List<GroupPage_infoActivity> GroupsJoinedList, GroupBean groupBean, Context context) {
        try {
            this.GroupPageList = GroupsJoinedList;
            g_name = groupBean.getName();
            g_icon = groupBean.getPhotourl();
            m_count = Integer.parseInt(groupBean.getMemberCount());
            f_count = Integer.parseInt(groupBean.getFollowCount());
            follow_click_count = Integer.parseInt(groupBean.getFollow());
            member_click_count = Integer.parseInt(groupBean.getIsMember());
            Imageurl = groupBean.getPhotourl();
            this.groupBean = groupBean;
            this.context = context;

            if (db == null) {
                db = new DatabaseHandler(context);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return GroupPageList.size();
    }

    @Override
    public void onBindViewHolder(GroupPageHolder groupViewHolder, int i) {
        if (getItemViewType(i) == 0) {

            holder1 = (GroupInfoViewHolder) groupViewHolder;
            holder1.group_name.setText(g_name);
            try {
                Picasso.with(context).load(Imageurl).into(holder1.group_icon);
            } catch (Exception e) {
                Picasso.with(context).load(R.mipmap.default_image).into(holder1.group_icon);
            }
            holder1.members_count.setText(m_count.toString());
            holder1.followers_count.setText(f_count.toString());
            if (follow_click_count % 2 == 1) {
                holder1.tbtn_follow.setBackgroundResource(R.mipmap.going_selected);
            } else {
                holder1.tbtn_follow.setBackgroundResource(R.mipmap.going);
            }
            if (member_click_count == 1) {

                holder1.iv_member.setBackgroundResource(R.drawable.group_member_selected);
            } else {
                holder1.iv_member.setBackgroundResource(R.drawable.group_member);
            }
        } else {
            holder2 = (ExtraGroupInfoListHolder) groupViewHolder;
            holder2.g_name_joined.setText(GroupInfoAttributes[i - 1]);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0)
            viewType = 0;
        else
            viewType = 1;
        return viewType;
    }

    @Override
    public GroupPageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        GroupPageHolder holder;
        if (i == 0) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_group_info, viewGroup, false);
            holder = new GroupInfoViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_extra_group_info, viewGroup, false);
            holder = new ExtraGroupInfoListHolder(itemView);
        }
        return holder;
    }

    public static class GroupPageHolder extends RecyclerView.ViewHolder {

        public GroupPageHolder(View v) {
            super(v);
        }
    }

    public class GroupInfoViewHolder extends GroupPageAdapterActivity.GroupPageHolder {

        TextView group_name, members_count, followers_count;
        CircularImageView group_icon;
        ToggleButton tbtn_follow;
        ImageView iv_member;

        public GroupInfoViewHolder(View itemView) {
            super(itemView);
            group_name = (TextView) itemView.findViewById(R.id.group_name);
            members_count = (TextView) itemView.findViewById(R.id.tv_members_count);
            followers_count = (TextView) itemView.findViewById(R.id.tv_followers_count);
            group_icon = (CircularImageView) itemView.findViewById(R.id.group_image);
            tbtn_follow = (ToggleButton) itemView.findViewById(R.id.tb_followers);
            iv_member = (ImageView) itemView.findViewById(R.id.tb_members);


            tbtn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (follow_click_count % 2 == 0) {
                        f_count++;
                        String clubId = groupBean.getClubId();
                        int i = db.updateFollow(clubId, "1");
                        notifyDataSetChanged();
                        webApiFollow(clubId, v.getContext());
                        followClick = 1;
                    } else {
                        String clubId = groupBean.getClubId();
                        // updating value in the database;
                        int i = db.updateFollow(clubId, "0");
                        webApiFollow(clubId, v.getContext());
                        f_count--;
                        followClick = 0;
                        notifyDataSetChanged();
                    }
                    follow_click_count++;
                }
            });
            iv_member.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MemberConfirmationDialog confirmDialog = new MemberConfirmationDialog((Activity) v.getContext());
                    Window window = confirmDialog.getWindow();
                    window.setLayout(450, ViewGroup.LayoutParams.WRAP_CONTENT);
                    confirmDialog.show();

                }
            });
        }

        public void webApiFollow(String clubId, Context context) {

            try {
                String personPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("club_id", "" + clubId);
                jsonObject.put("from_pid", "" + personPid);
        /*    jsonObject.put("club_id", "5197870353350656");*/
           /* jsonObject.put("from_pid", "5688424874901504");*/
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url;
                if (followClick == 1) {
                    url = WebServiceDetails.DEFAULT_BASE_URL + "followClub";
                } else {
                    url = WebServiceDetails.DEFAULT_BASE_URL + "unfollowclub";

                }
                Log.e("follow", "" + jsonObject.toString());
                Log.e("follow", url);
                call_web_api = 2;
                new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_FOLLOW_UP,
                        true, url).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ExtraGroupInfoListHolder extends GroupPageAdapterActivity.GroupPageHolder {
        TextView g_name_joined;
        CardView cv_group_attributes;

        public ExtraGroupInfoListHolder(View itemView) {
            super(itemView);
            g_name_joined = (TextView) itemView.findViewById(R.id.tv_group_attribute);
            cv_group_attributes = (CardView) itemView.findViewById(R.id.group_attributes_card);

            cv_group_attributes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() == 1) {
                        Intent intent_temp = new Intent(v.getContext(), AboutGroupActivity.class);
                        intent_temp.putExtra("About", groupBean.getDescription());
                        v.getContext().startActivity(intent_temp);
                    } else if (getAdapterPosition() == 2) {
                        Intent intent_temp = new Intent(v.getContext(), GroupMembersByGroupActivity.class);
                        intent_temp.putExtra("clubId", groupBean.getClubId());
                        v.getContext().startActivity(intent_temp);
                    } else if (getAdapterPosition() == 3) {
                        Intent intent_temp = new Intent(v.getContext(), UpcomingEventsActivity.class);
                        intent_temp.putExtra("clubId", groupBean.getClubId());
                        v.getContext().startActivity(intent_temp);
                    } else if (getAdapterPosition() == 4) {
                        Intent intent_temp = new Intent(v.getContext(), NewsPostsByGroupActivity.class);
                        intent_temp.putExtra("clubId", groupBean.getClubId());
                        v.getContext().startActivity(intent_temp);
                    } else if (getAdapterPosition() == 5) {
                      /*  Intent intent_temp = new Intent(v.getContext(), PreviousEventsActivity.class);
                        v.getContext().startActivity(intent_temp);*/
                    } else {

                    }
                }
            });

        }
    }

    public class MemberConfirmationDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public TextView yes, no;
        Context context;


        public MemberConfirmationDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.member_confirmation_dialog);
            yes = (TextView) findViewById(R.id.btn_yes);
            no = (TextView) findViewById(R.id.btn_no);
            dialog_info = (TextView) findViewById(R.id.tv_dialog_info);
            dialog_info.setText(GroupPageAdapterActivity.this.g_name);
            if (yes_dialog_box_click_count % 2 != 0) {
                dialog_info.setText("Are you sure you want to leave the group?");
            } else {
                dialog_info.setText("Become a member of " + GroupPageAdapterActivity.this.g_name);
            }

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes: {
                    if (member_click_count == 1) {
                        member_click_count = 0;
                        holder1.iv_member.setBackgroundResource(R.drawable.group_member);
                    } else {
                        member_click_count = 1;
                        holder1.iv_member.setBackgroundResource(R.drawable.group_member_selected);
                    }
                    yes_dialog_box_click_count++;
                    webApiGroupJoin(v.getContext());
                    dismiss();
                    break;
                }
                case R.id.btn_no:
                    dismiss();
                    break;
                default:
                    break;
            }
        }

        public void webApiGroupJoin(Context context) {
            try {
                String pid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("club_id", groupBean.getClubId());
                jsonObject.put("from_pid", pid);
                call_web_api = 1;
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "joinClub";
                new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CLUB_MEMEBER_JOIN,
                        true, url).execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_CLUB_MEMEBER_JOIN: {
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_FOLLOW_UP: {

                        }
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else if (response_code == 204) {
                if (call_web_api == 1) {
                    if (member_click_count == 1) {
                        Toast.makeText(context, "Your request to join group has been sent to admin for approval", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Your request to leave group has been sent to admin for approval", Toast.LENGTH_LONG).show();
                    }
                }
                if (call_web_api == 2) {
                    if (followClick == 1) {
                        Toast.makeText(context, "following", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "unfollowing", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };
}

