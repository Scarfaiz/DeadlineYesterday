package com.example.jeavie.deadlineyesterday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by jeavie on 29.01.2018.
 */

public class DbActivity extends SQLiteOpenHelper {


    private static final String DB_NAME = "JDB";
    private static final int DB_VER = 1;
    public static final String DB_TABLE = "Deadlines";

    // поля таблицы для хранения данных (id формируется автоматически)
    public static final String DB_ID = "_id";
    public static final String DB_SUMMARY = "summary";
    public static final String DB_DATE = "date";
    public static final String DB_TIME = "time";
    public static final String DB_DEADLINE = "deadline";
    public static final String DB_TAGS = "tags";

    private static final String DATABASE_CREATE = "create table " + DB_TABLE + "(" + DB_ID + " integer primary key autoincrement, " + DB_SUMMARY + " text not null, " + DB_DATE + " text not null, " + DB_TIME + " text not null, " + DB_DEADLINE + " text not null, " + DB_TAGS + " text not null " + ");";

    public DbActivity(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        ContentValues initialValues = createContentValues("kmkmsdkad","","", "", "");
        db.insert(DB_TABLE, null, initialValues);
    }

    private ContentValues createContentValues(String summary, String date,
                                              String time, String deadline, String tags) {
        ContentValues values = new ContentValues();
        values.put(DB_SUMMARY, summary);
        values.put(DB_DATE, date);
        values.put(DB_TIME, time);
        values.put(DB_DEADLINE, deadline);
        values.put(DB_TAGS, tags);
        return values;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS table1");
        onCreate(db);
    }

    public long createNewTable(String summary, String date,
                               String time, String deadline, String tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentValues(summary, date, time, deadline, tags);

        long row = db.insert(DB_TABLE, null, initialValues);
        db.close();

        return row;
    }

    public boolean updateTable(long rowId, String summary, String date,
                               String time, String deadline, String tags) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentValues(summary, date, time, deadline, tags);

        return db.update(DB_TABLE, updateValues, DB_ID + "=" + rowId,
                null) > 0;
    }

    public Cursor getTable(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DB_TABLE,
                new String[] { DB_SUMMARY,
                        DB_DATE, DB_TIME, DB_DEADLINE, DB_TAGS}, DB_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_ID + "=" + rowId, null);
        db.close();
    }

    public Cursor getFullTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DB_TABLE, new String[] { DB_SUMMARY,
                        DB_DATE, DB_TIME, DB_DEADLINE, DB_TAGS}, null,
                null, null, null, null);
    }
}
