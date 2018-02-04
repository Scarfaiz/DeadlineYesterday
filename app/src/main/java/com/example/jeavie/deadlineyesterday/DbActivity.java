package com.example.jeavie.deadlineyesterday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeavie on 29.01.2018.
 */

public class DbActivity extends SQLiteOpenHelper {


    private static final String DB_NAME = "JDB";
    public static final String DB_TABLE = "Deadlines";
    private static final int DB_VER = 1;

    public static final String DB_ID = "id";
    public static final String DB_NUMBER = "number";
    public static final String DB_SUMMARY = "summary";
    public static final String DB_DATE = "date";
    public static final String DB_TIME = "time";
    public static final String DB_DEADLINE = "deadline";
    public static final String DB_TAGS = "tags";
    public static final String DB_LIST = "list";

    private static final String DATABASE_CREATE = "create table " + DB_TABLE + " ( " + DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_NUMBER + " TEXT NOT NULL, " + DB_SUMMARY + " TEXT NOT NULL, " + DB_DATE + " TEXT NOT NULL, " + DB_TIME + " TEXT NOT NULL, " + DB_DEADLINE + " TEXT NOT NULL, " + DB_TAGS + " TEXT NOT NULL, " + DB_LIST + " TEXT NOT NULL" + " )";

    public DbActivity(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    public boolean insertData (String number, String summary, String date,
                               String time, String deadline, String tags, String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_NUMBER, number);
        contentValues.put(DB_SUMMARY, summary);
        contentValues.put(DB_DATE, date);
        contentValues.put(DB_TIME, time);
        contentValues.put(DB_DEADLINE, deadline);
        contentValues.put(DB_TAGS, tags);
        contentValues.put(DB_LIST, list);
        long result = db.insert(DB_TABLE, null, contentValues);
        return (result != -1);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + DB_TABLE, null);
    }

    public boolean updateData(String id, String number, String summary, String date,
                              String time, String deadline, String tags, String list) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_NUMBER, number);
        contentValues.put(DB_SUMMARY, summary);
        contentValues.put(DB_DATE, date);
        contentValues.put(DB_TIME, time);
        contentValues.put(DB_DEADLINE, deadline);
        contentValues.put(DB_TAGS, tags);
        contentValues.put(DB_LIST, list);
        db.update(DB_TABLE, contentValues, DB_ID + " = ?", new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, DB_ID + " = ?", new String[]{id});
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.query(true, DB_TABLE,
                new String[] { DB_NUMBER, DB_SUMMARY,
                        DB_DATE, DB_TIME, DB_DEADLINE, DB_TAGS}, DB_ID + "=" + id, null,
                null, null, null, null);
        if (data != null) {
            data.moveToFirst();
        }
        return data;
    }

//    public void deleteTable(long rowId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(DB_TABLE, DB_ID + "=" + rowId, null);
//        db.close();
//    }
//
}
