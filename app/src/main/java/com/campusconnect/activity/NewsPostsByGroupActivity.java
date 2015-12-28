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
import com.campusconnect.adapter.NewsPostsByGroupAdapterActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.supportClasses.NewsPostsByGroup_infoActivity;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 06/11/2015.
 */
public class NewsPostsByGroupActivity extends ActionBarActivity {

    ImageButton search;
    ImageButton calender;
    ImageButton profile;
    ImageButton home;
    ImageButton notification;
    LinearLayout searchLine, calLine, notificationLine, profileLine, homeLine;

    RecyclerView news_posts_by_group;
    LinearLayout close;
    Typeface r_med;
    TextView news_posts_text;
    NewsPostsByGroupAdapterActivity newsPostsByGroupAdapterActivity;
    ArrayList<CampusFeedBean> postNewsList = new ArrayList<CampusFeedBean>();

    @Override
    protected void onResume() {
        super.onResume();
        if (newsPostsByGroupAdapterActivity != null) {
            newsPostsByGroupAdapterActivity.notifyDataSetChanged();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_posts_by_group);

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

        String clubid = getIntent().getStringExtra("clubId");
        WebApiGetPostNews(clubid);


        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        news_posts_text = (TextView) findViewById(R.id.tv_news_posts_text);
        news_posts_text.setTypeface(r_med);
        news_posts_text.setText("News Posts");
        news_posts_by_group = (RecyclerView) findViewById(R.id.rv_news_posts_by_group);
        news_posts_by_group.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        news_posts_by_group.setLayoutManager(llm);
        news_posts_by_group.setItemAnimator(new DefaultItemAnimator());

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

    private List<NewsPostsByGroup_infoActivity> createList_newsPostsByGroupAdapterActivity(int size) {
        List<NewsPostsByGroup_infoActivity> result = new ArrayList<NewsPostsByGroup_infoActivity>();
        for (int i = 1; i <= size; i++) {
            NewsPostsByGroup_infoActivity ci = new NewsPostsByGroup_infoActivity();
            result.add(ci);
        }

        return result;
    }

    public void WebApiGetPostNews(String clubId) {
        try {
            JSONObject jsonObject = new JSONObject();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getPosts?clubId=" + clubId;
            new WebRequestTask(NewsPostsByGroupActivity.this, param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_GET_Events,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
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
                        case WebServiceDetails.PID_GET_Events: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("items")) {
                                    JSONArray array = jsonObject.getJSONArray("items");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {

                                            JSONObject innerObj = array.getJSONObject(i);
                                            CampusFeedBean bean = new CampusFeedBean();
                                            String description = innerObj.optString("description");
                                            String pid = innerObj.optString("from_pid");
                                            String photo = innerObj.optString("photoUrl");
                                            //TODO remvpe comment here
                                            String clubname = innerObj.optString("club_name");
                                            String likes = innerObj.optString("likes");
                                            String views = innerObj.optString("views");
                                            String title = innerObj.optString("title");
                                            String time = innerObj.optString("time");
                                            String date = innerObj.optString("date");
                                            String clubphotoUrl = innerObj.optString("clubphotoUrl");
                                            String kind = innerObj.optString("kind");
                                            String timeStamp = innerObj.optString("timestamp");
                                            String abbreviation=innerObj.optString("clubabbreviation");

                                            ArrayList<String> likesList = new ArrayList<>();
                                            try {

                                                if (innerObj.has("likers")) {
                                                    JSONArray attArray = innerObj.getJSONArray("likers");
                                                    if (attArray.length() > 0) {
                                                        for (int j = 0; j < attArray.length(); j++) {
                                                            String likersstr = attArray.getString(j);
                                                            likesList.add(likersstr);
                                                        }
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            //TOdo venue is missing
                                            //Creator name
                                            bean.setDescription(description);
                                            bean.setPid(pid);
                                            bean.setViews(views);
                                            bean.setPhoto(photo);
                                            bean.setLikes(likes);
                                            bean.setViews(views);
                                            bean.setTitle(title);
                                            bean.setTime(time);
                                            bean.setDate(date);
                                            bean.setKind(kind);
                                            bean.setClubphoto("" + clubphotoUrl);
                                            bean.setLikers("" + likesList.toString());
                                            bean.setClubname(clubname);
                                            bean.setTimeStamp(timeStamp);
                                            bean.setVenue(" ");
                                            bean.setClubAbbreviation(abbreviation);


                                            postNewsList.add(bean);
                                        }

                                        newsPostsByGroupAdapterActivity = new NewsPostsByGroupAdapterActivity(
                                                postNewsList, NewsPostsByGroupActivity.this);
                                        news_posts_by_group.setAdapter(newsPostsByGroupAdapterActivity);
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
                    Toast.makeText(NewsPostsByGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(NewsPostsByGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
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


