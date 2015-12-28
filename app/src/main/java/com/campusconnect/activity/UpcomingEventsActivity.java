package com.campusconnect.activity;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.UpcomingEventsAdapterActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.supportClasses.UpcomingEvents_infoActivity;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 05/11/2015.
 */
public class UpcomingEventsActivity extends ActionBarActivity {

    ImageButton search;
    ImageButton calender;
    ImageButton profile;
    ImageButton home;
    ImageButton notification;
    LinearLayout searchLine, calLine, notificationLine, profileLine, homeLine;

    RecyclerView upcoming_events;
    LinearLayout close;
    Typeface r_med;
    TextView upcoming_events_text;
    ArrayList<CampusFeedBean> eventList= new ArrayList<CampusFeedBean>();
    UpcomingEventsAdapterActivity upcomingEventsAdapterActivity;
    @Override
    protected void onResume() {
        super.onResume();
        if(upcomingEventsAdapterActivity!=null)
        upcomingEventsAdapterActivity.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

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


        String clubId= getIntent().getStringExtra("clubId");
        WebApiGetUpComingEvent(clubId);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        upcoming_events_text = (TextView) findViewById(R.id.tv_upcoming_events);
        upcoming_events_text.setTypeface(r_med);

        upcoming_events = (RecyclerView) findViewById(R.id.rv_upcoming_events);
        upcoming_events.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        upcoming_events.setLayoutManager(llm);
        upcoming_events.setItemAnimator(new DefaultItemAnimator());


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
    public void WebApiGetUpComingEvent(String clubId) {
        try {
            JSONObject jsonObject = new JSONObject();
            String pid = SharedpreferenceUtility.getInstance(UpcomingEventsActivity.this).getString(AppConstants.PERSON_PID);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getEvents?clubId="+clubId;
            new WebRequestTask(UpcomingEventsActivity.this, param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_GET_Events,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private List<UpcomingEvents_infoActivity> createList_upcomingEventsAdapterActivity(int size) {
        List<UpcomingEvents_infoActivity> result = new ArrayList<UpcomingEvents_infoActivity>();
        for (int i = 1; i <= size; i++) {
            UpcomingEvents_infoActivity ci = new UpcomingEvents_infoActivity();
            result.add(ci);
        }

        return result;
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GET_Events: {
                            try {

                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("items")) {

                                    JSONArray array = jsonObject.getJSONArray("items");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {

                                            JSONObject innerObj = array.getJSONObject(i);
                                            CampusFeedBean bean = new CampusFeedBean();

                                            String eventCreator = innerObj.optString("event_creator");
                                            String description = innerObj.optString("description");
                                            String views = innerObj.optString("views");
                                            String photo = innerObj.optString("photoUrl");

                                            //TODO remvpe comment here

                                           // String clubid = innerObj.optString("club_id");
                                            String eventid = innerObj.optString("eventId");
                                            String timeStamp = innerObj.optString("timestamp");
                                            String title = innerObj.optString("title");

                                            String collegeId = innerObj.optString("collegeId");
                                            String kind = innerObj.optString("kind");
                                            String endDate = innerObj.optString("end_date");
                                            String startDate = innerObj.optString("start_date");

                                            String startTime = innerObj.optString("start_time");
                                            String endTime = innerObj.optString("end_time");
                                            String venue = innerObj.optString("venue");
                                            String clubphoto = innerObj.optString("clubphotoUrl");


                                            String date= innerObj.optString("time");
                                            String time=innerObj.optString("date");
                                            String liker = innerObj.optString("views");
                                            String complete = innerObj.optString("completed");

                                            String alumni=innerObj.optString("isAlumni");
                                            String clubName=innerObj.optString("club_name");
                                            String abbreviation=innerObj.optString("clubabbreviation");
                                            ArrayList<String> attendList = new ArrayList<>();
                                           try {

                                               if (innerObj.has("attendees")) {
                                                   JSONArray attArray = innerObj.getJSONArray("attendees");
                                                   if (attArray.length() > 0) {
                                                       for (int j = 0; j < attArray.length(); j++) {
                                                           String attendStr = attArray.getString(j);
                                                           attendList.add(attendStr);
                                                       }
                                                   }
                                               }
                                           }catch (Exception ex){
                                               ex.printStackTrace();
                                           }

                                            bean.setEventCreator(eventCreator);
                                            bean.setDescription(description);
                                            bean.setViews(views);
                                        //    bean.setClubid(clubid);
                                            bean.setPid(eventid);

                                            bean.setPhoto(photo);
                                            bean.setTimeStamp(timeStamp);
                                            bean.setTitle(title);
                                            bean.setCollegeId(collegeId);

                                            bean.setKind(kind);
                                            bean.setAttendees(attendList);
                                            bean.setEnd_date("" + endDate);
                                            bean.setStart_date("" + startDate);

                                            bean.setStart_time("" + startTime);
                                            bean.setEnd_time("" + endTime);
                                            bean.setVenue("" + venue);
                                            bean.setClubphoto("" + clubphoto);

                                            bean.setLikers("" + liker);
                                            bean.setCompleted("" + complete);
                                            bean.setClubname(clubName);
                                            bean.setClubid(clubName);
                                            bean.setClubAbbreviation(abbreviation);
                                            ArrayList<String> tagList = new ArrayList<>();
                                            try {

                                                if (innerObj.has("tags")) {

                                                    JSONArray tagArray = innerObj.getJSONArray("tags");
                                                    if (tagArray.length() > 0) {
                                                        for (int l = 0; l < tagArray.length(); l++) {
                                                            String tag = tagArray.getString(l);
                                                            tagList.add(tag);
                                                        }

                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            bean.setTag(tagList);
                                            bean.setTime(time);
                                            bean.setDate(date);
                                            bean.setAlumni(alumni);

                                            eventList.add(bean);
                                        }
                                     upcomingEventsAdapterActivity = new UpcomingEventsAdapterActivity(
                                                eventList,UpcomingEventsActivity.this);
                                        upcoming_events.setAdapter(upcomingEventsAdapterActivity);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(UpcomingEventsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(UpcomingEventsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
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

