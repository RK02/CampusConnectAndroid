package com.campusconnect.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.flurry.android.FlurryAgent;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkd on 3/12/15.
 */
public class FlashActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashpage);
        WebApiUpdateStatus();


    }


    public void WebApiUpdateStatus() {
        JSONObject jsonObject = new JSONObject();
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "updateStatus";
        new WebRequestTask(FlashActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_UPDATE_STATUS,
                false, url).execute();


    }

    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GET_UPDATE_STATUS: {
                            try {
                                JSONObject updateObj = new JSONObject(strResponse);
                                String update = updateObj.getString("update");
                                if(update.equals("NO")){
                                    Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            super.run();
                                            try {
                                                sleep(2000);
                                                SharedpreferenceUtility.getInstance(FlashActivity.this).putInt("AdminStatus",0);
                                                Boolean loggedIn = SharedpreferenceUtility.getInstance(FlashActivity.this).getBoolean(AppConstants.LOG_IN_STATUS);
                                                SharedPreferences prefs = FlashActivity.this.getSharedPreferences("AllPersonalFeeds", Context.MODE_PRIVATE);
//save the user list to preference
                                                SharedPreferences.Editor editor = prefs.edit();

                                                prefs.edit().remove(AppConstants.PERSONAL_FEED_ARRAYLIST).commit();
                                                if (loggedIn) {
                                                    Intent next = new Intent(FlashActivity.this, MainActivity.class);
                                                    startActivity(next);
                                                    finish();
                                                } else {
                                                    Intent next = new Intent(FlashActivity.this, SelectCollegeActivity.class);
                                                    startActivity(next);
                                                    finish();
                                                }

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };
                                    thread.start();

                                }else{
                                    Intent next = new Intent(FlashActivity.this, UpdateActivity.class);
                                    startActivity(next);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent next = new Intent(FlashActivity.this, UpdateActivity.class);
                                startActivity(next);
                                finish();
                            }

                        }
                        break;

                        default:
                            break;
                    }
                } else {
                    Toast.makeText(FlashActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(FlashActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };
}
