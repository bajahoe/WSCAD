package com.example.wscad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//
public class DbOpenHelper {
    private static final String DATABASE_NAME = "status(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        //DatabaseHelper(): 생성자.
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //onCreate(): 테이블을 생성. 다른 테이블 명칭을 추가하여 작성하면 하나의 데이터베이스에서 여러 테이블도 생성 가능.
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        //onUpgrade(): 버전 업그레이드 시 사용. 이전 버전을 지우고 새 버전을 생성.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    //open(): 해당 데이터베이스를 열어서 사용할 수 있도록 해줌. getWritableDatabase()는 데이터베이스를 읽고 쓸 수 있도록 해줌.
    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    //close(): 해당 데이터베이스를 닫음. 사용중엔 상관 없으나 작업 후에는 닫는걸 권장.
    public void close(){
        mDB.close();
    }

    //데이터 삽입.
    public long insertColumn(String userid, String name, long age , String gender){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.USERID, userid);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.AGE, age);
        values.put(DataBases.CreateDB.GENDER, gender);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    //데이터 선택
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    //데이터 정렬
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM usertable ORDER BY " + sort + ";", null);
        return c;
    }

    //갱신
    public boolean updateColumn(long id, String userid, String name, long age , String gender){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.USERID, userid);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.AGE, age);
        values.put(DataBases.CreateDB.GENDER, gender);
        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    //삭제
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }
    
    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
}
/*package com.example.wscad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
    private static final String DATABASE_NAME = "status(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        //DatabaseHelper(): 생성자.
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //onCreate(): 테이블을 생성. 다른 테이블 명칭을 추가하여 작성하면 하나의 데이터베이스에서 여러 테이블도 생성 가능.
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        //onUpgrade(): 버전 업그레이드 시 사용. 이전 버전을 지우고 새 버전을 생성.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context){
        this.mCtx = context;
    }

    //open(): 해당 데이터베이스를 열어서 사용할 수 있도록 해줌. getWritableDatabase()는 데이터베이스를 읽고 쓸 수 있도록 해줌.
    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    //close(): 해당 데이터베이스를 닫음. 사용중엔 상관 없으나 작업 후에는 닫는걸 권장.
    public void close(){
        mDB.close();
    }

    //데이터 삽입.
    public long insertColumn(String userid, String date, String time , String bpm, String status){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.USERID, userid);
        values.put(DataBases.CreateDB.DATE, date);
        values.put(DataBases.CreateDB.TIME, time);
        values.put(DataBases.CreateDB.BPM, bpm);
        values.put(DataBases.CreateDB.STATUS, status);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    //데이터 선택
    public Cursor selectColumns(){
        return mDB.query(DataBases.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }

    //데이터 정렬
    public Cursor sortColumn(String sort){
        Cursor c = mDB.rawQuery( "SELECT * FROM usertable ORDER BY " + sort + ";", null);
        return c;
    }

    //갱신
    public boolean updateColumn(long id, String userid, String date, String time , String bpm, String status){
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.USERID, userid);
        values.put(DataBases.CreateDB.DATE, date);
        values.put(DataBases.CreateDB.TIME, time);
        values.put(DataBases.CreateDB.BPM, bpm);
        values.put(DataBases.CreateDB.STATUS, status);
        return mDB.update(DataBases.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    //삭제
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }

    public boolean deleteColumn(long id){
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }
}
*/