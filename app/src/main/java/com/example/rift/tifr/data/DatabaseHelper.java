package com.example.rift.tifr.data;
;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rift.tifr.Event.Event;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "notification_table";
    private static final String COL0 = "Id";
    private static final String COL1 = "Title";
    private static final String COL2 = "Date";
    private static final String COL3 = "Time";
    private static final String COL4 = "Description";
    private static final String COL5 = "Room";
    private static final String COL6 = "Url";
    private static final String COL7 = "Image";
    private static final String COL8 = "Edate";
    private static final String COL9 = "Etime";



    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table notification_table " +
                        "(Id text primary key, Title text,Date text,Time text, Description text,Room text, Url text, Image text, Edate text, Etime text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean isExist(String id) {
        db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Id = '" + id+ "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;
    }

    public int insertNotification(String id, String title, String date, String time, String description,
                                      String room, String url, String image, String edate, String etime) {

        if(isExist(id)) {
            return 0;
        }
        else if(getProfilesCount()==20) {
            return 2;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", id);
        contentValues.put("Title", title);
        contentValues.put("Date", date);
        contentValues.put("Time", time);
        contentValues.put("Description", description);
        contentValues.put("Room", room);
        contentValues.put("Url", url);
        contentValues.put("Image", image);
        contentValues.put("Edate", edate);
        contentValues.put("Etime", etime);
        long result = db.insert("notification_table", null, contentValues);

        Log.d(TAG, "insertNotification: Adding " + id + " to " + TABLE_NAME);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return 0;
        } else {
            return 1;
        }

    }

    /**
     * Returns all the data from database
     *
     * @return
     */

        public ArrayList<Event> getData() {
            ArrayList<Event> dataEventList = new ArrayList<Event>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select Id, Title, Date, Time, Description, Room, Url, Image, Edate, Etime from notification_table", null);

            while(cursor.moveToNext()) {
                dataEventList.add(new Event(cursor.getString(cursor.getColumnIndex(COL0)),cursor.getString(cursor.getColumnIndex(COL1)),
                        cursor.getString(cursor.getColumnIndex(COL2)),cursor.getString(cursor.getColumnIndex(COL3)),
                        cursor.getString(cursor.getColumnIndex(COL4)), cursor.getString(cursor.getColumnIndex(COL5)),
                        cursor.getString(cursor.getColumnIndex(COL6)),cursor.getString(cursor.getColumnIndex(COL7)),
                        cursor.getString(cursor.getColumnIndex(COL8)),cursor.getString(cursor.getColumnIndex(COL9))));
            }
            db.close();
            return dataEventList ;
        }

    // Context of application who uses us.

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;


    // Open the database connection.
    public DatabaseHelper open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }


    /**
     * Delete from database
     *
     * @param id
     */

    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL0 + "=" + id , null);
        db.close();
    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return cnt;
    }



}



























