package me.puneetsingh.learn1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by puneetsingh on 6/29/15.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String TABLE_COMMENTS = "memodiction";
    public static final String COLUMN_ID = "_id";

    private static final String DATABASE_NAME = "memodiction.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DB_CREATE =
            "create table if not exists words (\n" +
            COLUMN_ID+ " integer primary key autoincrement,\n" +
            "  word varchar(64) NOT NULL,\n" +
            "  meaning varchar(256),\n" +
            "  POS varchar(20) NOT NULL,\n" +
            "  example varchar(512),\n" +
            "  difScore double NOT NULL,\n" +
            "  lmScore double NOT NULL,\n" +
            "  score double NOT NULL\n" +
            ");";
    private static final String USER_CREATE =
            "create table if not exists shuffletimes (\n" +
                    COLUMN_ID+ " integer primary key autoincrement,\n" +
                    "shuffle  int not null\n"+
                    ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DBHelper.class.getName(),"Created table first time");
        db.execSQL(DB_CREATE);
        db.execSQL(USER_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);

    }
}
