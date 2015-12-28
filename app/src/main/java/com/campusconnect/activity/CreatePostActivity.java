package com.campusconnect.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.campusconnect.R;
import com.campusconnect.bean.GroupBean;
import com.campusconnect.communicator.WebRequestTask;
import com.campusconnect.communicator.WebServiceDetails;
import com.campusconnect.constant.AppConstants;
import com.campusconnect.slidingtab.SlidingTabLayout_CreatePost;
import com.campusconnect.utility.NetworkAvailablity;
import com.campusconnect.utility.SharedpreferenceUtility;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by RK on 07-10-2015.
 */
public class CreatePostActivity extends AppCompatActivity {

    private static final String LOG_TAG = "CreatePostActivity";
    TextView create_post_title;
    LinearLayout close;
    ViewPager pager;
    ViewPagerAdapter_CreatePost adapter;
    SlidingTabLayout_CreatePost tabs;
    String Tag = "CreatePostActivity";
    //   public static Button  post;
    CharSequence Titles[] = {"Event", "News"};
    public ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
    // List<ModelsClubMiniForm> modelsClubMiniForms;

    static SharedPreferences sharedPreferences;
    private String mEmailAccount = "";

    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;
    static String imageUrlForUpload = "";


    int Numboftabs = 2;
    Typeface r_med;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public CreatePostActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        r_med = Typeface.createFromAsset(getAssets(), "font/Roboto_Medium.ttf");

        close = (LinearLayout) findViewById(R.id.cross_button);
        create_post_title = (TextView) findViewById(R.id.tv_create_post);
        create_post_title.setTypeface(r_med);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (SlidingTabLayout_CreatePost) findViewById(R.id.tabs_createpost);
        adapter = new ViewPagerAdapter_CreatePost(getSupportFragmentManager(), Titles, Numboftabs, CreatePostActivity.this);

        pager.setAdapter(adapter);

        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        mEmailAccount = sharedPreferences.getString(AppConstants.EMAIL_KEY, null);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //TODO DONE getGroup in createpostActivity
    public void webApiGetGroups() {
        try {
            String collegeId = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.COLLEGE_ID);
            String pid = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.PERSON_PID);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("college_id", collegeId);
            //   jsonObject.put("pid", pid);
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "getClubList";
            new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_GET_GROUPS,
                    true, url).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isSignedIn() {
        if (!Strings.isNullOrEmpty(mEmailAccount)) {
            return true;
        } else {
            return false;
        }
    }

    public void webApiCreatePost(JSONObject jsonObject) {
        try {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String url = WebServiceDetails.DEFAULT_BASE_URL + "postEntry";
            Log.e("post Enrty url", "" + url);
            Log.e("request", "" + jsonObject.toString());

            new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_POST,
                    true, url).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void webApiCreateEvent(JSONObject jsonObject) {


        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String url = WebServiceDetails.DEFAULT_BASE_URL + "eventEntry";
        Log.e("event entry  url", "" + url);
        Log.e("request", "" + jsonObject.toString());
        new WebRequestTask(CreatePostActivity.this, param, _handler, WebRequestTask.POST, jsonObject, WebServiceDetails.PID_CREATE_EVENT,
                true, url).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            client.connect();
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "CreatePost Page", // TODO: Define a title for the content shown.
                    // TODO: If you have web page content that matches this app activity's content,
                    // make sure this auto-generated web page URL is correct.
                    // Otherwise, set the URL to null.
                    Uri.parse("http://host/path"),
                    // TODO: Make sure this auto-generated app deep link URI is correct.
                    Uri.parse("android-app://com.campusconnect.activity/http/host/path")
            );
            AppIndex.AppIndexApi.start(client, viewAction);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {

            // ATTENTION: This was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "CreatePost Page", // TODO: Define a title for the content shown.
                    // TODO: If you have web page content that matches this app activity's content,
                    // make sure this auto-generated web page URL is correct.
                    // Otherwise, set the URL to null.
                    Uri.parse("http://host/path"),
                    // TODO: Make sure this auto-generated app deep link URI is correct.
                    Uri.parse("android-app://com.campusconnect.activity/http/host/path")
            );
            AppIndex.AppIndexApi.end(client, viewAction);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewPagerAdapter_CreatePost extends FragmentPagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter_home is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter_home is created

        private Context mContext;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter_CreatePost(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context context) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
            this.mContext = context;
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            //TODO removed old api here
            webApiGetGroups();
            // CreatePostActivity.this.getGroups();
            Fragment fragment = null;
            if (position == 0) {
                fragment = new FragmentPostEvent();
                return fragment;
            } else if (position == 1) {
                fragment = new FragmentPostNews();
                return fragment;
            }
            return fragment;

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }


        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }


    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(CreatePostActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public class FragmentPostNews extends Fragment {
        RelativeLayout group_name_post;
        TextView group_selected_text_post;
        ImageView iv_upload;
        Button post;
        EditText et_title, et_description, et_tags;
        GroupBean gpBean = new GroupBean();
        int position;
        String encodedImageStr = "";
        String Clubid = "";

        private Uri fileUri;

        public FragmentPostNews() {
        }

        private static final String LOG_TAG = "CreatePostActivity";


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_post_news, container, false);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            group_name_post = (RelativeLayout) v.findViewById(R.id.group_select_when_posting);
            group_selected_text_post = (TextView) v.findViewById(R.id.tv_group_name_selected_when_posting);

            et_title = (EditText) v.findViewById(R.id.et_post_title);
            et_description = (EditText) v.findViewById(R.id.et_post_description);

            et_tags = (EditText) v.findViewById(R.id.et_tags);
            iv_upload = (ImageView) v.findViewById(R.id.iv_upload);
            post = (Button) v.findViewById(R.id.b_post);

            et_title.setTypeface(r_reg);
            et_description.setTypeface(r_reg);
            et_tags.setTypeface(r_reg);
            group_selected_text_post.setTypeface(r_reg);
            post.setTypeface(r_reg);

            //    Toast.makeText(getActivity(), "Fragment post news", Toast.LENGTH_LONG).show();
            group_name_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Group:");
                    if (CreatePostActivity.this.groupList == null) {
                        group_selected_text_post.setText("Loading Groups");
                    } else {
                        final String[] groupList = new String[CreatePostActivity.this.groupList.size()];
                        for (int i = 0; i < CreatePostActivity.this.groupList.size(); i++) {
                            groupList[i] = CreatePostActivity.this.groupList.get(i).getAbb();
                        }
                        builder.setItems(groupList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                position = item;
                                group_selected_text_post.setText(CreatePostActivity.this.groupList.get(position).getAbb());
                                // pmf.setClubId(CreatePostActivity.this.groupList.get(position).getClubId());
                                Clubid = CreatePostActivity.this.groupList.get(position).getClubId();
                                Log.e(LOG_TAG + "CLUB", Clubid + " " + group_selected_text_post.getText());
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });

            iv_upload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Start Activity To Select Image From Gallery
                  /*  Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);*/
                    //break;
                    uploadImage();

                }
            });


            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO network Check


                    if (NetworkAvailablity.hasInternetConnection(CreatePostActivity.this)) {


                        String title = et_title.getText().toString();
                        //CreatePostActivity.post.setText(test);
                        SharedPreferences
                                sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);

                        Date cDate = new Date();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                        String time = new SimpleDateFormat("HH:mm:ss").format(cDate);

                     /*   pmf.setDate(date);
                        pmf.setTime(time);
                        pmf.setTitle(et_title.getText().toString());
                        pmf.setDescription(et_description.getText().toString());
                        pmf.setPhoto(encodedImageStr);
                        pmf.setFromPid(sharedPreferences.getString(AppConstants.PERSON_PID, null));
                        pmf.getClubId();*/
                        if (title.isEmpty() || et_description.getText().toString().isEmpty() || Clubid.isEmpty()) {


                            Toast.makeText(getActivity(), "Please Fill all data", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (imageUrlForUpload.isEmpty()) {
                            Toast.makeText(getActivity(), "Please Select image again and Upload it", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            String pid = SharedpreferenceUtility.getInstance(CreatePostActivity.this).getString(AppConstants.PERSON_PID);
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("time", time);
                            jsonObject.put("date", date);
                            jsonObject.put("title", et_title.getText().toString());
                            jsonObject.put("description", et_description.getText().toString());
                            jsonObject.put("photoUrl", "" + imageUrlForUpload);
                            jsonObject.put("from_pid", pid);
                            jsonObject.put("club_id", Clubid);


                            webApiCreatePost(jsonObject);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        /*CreatePostActivity.this.createPost(pmf);*/
                    } else {
                        Toast.makeText(CreatePostActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return v;
        }

        void showAlertDialog(final File imageFile) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreatePostActivity.this);
            alertDialog.setTitle("Select image");
            alertDialog.setMessage("Do you want to Upload this image?");
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new Blob(getContext(), imageFile).execute();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    iv_upload.setImageResource(R.mipmap.upload);
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

        void uploadImage() {
            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
            return Uri.parse(path);
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

        /*    public String getRealPathFromURI(Uri uri) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }*/
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
//                        bitmap = (Bitmap) data.getExtras().get("data");
                        //  _addPhotoBitmap = bitmap;
                        iv_upload.setImageBitmap(bitmap);
                       /* bitmap = (Bitmap) data.getExtras().get("data");
                        //  _addPhotoBitmap = bitmap;
                        iv_upload.setImageBitmap(bitmap);*/
//                        iv_upload.setScaleType(ImageView.ScaleType.FIT_XY);

                       /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] _byteArray = baos.toByteArray();
                        encodedImageStr = Base64.encodeToString(_byteArray, Base64.DEFAULT);*/

                        Uri selectedImageUri = getImageUri(getContext(), bitmap);
                        File finalFile = new File(getPath(selectedImageUri, getActivity()));
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
                        String tempPath = getPath(selectedImageUri, getActivity());
                        File finalFile = new File(getPath(selectedImageUri, getActivity()));
                        if (finalFile.exists()) {
                            showAlertDialog(finalFile);

                        }
                        Bitmap bm;
                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                        bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                        iv_upload.setImageBitmap(bm);
//                        iv_upload.setScaleType(ImageView.ScaleType.FIT_XY);
                        encodedImageStr = Base64.encodeToString(getBytesFromBitmap(bm), Base64.NO_WRAP);

                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Image size is too large.Please upload small image.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }


           /* if (requestCode == GALLERY_ACTIVITY_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    String picturePath = data.getStringExtra("picturePath");
                    //perform Crop on the Image Selected from Gallery
                    performCrop(picturePath);
                }
            }

            if (requestCode == RESULT_CROP) {
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    // Set The Bitmap Data To ImageView
                    iv_upload.setImageBitmap(selectedBitmap);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    byte[] b = baos.toByteArray();
                    encodedImageStr = Base64.encodeToString(b, Base64.DEFAULT);
                    iv_upload.setScaleType(ImageView.ScaleType.CENTER);
                }
            }
        }*/
        }
    }

    public class FragmentPostEvent extends Fragment {

        RelativeLayout group_name_post;
        TextView group_selected_text_post;
        // ModelsEventMiniForm eventMiniForm = new ModelsEventMiniForm();
        EditText et_title, et_post_description, et_tags, et_venue;
        int position;
        Button post;
        TextView s_date, e_date, s_time, e_time, et_end_time;
        Calendar myCalendar_s_date, myCalendar_e_date;
        DatePickerDialog.OnDateSetListener start_date, end_date;
        int start_hour, start_min;
        Context context;
        ImageView iv_upload;
        String encodedImageStr = "";
        String clubid = "";
        private Uri fileUri;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_post_event, container, false);

            Typeface r_reg = Typeface.createFromAsset(v.getContext().getAssets(), "font/Roboto_Regular.ttf");

            group_name_post = (RelativeLayout) v.findViewById(R.id.group_select_when_posting);
            group_selected_text_post = (TextView) v.findViewById(R.id.tv_group_name_selected_when_posting);
            et_title = (EditText) v.findViewById(R.id.et_post_title);
            et_post_description = (EditText) v.findViewById(R.id.et_post_description);
            post = (Button) v.findViewById(R.id.b_post);

            s_date = (TextView) v.findViewById(R.id.et_start_date);
            e_date = (TextView) v.findViewById(R.id.et_end_date);
            s_time = (TextView) v.findViewById(R.id.et_start_time);
            e_time = (TextView) v.findViewById(R.id.et_end_time);
            et_tags = (EditText) v.findViewById(R.id.et_tags);
            et_end_time = (TextView) v.findViewById(R.id.et_end_time);
            et_venue = (EditText) v.findViewById(R.id.et_venue);
            iv_upload = (ImageView) v.findViewById(R.id.iv_upload);
            context = v.getContext();

            et_title.setTypeface(r_reg);
            et_post_description.setTypeface(r_reg);
            s_date.setTypeface(r_reg);
            e_date.setTypeface(r_reg);
            s_time.setTypeface(r_reg);
            e_time.setTypeface(r_reg);
            et_tags.setTypeface(r_reg);
            group_selected_text_post.setTypeface(r_reg);
            post.setTypeface(r_reg);

            //  Toast.makeText(getActivity(), "Fragment post", Toast.LENGTH_LONG).show();

            group_name_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Group:");
                    if (CreatePostActivity.this.groupList == null) {
                        group_selected_text_post.setText("Loading Groups");
                    } else {
                        final String[] groupList = new String[CreatePostActivity.this.groupList.size()];
                        for (int i = 0; i < CreatePostActivity.this.groupList.size(); i++) {
                            groupList[i] = CreatePostActivity.this.groupList.get(i).getAbb();
                        }
                        builder.setItems(groupList, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                position = item;
                                group_selected_text_post.setText(CreatePostActivity.this.groupList.get(position).getAbb());
                                //eventMiniForm.setClubId(CreatePostActivity.this.groupList.get(position).getClubId());
                                clubid = CreatePostActivity.this.groupList.get(position).getClubId();
                                Log.e(LOG_TAG + "CLUB", clubid + " " + group_selected_text_post);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            });

            iv_upload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    uploadImage();
                }
            });


            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //TODO network check
                    if (NetworkAvailablity.hasInternetConnection(CreatePostActivity.this)) {
                        String test = et_title.getText().toString();
                        SharedPreferences
                                sharedPreferences = v.getContext().getSharedPreferences(AppConstants.SHARED_PREFS, Context.MODE_PRIVATE);
                        Date cDate = new Date();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                        String time = new SimpleDateFormat("HH:mm:ss").format(cDate);

                        Log.e(LOG_TAG, "" + date);
                        Log.e(LOG_TAG, "" + time);
                        Log.e(LOG_TAG, "" + et_title.getText().toString());
                        Log.e(LOG_TAG, "" + et_post_description.getText().toString());

                        Log.e(LOG_TAG, "" + sharedPreferences.getString(AppConstants.COLLEGE_ID, null));
                        Log.e(LOG_TAG, "" + sharedPreferences.getString(AppConstants.PERSON_PID, null));

                        try {

                            String pid = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.PERSON_PID);
                            String isAlumni = SharedpreferenceUtility.getInstance(getActivity()).getString(AppConstants.ALUMNI);
                            if (isAlumni.equalsIgnoreCase("") || isAlumni.isEmpty()) {
                                isAlumni = "N";
                            } else {
                                isAlumni = "Y";
                            }
                            JSONObject jsonObject = new JSONObject();


                            String startDate = s_date.getText().toString();
                            String endDate = e_date.getText().toString();
                            startDate = startDate.replaceAll("/", "-");
                            startDate = startDate.replaceAll("\\/", "");
                            endDate = endDate.replace("/", "-");
                            endDate = endDate.replaceAll("\\/", "");
                            String vanue = et_venue.getText().toString();
                            String s_timestr = s_time.getText().toString();
                            String title = et_title.getText().toString();
                            String description = et_post_description.getText().toString();
                            String endTime = e_time.getText().toString();

                            if (title.isEmpty() || vanue.isEmpty() || description.isEmpty() || endTime.isEmpty() || s_timestr.isEmpty()
                                    || startDate.isEmpty() || endDate.isEmpty() || clubid.isEmpty()) {
                                Toast.makeText(getActivity(), "Please fill all data", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (imageUrlForUpload.isEmpty()) {
                                Toast.makeText(getActivity(), "Please Select image again and Upload it", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            jsonObject.put("club_id", "" + clubid);
                            jsonObject.put("date", "" + date);
                            jsonObject.put("description", description);
                            jsonObject.put("completed", "No");
                            jsonObject.put("end_date", "" + endDate);
                            jsonObject.put("start_date", "" + startDate);
                            jsonObject.put("end_time", "" + endTime);
                            jsonObject.put("venue", "" + vanue);
                            jsonObject.put("title", "" + title);
                            jsonObject.put("isAlumni", "" + isAlumni);
                            jsonObject.put("time", "" + time);
                            jsonObject.put("start_time", "" + s_timestr);
                            jsonObject.put("event_creator", "" + pid);
                            jsonObject.put("photoUrl", "" + imageUrlForUpload);
                            /* calling webserivice here*/
                            webApiCreateEvent(jsonObject);
                        } catch (Exception e) {
                        }
                        //  CreatePostActivity.this.createEvent(eventMiniForm);
                    } else {
                        Toast.makeText(CreatePostActivity.this, "Network is not available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            myCalendar_s_date = Calendar.getInstance();
            myCalendar_e_date = Calendar.getInstance();
            start_date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar_s_date.set(Calendar.YEAR, year);
                    myCalendar_s_date.set(Calendar.MONTH, monthOfYear);
                    myCalendar_s_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel_start();
                }

            };

            end_date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar_e_date.set(Calendar.YEAR, year);
                    myCalendar_e_date.set(Calendar.MONTH, monthOfYear);
                    myCalendar_e_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel_end();
                }
            };

            s_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    DatePickerDialog start_date_picker = new DatePickerDialog(context, start_date, myCalendar_s_date
                            .get(Calendar.YEAR), myCalendar_s_date.get(Calendar.MONTH),
                            myCalendar_s_date.get(Calendar.DAY_OF_MONTH));

//                    start_date_picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    start_date_picker.show();
                }
            });

            e_date.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (!s_date.getText().toString().trim().isEmpty()) {

                        DatePickerDialog end_date_picker = new DatePickerDialog(context, end_date, myCalendar_e_date
                                .get(Calendar.YEAR), myCalendar_e_date.get(Calendar.MONTH),
                                myCalendar_e_date.get(Calendar.DAY_OF_MONTH));
//                    end_date_picker.getDatePicker().setMinDate(System.currentTimeMillis() - 2000);
                        end_date_picker.show();
                    } else {
                        Toast.makeText(getActivity(), "Please enter start date", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            s_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    final int second = mcurrentTime.get(Calendar.SECOND);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            start_hour = selectedHour;
                            start_min = selectedMinute;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            calendar.set(Calendar.SECOND, 0);

                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                            s_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                            if (selectedHour > hour)
                                s_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                                //  s_time.setText(dateFormat.format(calendar.getTime()));
                            else if (selectedHour == hour) {
                                if (selectedMinute > minute)
                                    // s_time.setText(dateFormat.format(calendar.getTime()));
                                    s_time.setText("" + selectedHour + ":" + selectedMinute + ":00");

                                   /* Toast.makeText(getActivity().getApplicationContext(), "The start time you entered occurred before the current time.",
                                            Toast.LENGTH_SHORT).show();*/
                            }
                            /* Toast.makeText(getActivity().getApplicationContext(), "The start time you entered occurred before the current time.",
                                        Toast.LENGTH_SHORT).show();*/

                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });
            e_time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    final int second = mcurrentTime.get(Calendar.SECOND);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR, selectedHour);
                            calendar.set(Calendar.MINUTE, selectedMinute);
                            calendar.set(Calendar.SECOND, 0);


                            if (s_time.getText().toString().trim().isEmpty()) {
                                Toast.makeText(getActivity(), "Please enter start time", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (e_date.getText().toString().trim().isEmpty()) {
                                Toast.makeText(getActivity(), "Please enter end date", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                               /* if (myCalendar_s_date.get(Calendar.MONTH) <= myCalendar_e_date.get(Calendar.MONTH)) {

                                    if (myCalendar_s_date.get(Calendar.MONTH) == myCalendar_e_date.get(Calendar.MONTH)) {
                                        if (myCalendar_s_date.get(Calendar.DAY_OF_MONTH) > myCalendar_e_date.get(Calendar.DAY_OF_MONTH)) {
                                            e_date.setText("");
                                            Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End date is after Start date",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            e_date.setText(sdf.format(myCalendar_e_date.getTime()));
                                        }
                                    } else {
                                        e_date.setText(sdf.format(myCalendar_e_date.getTime()));
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End date is after Start date",
                                            Toast.LENGTH_SHORT).show();
                                }*/
                                if (myCalendar_s_date.get(Calendar.DAY_OF_MONTH) == myCalendar_e_date.get(Calendar.DAY_OF_MONTH)) {
                                    try {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                                        //  e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");

                                        if (selectedHour > start_hour)
                                            e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                                            //  e_time.setText(dateFormat.format(calendar.getTime()));
                                        else if (selectedHour == start_hour) {
                                            if (selectedMinute > start_min)
                                                e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                                                // e_time.setText(dateFormat.format(calendar.getTime()));
                                            else
                                                Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End time is after Start time",
                                                        Toast.LENGTH_SHORT).show();

                                        } else
                                            Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End time is after Start time",
                                                    Toast.LENGTH_SHORT).show();

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                } else {

                                    e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");


                                }


                            }









                           /* SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

                            e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");

                            if (selectedHour > start_hour)
                                e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                                //  e_time.setText(dateFormat.format(calendar.getTime()));
                            else if (selectedHour == start_hour) {
                                if (selectedMinute > start_min)
                                    e_time.setText("" + selectedHour + ":" + selectedMinute + ":00");
                                    // e_time.setText(dateFormat.format(calendar.getTime()));
                                else
                                    Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End time is after Start time",
                                            Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End time is after Start time",
                                        Toast.LENGTH_SHORT).show();*/
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });
            return v;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GALLERY_ACTIVITY_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    String picturePath = data.getStringExtra("picturePath");
                    //perform Crop on the Image Selected from Gallery
                    performCrop(picturePath);
                }
            }
            Bitmap bitmap = null;
            switch (requestCode) {
                case 0:
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), fileUri);
                        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, true);
//                        bitmap = (Bitmap) data.getExtras().get("data");
                        //  _addPhotoBitmap = bitmap;

                        iv_upload.setImageBitmap(bitmap);
//                        iv_upload.setScaleType(ImageView.ScaleType.FIT_XY);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] _byteArray = baos.toByteArray();
                        encodedImageStr = Base64.encodeToString(_byteArray, Base64.DEFAULT);


                        Uri selectedImageUri = getImageUri(getContext(), bitmap);
                        File finalFile = new File(getPath(selectedImageUri, getActivity()));
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
                        String tempPath = getPath(selectedImageUri, getActivity());
                        File finalFile = new File(tempPath);
                        if (finalFile.exists()) {
                            showAlertDialog(finalFile);
                        }
                        Bitmap bm;
                        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                        bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                        iv_upload.setImageBitmap(bm);
//                        iv_upload.setScaleType(ImageView.ScaleType.FIT_XY);
                        encodedImageStr = Base64.encodeToString(getBytesFromBitmap(bm), Base64.NO_WRAP);

                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Image size is too large.Please upload small image.", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        private void scaleImage(ImageView view, Bitmap bm) {
            Drawable drawing = view.getDrawable();
            if (drawing == null) {
                return;
            }


            int width = bm.getWidth();
            int height = bm.getHeight();
            int bounding_x = ((View) view.getParent()).getWidth();//EXPECTED WIDTH
            int bounding_y = ((View) view.getParent()).getHeight();//EXPECTED HEIGHT

            float xScale = ((float) bounding_x) / width;
            float yScale = ((float) bounding_y) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(xScale, yScale);

            Bitmap scaledBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
            width = scaledBitmap.getWidth();
            height = scaledBitmap.getHeight();
            BitmapDrawable result = new BitmapDrawable(context.getResources(), scaledBitmap);

            view.setImageDrawable(result);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.width = width;
            params.height = height;
            view.setLayoutParams(params);
        }

        void uploadImage() {
            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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


        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "image", null);
            return Uri.parse(path);
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

        private void updateLabel_start() {
        /* String myFormat = "MM/dd/yy";
            String myFormat = "yy/mm/dd"; //In which you need put here*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            s_date.setText(sdf.format(myCalendar_s_date.getTime()));
           /* if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > myCalendar_s_date.get(Calendar.DAY_OF_MONTH)) {
                s_date.setText("");
                Toast.makeText(getActivity().getApplicationContext(), "The start date you entered occurred before the current date.",
                        Toast.LENGTH_SHORT).show();

            } else {
                s_date.setText(sdf.format(myCalendar_s_date.getTime()));
            }*/
        }

        void showAlertDialog(final File imageFile) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreatePostActivity.this);
            alertDialog.setTitle("Select image");
            alertDialog.setMessage("Do you want to upload this image?");
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    new Blob(getContext(), imageFile).execute();
                }
            });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    iv_upload.setImageResource(R.mipmap.upload);
                    dialog.cancel();
                }
            });
            alertDialog.show();

        }

        private void updateLabel_end() {

            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            if (myCalendar_s_date.get(Calendar.MONTH) <= myCalendar_e_date.get(Calendar.MONTH)) {

                if (myCalendar_s_date.get(Calendar.MONTH) == myCalendar_e_date.get(Calendar.MONTH)) {
                    if (myCalendar_s_date.get(Calendar.DAY_OF_MONTH) > myCalendar_e_date.get(Calendar.DAY_OF_MONTH)) {
                        e_date.setText("");
                        Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End date is after Start date",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        e_date.setText(sdf.format(myCalendar_e_date.getTime()));
                    }
                } else {
                    e_date.setText(sdf.format(myCalendar_e_date.getTime()));
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Please make sure that End date is after Start date",
                        Toast.LENGTH_SHORT).show();
            }

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

                        case WebServiceDetails.PID_GET_GROUPS: {
                            try {
                                groupList.clear();
                                JSONObject grpJson = new JSONObject(strResponse);
                                if (grpJson.has("list")) {
                                    JSONArray grpArray = grpJson.getJSONArray("list");
                                    for (int i = 0; i < grpArray.length(); i++) {

                                        JSONObject innerGrpObj = grpArray.getJSONObject(i);
                                        String description = innerGrpObj.optString("description");
                                        String admin = innerGrpObj.optString("admin");
                                        String clubId = innerGrpObj.optString("club_id");
                                        String abb = innerGrpObj.optString("abbreviation");
                                        String name = innerGrpObj.optString("name");

                                        GroupBean bean = new GroupBean();
                                        bean.setAbb(abb);
                                        bean.setName(name);
                                        bean.setAdmin(admin);
                                        bean.setClubId(clubId);
                                        bean.setDescription(description);
                                        groupList.add(bean);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_CREATE_POST: {
                            try {
                                JSONObject createPost = new JSONObject(strResponse);
                                String status = createPost.optString("status");
                                String text = createPost.optString("text");

                                if (status.equals("1")) {
                                    Toast.makeText(CreatePostActivity.this, "Your News has been posted", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
                                } else if (status.equals("2")) {
                                    Toast.makeText(CreatePostActivity.this, "Your News has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
                                } else if (status.equals("3")) {
                                    Toast.makeText(CreatePostActivity.this, "Invalid Entries, please check", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                        case WebServiceDetails.PID_CREATE_EVENT: {
                           /* "status": "2",
                                    "text": "Could not insert",
                                    "kind": "clubs#resourcesItem",
                                    "etag": "\"JLMOwqwHg7-XCgdx_V36F7oMtu8/4_4CPmnvJ9H91631TVAP57KBGqA\""*/
                            try {
                                JSONObject grpJson = new JSONObject(strResponse);
                                String status = grpJson.optString("status");
                                String text = grpJson.optString("text");
                                //  Toast.makeText(CreatePostActivity.this, "" + text, Toast.LENGTH_SHORT).show();
                                if (status.equals("1")) {
                                    Toast.makeText(CreatePostActivity.this, "Your Event has been posted", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
                                } else if (status.equals("2")) {
                                    Toast.makeText(CreatePostActivity.this, "Your Event has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
                                } else if (status.equals("3")) {
                                    Toast.makeText(CreatePostActivity.this, "Invalid Entries, please check", Toast.LENGTH_SHORT).show();
                                    CreatePostActivity.this.finish();
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
                    Toast.makeText(CreatePostActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(CreatePostActivity.this, "SERVER_ERROR", Toast.LENGTH_LONG).show();
            }
            imageUrlForUpload = "";
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


}