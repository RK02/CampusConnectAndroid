package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by RK on 21-09-2015.
 */
public class CollegeMyFeedAdapter extends
        RecyclerView.Adapter<CollegeMyFeedAdapter.CollegeFeedViewHolder> {

    public static Typeface r_med, r_reg;
    private List<CampusFeedBean> myFeedList;
    int posi = 0;
    ArrayList<Boolean> flag_news = new ArrayList<Boolean>();

    // boolean[] flag_news;
    ArrayList<Boolean> flag_attending_clicked, flag_share_clicked;
    Context context;

    private DatabaseHandler dataBase;

    public CollegeMyFeedAdapter(List<CampusFeedBean> myFeedList, Context contect) {
        this.myFeedList = myFeedList;
        this.context = contect;
        //  flag_news = new boolean[getItemCount()];
        flag_attending_clicked = new ArrayList<Boolean>();
        flag_share_clicked = new ArrayList<Boolean>();
        dataBase = new DatabaseHandler(context);
    }

    @Override
    public int getItemCount() {
        return myFeedList.size();
    }

    @Override
    public void onBindViewHolder(CollegeFeedViewHolder college_feedViewHolder, int i) {

        try {
            flag_attending_clicked.add(i, false);
            flag_share_clicked.add(i, false);

            CampusFeedBean cf = myFeedList.get(i);
            college_feedViewHolder.event_title.setText(cf.getTitle());
            college_feedViewHolder.timestamp.setText("" + timeAgo(cf.getTimeStamp()));
            college_feedViewHolder.group_name.setText(cf.getClubname());
            String url = "http://admin.bookieboost.com/admin/images/2015-02-0116-17-50.jpg";
            try {
                String urll = cf.getPhoto();
                Log.e("url campusfeed", "" + urll);
                Picasso.with(context).load(cf.getPhoto()).into(college_feedViewHolder.event_photo);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(context).load(R.mipmap.default_image).into(college_feedViewHolder.event_photo);
            }
            try {
                String urll = cf.getClubphoto();
                Log.e("url campusfeed", "" + urll);
                Picasso.with(context).load(cf.getClubphoto()).into(college_feedViewHolder.group_icon);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(context).load(R.mipmap.default_image).into(college_feedViewHolder.group_icon);
            }

            //news
            if (cf.getStart_time() == null || cf.getStart_time().isEmpty()) {
                college_feedViewHolder.day.setVisibility(View.GONE);
                college_feedViewHolder.date_month.setVisibility(View.GONE);
                college_feedViewHolder.time.setVisibility(View.GONE);
                college_feedViewHolder.c_tag.setBackgroundResource(R.drawable.post_tag);
                flag_news.add(i, true);
//            dataBase.saveFeedInfo(cf.getPid(), "1");
                if (dataBase.getFeedIsLike(cf.getPid())) {
                    flag_attending_clicked.set(i, true);
                    college_feedViewHolder.going.setImageResource(R.mipmap.heart_selected);
                } else {
                    flag_attending_clicked.set(i, false);
                    college_feedViewHolder.going.setImageResource(R.mipmap.heart);
                }
            } else {
                flag_news.add(i, false);
                college_feedViewHolder.day.setVisibility(View.VISIBLE);
                college_feedViewHolder.date_month.setVisibility(View.VISIBLE);
                college_feedViewHolder.time.setVisibility(View.VISIBLE);
                college_feedViewHolder.c_tag.setBackgroundResource(R.drawable.tag);

                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = inFormat.parse(cf.getStart_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                try {
                    calendar.setTime(date);
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String goal = outFormat.format(date);
                    Log.e("day", goal);
                    //  Log.e("entry", cf.getStartDate());
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
                    String month = monthFormat.format(date);
                    Log.e("month", month);

                    if (goal.length() > 3) {
                        goal = goal.substring(0, 3);
                    } else {
                        goal = goal.substring(0, 3);
                    }
                    college_feedViewHolder.day.setText(goal.toUpperCase());

                    String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
                    Log.e("day of moth", day);

                    if (month.length() > 0) {
                        month = month.substring(0, 3);
                    }
                    college_feedViewHolder.date_month.setText("" + day + "" + month);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(cf.getStart_time());
              String time12=   _12HourSDF.format(_24HourDt);

                college_feedViewHolder.time.setText("" + time12);

                // college_feedViewHolder.date_month.setText(Date_Month[i]);
                //college_feedViewHolder.time.setText(Time_[i]);
                college_feedViewHolder.news_icon.setVisibility(View.GONE);
                if (dataBase.getFeedIsLike(cf.getPid())) {
                    flag_attending_clicked.set(i, true);
                    college_feedViewHolder.going.setImageResource(R.mipmap.going_selected);
                } else {
                    flag_attending_clicked.set(i, false);
                    college_feedViewHolder.going.setImageResource(R.mipmap.going);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public CollegeFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_college_feed, viewGroup, false);

        return new CollegeFeedViewHolder(itemView);
    }

    public class CollegeFeedViewHolder extends RecyclerView.ViewHolder {

        CardView college_feed;
        RelativeLayout c_tag;
        TextView event_title, group_name, timestamp, day, date_month, time;
        ImageView event_photo, news_icon, going, share;
        CircularImageView group_icon;

        public CollegeFeedViewHolder(View v) {
            super(v);

            r_med = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Medium.ttf");
            r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            college_feed = (CardView) v.findViewById(R.id.college_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            going = (ImageView) v.findViewById(R.id.iv_going);
            share = (ImageView) v.findViewById(R.id.iv_share);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            day = (TextView) v.findViewById(R.id.tv_day);
            date_month = (TextView) v.findViewById(R.id.tv_date_month);
            time = (TextView) v.findViewById(R.id.tv_time);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);
            c_tag = (RelativeLayout) v.findViewById(R.id.card_tag);

            event_title.setTypeface(r_med);
            group_name.setTypeface(r_reg);
            timestamp.setTypeface(r_reg);

            college_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                        posi = getPosition();

                        CampusFeedBean bean = myFeedList.get(posi);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("BEAN", bean);
                        bundle.putBoolean("FLAG_NEWS", flag_news.get(posi));
                        bundle.putBoolean("FLAG_SELECTED_SHARE", flag_share_clicked.get(posi));
                        bundle.putBoolean("FLAG_SELECTED_ATTEND/LIKE", flag_attending_clicked.get(posi));
                        intent_temp.putExtras(bundle);
                        context.startActivity(intent_temp);
                        //Create the bundle


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            going.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos_for_going = getAdapterPosition();
                    if (flag_attending_clicked.get(pos_for_going)) {
                        if (flag_news.get(pos_for_going)) {
                            going.setImageResource(R.mipmap.heart);
                            //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                            dataBase.saveFeedInfo(myFeedList.get(pos_for_going).getPid(), "0");
                        } else {
                            dataBase.saveFeedInfo(myFeedList.get(pos_for_going).getPid(), "0");
                            going.setImageResource(R.mipmap.going);
                            try {
                                String persoPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                                String pid = myFeedList.get(posi).getPid();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("eventId", pid);
                                jsonObject.put("from_pid", persoPid);
                                WebApiAttending(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        flag_attending_clicked.set(pos_for_going, false);
                    } else {
                        if (flag_news.get(pos_for_going)) {
                            //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                            going.setImageResource(R.mipmap.heart_selected);
                            dataBase.saveFeedInfo(myFeedList.get(pos_for_going).getPid(), "1");
                        } else {
                            dataBase.saveFeedInfo(myFeedList.get(pos_for_going).getPid(), "1");
                            going.setImageResource(R.mipmap.going_selected);
                            try {
                                String persoPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                                String pid = myFeedList.get(posi).getPid();
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("eventId", pid);
                                jsonObject.put("from_pid", persoPid);
                                WebApiAttending(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        flag_attending_clicked.set(pos_for_going, true);
//                        flag_attending_clicked[pos_for_going] = true;
                    }


                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for_share = getAdapterPosition();
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String shareBody = "Title : " + myFeedList.get(pos_for_share).getTitle() + "\n" + "Description : " + myFeedList.get(pos_for_share).getDescription() + " Download the app now from visit bit.ly/campusconnectandroid";

//                    String shareBody = myFeedList.get(pos_for_share).getTitle() + "/n" + myFeedList.get(posi).getDescription();
                    i.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    v.getContext().startActivity(Intent.createChooser(i, "Share via"));

                    if (flag_share_clicked.get(pos_for_share)) {
                        share.setAlpha((float) 0.5);
                        flag_share_clicked.set(pos_for_share, false);
                    } else {
                        share.setAlpha((float) 1);
                        flag_share_clicked.set(pos_for_share, true);
                    }
                }
            });
        }

        public void WebApiAttending(JSONObject jsonObject) {
            try {
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "attendEvent";
                new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_ATTENDING,
                        true, url).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String timeAgo(String createTimeStr) {
        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            Date d = simpleDateFormat.parse(createTimeStr);

//        java.util.Date d = f.parse(createTimeStr);
            String currentDateandTime = f.format(new Date());
            Date d1 = f.parse(currentDateandTime);
            long milliseconds = d.getTime();
            long millisecondsCurrent = d1.getTime();
            long diff_Milli = millisecondsCurrent - milliseconds;
            long minutes = Math.abs((millisecondsCurrent - milliseconds) / 60000);
            long seconds = Math.abs((diff_Milli) / 1000);
            long hours = Math.abs((minutes) / 60);
            long days = Math.abs((hours) / 24);
            long weeks = Math.abs((days) / 7);
            if (days > 7) {
                createTimeStr = String.valueOf(weeks);
                createTimeStr = createTimeStr + " Weeks Ago ";
            } else if (hours > 24) {
                createTimeStr = String.valueOf(days);
                createTimeStr = createTimeStr + " Days Ago ";
            } else if (minutes > 60) {
                createTimeStr = String.valueOf(hours);
                createTimeStr = createTimeStr + " Hours Ago ";
            } else {
                createTimeStr = String.valueOf(minutes);
                createTimeStr = createTimeStr + " Minutes Ago ";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return createTimeStr;
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_ATTENDING: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else if (response_code == 204) {

                Toast.makeText(context, "Attending", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}

