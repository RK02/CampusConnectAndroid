package com.campusconnect.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.ClubRequestsAdapterActivity;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015. 
 */
public class AdminPageActivity extends AppCompatActivity {

    ImageButton close;
    Button request,members;
    RelativeLayout admin_group;
    TextView admin_group_selected_text, admin_text;
    public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
    int position;
    String clubid = "";
    private static final String LOG_TAG="AdminPageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        Typeface r_lig = Typeface.createFromAsset(getAssets(), "font/Roboto_Light.ttf");
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        request = (Button) findViewById(R.id.b_request);
        members = (Button) findViewById(R.id.b_members);
        close = (ImageButton) findViewById(R.id.ib_cancel);
        admin_group = (RelativeLayout) findViewById(R.id.group_select);
        admin_group_selected_text = (TextView) findViewById(R.id.tv_group_name_selected);
        admin_text = (TextView) findViewById(R.id.tv_admin_text);

        admin_group_selected_text.setTypeface(r_lig);
        admin_text.setTypeface(r_med);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(), RequestsPage_InAdminActivity.class);
                intent_temp.putExtra("club_id",clubid);
                Log.d("AdminPageActivity","CLUB ID "+clubid);
                startActivity(intent_temp);
            }
        });
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_temp = new Intent(v.getContext(),GroupMembersPage_InAdminActivity.class);
                intent_temp.putExtra("club_id",clubid);
                Log.d("AdminPageActivity","CLUB ID "+clubid);
                startActivity(intent_temp);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        admin_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Group:");
                if (AdminPageActivity.this.groupList == null) {
                    admin_group_selected_text.setText("Loading Groups");
                } else {
                    final String[] groupList = new String[AdminPageActivity.this.groupList.size()];
                    for (int i = 0; i < AdminPageActivity.this.groupList.size(); i++) {
                        groupList[i] = AdminPageActivity.this.groupList.get(i).getAbb();
                    }
                    builder.setItems(groupList, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            position = item;
                            admin_group_selected_text.setText(AdminPageActivity.this.groupList.get(position).getAbb());
                            //eventMiniForm.setClubId(CreatePostActivity.this.groupList.get(position).getClubId());
                            clubid = AdminPageActivity.this.groupList.get(position).getClubId();
                            Log.d(LOG_TAG, clubid + " " + admin_group_selected_text);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        webApiGetGroups();

    }

    public void webApiGetGroups() {
        try {
            String collegeId = SharedpreferenceUtility.getInstance(AdminPageActivity.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(AdminPageActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            //jsonObject.put("college_id", collegeId);
            jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubListofAdmin";
            new WebRequestTask(AdminPageActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
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
                    Toast.makeText(AdminPageActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(AdminPageActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
