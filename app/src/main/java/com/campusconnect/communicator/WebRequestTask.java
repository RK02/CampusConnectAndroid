package com.campusconnect.communicator;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

public class WebRequestTask extends AsyncTask<Void, Integer, Void> {
    private ProgressDialog dialog;
    private Context context;
    private Handler handler;
    private JSONObject jsonObject;
    private List<NameValuePair> nameValuePairs;
    private int what;
    public static final int GET = 0;
    public static final int POST = 1;
    private boolean showDialog;
    static String link;
    private int type;

    public WebRequestTask(Context context, List<NameValuePair> nameValuePairs,
                          Handler handler, int type, JSONObject obj, int what, boolean showDiaolog, String link) {
        this.context = context;
        this.handler = handler;
        this.nameValuePairs = nameValuePairs;
        this.what = what;
        this.showDialog = showDiaolog;
        this.link = link;
        this.type = type;
        this.jsonObject = obj;
    }

    public WebRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (showDialog) {
                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        try {
            if (showDialog && dialog != null)
                dialog.dismiss();
            super.onPostExecute(result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(Void... params) {
        int statuscode = 0;
        HttpClient httpClient = new DefaultHttpClient();
        if (type == GET) {
            try {
                //httpPost.getParams().setBooleanParameter( "http.protocol.expect-continue", false );
                HttpGet httpget = new HttpGet(link);
                HttpResponse response = httpClient.execute(httpget);
                String responseString = EntityUtils.toString(response.getEntity());
                Log.v("", "responsessdf : " + responseString);
                Message message = handler.obtainMessage();
                message.obj = responseString;
                message.what = this.what;
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
                Message message = handler.obtainMessage();
                message.what = 0;
                handler.sendMessage(message);
            }

        } else if (type == POST) {
            HttpPost httpPost = new HttpPost(link);
            try {
                //httpPost.getParams().setBooleanParameter( "http.protocol.expect-continue", false );

                StringEntity httpEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpEntity);
                //	httpEntity.setContentType("application/json");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpPost.setHeader("Accept", "application/json");
                //	UrlEncodedFormEntity url= new UrlEncodedFormEntity(nameValuePairs);
                //	url.setContentType("application/json");
                //	httpPost.setEntity(url);
                HttpResponse response = httpClient.execute(httpPost);
                statuscode = response.getStatusLine().getStatusCode();
                String responseString = EntityUtils.toString(response.getEntity());

                Log.v("", "responsessdf : " + responseString);
                Message message = handler.obtainMessage();
                message.obj = responseString;
                message.what = this.what;
                handler.sendMessage(message);


            } catch (Exception e) {
                e.printStackTrace();
                Message message = handler.obtainMessage();
                if(statuscode==204) {
                    message.what = statuscode;
                }else{
                    message.what=0;
                }
                handler.sendMessage(message);

            }
        }
        return null;
    }
}

