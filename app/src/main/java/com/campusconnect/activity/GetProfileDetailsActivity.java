package com.campusconnect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.common.base.Strings;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 26-09-2015.
 */
public class GetProfileDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button cont;
    EditText et_batch, et_branch, et_name, et_location, et_company;
    Switch sw_student;
    private static final String LOG_TAG = "GetProfileDetails";

    private String mEmailAccount = "";
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_profile_details);

        sw_student = (Switch) findViewById(R.id.student_alumini_switch);
        et_name = (EditText) findViewById(R.id.et_name);
        et_batch = (EditText) findViewById(R.id.et_batch);
        et_branch = (EditText) findViewById(R.id.et_branch);
        et_company = (EditText) findViewById(R.id.et_company);
        et_location = (EditText) findViewById(R.id.et_location);

        cont = (Button) findViewById(R.id.b_continue);

        name = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.PERSON_NAME);
        mEmailAccount = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.EMAIL_KEY);

        et_name.setText(name);

        sw_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (!isChecked) {
                    SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.ALUMNI, "N");
                    et_location.setVisibility(View.INVISIBLE);
                    et_company.setVisibility(View.INVISIBLE);
                } else {
                    SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.ALUMNI, "Y");
                    et_company.setVisibility(View.VISIBLE);
                    et_location.setVisibility(View.VISIBLE);
                }

            }
        });

        cont.setOnClickListener(this);
    }

    private void createProfile(View v) {


        //String str_phone = et_phone.getText().toString().trim();
        //if (str_phone == null) {
        //    str_phone = "";
        //}
        String str_batch = et_batch.getText().toString().trim();
        if (str_batch == null) {
            str_batch = "";
        }
        String str_branch = et_branch.getText().toString().trim();
        if (str_branch == null) {
            str_branch = "";
        }

        String isAlumini = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.ALUMNI);
        if (isAlumini.equals("")) {
            isAlumini = "N";
        }

        String str_company = et_company.getText().toString().trim();
        if (str_company == null) {
            str_company = "";
        }
        String str_location = et_location.getText().toString().trim();
        if (str_location == null) {
            str_location = "";
        }

        SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.BATCH, str_batch);
        //SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.PHONE,str_phone);
        SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.BRANCH, str_branch);
        String name = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.PERSON_NAME);
        String email = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.EMAIL_KEY);
        String collegeId = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.COLLEGE_ID);
        String gcmId = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.GCM_TOKEN);
        String photoUrl = SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).getString(AppConstants.PHOTO_URL);

        try {
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("phone", "");
            object.put("isAlumni", isAlumini);
            object.put("email", email);
            object.put("collegeId", collegeId);
            object.put("branch", str_branch);
            object.put("batch", str_batch);
            object.put("gcmId", gcmId);
            object.put("photoUrl", photoUrl);
            if (isAlumini == "Y") {
                object.put("company", str_company);
                object.put("location", str_location);
            }
            Log.i("Save profile Web api", object.toString());
            WebApiSaveProfile(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }

     /*   {"name":"",
                "phone":"43121231"
                ,"isAlumni":"y"
                ,"email":"canopusinfo01@gamil.com",
                "collegeId":"5644309118320640"
                ,"branch":"fgfdfgdfg",
                "branch":"DSFDSFD"}*/
    }


    public void WebApiSaveProfile(JSONObject jsonObject) {

        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "profile";
        new WebRequestTask(GetProfileDetailsActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_SAVE_PROFILE,
                true, url).execute();


    }
   /* Log.d(LOG_TAG, "starting save profile");
    if (!isSignedIn()) {
        Toast.makeText(this, "You must sign in for this action.", Toast.LENGTH_LONG).show();
        return;
    }*/


    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_continue:
                createProfile(v);
                Intent intent_temp = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent_temp);
                finish();
                break;
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
                        case WebServiceDetails.PID_SAVE_PROFILE: {
                            try {
                                JSONObject profileObj = new JSONObject(strResponse);
                                String pid = profileObj.getString("pid");
                                String collegeid = profileObj.getString("collegeId");
                                Log.v("GetProfileDetails", pid + " " + collegeid);
                                SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.COLLEGE_ID, collegeid);
                                 SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.PERSON_PID, pid);
                                SharedpreferenceUtility.getInstance(GetProfileDetailsActivity.this).putString(AppConstants.PERSON_PID, pid);

                              } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            try {


//
//                                List<CollegeListInfoBean> list = new ArrayList<CollegeListInfoBean>();
//                                JSONObject collegeObj = new JSONObject(strResponse);
//                                if (collegeObj.has("collegeList")) {
//                                    JSONArray collegeArr = collegeObj.getJSONArray("collegeList");
//                                    for (int i = 0; i < collegeArr.length(); i++) {
//                                        JSONObject obj = collegeArr.getJSONObject(i);
//                                        String abbreviation = obj.optString("abbreviation");
//                                        String location = obj.optString("location");
//                                        String name = obj.optString("name");
//                                        String collegeid = obj.optString("collegeId");
//
//
//                                        CollegeListInfoBean bean = new CollegeListInfoBean();
//                                        bean.setCollegeId(collegeid);
//                                        bean.setLocation(location);
//                                        bean.setName(name);
//                                        list.add(bean);
//                                    }
//
//
//
//                                } else {
//                                    Toast.makeText(GetProfileDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }

                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(GetProfileDetailsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(GetProfileDetailsActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}