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
import com.campusconnect.adapter.CollegeListAdapterActivity;
import com.campusconnect.bean.CollegeListInfoBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.utility.NetworkAvailablity;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class SelectCollegeActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SelectCollegeActivity";

  //  private String mEmailAccount = "";
   static Context context;
    RecyclerView college_list;
    TextView tv_not_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_college);

        college_list = (RecyclerView) findViewById(R.id.rv_college_list);

        college_list.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        college_list.setLayoutManager(llm);
        tv_not_found= (TextView) findViewById(R.id.tv_not_found);
        college_list.setItemAnimator(new DefaultItemAnimator());

        tv_not_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("SelectClgActivity", "Clicked on not found");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://campusconnect.cc/"));
                startActivity(browserIntent);
                }

        });
        //TODO network check

        if (NetworkAvailablity.hasInternetConnection(SelectCollegeActivity.this)) {
            GetAllCollegesWebAPI();

        } else {
            Toast.makeText(SelectCollegeActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void GetAllCollegesWebAPI() {
        JSONObject jsonObject = new JSONObject();
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "getColleges";
        new WebRequestTask(SelectCollegeActivity.this, param, _handler, WebRequestTask.GET, jsonObject, WebServiceDetails.PID_SELECT_COLLEGE,
                true, url).execute();
    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_SELECT_COLLEGE: {
                            try {

                                List<CollegeListInfoBean> list = new ArrayList<CollegeListInfoBean>();
                                JSONObject collegeObj = new JSONObject(strResponse);
                                if (collegeObj.has("collegeList")) {
                                    JSONArray collegeArr = collegeObj.getJSONArray("collegeList");
                                    CollegeListInfoBean bean = new CollegeListInfoBean();
                                    bean.setName("Select your campus");
                                    list.add(0,bean);
                                    for (int i = 0; i < collegeArr.length(); i++) {
                                        JSONObject obj = collegeArr.getJSONObject(i);
                                        String abbreviation = obj.optString("abbreviation");
                                        String location = obj.optString("location");
                                        String name = obj.optString("name");
                                        String collegeid = obj.optString("collegeId");


                                        bean = new CollegeListInfoBean();
                                        bean.setCollegeId(collegeid);
                                        bean.setLocation(location);
                                        bean.setName(name);
                                        list.add(i+1,bean);
                                    }
                                    CollegeListAdapterActivity cl = new CollegeListAdapterActivity(list,SelectCollegeActivity.this);
                                    college_list.setAdapter(cl);

                                } else {
                                    Toast.makeText(SelectCollegeActivity.this, "error", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(SelectCollegeActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SelectCollegeActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };


}
