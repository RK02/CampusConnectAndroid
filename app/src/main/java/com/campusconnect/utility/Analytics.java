package com.campusconnect.utility;

import android.content.Context;

import com.campusconnect.constant.AppConstants;
import com.flurry.android.FlurryAgent;

import java.util.HashMap;

/**
 * Created by rkd on 22/12/15.
 */
public class Analytics {
    public static void startSession(Context context,HashMap<String,String> flurryParams) {
        String eventName=flurryParams.get("EventName");
        HashMap<String,String> pass=flurryParams;
        pass.remove("EventName");
        FlurryAgent.logEvent(eventName,pass,true);
        FlurryAgent.onStartSession(context, AppConstants.MY_FLURRY_APIKEY);
    }
    public static void stopSession(Context context,HashMap<String,String> flurryParams) {
        String eventName=flurryParams.get("EventName");
        FlurryAgent.endTimedEvent(eventName);
        FlurryAgent.onEndSession(context);
    }
}
