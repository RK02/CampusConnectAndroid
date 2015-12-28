package com.campusconnect.communicator;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created by canopus on 7/12/15.
 */
public class Blob extends AsyncTask<Void, Integer, String> {
    String url;
    File imageFIle;

    public Blob(String Url) {
        this.url = Url;

    }

    private ProgressDialog dialog;
    private Context context;
    private Handler handler;
    public static final int POST = 1;
    private boolean showDialog;
    String responseStringfianl;

    private int type;


    public Blob(Context context, File imageFIle) {
        this.context = context;
        this.url = url;
        this.imageFIle = imageFIle;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {

                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Please wait...");
                dialog.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if ( dialog != null)
                dialog.dismiss();
            super.onPostExecute(result);
            Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://campus-connect-2015.appspot.com/get_upload_url");
            httpget.setHeader("Content-Type", "application/json");
            httpget.setHeader("Accept-Encoding", "gzip");
            //  httpget.setHeader("");
            //  entity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");//text/plain;charset=UTF-8
            HttpResponse response = httpClient.execute(httpget);
            int responsecode = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity());
            Log.v("", "responsessdf : " + responseString);
            if (responseString != null) {
                SharedpreferenceUtility.getInstance(context).putString(AppConstants.BLOB_URL, responseString);
            }


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(responseString);
            String BOUNDARY = "Boundary-8B33EF29-2436-47F6-A415-62EF61F62D14";

            FileBody fileBody = new FileBody(imageFIle);
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, BOUNDARY, Charset.defaultCharset());
            entity.addPart("file", fileBody);
            httppost.setEntity(entity);
            HttpResponse response1 = httpclient.execute(httppost);
           responseStringfianl = EntityUtils.toString(response1.getEntity());
            Log.e("response String", responseStringfianl.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseStringfianl;
    }


}
