package com.example.duisen.usefulnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Duisen on 10.03.2016.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String database_name = "notes";
    private static final String database_table = "notes_table";
    private static final String ID = "_id";
    private static final String NOTE = "_content";
    private static final String BACKGROUD = "_backgroud";
    private static final String DATE = "_date";

    public DBHelper(Context context,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, database_name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+database_table+"("+ID+
                " integer primary key autoincrement, "+NOTE+" text not null, "+BACKGROUD+" int not null, "+DATE+" datetime) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + database_table);
        onCreate(db);
    }

    public void addNote(String note, int backgroud, String date){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTE,note);
        cv.put(BACKGROUD,backgroud);
        cv.put(DATE,date);
        database.insert(database_table, null, cv);
        database.close();
    }

    public ArrayList<String> getAllNotes(){
        ArrayList<String> temp_content = new ArrayList<String>();
        SQLiteDatabase database = getWritableDatabase();
        String query ="select * from "+database_table+" order by "+DATE+" desc";
        Cursor c= database.rawQuery(query,null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            String note = c.getString(c.getColumnIndex(NOTE));
            String background = c.getString(c.getColumnIndex(BACKGROUD));
            String date = c.getString(c.getColumnIndex(DATE));
            temp_content.add(note+"&"+background+"&"+date);
        }
        return temp_content;
    }

    public void deleteNote(String note, String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "delete from "+database_table+" where "+NOTE+"='"+note+"'"+" and "+DATE+"='"+date+"'";
        database.execSQL(query);
        database.close();
    }

    public void updateBG(int num,String note, String date){
        SQLiteDatabase database = getWritableDatabase();
        String query = "update "+database_table+" set "+BACKGROUD+"='"+num+"'"+" where "+DATE+"='"+date+"'"+" and "+
                NOTE+"='"+note+"'";
        database.execSQL(query);
        database.close();
    }

}

