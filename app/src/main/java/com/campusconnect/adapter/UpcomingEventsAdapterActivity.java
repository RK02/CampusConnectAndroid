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
 * Created by RK on 05/11/2015.
 */
public class UpcomingEventsAdapterActivity extends
        RecyclerView.Adapter<UpcomingEventsAdapterActivity.UpcomingEventsViewHolder> {

    private List<CampusFeedBean> UpcomingEventsList;
    ArrayList<Boolean> flag_attending_clicked;
    boolean[] flag_share_clicked;
    int posi = 0;
    int going_click_count = 0;
    int share_click_count = 0;
    Context context;
    public static int attending = 1;
    private DatabaseHandler dataBase;

    public UpcomingEventsAdapterActivity(List<CampusFeedBean> UpcomingEventsList, Context context) {
        this.UpcomingEventsList = UpcomingEventsList;
        this.context = context;
        flag_attending_clicked = new ArrayList<Boolean>();
        flag_share_clicked = new boolean[getItemCount()];
        dataBase = new DatabaseHandler(context);
    }

    @Override
    public int getItemCount() {
        return UpcomingEventsList.size();
    }

    @Override
    public void onBindViewHolder(UpcomingEventsViewHolder upcoming_eventsViewHolder, int i) {
        CampusFeedBean ci = UpcomingEventsList.get(i);
        flag_attending_clicked.add(i, false);
        try {
            upcoming_eventsViewHolder.event_title.setText(ci.getTitle());
            upcoming_eventsViewHolder.group_name.setText(ci.getClubAbbreviation());
            upcoming_eventsViewHolder.timestamp.setText(timeAgo(ci.getTimeStamp()));
            if (dataBase.getFeedIsLike(ci.getPid())) {
                flag_attending_clicked.set(i, true);
                upcoming_eventsViewHolder.going.setImageResource(R.mipmap.going_selected);
            } else {
                flag_attending_clicked.set(i, false);
                upcoming_eventsViewHolder.going.setImageResource(R.mipmap.going);
            }
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = null;

            _24HourDt = _24HourSDF.parse(ci.getStart_time());
            String time12 = _12HourSDF.format(_24HourDt);
            upcoming_eventsViewHolder.time.setText("" + time12);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        upcoming_eventsViewHolder.group_icon.setImageResource(R.mipmap.default_image);
        String url = "http://admin.bookieboost.com/admin/images/2015-02-0116-17-50.jpg";
        try {
            Picasso.with(context).load(ci.getPhoto()).into(upcoming_eventsViewHolder.event_photo);

        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(upcoming_eventsViewHolder.event_photo);
        }
        try {
            Picasso.with(context).load(ci.getClubphoto()).into(upcoming_eventsViewHolder.group_icon);

        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(upcoming_eventsViewHolder.event_photo);
        }


        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = inFormat.parse(ci.getTimeStamp());
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTime(date);

            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            Log.e("day", goal);
            //  Log.e("entry", cf.getStartDate());
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
            String month = monthFormat.format(date);
            Log.e("month", month);

            SimpleDateFormat dateformate = new SimpleDateFormat("dd");
            String dayFormate = monthFormat.format(date);
            Log.e("day", "" + calendar.get(Calendar.DAY_OF_MONTH));
            if (goal.length() > 3) {
                goal = goal.substring(0, 3);
            } else {
            }
            upcoming_eventsViewHolder.day.setText("" + goal.toUpperCase());
            String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
            if (month.length() > 0) {
                month = month.substring(0, 3);
            } else {
            }
            Log.e(ci.getTitle(), day + "" + month);
            upcoming_eventsViewHolder.date_month.setText("" + day + "" + month);

        } catch (ParseException e) {
            e.printStackTrace();
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
            java.util.Date d1 = f.parse(currentDateandTime);
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


    @Override
    public UpcomingEventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_my_feed, viewGroup, false);
        return new UpcomingEventsViewHolder(itemView);
    }

    public class UpcomingEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView my_feed;
        TextView event_title;
        TextView group_name;
        TextView timestamp;
        TextView day;
        TextView date_month;
        TextView time;
        ImageView event_photo;
        ImageView news_icon;
        ImageView going;
        ImageView share;
        CircularImageView group_icon;

        public UpcomingEventsViewHolder(View v) {
            super(v);

            Typeface r_med = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Medium.ttf");
            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            my_feed = (CardView) v.findViewById(R.id.college_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            going = (ImageView) v.findViewById(R.id.iv_going);
            share = (ImageView) v.findViewById(R.id.iv_share);
            day = (TextView) v.findViewById(R.id.tv_day);
            date_month = (TextView) v.findViewById(R.id.tv_date_month);
            time = (TextView) v.findViewById(R.id.tv_time);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);

            event_title.setTypeface(r_med);
            group_name.setTypeface(r_reg);
            timestamp.setTypeface(r_reg);

            my_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for = getAdapterPosition();
                    Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                    Bundle bundle = new Bundle();
                    CampusFeedBean bean = UpcomingEventsList.get(pos_for);
                    bundle.putSerializable("BEAN", bean);
                    bundle.putBoolean("FLAG_SELECTED_SHARE", flag_share_clicked[pos_for]);
                    bundle.putBoolean("FLAG_SELECTED_ATTEND/LIKE", flag_attending_clicked.get(pos_for));
                    intent_temp.putExtras(bundle);
                    context.startActivity(intent_temp);
                }
            });
            going.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for_going = getAdapterPosition();
                    if (flag_attending_clicked.get(pos_for_going)) {
                        going.setImageResource(R.mipmap.going);
                        try {
                            attending = 2;
                            dataBase.saveFeedInfo(UpcomingEventsList.get(pos_for_going).getPid(), "0");
                            String persoPid = SharedpreferenceUtility.getInstance(v.getContext()).getString(AppConstants.PERSON_PID);
                            String pid = UpcomingEventsList.get(pos_for_going).getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject, v.getContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        flag_attending_clicked.set(pos_for_going, false);
                    } else {
                        going.setImageResource(R.mipmap.going_selected);
                        attending = 1;
                        flag_attending_clicked.set(pos_for_going, true);
                        try {
                            dataBase.saveFeedInfo(UpcomingEventsList.get(pos_for_going).getPid(), "1");
                            String persoPid = SharedpreferenceUtility.getInstance(v.getContext()).getString(AppConstants.PERSON_PID);
                            String pid = UpcomingEventsList.get(posi).getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject, v.getContext());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for_share = getAdapterPosition();
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");

                    String shareBody = "Title : " + UpcomingEventsList.get(pos_for_share).getTitle() + "\n" + "Description : " + UpcomingEventsList.get(posi).getDescription() + " for more info visit http://campusconnect.cc";
                    i.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    v.getContext().startActivity(Intent.createChooser(i, "Share via"));

                    if (flag_share_clicked[pos_for_share]) {
                        share.setAlpha((float) 0.5);
                        flag_share_clicked[pos_for_share] = false;
                    } else {
                        share.setAlpha((float) 1);
                        flag_share_clicked[pos_for_share] = true;
                    }
                }
            });


        }


        @Override
        public void onClick(View view) {

        }

        public void WebApiAttending(JSONObject jsonObject, Context context) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "attendEvent";
            Log.e("", jsonObject.toString());
            Log.e("", url);
            new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_ATTENDING,
                    true, url).execute();
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
            }
            if (response_code == 204) {
                //  if (!flag_news.get(posi)) {


                if (attending == 1) {
                    attending = 2;
                    Toast.makeText(context, "Attending", Toast.LENGTH_LONG).show();
                } else if (attending == 2) {
                    attending = 1;
                    Toast.makeText(context, "Not Attending", Toast.LENGTH_LONG).show();
                }
                //  } else {
                  /*  if (liking == 1) {
                        liking = 2;
                        Toast.makeText(context, "Like", Toast.LENGTH_LONG).show();
                    } else if (liking == 2) {
                        liking = 1;
                        Toast.makeText(context, "Unlike", Toast.LENGTH_LONG).show();
                    }
                }*/

            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}

