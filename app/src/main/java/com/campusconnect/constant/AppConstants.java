package com.campusconnect.constant;

/**
 * Created by Rishab on 08-10-2015.
 */


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class AppConstants {



  /*  public static final String WEB_CLIENT_ID = "245400873255-u13ijemvsqbej9quq5g2i2q3caif3rp2.apps.googleusercontent.com";*/
    public static final String WEB_CLIENT_ID = "722474693619-qbigsh12fg6a7m14ca50d7m9v1la6iq4.apps.googleusercontent.com";

    public static final String MY_FLURRY_APIKEY="N26DF2K5GN459DRKK558";

    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

    public static final String SHARED_PREFS = "MyPrefs";

    public static final String EMAIL_KEY = "emailKey";

    public static final String COLLEGE_NAME = "collegeName";

    public static final String COLLEGE_LOCATION = "collegeLocation";

    public static final String COLLEGE_ID="collegeId";

    public static final String PERSON_NAME = "personName";

    public static final String PROFILE_CATEGORY = "category";

    public static final String PHONE = "phone";

    public static final String ALUMNI = "alumni";

    public static final String STUDENT = "student";

    public static final String BATCH = "batch";

    public static final String BRANCH = "branch";

    public static final String PERSON_PID = "person_pid";

    public static final String FOLLOW_CLUB_COUNT = "follow_club_count";

    public static final String MEMBER_CLUB_COUNT = "member_club_count";

    public static final String FOLLOW="follows";

    public static final String FOLLOW_NAMES="follows_names";

    public static final String PHOTO_URL="photo_url";

    public static final String GCM_GENERATED="gcm_generated";

    public static final String GCM_TOKEN="gcm_token";

    public static final String BLOB_URL="blob_url";

    public static final String PERSONAL_FEED_ARRAYLIST="personal_feed_arraylist";

    public static final String CAMPUS_FEED_ARRAYLIST="campus_feed_arraylist";

    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();

    public static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    public static final String LOG_IN_STATUS="LoggedIn";

    public static final String IS_ADMIN="isAdmin";

    public static final String IS_SUP_ADMIN="isSuperAdmin";


}