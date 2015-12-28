package com.campusconnect.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.campusconnect.R;
import com.campusconnect.adapter.NotificationAdapterActivity;
import com.campusconnect.bean.NotificationBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    RecyclerView notification_list;
    ImageButton noti, profile, home, calendar, search;
    View mRootView;
    NotificationAdapterActivity nl;
    ArrayList<NotificationBean> mNotifiList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.activity_notification, container, false);


            notification_list = (RecyclerView) mRootView.findViewById(R.id.rv_notification);
            notification_list.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            notification_list.setLayoutManager(llm);
            notification_list.setItemAnimator(new DefaultItemAnimator());

            WebApiNotification();
           /* nl = new NotificationAdapterActivity(
                    createList_nl(4));*/
            //   notification_list.setAdapter(nl);


        } catch (InflateException e) {
            e.printStackTrace();
        }
        return mRootView;
    }

    public void WebApiNotification() {
        try {
            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "myNotifications";
            new WebRequestTask(getActivity(), param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_NOTIFICATION,
                    true, url).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<NotificationBean> createList_nl(int size) {
        List<NotificationBean> result = new ArrayList<NotificationBean>();
        for (int i = 1; i <= size; i++) {
            NotificationBean ci = new NotificationBean();
            result.add(ci);
        }
        return result;
    }


    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_NOTIFICATION: {
                            try {
                                mNotifiList.clear();
                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("list")) {

                                    JSONArray array = jsonObject.getJSONArray("list");
                                    if (array.length() > 0) {

                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject obj = array.getJSONObject(i);
                                            String gropname = obj.optString("clubName");
                                            String type = obj.optString("type");
                                            String groupId = obj.optString("clubId");
                                            String timestamp = obj.optString("timestamp");
                                            String eventName = obj.optString("eventName");
                                            String eventid = obj.optString("eventId");
                                            String photoUrl = obj.optString("clubphotoUrl");
                                            String postName= obj.optString("postName");

                                            NotificationBean bean = new NotificationBean();
                                            bean.setGroup_name(gropname);
                                            bean.setGroupId(groupId);
                                            bean.setType(type);
                                            bean.setEventName(eventName);
                                            bean.setTimestamp(timestamp);
                                            bean.setPhotoUrl(photoUrl);
                                            bean.setPostName(postName);

                                            if(type.equalsIgnoreCase("post")||type.equalsIgnoreCase("Event")||type.equalsIgnoreCase("Reminder")||type.equalsIgnoreCase("approved join reques"))
                                            {
                                                mNotifiList.add(bean);
                                            }
                                        }
                                        nl = new NotificationAdapterActivity(mNotifiList, getActivity());
                                        notification_list.setAdapter(nl);
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
