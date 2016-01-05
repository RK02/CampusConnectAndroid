package com.campusconnect.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.TypefaceSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.CustomTypefaceSpan;
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
 * Created by RK on 22-09-2015.
 */
public class InEventActivity extends AppCompatActivity {

    Typeface r_lig, r_reg, r_med;
    TypefaceSpan robotoRegularSpan_for_attendees;
    SpannableStringBuilder attendees_text;
    int no_of_attendees;
    Boolean flag_news, flag_selected_share, flag_selected_attend_like;
    Boolean flag_attended_clicked = false, flag_share_clicked = false;
    ImageView event_photo, location_icon, going, share;
    TextView e_name, e_time, e_date, g_name, v_name, e_description, attendees_count, tv_header;
    CircularImageView g_icon;
    static int attending = 1;
    static int liking = 1;
    CampusFeedBean bean;
    private DatabaseHandler dataBase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_event);

        dataBase = new DatabaseHandler(InEventActivity.this);

        r_lig = Typeface.createFromAsset(getAssets(), "font/Roboto_Light.ttf");
        r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");
        robotoRegularSpan_for_attendees = new CustomTypefaceSpan("", r_reg);

        event_photo = (ImageView) findViewById(R.id.iv_event);
        location_icon = (ImageView) findViewById(R.id.iv_location);
        going = (ImageView) findViewById(R.id.heart_going);
        share = (ImageView) findViewById(R.id.iv_share);
        e_name = (TextView) findViewById(R.id.tv_event_name);
        e_time = (TextView) findViewById(R.id.tv_time);
        e_date = (TextView) findViewById(R.id.tv_date);
        g_name = (TextView) findViewById(R.id.tv_group_name);
        e_description = (TextView) findViewById(R.id.tv_event_description);
        v_name = (TextView) findViewById(R.id.tv_venue);
        tv_header = (TextView) findViewById(R.id.tv_header);
        g_icon = (CircularImageView) findViewById(R.id.group_icon);
        attendees_count = (TextView) findViewById(R.id.tv_attendees_count);

        no_of_attendees = 255; //Value has to be taken from the server
        attendees_text = new SpannableStringBuilder("+" + no_of_attendees + attending);
        attendees_text.setSpan(robotoRegularSpan_for_attendees, 0, Integer.toString(no_of_attendees).length() + 1, 0);
        attendees_count.setText(attendees_text);

        e_name.setTypeface(r_med);
        e_time.setTypeface(r_reg);
        e_date.setTypeface(r_reg);
        e_description.setTypeface(r_reg);
        g_name.setTypeface(r_reg);
        v_name.setTypeface(r_reg);
        attendees_count.setTypeface(r_lig);

        Bundle bundle = getIntent().getExtras();
        flag_news = bundle.getBoolean("FLAG_NEWS");
        flag_selected_share = bundle.getBoolean("FLAG_SELECTED_SHARE");

        flag_selected_attend_like = bundle.getBoolean("FLAG_SELECTED_ATTEND/LIKE");

        if (flag_news) {
            location_icon.setVisibility(View.GONE);
            going.setImageResource(R.drawable.selector_heart);
        } else {
            location_icon.setVisibility(View.VISIBLE);
            going.setImageResource(R.mipmap.going);
        }

        bean = (CampusFeedBean) getIntent().getSerializableExtra("BEAN");
        if (bean != null) {
            bean.getClubid();
            bean.getCollegeId();
            bean.getViews();
            g_name.setText("" + bean.getClubAbbreviation());
            e_name.setText("" + bean.getTitle());
            e_description.setText("" + bean.getDescription());
            Linkify.addLinks(e_description, Linkify.ALL);

            if (bean.getStart_date() == null || bean.getStart_date().isEmpty()) {
                attendees_count.setVisibility(View.INVISIBLE);
                e_time.setVisibility(View.INVISIBLE);
                tv_header.setText("News");
                v_name.setVisibility(View.INVISIBLE);
            } else {

                v_name.setText("" + bean.getVenue());
                int attendies = bean.getAttendees().size();
                attendees_count.setText("+" + attendies + " attending");
                try {
                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                    Date _24HourDt = null;
                    _24HourDt = _24HourSDF.parse(bean.getStart_time());
                    String time12 = _12HourSDF.format(_24HourDt);
                    e_time.setText("" + time12);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            try {
                Picasso.with(InEventActivity.this).load(bean.getPhoto()).into(event_photo);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(InEventActivity.this).load(R.mipmap.default_image).into(event_photo);
            }
            try {
                Picasso.with(InEventActivity.this).load(bean.getClubphoto()).into(g_icon);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(InEventActivity.this).load(R.mipmap.default_image).into(event_photo);
            }
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = inFormat.parse(bean.getStart_date());
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.setTime(date);


                SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                String goal = outFormat.format(date);
                Log.e("day", goal);
                //  Log.e("entry", cf.getStartDate());
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMMMMM");
                String month = monthFormat.format(date);
                Log.e("month", month);

                SimpleDateFormat dateformate = new SimpleDateFormat("dd");
                String dayFormate = monthFormat.format(date);
                Log.e("day", "" + calendar.get(Calendar.DAY_OF_MONTH));
                String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
                e_date.setText("" + goal + " " + day + " " + month);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
//Extract the data…
        if (flag_selected_share) {
            share.setAlpha((float) 1);
            flag_share_clicked = true;
        } else {
            share.setAlpha((float) 0.5);
            flag_share_clicked = false;
        }
        if (flag_selected_attend_like) {
            if (flag_news)
                going.setImageResource(R.mipmap.heart_selected);
            else
                going.setImageResource(R.mipmap.going_selected);
            flag_attended_clicked = true;
        } else {
            if (flag_news)
                going.setImageResource(R.mipmap.heart);
            else
                going.setImageResource(R.mipmap.going);
            flag_attended_clicked = false;
        }
        going.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag_attended_clicked) {
                    if (flag_news) {
                        going.setImageResource(R.mipmap.heart);
                        dataBase.saveFeedInfo(bean.getPid(), "0");
                        //Toast.makeText(InEventActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        going.setImageResource(R.mipmap.going);
                        dataBase.saveFeedInfo(bean.getPid(), "0");

                        try {
                            String persoPid = SharedpreferenceUtility.getInstance(InEventActivity.this).getString(AppConstants.PERSON_PID);
                            String pid = bean.getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    flag_attended_clicked = false;
                } else {
                    if (flag_news) {
                        dataBase.saveFeedInfo(bean.getPid(), "1");
                        going.setImageResource(R.mipmap.heart_selected);
                        //Toast.makeText(InEventActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        dataBase.saveFeedInfo(bean.getPid(), "1");
                        going.setImageResource(R.mipmap.going_selected);
                        try {
                            String persoPid = SharedpreferenceUtility.getInstance(InEventActivity.this).getString(AppConstants.PERSON_PID);
                            String pid = bean.getPid();
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("eventId", pid);
                            jsonObject.put("from_pid", persoPid);
                            WebApiAttending(jsonObject);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    flag_attended_clicked = true;
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                String shareBody = bean.getTitle() + "\n" + bean.getDescription();
                i.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(i, "Share via"));

                if (flag_share_clicked) {
                    share.setAlpha((float) 0.5);
                    flag_share_clicked = false;
                } else {
                    share.setAlpha((float) 1);
                    flag_share_clicked = true;
                }
            }
        });


        event_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), EventPhotoFullScreenActivity.class);
                intent_temp.putExtra("PICTURE", bean.getPhoto());
                startActivity(intent_temp);
            }
        });


    }


    public void WebApiAttending(JSONObject jsonObject) {

        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "attendEvent";
        Log.e("", jsonObject.toString());
        Log.e("", url);
        new WebRequestTask(InEventActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_ATTENDING,
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
                    Toast.makeText(InEventActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            }
            if (response_code == 204) {
                if (attending == 1) {
                    attending = 2;
                    Toast.makeText(InEventActivity.this, "Attending", Toast.LENGTH_LONG).show();
                } else if (attending == 2) {
                    attending = 1;
                    Toast.makeText(InEventActivity.this, "Not Attending", Toast.LENGTH_LONG).show();
                }



            } else {
                Toast.makeText(InEventActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}
