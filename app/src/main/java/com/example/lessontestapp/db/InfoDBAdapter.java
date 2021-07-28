package com.example.lessontestapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import com.example.lessontestapp.dao.MyFriendInfoDao;
import com.example.lessontestapp.dao.MyInfoDao;
import com.example.lessontestapp.dao.MyMajorDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoDBAdapter {

    private SQLiteDatabase sqldb;
    private MyInfoDBOpenHelper myInfoDBOpenHelper;
    private Context context;

    private static final String DB_MAJOR_TABLE_NAME = "major_info";
    private static final String DB_MAJOR_KEY_ID = "major_id";
    private static final String DB_MAJOR_KEY_NAME = "major_name";

    private static final String DB_BASE_TABLE_NAME = "base_info";
    private static final String DB_BASE_KEY_ID = "info_id";
    private static final String DB_BASE_KEY_NAME = "info_name";
    private static final String DB_BASE_KEY_PHONE = "info_phone";
    private static final String DB_BASE_KEY_GENDER = "info_gender";
    private static final String DB_BASE_KEY_HOBBY = "info_hobby";
    private static final String DB_BASE_KEY_BIRTHPLACE = "info_birthplace";
    private static final String DB_BASE_KEY_MAJORID = "info_majorid";
    private static final String DB_BASE_KEY_FOCUS = "info_focus";
    private static final String DB_BASE_KEY_PROFILE = "info_profile";
    private static final String DB_BASE_KEY_PASSWORD = "info_password";
    private static final String DB_BASE_KEY_BALANCE = "info_balance";

    private static final String DB_SELF_TABLE_NAME = "self_info";
    private static final String DB_SELF_KEY_ID = "self_id";
    private static final String DB_SELF_KEY_NAME = "self_name";
    private static final String DB_SELF_KEY_PHONE = "self_phone";
    private static final String DB_SELF_KEY_GENDER = "self_gender";
    private static final String DB_SELF_KEY_HOBBY = "self_hobby";
    private static final String DB_SELF_KEY_BIRTHPLACE = "self_birthplace";
    private static final String DB_SELF_KEY_MAJORID = "self_majorid";
    private static final String DB_SELF_KEY_PROFILE = "self_profile";


    public InfoDBAdapter(Context context){
        this.context = context;
    }
    public InfoDBAdapter(){

    }

    public static class MyInfoDBOpenHelper extends SQLiteOpenHelper{

        private static final String DB_CREATE_TABLE_MAJOR = "CREATE TABLE " + DB_MAJOR_TABLE_NAME + " (" +
                                                DB_MAJOR_KEY_ID + " integer primary key autoincrement, " +
                                                DB_MAJOR_KEY_NAME + " text not null unique);";

        private static final String DB_CREATE_TABLE_BASE = "CREATE TABLE " + DB_BASE_TABLE_NAME + " (" +
                DB_BASE_KEY_ID + " integer primary key autoincrement, " +
                DB_BASE_KEY_NAME + " text not null, " +
                DB_BASE_KEY_PHONE + " text not null unique, " +
                DB_BASE_KEY_GENDER + " text default '男', " +
                DB_BASE_KEY_HOBBY + " text, " +
                DB_BASE_KEY_BIRTHPLACE + " text, " +
                DB_BASE_KEY_MAJORID + " integer, " +
                DB_BASE_KEY_FOCUS + " integer default 0," +
                DB_BASE_KEY_PROFILE + " text, " +
                DB_BASE_KEY_PASSWORD + " text, "+
                DB_BASE_KEY_BALANCE + " integer, "+
                "foreign KEY (" +DB_BASE_KEY_MAJORID+ ") REFERENCES " +DB_MAJOR_TABLE_NAME+"(DB_MAJOR_KEY_ID));";

        private static final String DB_CREATE_TABLE_SELF = "CREATE TABLE " + DB_SELF_TABLE_NAME + " (" +
                DB_SELF_KEY_ID + " integer primary key autoincrement, " +
                DB_SELF_KEY_NAME + " text not null, " +
                DB_SELF_KEY_PHONE + " text not null unique, " +
                DB_SELF_KEY_GENDER + " text default '男', " +
                DB_SELF_KEY_HOBBY + " text, " +
                DB_SELF_KEY_BIRTHPLACE + " text, " +
                DB_SELF_KEY_MAJORID + " integer, " +
                DB_SELF_KEY_PROFILE + " text);";

        public MyInfoDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_MAJOR);
            db.execSQL("insert into " + DB_MAJOR_TABLE_NAME + " values (1,'计算机')");
            db.execSQL("insert into " + DB_MAJOR_TABLE_NAME + " values (2,'软件工程')");
            db.execSQL("insert into " + DB_MAJOR_TABLE_NAME + " values (3,'物联网')");
            db.execSQL(DB_CREATE_TABLE_BASE);
            db.execSQL(DB_CREATE_TABLE_SELF);
            db.execSQL("insert into " + DB_SELF_TABLE_NAME +
                    " values (1,'DJ11','12345678901','男','[运动, 其他]','江苏省',2,'')");

            db.execSQL("insert into " + DB_BASE_TABLE_NAME +
                    " values (1,'YY','12345677','男','[运动, 其他]','',2,0,'','123456','250')");
            db.execSQL("insert into " + DB_BASE_TABLE_NAME +
                    " values (2,'WYB','12345678','男','[运动, 其他]','',2,0,'','123457','50')");
            db.execSQL("insert into " + DB_BASE_TABLE_NAME +
                    " values (3,'XZ','12345679','男','[运动, 其他]','',2,0,'','123458','100')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void open(){
        myInfoDBOpenHelper = new MyInfoDBOpenHelper(context, "info", null, 1);
        try {
            sqldb = myInfoDBOpenHelper.getWritableDatabase();
        }catch (Exception e){
            sqldb = myInfoDBOpenHelper.getReadableDatabase();
        }
    }

    public void close(){
        if (sqldb != null){
            sqldb.close();
            sqldb = null;
        }
    }

    /*
    * 插入操作
    * */

    //专业表插入
    public long insertMajor(MyMajorDao majorDao){
        ContentValues values = new ContentValues();
        values.put(DB_MAJOR_KEY_NAME,majorDao.getMajorName());
        return sqldb.insert(DB_MAJOR_TABLE_NAME, null, values);

    }
    //基本信息表的插入
    public long insertBaseInfo(MyFriendInfoDao friendInfoDao){
        ContentValues values = new ContentValues();
        values.put(DB_BASE_KEY_NAME,friendInfoDao.getName());
        values.put(DB_BASE_KEY_PHONE,friendInfoDao.getPhone());
        values.put(DB_BASE_KEY_GENDER,friendInfoDao.getGender());
        values.put(DB_BASE_KEY_FOCUS,friendInfoDao.getFocus());
        values.put(DB_BASE_KEY_HOBBY,friendInfoDao.getHobbies().toString());
        values.put(DB_BASE_KEY_BIRTHPLACE,friendInfoDao.getBirthPlace());
        values.put(DB_BASE_KEY_MAJORID,friendInfoDao.getSpecialtyID());
        values.put(DB_BASE_KEY_PROFILE,friendInfoDao.getProfile());
        values.put(DB_BASE_KEY_PASSWORD,friendInfoDao.getPassword());
        values.put(DB_BASE_KEY_BALANCE,friendInfoDao.getBalance());
        return sqldb.insert(DB_BASE_TABLE_NAME,null,values);
    }

    /*
    * 删除操作
    * */
    public void deleteBaseInfo(String phoneStr){
        sqldb.execSQL("delete from "+DB_BASE_TABLE_NAME+" where info_phone = "+phoneStr+";");
    }


    /*
    * 更新操作
    * */
    //基本信息表的更新操作
    public long updateFocusBaseInfo(MyFriendInfoDao friendInfoDao, long id){
        ContentValues values = new ContentValues();
        values.put(DB_BASE_KEY_FOCUS,friendInfoDao.getFocus());
        return sqldb.update(DB_BASE_TABLE_NAME, values, DB_BASE_KEY_ID + "="+id,null);
    }
    //编辑页面更新好友数据操作
    public long updateFriendsAllBaseInfo(MyFriendInfoDao friendInfoDao, long id){

        ContentValues values = new ContentValues();
        values.put(DB_BASE_KEY_NAME,friendInfoDao.getName());
        values.put(DB_BASE_KEY_PHONE,friendInfoDao.getPhone());
        values.put(DB_BASE_KEY_GENDER,friendInfoDao.getGender());
        values.put(DB_BASE_KEY_HOBBY,friendInfoDao.getHobbies().toString());
        values.put(DB_BASE_KEY_BIRTHPLACE,friendInfoDao.getBirthPlace());
        values.put(DB_BASE_KEY_MAJORID,friendInfoDao.getSpecialtyID());
        values.put(DB_BASE_KEY_PROFILE,friendInfoDao.getProfile());
        return sqldb.update(DB_BASE_TABLE_NAME, values, DB_BASE_KEY_ID + "="+id,null);
    }

    /*
    * 查询操作
    * */

    //专业表的查询
    public MyMajorDao[] queryMajor(){
        Cursor cursor = sqldb.query(DB_MAJOR_TABLE_NAME, null,null,null,null,
                                null, null);
        int count = cursor.getCount();
        System.out.println(cursor.getCount());
        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyMajorDao[] majorDaos = new MyMajorDao[count];
            for (int i = 0; i < count; i++){
                majorDaos[i] = new MyMajorDao();
                majorDaos[i].setId(cursor.getInt(cursor.getColumnIndex(DB_MAJOR_KEY_ID)));
                majorDaos[i].setMajorName(cursor.getString(cursor.getColumnIndex(DB_MAJOR_KEY_NAME)));
                cursor.moveToNext();
            }
            return majorDaos;
        }
    }
    //基本信息表查询手机号
    public MyFriendInfoDao[] queryPhoneOfBaseInfo(){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,null,null,null,
                                    null,null);
        int count = cursor.getCount();
        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendPhones = new MyFriendInfoDao[count];
            for (int i = 0; i < count; i++){
                myFriendPhones[i] = new MyFriendInfoDao();
                myFriendPhones[i].setPhone(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_PHONE)));
                cursor.moveToNext();
            }
            return myFriendPhones;
        }
    }
    //基本信息表查询姓名
    public MyFriendInfoDao[] queryNameOfBaseInfo(){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,null,null,null,
                null,null);
        int count = cursor.getCount();
        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendNames = new MyFriendInfoDao[count];
            for (int i = 0; i < count; i++){
                myFriendNames[i] = new MyFriendInfoDao();
                myFriendNames[i].setName(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_NAME)));
                cursor.moveToNext();
            }
            return myFriendNames;
        }
    }
    //基本信息表查询密码
    public MyFriendInfoDao[] queryPasswordOfBaseInfo(String phoneNum){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,DB_BASE_KEY_PHONE+"="+phoneNum,null,null,
                null,null);
        System.out.println("cursor.getCount()::"+cursor.getCount());
        if (cursor.getCount() <= 0){
            return null;
        }else {
            System.out.println("cursor.getPosition()::"+cursor.getPosition());
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendPasswords = new MyFriendInfoDao[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++){
                myFriendPasswords[i] = new MyFriendInfoDao();
                myFriendPasswords[i].setPassword(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_PASSWORD)));
                cursor.moveToNext();
            }
            return myFriendPasswords;
        }
    }
    //基本信息表查询用户余额
    public MyFriendInfoDao[] queryBalanceOfBaseInfo(String phoneNum){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,DB_BASE_KEY_PHONE+"="+phoneNum,null,null,
                null,null);
        if (cursor.getCount()<=0){
            return null;
        }else {
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendBalance = new MyFriendInfoDao[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++){
                myFriendBalance[i] = new MyFriendInfoDao();
                myFriendBalance[i].setBalance(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_BALANCE)));
                cursor.moveToNext();
            }
            return myFriendBalance;
        }
    }

    //基本信息表查询是否关注
    public MyFriendInfoDao[] queryFocusOfBaseInfo(){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,null,null,null,
                null,null);
        int count = cursor.getCount();
        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendFocus = new MyFriendInfoDao[count];
            for (int i = 0; i < count; i++){
                myFriendFocus[i] = new MyFriendInfoDao();
                myFriendFocus[i].setFocus(cursor.getInt(cursor.getColumnIndex(DB_BASE_KEY_FOCUS)));
                cursor.moveToNext();
            }
            return myFriendFocus;
        }
    }
    //查询基本信息
    public MyFriendInfoDao[] queryFriendsBaseInfo(){
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,null,null,null,null,
                null,null);
        int count = cursor.getCount();

        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyFriendInfoDao[] myFriendInfos = new MyFriendInfoDao[count];
            for (int i = 0; i < count; i++){
                myFriendInfos[i] = new MyFriendInfoDao();
                List<String> hobbiesList = new ArrayList<>();
                myFriendInfos[i].setInfoID(cursor.getInt(cursor.getColumnIndex(DB_BASE_KEY_ID)));
                myFriendInfos[i].setName(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_NAME)));
                myFriendInfos[i].setPhone(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_PHONE)));
                myFriendInfos[i].setGender(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_GENDER)));
                String str = cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_HOBBY));
                String str1 = str.substring(1,str.length()-1);
                String str2 = str1.replace(" ", "");
                System.out.println(str2);
                String[] str3 = str2.split(",");
                for (int j = 0; j < str3.length; j++){
                    hobbiesList.add(str3[j]);
                }
                myFriendInfos[i].setFocus(cursor.getInt(cursor.getColumnIndex(DB_BASE_KEY_FOCUS)));
                myFriendInfos[i].setHobbies(hobbiesList);
                myFriendInfos[i].setBirthPlace(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_BIRTHPLACE)));
                myFriendInfos[i].setSpecialtyID(cursor.getInt(cursor.getColumnIndex(DB_BASE_KEY_MAJORID)));
                myFriendInfos[i].setProfile(cursor.getString(cursor.getColumnIndex(DB_BASE_KEY_PROFILE)));
                cursor.moveToNext();
            }
            return myFriendInfos;
        }
    }

    //个人信息表的查询
    public MyInfoDao queryMyInfo(){
        Cursor cursor = sqldb.query(DB_SELF_TABLE_NAME,null,null,null,null,
                null,null);
        int count = cursor.getCount();

        if (count <= 0){
            return null;
        }else {
            cursor.moveToFirst();
            MyInfoDao myInfos = new MyInfoDao();
            for (int i = 0; i < count; i++){
                myInfos = new MyInfoDao();
                List<String> hobbiesList = new ArrayList<>();
                myInfos.setInfoID(cursor.getInt(cursor.getColumnIndex(DB_SELF_KEY_ID)));
                myInfos.setName(cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_NAME)));
                myInfos.setPhone(cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_PHONE)));
                myInfos.setGender(cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_GENDER)));
                String str = cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_HOBBY));
                String str1 = str.substring(1,str.length()-1);
                String str2 = str1.replace(" ", "");
                System.out.println(str2);
                String[] str3 = str2.split(",");
                for (int j = 0; j < str3.length; j++){
                    hobbiesList.add(str3[j]);
                }
                myInfos.setHobbies(hobbiesList);
                myInfos.setBirthPlace(cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_BIRTHPLACE)));
                myInfos.setSpecialtyID(cursor.getInt(cursor.getColumnIndex(DB_SELF_KEY_MAJORID)));
                myInfos.setProfile(cursor.getString(cursor.getColumnIndex(DB_SELF_KEY_PROFILE)));
                cursor.moveToNext();
            }
            return myInfos;
        }
    }

    public long updateMyProfileInfo(MyInfoDao myInfoDao, long id){
        ContentValues values = new ContentValues();
        values.put(DB_SELF_KEY_PROFILE,myInfoDao.getProfile());
        return sqldb.update(DB_SELF_TABLE_NAME, values, DB_SELF_KEY_ID + "="+id,null);
    }
}
