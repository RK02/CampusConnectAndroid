package com.campusconnect.communicator;

/**
 * This class is used for providing all constants parameters for web api calling.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2015-02-26
 */




public class WebServiceDetails {


    /*For deveopment*/
	public static final String DEFAULT_ROOT_URL = "https://campus-connect-2015.appspot.com/_ah/api/";
//	public static final String DEFAULT_ROOT_URL = "https://campus-connect-test.appspot.com/_ah/api/";
	public static final String DEFAULT_SERVICE_PATH = "clubs/v1/";

	public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;


	public static final int PID_GET_PROFILE = 1;
	public static final int PID_GET_COLLEGE = 2;
	public static final int PID_SELECT_COLLEGE = 3;
	public static final int PID_GET_PERSONAL_FEED =4;
	public static final int PID_GET_CAMPUS_FEED =5;
	public static final int PID_GET_GROUPS =6;
	public static final int PID_FOLLOW_UP =7;
	public static final int PID_UNFOLLOW_UP =8;
	public static final int PID_SAVE_PROFILE=9;
	public static final int PID_CREATE_GROUP=10;
	public static final int PID_CREATE_POST=11;
	public static final int PID_CREATE_EVENT=12;
	public static final int PID_GET_GCM_PROFILE=13;
	public static final int PID_ATTENDING=14;
	public static final int PID_NOTIFICATION=15;
	public static final int PID_CLUB_MEMEBER_JOIN =16;
	public static final int PID_GET_CLUB_DETAIL=17;
	public static final int PID_GET_CLUB_MEMBER=18;
	public static final int PID_GET_Events=19;
	public static final int  PID_GET_CLUB=20;
	public static final int  PID_GET_CALENDER=21;
	public static final int PID_GET_ADMIN_REQUESTS=22;
	public static final int PID_GROUP_JOIN=23;
	public static final int PID_GET_SUPER_ADMIN_REQUESTS=24;
	public static final int PID_GROUP_CREATE=25;
	public static final int PID_GET_ADMIN_STATUS=26;
	public static final int PID_GET_UPDATE_STATUS=27;

}