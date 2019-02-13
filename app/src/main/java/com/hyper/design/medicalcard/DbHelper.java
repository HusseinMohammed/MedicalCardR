package com.hyper.design.medicalcard;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Hyper Design Hussien on 12/30/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String TAG = DbHelper.class.getSimpleName();

    // User name (make variable public to access from outside)
    public static final String DB_NAME = "myapp.db";

    // User name (make variable public to access from outside)
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // User name (make variable public to access from outside)
    public static final String KEY_PHONE = "phone";

    // User name (make variable public to access from outside)
    public static final String KEY_IMAGE = "image";

    public static final String CREATE_TABLE_USERS = "CREATE TABLE" + USER_TABLE + "("
            + KEY_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + "TEXT,"
            + KEY_EMAIL + "TEXT,"
            + KEY_PHONE + "TEXT,"
            + KEY_IMAGE + "TEXT);";


    // User name (make variable public to access from outside)
    public static final String KEY_CODE = "client_id";
    public DbHelper(Context context){ //, String name, SQLiteDatabase.CursorFactory factory, int version
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+USER_TABLE);
        onCreate(sqLiteDatabase);
    }

    /*
    * Storing user details in database*/
    public void addUser(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        long id = sqLiteDatabase.insert(USER_TABLE, null, contentValues);
        sqLiteDatabase.close();

        Log.i(TAG, "user inserted" + id);
    }
}
