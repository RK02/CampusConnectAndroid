package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.ClubListAdapterActivity;
import com.campusconnect.adapter.CollegeListAdapterActivity;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkd on 28/12/15.
 */
public class SelectAdminClub  extends AppCompatActivity {

    private static final String LOG_TAG = "SelectAdminClub";

    //  private String mEmailAccount = "";
    static Context context;
    RecyclerView club_list;
    public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_admin_club);

        club_list = (RecyclerView) findViewById(R.id.rv_admin_club_list);

        club_list.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        club_list.setLayoutManager(llm);
        club_list.setItemAnimator(new DefaultItemAnimator());

        //TODO network check

        if (NetworkAvailablity.hasInternetConnection(SelectAdminClub.this)) {
            webApiGetGroups();

        } else {
            Toast.makeText(SelectAdminClub.this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void webApiGetGroups() {
        try {
            String collegeId = SharedpreferenceUtility.getInstance(SelectAdminClub.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(SelectAdminClub.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubListofAdmin";
            new WebRequestTask(SelectAdminClub.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
                    true, url).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {

                        case WebServiceDetails.PID_GET_GROUPS: {
                            try {
                                groupList.clear();
                                JSONObject grpJson = new JSONObject(strResponse);
                                if (grpJson.has("list")) {
                                    JSONArray grpArray = grpJson.getJSONArray("list");
                                    GroupBean b = new GroupBean();
                                    b.setAbb("Select your club");
                                    groupList.add(0,b);

                                    for (int i = 0; i < grpArray.length(); i++) {

                                        JSONObject innerGrpObj = grpArray.getJSONObject(i);
                                        String description = innerGrpObj.optString("description");
                                        String admin = innerGrpObj.optString("admin");
                                        String clubId = innerGrpObj.optString("club_id");
                                        String abb = innerGrpObj.optString("abbreviation");
                                        String name = innerGrpObj.optString("name");

                                        GroupBean bean = new GroupBean();
                                        bean.setAbb(abb);
                                        bean.setName(name);
                                        bean.setAdmin(admin);
                                        bean.setClubId(clubId);
                                        bean.setDescription(description);
                                        groupList.add(bean);
                                    }
                                    ClubListAdapterActivity cl = new ClubListAdapterActivity(groupList,SelectAdminClub.this);
                                    club_list.setAdapter(cl);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;

                        default:
                            break;


                    }
                } else {
                    Toast.makeText(SelectAdminClub.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SelectAdminClub.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
