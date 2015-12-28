package com.campusconnect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.GroupPageAdapterActivity;
import com.campusconnect.adapter.GroupPage_infoActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupPageActivity extends ActionBarActivity {

    ImageButton search;
    ImageButton calender;
    ImageButton profile;
    ImageButton home;
    ImageButton notification;
    LinearLayout searchLine, calLine, notificationLine, profileLine, homeLine;

    RecyclerView group_page;
    String follow;
    DatabaseHandler db;
    private static final String LOG_TAG = "GroupPageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);

        search = (ImageButton) findViewById(R.id.ib_search);
        calender = (ImageButton) findViewById(R.id.ib_calendar);
        home = (ImageButton) findViewById(R.id.ib_home);
        profile = (ImageButton) findViewById(R.id.ib_profile);
        notification = (ImageButton) findViewById(R.id.ib_notification);
/*        first,second,third,fourth,fifth;*/
        searchLine = (LinearLayout) findViewById(R.id.lnr_search_line);
        calLine = (LinearLayout) findViewById(R.id.lnr_cal_line);
        notificationLine = (LinearLayout) findViewById(R.id.lnr_notification_line);
        profileLine = (LinearLayout) findViewById(R.id.lnr_profile_line);
        homeLine = (LinearLayout) findViewById(R.id.lnr_home_line);

        db = new DatabaseHandler(GroupPageActivity.this);
        group_page = (RecyclerView) findViewById(R.id.recycler_group_page);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        group_page.setLayoutManager(llm);
        group_page.setHasFixedSize(true);
        group_page.setItemAnimator(new DefaultItemAnimator());
        GroupBean bean = (GroupBean) getIntent().getSerializableExtra("BEAN");
        try {
            Bundle bundle = getIntent().getExtras();
            follow = bundle.getString("follow");
            if (follow == null) {
                follow = "0";
            }
        } catch (Exception e) {
        }
        if (bean != null) {
            String club_id = bean.getClubId();
            WebApiGetGroup(club_id);
        }
        String frag_location = MainActivity.frag_loc;
        UpdateUi(frag_location);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FRAGMENT", 1);
                intent_temp.putExtras(bundle);
                intent_temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_temp);
                finish();
            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FRAGMENT", 2);
                intent_temp.putExtras(bundle);
                intent_temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_temp);
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FRAGMENT", 3);
                intent_temp.putExtras(bundle);
                intent_temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_temp);
                finish();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FRAGMENT", 4);
                intent_temp.putExtras(bundle);
                intent_temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_temp);
                finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FRAGMENT", 5);
                intent_temp.putExtras(bundle);
                intent_temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_temp);
                finish();
            }
        });
    }
    private List<GroupPage_infoActivity> createList_group_page(int size) {

        List<GroupPage_infoActivity> result = new ArrayList<GroupPage_infoActivity>();
        for (int i = 1; i <= size; i++) {
            GroupPage_infoActivity ci = new GroupPage_infoActivity();
            result.add(ci);
       }
        return result;
    }

    public void WebApiGetGroup(String club_id) {
        if (NetworkAvailablity.hasInternetConnection(GroupPageActivity.this)) {
            try {
                String pid= SharedpreferenceUtility.getInstance(GroupPageActivity.this).getString(AppConstants.PERSON_PID);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("club_id", "" + club_id);
                jsonObject.put("pid",pid);
                //     jsonObject.put("pid", "" + pid);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "getClub";
                Log.e("getGroup", jsonObject.toString());
                Log.e("", url);

                new WebRequestTask(GroupPageActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_CLUB,
                        true, url).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(GroupPageActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
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
                        case WebServiceDetails.PID_GET_CLUB: {
                            try {
                                JSONObject grpJson = new JSONObject(strResponse);
                                String kind = grpJson.optString("kind");
                                String etag = grpJson.optString("etag");
                                String description = grpJson.optString("description");
                                String admin = grpJson.optString("admin");
                                String clubId = grpJson.optString("club_id");
                                String abb = grpJson.optString("abbreviation");
                                String name = grpJson.optString("name");
                                String photoUrl = grpJson.optString("photoUrl");
                                String followcount = grpJson.optString("followercount");
                                String member_count = grpJson.optString("membercount");
                                String isMember = grpJson.optString("isMember");
                                String isFollower = grpJson.optString("isFollower");

                                GroupBean bean = new GroupBean();
                                bean.setAbb(abb);
                                bean.setName(name);
                                bean.setAdmin(admin);
                                bean.setClubId(clubId);
                                if (isFollower.equalsIgnoreCase("Y")) {
                                    bean.setFollow("1");
                                    int i = db.updateFollow(clubId, "1");

                                } else {
                                    bean.setFollow("0");
                                    int i = db.updateFollow(clubId, "0");
                                }
                                if (isMember.equalsIgnoreCase("Y")) {
                                    bean.setIsMember("1");
                                } else {
                                    bean.setIsMember("0");
                                }
                                bean.setDescription(description);
                                bean.setPhotourl(photoUrl);
                                bean.setMemberCount(member_count);
                                bean.setFollowCount(followcount);
                                GroupPageAdapterActivity gp = new GroupPageAdapterActivity(createList_group_page(5), bean, GroupPageActivity.this);
                                group_page.setAdapter(gp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }
    };

    public void UpdateUi(String str) {

        if (str.equals("home")) {
            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home_selected);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.yello));

        } else if (str.equals("profile")) {

            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile_selected);
            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.yello));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("search")) {

            search.setImageResource(R.mipmap.search_selected);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.yello));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("calender")) {


            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar_selected);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.yello));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));

        } else if (str.equals("notification")) {

            search.setImageResource(R.mipmap.search);
            calender.setImageResource(R.mipmap.calendar);
            home.setImageResource(R.mipmap.home);
            notification.setImageResource(R.mipmap.notification_selected);
            profile.setImageResource(R.mipmap.profile);

            searchLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            calLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            notificationLine.setBackgroundColor(getResources().getColor(R.color.yello));
            profileLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
            homeLine.setBackgroundColor(getResources().getColor(R.color.carbonColor));
        }


    }
}
