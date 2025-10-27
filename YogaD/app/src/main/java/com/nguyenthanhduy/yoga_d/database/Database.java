package com.nguyenthanhduy.yoga_d.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static Database instance;

    private static final String DB_NAME = "yoga_duy.db";
    private static final int DB_VERSION = 1;

    public static Database GetInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    private Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        YogaClassDB.OnCreate(db);
        ClassInstanceDB.OnCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        YogaClassDB.OnUpgrade(db);
        ClassInstanceDB.OnUpgrade(db);
    }

    public YogaClassDB getYogaClassRepository() {
        return YogaClassDB.GetInstance(this);
    }

    public ClassInstanceDB getClassInstanceRepository() {
        return ClassInstanceDB.GetInstance(this);
    }
}
