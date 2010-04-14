package com.vivilab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class UserDbAdapter {
    public static final String KEY_ID = "userid";
    public static final String KEY_PASSWD = "passwd";
    public static final String KEY_ROWID = "_id";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE =
        "create table user (_id integer primary key autoincrement, "
                + "userid text not null, passwd text not null);";
    
    private static final String DATABASE_NAME = "smth";
    private static final String DATABASE_TABLE = "user";
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "UserDbAdapter";

    private final Context mCtx;
    
    public UserDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public UserDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    public long createUser(String userid, String passwd) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, userid);
        initialValues.put(KEY_PASSWD, passwd);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public Cursor fetchUser() throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_ID, KEY_PASSWD}, null, null,
                        null, null, null, null);
        if (mCursor.getCount()>0) {
        	Log.i(TAG,"we have one user");
            mCursor.moveToFirst();
        }
        else
        	Log.i(TAG,"we have no user");
        	
        return mCursor;

    }

    
}
