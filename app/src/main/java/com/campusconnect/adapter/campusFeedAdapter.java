package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
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
 * Created by canopus on 11/12/15.
 */
public class campusFeedAdapter extends BaseAdapter {
    ArrayList<CampusFeedBean> listItems;

    public static Typeface r_med, r_reg;
    private List<CampusFeedBean> CollegeFeedList;
    List<Boolean> flag_news = new ArrayList<Boolean>();
    List<Boolean> flag_attending_clicked = new ArrayList<Boolean>();
    List<Boolean> flag_share_clicked = new ArrayList<Boolean>();
    public static int attending = 1;
    public static int liking = 1;
    //boolean[] flag_news = new boolean[5];
    //boolean[] flag_attending_clicked = new boolean[5];
    //boolean[] flag_share_clicked = new boolean[5];

    int posi = 0;
    int going_click_count = 0;
    int share_click_count = 0;
    Context context;
    ViewHolder holder = null;
    LayoutInflater inflater;

    class ViewHolder {
        CardView college_feed;
        TextView event_title, group_name, timestamp, day, date_month, time;
        ImageView event_photo, news_icon, going, share;
        CircularImageView group_icon;
    }


        public campusFeedAdapter(ArrayList<CampusFeedBean> items,Context context){
        this.context = context;
        this.listItems = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final CampusFeedBean cf = listItems.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_card_layout_college_feed, null, false);
            holder.college_feed = (CardView) convertView.findViewById(R.id.college_feed_card);
            holder.event_title = (TextView) convertView.findViewById(R.id.tv_event);
            holder.group_name = (TextView) convertView.findViewById(R.id.tv_group);
            holder.timestamp = (TextView) convertView.findViewById(R.id.tv_timestamp);
            holder.event_photo = (ImageView) convertView.findViewById(R.id.iv_event_photo);
            holder.going = (ImageView) convertView.findViewById(R.id.iv_going);
            holder.share = (ImageView) convertView.findViewById(R.id.iv_share);
            holder.news_icon = (ImageView) convertView.findViewById(R.id.iv_news_icon);
            holder.day = (TextView) convertView.findViewById(R.id.tv_day);
            holder.date_month = (TextView) convertView.findViewById(R.id.tv_date_month);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.group_icon = (CircularImageView) convertView.findViewById(R.id.group_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        flag_attending_clicked.add(position, false);
        flag_share_clicked.add(position, false);
        holder.event_title.setText(cf.getTitle());
        holder.timestamp.setText("" + timeAgo(cf.getTimeStamp()));
        holder.group_name.setText(cf.getClubname());

        String url = "http://admin.bookieboost.com/admin/images/2015-02-0116-17-50.jpg";
        try {
            String urll = cf.getClubphoto();
            Log.e("url campusfeed", "" + urll);
            if (cf.getClubphoto().equalsIgnoreCase("None")) {
                Picasso.with(context).load(R.mipmap.default_image).into(holder.event_photo);
            } else {
                Picasso.with(context).load(cf.getClubphoto()).into(holder.event_photo);
            }
        } catch (Exception e) {

            Picasso.with(context).load(R.mipmap.default_image).into(holder.event_photo);
        }
        Picasso.with(context).load(url).into(holder.group_icon);

        String title = cf.getTitle();
        //news
        if (cf.getAttendees() == null || cf.getAttendees().size() == 0) {
            holder.day.setVisibility(View.GONE);
            holder.date_month.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.news_icon.setVisibility(View.VISIBLE);

            flag_news.add(position, true);
            holder.going.setImageResource(R.mipmap.heart);

        } else {
            flag_news.add(position, false);
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inFormat.parse(cf.getTimeStamp());
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
                holder.day.setText("" + goal.toUpperCase());
                String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
                if (month.length() > 0) {
                    month = month.substring(0, 3);
                } else {
                }
                Log.e(cf.getTitle(), day + "" + month);
                holder.date_month.setText("" + day + "" + month);
                holder.time.setText("" + cf.getEnd_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // holder.date_month.setText(Date_Month[i]);
            //holder.time.setText(Time_[i]);
            holder.news_icon.setVisibility(View.GONE);
            holder.going.setImageResource(R.mipmap.going);
        }


        holder.college_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posi = position;
                Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                Bundle bundle = new Bundle();
                CampusFeedBean bean = CollegeFeedList.get(posi);
                bundle.putSerializable("BEAN", bean);
                bundle.putBoolean("FLAG_NEWS", flag_news.get(posi));
                bundle.putBoolean("FLAG_SELECTED_SHARE", flag_share_clicked.get(posi));
                bundle.putBoolean("FLAG_SELECTED_ATTEND/LIKE", flag_attending_clicked.get(posi));
                intent_temp.putExtras(bundle);
                context.startActivity(intent_temp);
                String pid = bean.getPid();
            }
        });

        holder.going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int pos_for_going = position;
                if (flag_attending_clicked.get(pos_for_going)) {
                    if (flag_news.get(pos_for_going)) {
                        holder.going.setImageResource(R.mipmap.heart);
                        //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.going.setImageResource(R.mipmap.going);

                        try {
                            String persoPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                            String pid = CollegeFeedList.get(posi).getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    flag_attending_clicked.add(pos_for_going, false);
                } else {
                    if (flag_news.get(pos_for_going)) {
                        holder.going.setImageResource(R.mipmap.heart_selected);
                        //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        holder.going.setImageResource(R.mipmap.going_selected);

                        try {
                            String persoPid = SharedpreferenceUtility.getInstance(context).getString(AppConstants.PERSON_PID);
                            String pid = CollegeFeedList.get(posi).getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    flag_attending_clicked.add(pos_for_going, true);
                }
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos_for_share = position;
                if (flag_share_clicked.get(pos_for_share)) {
                    holder.share.setAlpha((float) 0.5);
                    flag_share_clicked.add(pos_for_share, false);
                } else {
                    holder.share.setAlpha((float) 1);
                    flag_share_clicked.add(pos_for_share, true);
                }
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void WebApiAttending(JSONObject jsonObject) {

        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "attendEvent";
        Log.e("", jsonObject.toString());
        Log.e("", url);
        new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_ATTENDING,
                true, url).execute();
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

            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

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


}
