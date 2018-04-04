package com.example.jeavie.deadlineyesterday.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jeavie on 29.01.2018.
 */

public class DbActivity extends SQLiteOpenHelper {


    public static final String DB_NAME = "JDB";
    public static final String DB_TABLE = "Deadlines";
    private static final int DB_VER = 1;

    public static final String DB_ID = "id";
    public static final String DB_NUMBER = "number";
    public static final String DB_SUMMARY = "summary";
    public static final String DB_DATE = "date";
    public static final String DB_TIME = "time";
    public static final String DB_DEADLINE = "deadline";
    public static final String DB_LABELS = "labels";

    private static final String DATABASE_CREATE = "create table " + DB_TABLE +
            " ( " + DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_NUMBER + " TEXT NOT NULL, " + DB_SUMMARY +
            " TEXT NOT NULL, " + DB_DATE + " TEXT NOT NULL, " + DB_TIME +
            " TEXT NOT NULL, " + DB_DEADLINE + " TEXT NOT NULL, " + DB_LABELS + " TEXT NOT NULL" + " )";

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
                               String time, String deadline, String labels) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_NUMBER, number);
        contentValues.put(DB_SUMMARY, summary);
        contentValues.put(DB_DATE, date);
        contentValues.put(DB_TIME, time);
        contentValues.put(DB_DEADLINE, deadline);
        contentValues.put(DB_LABELS, labels);
        long result = db.insert(DB_TABLE, null, contentValues);
        return (result != -1);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + DB_TABLE, null);
    }

    public boolean updateData(String id, String number, String summary, String date,
                              String time, String deadline, String labels) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_NUMBER, number);
        contentValues.put(DB_SUMMARY, summary);
        contentValues.put(DB_DATE, date);
        contentValues.put(DB_TIME, time);
        contentValues.put(DB_DEADLINE, deadline);
        contentValues.put(DB_LABELS, labels);
        db.update(DB_TABLE, contentValues, DB_ID + " = ?", new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DB_TABLE, DB_ID + " = ?", new String[]{id});
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(true, DB_TABLE,
                new String[] { DB_NUMBER, DB_SUMMARY,
                        DB_DATE, DB_TIME, DB_DEADLINE, DB_LABELS},
                DB_ID + " = ?",
                new String[]{id},
                null,null,null,null
        );
    }

}
