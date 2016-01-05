package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.database.DatabaseHandler;
import com.campusconnect.utility.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by RK on 06/11/2015.
 */
public class NewsPostsByGroupAdapterActivity extends
        RecyclerView.Adapter<NewsPostsByGroupAdapterActivity.NewsPostsByGroupViewHolder> {

    private List<CampusFeedBean> NewsPostsByGroupList;
    ArrayList<Boolean> flag_attending_clicked, flag_share_clicked;
    int posi = 0;
    int going_click_count = 0;
    int share_click_count = 0;
    Context context;
    private DatabaseHandler dataBase;

    public NewsPostsByGroupAdapterActivity(List<CampusFeedBean> NewsPostsByGroupList, Context context) {
        this.NewsPostsByGroupList = NewsPostsByGroupList;
        flag_attending_clicked = new ArrayList<Boolean>();
        flag_share_clicked = new ArrayList<Boolean>();
        this.context = context;
        dataBase = new DatabaseHandler(context);
    }

    @Override
    public int getItemCount() {
        return NewsPostsByGroupList.size();
    }


    @Override
    public void onBindViewHolder(NewsPostsByGroupViewHolder news_posts_by_groupViewHolder, int i) {
        CampusFeedBean ci = NewsPostsByGroupList.get(i);
        flag_attending_clicked.add(i, false);
        flag_share_clicked.add(i, false);
        news_posts_by_groupViewHolder.timestamp.setText(timeAgo(ci.getTimeStamp()));
        news_posts_by_groupViewHolder.event_title.setText(ci.getTitle());
        news_posts_by_groupViewHolder.group_name.setText(ci.getClubAbbreviation()) ;
        try {
            Picasso.with(context).load(ci.getPhoto()).into(news_posts_by_groupViewHolder.event_photo);

        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(news_posts_by_groupViewHolder.event_photo);
        }
        try {
            Picasso.with(context).load(ci.getClubphoto()).into(news_posts_by_groupViewHolder.group_icon);
        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(news_posts_by_groupViewHolder.group_icon);
        }
        if (dataBase.getFeedIsLike(ci.getPid())) {
            flag_attending_clicked.set(i, true);
            news_posts_by_groupViewHolder.like.setImageResource(R.mipmap.heart_selected);
        } else {
            flag_attending_clicked.set(i, false);
            news_posts_by_groupViewHolder.like.setImageResource(R.mipmap.heart);
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
    public NewsPostsByGroupViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_news_posts, viewGroup, false);

        return new NewsPostsByGroupViewHolder(itemView);
    }

    public class NewsPostsByGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView news_feed;
        TextView event_title, group_name, timestamp;
        ImageView event_photo, news_icon, like, share;
        CircularImageView group_icon;

        public NewsPostsByGroupViewHolder(View v) {
            super(v);

            Typeface r_med = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Medium.ttf");
            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            news_feed = (CardView) v.findViewById(R.id.news_feed_card);
            event_title = (TextView) v.findViewById(R.id.tv_event);
            group_name = (TextView) v.findViewById(R.id.tv_group);
            timestamp = (TextView) v.findViewById(R.id.tv_timestamp);
            event_photo = (ImageView) v.findViewById(R.id.iv_event_photo);
            news_icon = (ImageView) v.findViewById(R.id.iv_news_icon);
            like = (ImageView) v.findViewById(R.id.iv_like);
            share = (ImageView) v.findViewById(R.id.iv_share);
            group_icon = (CircularImageView) v.findViewById(R.id.group_image);

            event_title.setTypeface(r_med);
            group_name.setTypeface(r_reg);
            timestamp.setTypeface(r_reg);

            news_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent_temp = new Intent(v.getContext(), InEventActivity.class);
                    posi = getAdapterPosition();
                    Bundle bundle = new Bundle();
                    CampusFeedBean bean = NewsPostsByGroupList.get(pos);
                    bundle.putSerializable("BEAN", bean);
                    bundle.putBoolean("FLAG_NEWS",true);
                    bundle.putBoolean("FLAG_SELECTED_SHARE", flag_share_clicked.get(pos));
                    bundle.putBoolean("FLAG_SELECTED_ATTEND/LIKE", flag_attending_clicked.get(pos));
                    intent_temp.putExtras(bundle);
                    v.getContext().startActivity(intent_temp);
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos_for_going = getAdapterPosition();
                    if (flag_attending_clicked.get(pos_for_going)) {
                        like.setImageResource(R.mipmap.heart);

                        dataBase.saveFeedInfo(NewsPostsByGroupList.get(pos_for_going).getPid(), "0");
                        flag_attending_clicked.set(pos_for_going, false);
                        //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                    } else {
                        like.setImageResource(R.mipmap.heart_selected);
                        dataBase.saveFeedInfo(NewsPostsByGroupList.get(pos_for_going).getPid(), "1");
                        flag_attending_clicked.set(pos_for_going, true);
                        //Toast.makeText(context, "coming soon", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent i = new Intent(android.content.Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String shareBody = "Title : " + NewsPostsByGroupList.get(pos).getTitle() + "\n" + "Description : " + NewsPostsByGroupList.get(posi).getDescription() + " Download the app now from visit bit.ly/campusconnectandroid";
                    i.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    v.getContext().startActivity(Intent.createChooser(i, "Share via"));
                    int pos_for_share = getAdapterPosition();
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

        @Override
        public void onClick(View view) {

        }
    }
}


