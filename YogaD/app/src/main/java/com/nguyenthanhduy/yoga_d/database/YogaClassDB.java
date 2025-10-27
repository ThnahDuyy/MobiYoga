package com.nguyenthanhduy.yoga_d.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.ArrayList;
import java.util.List;

public class YogaClassDB implements IDatabase<YogaClass> {
    public static final String TABLE_NAME = YogaClass.class.getSimpleName();
    public static final String YOGA_CLASS_ID = "YOGA_CLASS_ID";
    public static final String NAME = "NAME";
    public static final String PRICE = "PRICE";
    public static final String CAPACITY = "CAPACITY";
    public static final String TYPE_OF_CLASS = "TYPE_OF_CLASS";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TIME_OF_COURSE = "TIME_OF_COURSE";
    public static final String DAY_OF_WEEK = "DAY_OF_WEEK";
    public static final String DURATION = "DURATION";

    private final SQLiteOpenHelper sqliteHelper;
    private static YogaClassDB instance;

    private YogaClassDB(SQLiteOpenHelper sqliteHelper) {
        this.sqliteHelper = sqliteHelper;
    }

    public static YogaClassDB GetInstance(SQLiteOpenHelper sqliteHelper) {
        if (instance == null) {
            instance = new YogaClassDB(sqliteHelper);
        }
        return instance;
    }

    public static void OnCreate(SQLiteDatabase database) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" +
                YOGA_CLASS_ID + " TEXT PRIMARY KEY, " +
                NAME + " TEXT, " +
                DAY_OF_WEEK + " INTEGER ," +
                TIME_OF_COURSE + " TEXT," +
                CAPACITY + " INTEGER, " +
                PRICE + " DOUBLE, " +
                DURATION + " INTEGER ," +
                TYPE_OF_CLASS + " TEXT ," +
                DESCRIPTION + " TEXT " +
                " ) ";
        database.execSQL(CREATE_TABLE);
    }

    public static void OnUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    @Override
    public void add(YogaClass yogaClass) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(YOGA_CLASS_ID, yogaClass.yogaClassID);
        insertValues.put(NAME, yogaClass.yogaName);
        insertValues.put(PRICE, yogaClass.getPrice());
        insertValues.put(CAPACITY, yogaClass.capacity);
        insertValues.put(DURATION, yogaClass.getDuration());
        insertValues.put(TYPE_OF_CLASS, yogaClass.typeOfClass);
        insertValues.put(DAY_OF_WEEK, yogaClass.dayOfWeek);
        insertValues.put(DESCRIPTION, yogaClass.description);
        insertValues.put(TIME_OF_COURSE, yogaClass.timeOfCourse);
        database.insert(TABLE_NAME, null, insertValues);
        database.close();
    }

    @Override
    public void update(YogaClass yogaClass) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(NAME, yogaClass.yogaName);
        updateValues.put(PRICE, yogaClass.getPrice());
        updateValues.put(CAPACITY, yogaClass.capacity);
        updateValues.put(DURATION, yogaClass.getDuration());
        updateValues.put(TYPE_OF_CLASS, yogaClass.typeOfClass);
        updateValues.put(DAY_OF_WEEK, yogaClass.dayOfWeek);
        updateValues.put(DESCRIPTION, yogaClass.description);
        updateValues.put(TIME_OF_COURSE, yogaClass.timeOfCourse);
        database.update(TABLE_NAME, updateValues, YOGA_CLASS_ID + "=?", new String[]{String.valueOf(yogaClass.yogaClassID)});
        database.close();
    }

    @Override
    public void remove(String id) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        database.delete(ClassInstanceDB.TABLE_NAME, YOGA_CLASS_ID + "=?", new String[]{String.valueOf(id)});
        database.delete(YogaClassDB.TABLE_NAME, YOGA_CLASS_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @SuppressLint("Range")
    @Override
    public YogaClass getOne(String id) {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        YogaClass yogaClass = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " + YOGA_CLASS_ID + " = ?", new String[]{id});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                yogaClass = new YogaClass(id);
                yogaClass.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
                yogaClass.yogaName = cursor.getString(cursor.getColumnIndex(NAME));
                yogaClass.capacity = cursor.getInt(cursor.getColumnIndex(CAPACITY));
                yogaClass.setDuration(cursor.getInt(cursor.getColumnIndex(DURATION)));
                yogaClass.typeOfClass = cursor.getString(cursor.getColumnIndex(TYPE_OF_CLASS));
                yogaClass.dayOfWeek = cursor.getInt(cursor.getColumnIndex(DAY_OF_WEEK));
                yogaClass.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                yogaClass.timeOfCourse = cursor.getString(cursor.getColumnIndex(TIME_OF_COURSE));
            }
            cursor.close();
        }
        database.close();
        return yogaClass;
    }

    @Override
    public List<YogaClass> getAll() {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<YogaClass> yoga_class_list = cursorToList(cursor);
        database.close();
        return yoga_class_list;
    }

    @Override
    public void clearData() {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    @SuppressLint("Range")
    public List<YogaClass> search(String teacher) {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + YogaClassDB.TABLE_NAME + " AS yc, " +
                ClassInstanceDB.TABLE_NAME + " AS ci " +
                " WHERE yc.yoga_class_id = ci.yoga_class_id " +
                " AND ci.teacher like ?", new String[]{"%" + teacher + "%"});

        List<YogaClass> yogaClassList = cursorToList(cursor);
        database.close();
        return yogaClassList;
    }

    @SuppressLint("Range")
    public List<YogaClass> advanceSearch(int dayOfWeek, String classInstanceDate) {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM " + YogaClassDB.TABLE_NAME + " AS yoga " +
                        "LEFT JOIN " + ClassInstanceDB.TABLE_NAME + " AS class_ins " +
                        "ON yoga.YOGA_CLASS_ID = class_ins.YOGA_CLASS_ID " +
                        "WHERE yoga.DAY_OF_WEEK = ? OR class_ins.DATE = ?" +
                        "GROUP BY yoga.YOGA_CLASS_ID"
                , new String[]{String.valueOf(dayOfWeek), classInstanceDate});

        List<YogaClass> yogaClassList = cursorToList(cursor);
        database.close();
        return yogaClassList;
    }

    @SuppressLint("Range")
    private List<YogaClass> cursorToList(Cursor cursor) {
        List<YogaClass> yogaList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(YOGA_CLASS_ID));
                YogaClass yogaClass = new YogaClass(id);
                yogaClass.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
                yogaClass.yogaName = cursor.getString(cursor.getColumnIndex(NAME));
                yogaClass.capacity = cursor.getInt(cursor.getColumnIndex(CAPACITY));
                yogaClass.setDuration(cursor.getInt(cursor.getColumnIndex(DURATION)));
                yogaClass.typeOfClass = cursor.getString(cursor.getColumnIndex(TYPE_OF_CLASS));
                yogaClass.dayOfWeek = cursor.getInt(cursor.getColumnIndex(DAY_OF_WEEK));
                yogaClass.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                yogaClass.timeOfCourse = cursor.getString(cursor.getColumnIndex(TIME_OF_COURSE));
                yogaList.add(yogaClass);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return yogaList;
    }
}
