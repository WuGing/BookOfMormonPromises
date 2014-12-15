package com.jacobburdis.bompromises;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper{

    private SQLiteDatabase myDataBase;

    public static final int DATABASE_VERSION = 4;
    private static final String SP_KEY_DB_VER = "db_ver";

    private final Context myContext;

    private static String DB_PATH = "/data/data/com.jacobburdis.bompromises/databases/";
    private static String DB_NAME = "promise";

    public String DB_TABLE = "promise";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CONDITION = "condition";
    public static final String KEY_CONDITIONS = "conditions";
    public static final String KEY_BLESSING = "blessing";
    public static final String KEY_BLESSINGS = "blessings";
    public static final String KEY_FAVORITE = "favorite";
    public static final String KEY_REFERENCE = "reference";
    public static final String KEY_BOOK = "book";
    public static final String KEY_CONTENT= "content";
    public static final String KEY_FAVORITE_HELPER = "favoriteHelper";

    public static final String[] ALL_KEYS_NORM = new String[] { KEY_ROWID, KEY_CONDITION,
            KEY_CONDITIONS, KEY_BLESSING, KEY_BLESSINGS, KEY_FAVORITE, KEY_REFERENCE,
            KEY_BOOK, KEY_CONTENT, KEY_FAVORITE_HELPER};

    public DataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    public DataBaseHelper(Context fragmentContext, String table)
    {
        super(fragmentContext, DB_NAME, null, DATABASE_VERSION);
        this.myContext = fragmentContext;
        //this.DB_TABLE = table;
    }

    public void createDataBase() throws IOException
    {

        if (checkDataBase())
        {
            onUpgrade(myDataBase, 3, 4);
        } else
        {
            getReadableDatabase();

            try
            {
                copyDataBase();

            } catch (IOException e)
            {
                throw new Error("Error copying database");
            }

        }
    }

    private boolean checkDataBase()
    {
        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        } catch (SQLiteException e)
        {

        }

        if (checkDB != null)
        {
            checkDB.close();
        }

        return checkDB !=null ? true : false;
    }

    private void copyDataBase() throws IOException
    {
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException
    {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public Cursor getAllRows(int option)
    {
        String where = null;
        Cursor c = null;
        switch (option)
        {
            // favorite
            case 0:
                where = KEY_FAVORITE + "=?";
                c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                        where, new String[] { "1" }, KEY_BOOK, null, KEY_ROWID, null);
                break;

            // condition
            case 1:
                where = KEY_CONDITION + " IS NOT NULL";
                c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                        where, null, KEY_CONDITION, null, KEY_CONDITION, null);
                break;

            // blessing
            case 2:
                where = KEY_BLESSING + " IS NOT NULL";
                c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                        where, null, KEY_BLESSING, null, KEY_BLESSING, null);
                break;

            // chronological
            case 3:
                c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                        where, null, KEY_BOOK, null, KEY_ROWID, null);
                break;
        }

        if (c != null)
            c.moveToFirst();
        return c;
    }

    public Cursor getRowsFavorites(String favorite)
    {
        String where = KEY_BOOK + "=?" + " AND " + KEY_FAVORITE + "=?";
        Cursor c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                where, new String[] { favorite, "1" }, KEY_REFERENCE, null, KEY_ROWID, null);
        if ( c != null)
            c.moveToFirst();
        return c;
    }

    public Cursor getRowsBlessing(String blessing)
    {
        String where = KEY_BLESSING + "=?";
        Cursor c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                where, new String[] { blessing }, null, null, null, null);
        if (c != null)
            c.moveToFirst();
        return c;
    }


    public Cursor getRowsCondition(String condition)
    {
        String where = KEY_CONDITION + "=?";
        Cursor c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                where, new String[] { condition }, null, null, null, null);
        if (c != null)
            c.moveToFirst();
        return c;
    }

    public Cursor getRowsBook(String book)
    {
        String where = KEY_BOOK + "=?";
        Cursor c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                where, new String[] { book }, DataBaseHelper.KEY_REFERENCE,
                null, DataBaseHelper.KEY_ROWID, null);
        if (c != null)
            c.moveToFirst();
        return c;
    }

    public Cursor getRow(int selector, String identifier)
    {
        String where = KEY_ROWID + "=" + selector;
        openDataBase();
        Cursor c;
        if (identifier == "chronological") {
            c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                    where, null, null, null, null, null);
        }
        else {
            c = myDataBase.query(true, DB_TABLE, ALL_KEYS_NORM,
                    where, null, null, null, null, null);
        }
        if (c != null)
            c.moveToFirst();
        return c;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(myContext);
        int dbVersion = prefs.getInt(SP_KEY_DB_VER, 4);
        if (DATABASE_VERSION != dbVersion)
        {
            File dbFile = myContext.getDatabasePath(DB_NAME);
            if (!dbFile.delete())
            {
                Log.w(null, "Unable to update database");
            }
        }

        if (!checkDataBase())
        {
            try
            {
                copyDataBase();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void updateFavorite(String helperId, String favoriteIndicator)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put(KEY_FAVORITE, favoriteIndicator);

        db.update(DB_TABLE, args, "favoriteHelper=" + helperId, null);
    }

}