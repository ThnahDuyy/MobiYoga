package com.nguyenthanhduy.yoga_d.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nguyenthanhduy.yoga_d.model.ClassInstance;

import java.util.ArrayList;
import java.util.List;

public class ClassInstanceDB implements IDatabase<ClassInstance> {
    public static final String TABLE_NAME = ClassInstance.class.getSimpleName();
    public static final String CLASS_INSTANCE_ID = "CLASS_INSTANCE_ID";
    public static final String TEACHER = "TEACHER";
    public static final String DATE = "DATE";
    public static final String COMMENT = "COMMENT";
    private final SQLiteOpenHelper sqliteHelper;
    private static ClassInstanceDB instance;

    private ClassInstanceDB(SQLiteOpenHelper sqliteHelper) {
        this.sqliteHelper = sqliteHelper;
    }

    public static ClassInstanceDB GetInstance(SQLiteOpenHelper sqliteHelper) {
        if (instance == null) {
            instance = new ClassInstanceDB(sqliteHelper);
        }
        return instance;
    }

    public static void OnUpgrade(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static void OnCreate(SQLiteDatabase database) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" +
                CLASS_INSTANCE_ID + " TEXT PRIMARY KEY, " +
                YogaClassDB.YOGA_CLASS_ID + " TEXT, " +
                COMMENT + " TEXT, " +
                DATE + " TEXT ," +
                TEACHER + " TEXT" +
                ")";
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void clearData() {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    @Override
    public void add(ClassInstance classInstance) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(CLASS_INSTANCE_ID, classInstance.classInstanceID);
        insertValues.put(YogaClassDB.YOGA_CLASS_ID, classInstance.yogaClassID);
        insertValues.put(TEACHER, classInstance.teacher);
        insertValues.put(DATE, classInstance.date);
        insertValues.put(COMMENT, classInstance.comment);
        database.insert(TABLE_NAME, null, insertValues);
        database.close();
    }

    @Override
    public void update(ClassInstance classInstance) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(CLASS_INSTANCE_ID, classInstance.yogaClassID);
        updateValues.put(YogaClassDB.YOGA_CLASS_ID, classInstance.yogaClassID);
        updateValues.put(TEACHER, classInstance.teacher);
        updateValues.put(DATE, classInstance.date);
        updateValues.put(COMMENT, classInstance.comment);
        database.update(TABLE_NAME, updateValues, CLASS_INSTANCE_ID + "=?", new String[]{String.valueOf(classInstance.classInstanceID)});
        database.close();
    }

    @Override
    public void remove(String id) {
        SQLiteDatabase database = this.sqliteHelper.getWritableDatabase();
        database.delete(TABLE_NAME, CLASS_INSTANCE_ID + "=?", new String[]{String.valueOf(id)});
        database.close();
    }

    @Override
    public ClassInstance getOne(String id) {
        return null;
    }

    @SuppressLint("Range")
    public List<ClassInstance> getByYogaId(String id) {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + YogaClassDB.YOGA_CLASS_ID + " = ?", new String[]{String.valueOf(id)});
        List<ClassInstance> classInstanceList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String classInstanceId = cursor.getString(cursor.getColumnIndex(CLASS_INSTANCE_ID));
                ClassInstance classInstance = new ClassInstance(classInstanceId);
                classInstance.yogaClassID = cursor.getString(cursor.getColumnIndex(YogaClassDB.YOGA_CLASS_ID));
                classInstance.comment = cursor.getString(cursor.getColumnIndex(COMMENT));
                classInstance.date = cursor.getString(cursor.getColumnIndex(DATE));
                classInstance.teacher = cursor.getString(cursor.getColumnIndex(TEACHER));
                classInstanceList.add(classInstance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return classInstanceList;
    }

    @SuppressLint("Range")
    @Override
    public List<ClassInstance> getAll() {
        SQLiteDatabase database = this.sqliteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<ClassInstance> classInstanceList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(CLASS_INSTANCE_ID));
                ClassInstance classInstance = new ClassInstance(id);
                classInstance.yogaClassID = cursor.getString(cursor.getColumnIndex(YogaClassDB.YOGA_CLASS_ID));
                classInstance.comment = cursor.getString(cursor.getColumnIndex(COMMENT));
                classInstance.date = cursor.getString(cursor.getColumnIndex(DATE));
                classInstance.teacher = cursor.getString(cursor.getColumnIndex(TEACHER));
                classInstanceList.add(classInstance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return classInstanceList;
    }
}
