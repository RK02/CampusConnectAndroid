package com.campusconnect.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.campusconnect.bean.GroupBean;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    String Tag = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "campusconnect";


    private static final String TABLE_CLUBS = "clubs";
    private static final String TABLE_CAMPUSFEED = "campusfeed";
    private static final String TABLE_MYFEED = "myfeed";
/*
    private String description;
	private String admin;
	private String clubId;
	private String Abb;
	private String name;
	private boolean like=false;*/

    // Group Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_ADMIN = "admin";
    private static final String KEY_CLUBID = "clubId";
    private static final String KEY_ABB = "Abb";
    private static final String KEY_FOLLOWING = "following";
    SQLiteDatabase db;

    //    ----------------------------------------------
    private static final String TABLE_FEEDS = "feeds_table";
    private static final String FEED_ID = "feed_id";
    private static final String LIKE_UNLIKE_FEED = "like_unlike_feed";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLUB_TABLE = "CREATE TABLE " + TABLE_CLUBS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_CLUBID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_ADMIN + " TEXT,"
                + KEY_ABB + " TEXT,"
                + KEY_FOLLOWING + " TEXT" + ")";


        String sql = "CREATE TABLE  IF NOT EXISTS " + TABLE_FEEDS
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FEED_ID + " TEXT , "
                + LIKE_UNLIKE_FEED + " TEXT)";
        db.execSQL(sql);


        Log.e(Tag, CREATE_CLUB_TABLE);
        db.execSQL(CREATE_CLUB_TABLE);

    }


    public void saveFeedInfo(String feedID, String likeUnlike) {
        String id = "";
        try {
            String sql = " select * from " + TABLE_FEEDS + " where feed_id = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{feedID});
            cursor.moveToNext();
            id = cursor.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id.equalsIgnoreCase("")) {
            ContentValues values = new ContentValues();
            values.put(FEED_ID, feedID);
            values.put(LIKE_UNLIKE_FEED, likeUnlike);
            db.insert(TABLE_FEEDS, "_id", values);
        } else {
            ContentValues values = new ContentValues();
            values.put(LIKE_UNLIKE_FEED, likeUnlike);
            db.update(TABLE_FEEDS, values, "feed_id = ?", new String[]{feedID});
        }
    }


    public boolean getFeedIsLike(String feedID) {
        boolean isLike = false;
        String likeUnlikeStr = "";
        if (!feedID.equalsIgnoreCase("")) {
            try {
                String sql = " select * from " + TABLE_FEEDS + " where feed_id = ?";
                Cursor cursor = db.rawQuery(sql, new String[]{feedID});
                cursor.moveToNext();
                likeUnlikeStr = cursor.getString(2);
                if (likeUnlikeStr.equalsIgnoreCase("1")) {
                    isLike = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isLike;
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLUBS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new items
    public void addGroupItem(GroupBean bean) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bean.getName()); //
        values.put(KEY_CLUBID, bean.getClubId());
        values.put(KEY_DESC, bean.getDescription());
        values.put(KEY_ADMIN, bean.getAdmin());
        values.put(KEY_ABB, bean.getAbb());
        values.put(KEY_FOLLOWING, bean.getFollow());
        // Inserting Row
        db.insert(TABLE_CLUBS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single item
    public String getFollowUnfollow(String clubid) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*Cursor cursor = db.query(TABLE_CLUBS, new String[]{
                         KEY_CLUBID,KEY_FOLLOWING}, KEY_CLUBID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);*/
//        String sql = " select " + KEY_FOLLOWING + "from " + TABLE_CLUBS + " where " + KEY_CLUBID + "= ?";
        String sql = " select * from " + TABLE_CLUBS + " where clubId = ?";
        Log.e(Tag, sql);
        Cursor cursor = db.rawQuery(sql, new String[]{clubid});
        cursor.moveToNext();
        String isFollowing = cursor.getString(6);
        return isFollowing;
    }

    public int updateFollow(String clubId, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FOLLOWING, value);
        // updating row
        return db.update(TABLE_CLUBS, values, KEY_CLUBID + " = ?",
                new String[]{clubId});
    }

    public void clearClub() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CLUBS);
        Log.d("DB", "Cleared DB Data");

    }

    public ArrayList<GroupBean> getAllClubData() {
        ArrayList<GroupBean> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select * from " + TABLE_CLUBS;

        Cursor cursor = db.rawQuery(sql, null);


        if (cursor.moveToFirst()) {
            do {
                GroupBean bean = new GroupBean();
                bean.setClubId(cursor.getString(1));
                bean.setName(cursor.getString(2));
                bean.setDescription(cursor.getString(3));
                bean.setAdmin(cursor.getString(4));
                bean.setAbb(cursor.getString(5));
                bean.setFollow(cursor.getString(6));
                list.add(bean);
            } while (cursor.moveToNext());
        }

        return list;
    }

    public boolean didClubExist(String clubid) {

        SQLiteDatabase db = this.getWritableDatabase();
        boolean retBool = false;
        try {
            Cursor c = db.query(TABLE_CLUBS, null, KEY_CLUBID + "=?",
                    new String[]{String.valueOf(clubid)}, null, null, null);

            c.moveToFirst();

            if (!c.isAfterLast()) {

                retBool = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retBool;
    }


    public ArrayList<GroupBean> getFollowingClubData() {
        ArrayList<GroupBean> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select * from " + TABLE_CLUBS + " where " + KEY_FOLLOWING + "= ?";
        ;

        Cursor cursor = db.rawQuery(sql, new String[]{"1"});


        if (cursor.moveToFirst()) {
            do {
                Log.d("DB", cursor.getString(1) + " " + cursor.getString(2) + cursor.getString(3) + cursor.getString(4) + cursor.getString(5) + cursor.getString(6));
                GroupBean bean = new GroupBean();
                bean.setClubId(cursor.getString(1));
                bean.setAdmin(cursor.getString(2));
                bean.setDescription(cursor.getString(3));
                bean.setAbb(cursor.getString(5));
                //bean.setAbb(cursor.getString(5));
                bean.setFollow(cursor.getString(6));
                list.add(bean);
            } while (cursor.moveToNext());
        }

        return list;
    }


}






