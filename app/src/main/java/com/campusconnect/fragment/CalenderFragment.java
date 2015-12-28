package com.campusconnect.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.CalendarAdapter;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.slidingtab.SlidingTabLayout_Calendar;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.campusconnect.utility.StickyHeaderRecyclerDecor;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalenderFragment extends Fragment {
    public static Typeface r_med, r_reg;
    static ArrayList<CampusFeedBean> calenderList = new ArrayList<CampusFeedBean>();
    CalendarAdapter adapter;
    ViewPager pager;
    SlidingTabLayout_Calendar tabs;
    CharSequence Titles[] = {"Mon 5", "Tue 6", "Wed 7", "Thu 8", "Fri 9", "Sat 10", "Sun 11"};
    int Numboftabs = 7;
    RecyclerView calendar;
    View mRootView;
    StickyHeaderRecyclerDecor headersDecor;


    @Override
    public void onResume() {
        super.onResume();

        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.activity_calendar_design_two, container, false);
            calendar = (RecyclerView) mRootView.findViewById(R.id.rv_calendar);

            //getting data for sending to server
            Date cDate = new Date();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
            //calling api to get data from server
            webApiCalender(date);
            // Set adapter populated with example dummy data
            //initalize adapter other wise it will skips
            adapter = new CalendarAdapter(createList_cal(1), getActivity());
            calendar.setAdapter(adapter);
            calenderList.clear();
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            calendar.setLayoutManager(llm);
            // Add the sticky headers decoration
            headersDecor = new StickyHeaderRecyclerDecor(adapter);
            calendar.addItemDecoration(headersDecor);
            //  adapter.registerAdapterDataObserver(dataObserver);


        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }

    private ArrayList<CampusFeedBean> createList_cal(int size) {
        calenderList = new ArrayList<CampusFeedBean>();
        for (int i = 1; i <= size; i++) {
            CampusFeedBean ci = new CampusFeedBean();
            if (i == 0)
                ci.setStart_date("2014-12-11");

            calenderList.add(ci);
        }
        return calenderList;
    }


    RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            headersDecor.invalidateHeaders();
        }
    };


    public void webApiCalender(String date) {

        String collegeId = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.COLLEGE_ID);
        try {
            JSONObject jsonObject = new JSONObject();
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getEvents?collegeId=" + collegeId + "&future_date=" + date;
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_GET_CALENDER,
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
                        case WebServiceDetails.PID_GET_CALENDER: {
                            try {
                                calenderList.clear();
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
                                            String clubid = innerObj.optString("club_id");
                                            String pid = innerObj.optString("eventId");
                                            String timeStamp = innerObj.optString("timestamp");
                                            String title = innerObj.optString("title");
                                            String collegeId = innerObj.optString("collegeId");
                                            String kind = innerObj.optString("kind");
                                            String clubname = innerObj.optString("club_name");
                                            String clubAbbreviation = innerObj.optString("clubabbreviation");

                                            bean.setEventCreator(eventCreator);
                                            bean.setDescription(description);
                                            bean.setViews(views);
                                            bean.setClubid(clubid);
                                            bean.setPid(pid);

                                            bean.setPhoto(photo);
                                            bean.setTimeStamp(timeStamp);
                                            bean.setTitle(title);
                                            bean.setCollegeId(collegeId);
                                            bean.setKind(kind);
                                            bean.setClubname(clubname);
                                            bean.setClubAbbreviation(clubAbbreviation);
                                            ArrayList<String> attendList=null;
                                            try {
                                                attendList = new ArrayList<>();
                                                if (innerObj.has("attendees")) {
                                                    JSONArray attArray = innerObj.getJSONArray("attendees");
                                                    if (attArray.length() > 0) {
                                                        for (int j = 0; j < attArray.length(); j++) {
                                                            String attendStr = attArray.getString(j);
                                                            attendList.add(attendStr);
                                                        }
                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            bean.setAttendees(attendList);
                                            String endDate = innerObj.optString("end_date");
                                            String startDate = innerObj.optString("start_date");
                                            String startTime = innerObj.optString("start_time");
                                            String endTime = innerObj.optString("end_time");
                                            String venue = innerObj.optString("venue");
                                            String clubphoto = innerObj.optString("clubphotoUrl");
                                            String liker = innerObj.optString("");
                                            String complete = innerObj.optString("completed");


                                            bean.setEnd_date("" + endDate);
                                            bean.setStart_date("" + startDate);
                                            bean.setStart_time("" + startTime);
                                            bean.setEnd_time("" + endTime);
                                            bean.setVenue("" + venue);

                                            bean.setClubphoto("" + clubphoto);
                                            bean.setLikers("" + liker);
                                            bean.setCompleted("" + complete);
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
                                            calenderList.add(bean);

                                        }
                                        adapter.notifyDataSetChanged();
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
                    Toast.makeText(getActivity(), "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
