package com.example.chamadasrecebidas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "numberDb";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase) {
        try {
            String sql = "CREATE TABLE " + DbContract.TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + DbContract.INCOMING_NUMBER + " text);";
            sqliteDatabase.execSQL(sql);
        } catch(Exception e) {
            Log.i("INFO", "Erro ao criar tabela!");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, int i, int i1) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + DbContract.TABLE_NAME;
        sqliteDatabase.execSQL(DROP_TABLE);
        onCreate(sqliteDatabase);
    }

    public void saveNumber(String number, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.INCOMING_NUMBER, number);
        database.insert(DbContract.TABLE_NAME, null, contentValues);
    }

    public Cursor readNumber(SQLiteDatabase database) {
        String[] projection = {"id", DbContract.INCOMING_NUMBER};
        Cursor res = database.query(DbContract.TABLE_NAME, projection, null, null, null, null, null);
        return res;
    }

    public void deleteNumber(int id, SQLiteDatabase database) {
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };
        database.delete(DbContract.TABLE_NAME, selection, selectionArgs);
    }

}
