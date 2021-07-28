package cn.dj.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    SQLiteDatabase sqldb;

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

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        final String DB_CREATE_TABLE_BASE = "CREATE TABLE " + DB_BASE_TABLE_NAME + " (" +
                DB_BASE_KEY_ID + " integer primary key autoincrement, " +
                DB_BASE_KEY_NAME + " text not null, " +
                DB_BASE_KEY_PHONE + " text not null unique, " +
                DB_BASE_KEY_GENDER + " text default '男', " +
                DB_BASE_KEY_HOBBY + " text, " +
                DB_BASE_KEY_BIRTHPLACE + " text, " +
                DB_BASE_KEY_MAJORID + " integer, " +
                DB_BASE_KEY_FOCUS + " integer default 0," +
                "foreign KEY (" +DB_BASE_KEY_MAJORID+ ") REFERENCES " +DB_MAJOR_TABLE_NAME+"(DB_MAJOR_KEY_ID));";
        SQLiteOpenHelper helper = new SQLiteOpenHelper(getContext(),"stu.db",null,1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DB_CREATE_TABLE_BASE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
        sqldb = helper.getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = sqldb.query(DB_BASE_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}