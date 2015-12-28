package com.campusconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.activity.InEventActivity;
import com.campusconnect.bean.CampusFeedBean;
import com.campusconnect.bean.JoinRequestBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.supportClasses.JoinRequestsinfoActivity;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class JoinRequestsAdapterActivity extends
        RecyclerView.Adapter<JoinRequestsAdapterActivity.JoinRequestsViewHolder> {

    private static List<JoinRequestBean> JoinRequestsList;
    static Context context;
    public JoinRequestsAdapterActivity(List<JoinRequestBean> JoinRequestsList,Context context) {
        this.JoinRequestsList = JoinRequestsList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return JoinRequestsList.size();
    }

    @Override
    public void onBindViewHolder(JoinRequestsViewHolder join_requestsViewHolder,final int i) {
        final JoinRequestBean ci = JoinRequestsList.get(i);
        join_requestsViewHolder.request.setText(ci.getFrom_name()+" requested to join.");
        join_requestsViewHolder.batch.setText(ci.getFrom_batch());
        join_requestsViewHolder.branch.setText(ci.getFrom_branch());
        try {
            Picasso.with(context).load(ci.getFrom_photoUrl()).into(join_requestsViewHolder.profile_image);
        } catch (Exception e) {
            Picasso.with(context).load(R.mipmap.default_image).into(join_requestsViewHolder.profile_image);
        }
        join_requestsViewHolder.accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //JoinRequestBean joinRequestBean=JoinRequestsList.get(getAdapterPosition());
                String req_id=ci.getRequestId();
                // TODO Auto-generated method stub
                if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                    webApiAcceptReject("Y", req_id);
                    JoinRequestsList.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, JoinRequestsList.size());
                } else {
                    Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        join_requestsViewHolder.reject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //JoinRequestBean joinRequestBean=JoinRequestsList.get(getAdapterPosition());
                String req_id=ci.getRequestId();
                // TODO Auto-generated method stub
                if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                    webApiAcceptReject("N", req_id);
                    JoinRequestsList.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, JoinRequestsList.size());
                } else {
                    Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public JoinRequestsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_card_layout_join_request, viewGroup, false);

        return new JoinRequestsViewHolder(itemView);
    }

    public class JoinRequestsViewHolder extends RecyclerView.ViewHolder{

        TextView request,accept,reject,batch,branch;
        CircularImageView profile_image;

        public JoinRequestsViewHolder(View v) {
            super(v);
            request = (TextView) v.findViewById(R.id.tv_request);
            profile_image= (CircularImageView) v.findViewById(R.id.profile_image);
            accept=(TextView)v.findViewById(R.id.b_accept);
            reject=(TextView)v.findViewById(R.id.b_reject);
            batch=(TextView)v.findViewById(R.id.tv_member_batch);
            branch=(TextView)v.findViewById(R.id.tv_member_branch);
            //accept.setOnClickListener();
        }

    }

    public static void webApiAcceptReject(String action,String req_id) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("action", action);
            jsonObject.put("req_id", req_id);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "joinClubApproval";
            Log.d("JoinRequests",url);
            new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GROUP_JOIN,
                    true, url).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0 && response_code != 204) {
                String strResponse = (String) msg.obj;
                Log.v("Response", strResponse);
                if (strResponse != null && strResponse.length() > 0) {
                    switch (response_code) {
                        case WebServiceDetails.PID_GROUP_JOIN: {
                            try {
                                JSONObject jsonObject = new JSONObject(strResponse);
                                Log.v("Response",jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            }
            if (response_code == 204) {
                //  if (!flag_news.get(posi)) {



                //  } else {
                  /*  if (liking == 1) {
                        liking = 2;
                        Toast.makeText(context, "Like", Toast.LENGTH_LONG).show();
                    } else if (liking == 2) {
                        liking = 1;
                        Toast.makeText(context, "Unlike", Toast.LENGTH_LONG).show();
                    }

                }*/

            } else {
                Toast.makeText(context, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

}


