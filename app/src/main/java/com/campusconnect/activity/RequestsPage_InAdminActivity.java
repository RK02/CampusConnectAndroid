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

import com.campusconnect.adapter.ClubRequestsAdapterActivity;
import com.campusconnect.adapter.GroupMembersByGroupAdapterActivity;
import com.campusconnect.adapter.JoinRequestsAdapterActivity;
import com.campusconnect.bean.GroupMemberBean;
import com.campusconnect.bean.JoinRequestBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.supportClasses.JoinRequestsinfoActivity;
import com.campusconnect.R;
import com.campusconnect.utility.DividerItemDecoration;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class RequestsPage_InAdminActivity extends AppCompatActivity {

    LinearLayout close;
    TextView requests_text,caught_up;
    RecyclerView join_requests;
    ArrayList<JoinRequestBean> joinRequestBeans= new ArrayList<JoinRequestBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_in_admin);

        Bundle bundle = getIntent().getExtras();
        String club_id=bundle.getString("club_id");
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        join_requests = (RecyclerView) findViewById(R.id.rv_join_requests);
        requests_text = (TextView) findViewById(R.id.tv_requests_text);
        requests_text.setTypeface(r_med);
        caught_up=(TextView)findViewById(R.id.tv_caught_up);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        join_requests.setLayoutManager(llm);
        join_requests.setHasFixedSize(false);
        join_requests.setItemAnimator(new DefaultItemAnimator());
        join_requests.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Log.d("Requests","clubid "+club_id);
        webApiGetRequests(club_id);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

    public void webApiGetRequests(String club_id) {
        try {
            String pid = SharedpreferenceUtility.getInstance(RequestsPage_InAdminActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clubId", club_id);
            jsonObject.put("pid",pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "adminFeed";
            Log.d("Requests",url);
            new WebRequestTask(RequestsPage_InAdminActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_ADMIN_REQUESTS,
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
                        case WebServiceDetails.PID_GET_ADMIN_REQUESTS: {
                            try {

                                JSONObject jsonObject = new JSONObject(strResponse);
                                if (jsonObject.has("joinReq")) {

                                    JSONArray array = jsonObject.getJSONArray("joinReq");
                                    if (array.length() > 0) {
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject innerObject = array.getJSONObject(i);
                                            JoinRequestBean bean = new JoinRequestBean();
                                            bean.setFrom_photoUrl(innerObject.optString("from_photoUrl"));
                                            bean.setRequestId(innerObject.optString("requestId"));
                                            bean.setTimestamp(innerObject.optString("timestamp"));
                                            bean.setClub_name(innerObject.optString("club_name"));
                                            bean.setFrom_pid(innerObject.optString("from_pid"));
                                            bean.setFrom_name(innerObject.optString("from_name"));
                                            bean.setFrom_batch(innerObject.optString("from_batch"));
                                            bean.setFrom_branch(innerObject.optString("from_branch"));
                                            joinRequestBeans.add(bean);
                                        }
                                        if(joinRequestBeans.size()>0){
                                            JoinRequestsAdapterActivity gm = new JoinRequestsAdapterActivity(joinRequestBeans
                                                    , RequestsPage_InAdminActivity.this );
                                            join_requests.setAdapter(gm);
                                        }

                                    }
                                }
                                else{
                                    caught_up.setVisibility(View.VISIBLE);
                                    join_requests.setVisibility(View.GONE);
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
                    Toast.makeText(RequestsPage_InAdminActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RequestsPage_InAdminActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}
