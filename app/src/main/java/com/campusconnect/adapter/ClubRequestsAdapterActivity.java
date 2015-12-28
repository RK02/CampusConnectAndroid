package com.campusconnect.adapter;

import android.content.Context;
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
import com.campusconnect.bean.JoinRequestBean;
import com.campusconnect.bean.SuperAdminRequestBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.utility.CircularImageView;
import com.campusconnect.utility.NetworkAvailablity;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkd on 23/12/15.
 */
public class ClubRequestsAdapterActivity extends
        RecyclerView.Adapter<ClubRequestsAdapterActivity.ClubRequestsViewHolder>{
        private static List<SuperAdminRequestBean> clubcreateRequestList;
        static Context context;
        public ClubRequestsAdapterActivity(List<SuperAdminRequestBean> clubcreateRequestList,Context context) {
            this.clubcreateRequestList = clubcreateRequestList;
            this.context=context;
        }

        @Override
        public int getItemCount() {
            return clubcreateRequestList.size();
        }

        @Override
        public void onBindViewHolder(ClubRequestsViewHolder club_requestsViewHolder,final int i) {
            final SuperAdminRequestBean ci = clubcreateRequestList.get(i);
            club_requestsViewHolder.request.setText(ci.getFrom_name()+" requested to create "+ci.getClub_name());
            club_requestsViewHolder.description.setText(ci.getDescription());
            try {
                Picasso.with(context).load(ci.getFrom_photoUrl()).into(club_requestsViewHolder.profile_image);
            } catch (Exception e) {
                Picasso.with(context).load(R.mipmap.default_image).into(club_requestsViewHolder.profile_image);
            }

            club_requestsViewHolder.accept.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //JoinRequestBean joinRequestBean=JoinRequestsList.get(getAdapterPosition());
                    String req_id = ci.getRequestId();
                    // TODO Auto-generated method stub

                    if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                        webApiAcceptReject("Y", req_id);
                        clubcreateRequestList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, clubcreateRequestList.size());
                    } else {
                        Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            club_requestsViewHolder.reject.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //JoinRequestBean joinRequestBean=JoinRequestsList.get(getAdapterPosition());
                    String req_id = ci.getRequestId();
                    // TODO Auto-generated method stub

                    if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                        webApiAcceptReject("N", req_id);
                        clubcreateRequestList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, clubcreateRequestList.size());
                    } else {
                        Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }

        @Override
        public ClubRequestsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.activity_card_layout_club_request, viewGroup, false);

            return new ClubRequestsViewHolder(itemView);
        }

        public class ClubRequestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView request,accept,reject,description;
            CircularImageView profile_image;

            public ClubRequestsViewHolder(View v) {
                super(v);
                request = (TextView) v.findViewById(R.id.tv_request);
                profile_image= (CircularImageView) v.findViewById(R.id.profile_image);
                accept=(TextView)v.findViewById(R.id.b_accept);
                reject=(TextView)v.findViewById(R.id.b_reject);
                description=(TextView)v.findViewById(R.id.tv_about_club);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                SuperAdminRequestBean joinRequestBean=clubcreateRequestList.get(getAdapterPosition());
                String req_id=joinRequestBean.getRequestId();
                switch (v.getId()) {
                    case R.id.b_accept:
                        // accept button clicked
                        if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                            webApiAcceptReject("Y", req_id);
                            clubcreateRequestList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), clubcreateRequestList.size());
                        } else {
                            Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.b_reject:
                        // reject button clicked
                        if (NetworkAvailablity.hasInternetConnection(v.getContext())) {
                            webApiAcceptReject("N", req_id);
                            clubcreateRequestList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), clubcreateRequestList.size());
                        } else {
                            Toast.makeText(v.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        }

        public static void webApiAcceptReject(String action,String req_id) {
            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", action);
                jsonObject.put("req_id", req_id);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                String url = WebServiceDetails.DEFAULT_BASE_URL + "approveclubreq";
                new WebRequestTask(context, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GROUP_CREATE,
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
                            case WebServiceDetails.PID_GROUP_CREATE: {
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


