package com.campusconnect.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.common.base.Strings;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RK on 23-09-2015.
 */
public class CreateGroupActivity extends AppCompatActivity {

    static String imageUrlForUpload = "";
    ImageView upload;
    Button createGroup;
    EditText groupAbbreviation, groupName, groupDescription;
    Spinner groupType;
    LinearLayout close;
    private int PICK_IMAGE_REQUEST = 1;
    static SharedPreferences sharedPreferences;
    private String mEmailAccount = "";
    private static final String LOG_TAG = "CreateGroupActivity";
    TextView create_group_text;
    private Uri fileUri;


    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;

    String encodedImageStr;
    ArrayList<String> typeArry = new ArrayList<>();

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Typeface r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
        Typeface r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);

        create_group_text = (TextView) findViewById(R.id.tv_create_group);
        createGroup = (Button) findViewById(R.id.et_createGroup);
        groupType = (Spinner) findViewById(R.id.spin_group_type);
        groupName = (EditText) findViewById(R.id.et_group_name);
        groupAbbreviation = (EditText) findViewById(R.id.et_group_abbreviation);
        groupDescription = (EditText) findViewById(R.id.et_group_description);
        upload = (ImageView) findViewById(R.id.group_icon_group);

        create_group_text.setTypeface(r_med);
        createGroup.setTypeface(r_reg);
        groupName.setTypeface(r_reg);
        groupAbbreviation.setTypeface(r_reg);
        groupDescription.setTypeface(r_reg);

        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedPreferences.getString(AppConstants.EMAIL_KEY, null);
        typeArry.add("Group Type");
        typeArry.add("Student");
        typeArry.add("Alumni");



        CustomTypeAdapter typeAdapter = new CustomTypeAdapter(CreateGroupActivity.this, R.layout.spinn_text_item, typeArry);
        groupType.setAdapter(typeAdapter);
        groupType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webApiCreateGroup();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                uploadImage();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }


    public void webApiCreateGroup() {
        try {
            JSONObject jsonObject = new JSONObject();
            String collegeId = SharedpreferenceUtility.getInstance(CreateGroupActivity.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(CreateGroupActivity.this).getString(AppConstants.PERSON_PID);
            String groupTypestr = groupName.getText().toString();
            String groupname = groupName.getText().toString();
            String grpDes = groupDescription.getText().toString();
            String abbre = groupAbbreviation.getText().toString();
            String groupTypeStr = (String) groupType.getSelectedItem();

            if (groupTypestr.isEmpty() || groupname.isEmpty() || grpDes.isEmpty() || abbre.isEmpty()) {

                Toast.makeText(CreateGroupActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }   else   if (imageUrlForUpload.isEmpty()) {
                Toast.makeText(CreateGroupActivity.this, "Please Select image again and Upload it", Toast.LENGTH_SHORT).show();
                return;
            }



            jsonObject.put("club_name", "" + groupName.getText().toString());
            jsonObject.put("description", "" + groupDescription.getText().toString());
            jsonObject.put("abbreviation", "" + groupAbbreviation.getText().toString());
            jsonObject.put("from_pid", pid);
            jsonObject.put("college_id", "" + collegeId);
            jsonObject.put("photoUrl", "" + imageUrlForUpload);
            Log.e("Json String", jsonObject.toString());

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "club";
            new WebRequestTask(CreateGroupActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_GROUP,
                    true, url).execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void showAlertDialog(final File imageFile) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateGroupActivity.this);
        alertDialog.setTitle("Select image");
        alertDialog.setMessage("Do you want to upload this image?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Blob(CreateGroupActivity.this, imageFile).execute();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                upload.setImageResource(R.mipmap.upload);

            }
        });
        alertDialog.show();
    }

    void uploadImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    fileUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"), 1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        switch (requestCode) {
            case 0:
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), fileUri);
                    bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);

                    upload.setImageBitmap(bitmap);

                    Uri selectedImageUri = getImageUri(CreateGroupActivity.this, bitmap);
                    File finalFile = new File(getPath(selectedImageUri, CreateGroupActivity.this));
                    if (finalFile.exists()) {
                        showAlertDialog(finalFile);
                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    Uri selectedImageUri = data.getData();
                    String tempPath = getPath(selectedImageUri, CreateGroupActivity.this);
                    File finalFile = new File(getPath(selectedImageUri, CreateGroupActivity.this));
                    if (finalFile.exists()) {
                        showAlertDialog(finalFile);
                    }
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                    upload.setImageBitmap(bm);
                    upload.setScaleType(ImageView.ScaleType.FIT_XY);
                    encodedImageStr = Base64.encodeToString(getBytesFromBitmap(bm), Base64.NO_WRAP);

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    Toast.makeText(CreateGroupActivity.this, "Image size is too large.Please upload small image.", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
        return Uri.parse(path);
    }


    private final Handler _handler = new Handler() {
        public void handleMessage(Message msg) {
            int response_code = msg.what;
            if (response_code != 0) {
                if (response_code != 204) {
                    String strResponse = (String) msg.obj;
                    Log.v("Response", strResponse);
                    if (strResponse != null && strResponse.length() > 0) {
                        switch (response_code) {
                            case WebServiceDetails.PID_CREATE_GROUP: {
                                try {
                                    JSONObject jsonResponse = new JSONObject(strResponse);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                            default:
                                break;
                        }
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateGroupActivity.this, "Your group request has been sent for approval.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {

                Toast.makeText(CreateGroupActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();

            }
        }
    };

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
                if (dialog != null)
                    dialog.dismiss();
                super.onPostExecute(result);

                imageUrlForUpload = result;

                //   Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();

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

    private class CustomTypeAdapter extends ArrayAdapter {

        private Context context;
        private List<String> itemList;

        public CustomTypeAdapter(Context context, int textViewResourceId, List<String> itemList) {
            super(context, textViewResourceId, itemList);

            this.context = context;
            this.itemList = itemList;
        }

        public TextView getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinn_text_item, parent,
                    false);
            TextView make = (TextView) row.findViewById(R.id.tv_spinner);
            Typeface r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
            make.setTypeface(r_reg);
            make.setText(itemList.get(position));
            if (itemList.get(position).equalsIgnoreCase("group type")) {
                make.setTextColor(Color.parseColor("#808080"));
            }
            return (TextView) row;
        }

        public TextView getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinn_text_item, parent,
                    false);
            TextView make = (TextView) row.findViewById(R.id.tv_spinner);
            Typeface r_reg = Typeface.createFromAsset(getAssets(), "font/Roboto_Regular.ttf");
            make.setTypeface(r_reg);
            make.setText(itemList.get(position));

            return (TextView) row;
        }

    }


}
