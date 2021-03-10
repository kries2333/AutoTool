package com.kries.autotool.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "autotool.db";
    public static final String TABLE_NAME = "device";
    public SQLiteDatabase db = this.getWritableDatabase();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME +
                "(token varchar(12), " +
                "device_id varchar(20), " +
                "device_name varchar(12), " +
                "imei varchar(64), " +
                "android_os varchar(10), " +
                "model varchar(20), " +
                "memory varchar(20)," +
                "display_x varchar(10)," +
                "display_y varchar(10))";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void SetData(String token, String deviceId, String deviceName, String imei, String androidOs, String model, String memory, String displayX, String displayY) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", token);
        contentValues.put("device_id", deviceId);
        contentValues.put("device_name", deviceName);
        contentValues.put("imei", imei);
        contentValues.put("android_os", androidOs);
        contentValues.put("model", model);
        contentValues.put("memory", memory);
        contentValues.put("display_x", displayX);
        contentValues.put("display_y", displayY);

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"token", "device_id", "device_name", "imei", "android_os", "model", "memory", "display_x", "display_y"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String tokenTmp = cursor.getString(cursor.getColumnIndex("token"));
            String deviceIdTmp = cursor.getString(cursor.getColumnIndex("device_id"));
            String imeiTmp = cursor.getString(cursor.getColumnIndex("imei"));
            if (imeiTmp == "" || deviceIdTmp == "" || tokenTmp == "") {
                db.insertOrThrow(TABLE_NAME, null, contentValues);
                break;
            }
        }
    }
}
