package com.campusconnect.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.adapter.ClubRequestsAdapterActivity;
import com.campusconnect.bean.SuperAdminRequestBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.DividerItemDecoration;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkd on 23/12/15.
 */
public class RequestsSuperAdmin extends AppCompatActivity {
    LinearLayout close;
    TextView requests_text,caught_up;
    RecyclerView club_requests;
    ArrayList<SuperAdminRequestBean> superAdminRequestBeans= new ArrayList<SuperAdminRequestBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_super_admin);

        Bundle bundle = getIntent().getExtras();
        //String club_id=bundle.getString("club_id");
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        club_requests = (RecyclerView) findViewById(R.id.rv_club_requests);
        caught_up = (TextView) findViewById(R.id.tv_caught_up);

        requests_text = (TextView) findViewById(R.id.tv_requests_text);
        requests_text.setTypeface(r_med);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        club_requests.setLayoutManager(llm);
        club_requests.setHasFixedSize(false);
        club_requests.setItemAnimator(new DefaultItemAnimator());
        club_requests.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        webApiGetRequests();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    public void webApiGetRequests() {
        try {
            String pid = SharedpreferenceUtility.getInstance(RequestsSuperAdmin.this).getString(AppConstants.PERSON_PID);
            String collegeId=SharedpreferenceUtility.getInstance(RequestsSuperAdmin.this).getString(AppConstants.COLLEGE_ID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("collegeId", collegeId);
            jsonObject.put("pid",pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "superAdminFeed";
            new WebRequestTask(RequestsSuperAdmin.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_SUPER_ADMIN_REQUESTS,
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
                        case WebServiceDetails.PID_GET_SUPER_ADMIN_REQUESTS: {
                            try {

                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("items")) {

                                    JSONArray array = jsonObject.getJSONArray("items");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject innerObject = array.getJSONObject(i);
                                            SuperAdminRequestBean bean = new SuperAdminRequestBean();
                                            bean.setFrom_photoUrl(innerObject.optString("from_photoUrl"));
                                            bean.setRequestId(innerObject.optString("requestId"));
                                            bean.setTimestamp(innerObject.optString("timestamp"));
                                            bean.setClub_name(innerObject.optString("club_name"));
                                            bean.setFrom_pid(innerObject.optString("from_pid"));
                                            bean.setFrom_name(innerObject.optString("from_name"));
                                            bean.setDescription(innerObject.optString("description"));
                                            bean.setIsAlumni(innerObject.optString("isAlumni"));
                                            superAdminRequestBeans.add(bean);
                                        }
                                        if(superAdminRequestBeans.size()>0){
                                            ClubRequestsAdapterActivity gm = new ClubRequestsAdapterActivity(superAdminRequestBeans
                                                    , RequestsSuperAdmin.this );
                                            club_requests.setAdapter(gm);
                                        }


                                    }
                                }
                                else{
                                    caught_up.setVisibility(View.VISIBLE);
                                    club_requests.setVisibility(View.GONE);
                                    //club_requests.

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
                    Toast.makeText(RequestsSuperAdmin.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RequestsSuperAdmin.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}
