package com.rimon.nirmaltest;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.rimon.nirmaltest.SqlLiteTest.model.UserData;
import com.rimon.nirmaltest.appconfig.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class AppController extends Application {
    // Overriding this method is totally optional!
    public static Activity mActivity;
    private static AppController mInstance;

    /******** if debug is set true then it will show all Logcat message *******/
    public static final boolean DEBUG = true;

    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    /******************** Table Fields ************/
    public static final String KEY_ID = "id";

    public static final String KEY_USER_NAME = "user_name";

    public static final String KEY_USER_EMAIL = "user_email";


    /******************** Database Name ************/
    public static final String DATABASE_NAME = "DB_sqllite";

    /**** Database Version (Increase one if want to also upgrade your database) ***/
    public static final int DATABASE_VERSION = 1;// started at 1

    /** Table names */
    public static final String USER_TABLE = "tbl_user";

    /******* Set all table with comma seperated like USER_TABLE,ABC_TABLE *******/
    private static final String[ ] ALL_TABLES = { USER_TABLE };

    /** Create table syntax */
    private static final String USER_CREATE =
            "create table tbl_user( _id integer primary key autoincrement," +
                    "user_name text not null," +
                    "user_email text not null);";

    /******************** Used to open database in syncronized way ************/
    private static AppController.DataBaseHelper DBHelper = null;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInit(this);
        // Required initialization logic here!
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                //mNetworkReceiver = new MyNetworkReceiver();

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = activity;
                //mNetworkReceiver = new MyNetworkReceiver();
                // registerNetworkBroadcastForNougat();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mActivity = null;
                //unregisterReceiver(mNetworkReceiver);
                Log.d("MyApplicationTest", "onActivityResumed: un-registered");
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // unregisterReceiver(mNetworkReceiver);
            }
        });

    }

    private void parseAppConfig() {
        //others
        AppConfig.BASE_URL = getResources().getString(R.string.BASE_URL);
        AppConfig.API_KEY = getResources().getString(R.string.api_key);
    }




    private void appInit(Context context) {
        mInstance = this;
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new AppController.DataBaseHelper(context);
        }
        parseAppConfig();
    }

    /********** Main Database creation INNER class ********/
    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
                db.execSQL(USER_CREATE);


            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /***** Open database for insert,update,delete in syncronized manner *****/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    /****************** General functions*******************/


    /********** Escape string for single quotes (Insert,Update) *******/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace(", );
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }


    /********* UnEscape string for single quotes (show data) *******/
/*
    private static String sqlUnEscapeString(String aString) {

        String aReturn ="" ;

        if (null != aString) {
            aReturn = aString.replace(, );
        }

        return aReturn;
    }
*/

    /********* User data functons *********/

    public static void addUserData(UserData uData) {

        // Open database for Read / Write

        final SQLiteDatabase db = open();

        String name = sqlEscapeString(uData.getName());
        String email = sqlEscapeString(uData.getEmail());
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_USER_NAME, name);
        cVal.put(KEY_USER_EMAIL, email);
        // Insert user values in database
        db.insert(USER_TABLE, null, cVal);
        db.close(); // Closing database connection
    }


    // Updating single data
    public static int updateUserData(UserData data) {

        final SQLiteDatabase db = open();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, data.getName());
        values.put(KEY_USER_EMAIL, data.getEmail());

        // updating row
        return db.update(USER_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }

    // Getting single contact
    public static UserData getUserData(int id) {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(USER_TABLE, new String[] { KEY_ID,
                        KEY_USER_NAME, KEY_USER_EMAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        UserData data = new UserData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        // return user data
        return data;
    }

    // Getting All User data
    public static List<UserData> getAllUserData() {

        List<UserData> contactList = new ArrayList<UserData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserData data = new UserData();
                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setEmail(cursor.getString(2));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }



    // Deleting single contact
    public static void deleteUserData(UserData data) {
        final SQLiteDatabase db = open();
        db.delete(USER_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }

    // Getting dataCount

    public static int getUserDataCount() {

        final SQLiteDatabase db = open();

        String countQuery = "SELECT  * FROM " + USER_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }




}