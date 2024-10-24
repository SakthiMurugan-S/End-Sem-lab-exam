package com.example.end_sem_lab1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "currency_converter.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "conversion_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FROM_CURRENCY = "from_currency";
    private static final String COLUMN_TO_CURRENCY = "to_currency";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_RESULT = "result";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FROM_CURRENCY + " TEXT, " +
                COLUMN_TO_CURRENCY + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_RESULT + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addConversion(String fromCurrency, String toCurrency, double amount, double result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FROM_CURRENCY, fromCurrency);
        values.put(COLUMN_TO_CURRENCY, toCurrency);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_RESULT, result);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllConversions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}

