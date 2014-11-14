package com.jacobburdis.bom;

/**
 * Created by joshua on 4/24/14.
 */
import android.content.Context;
import android.database.SQLException;

import java.io.IOException;

public class DataBaseWriter {

    public static void WriteDataBase(Context context){

        DataBaseHelper myDbHelper = new DataBaseHelper(context);

        try {
            myDbHelper.createDataBase();
        }

        catch (IOException ioe) {
            throw new Error ("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        }

        catch (SQLException sqle) {
            throw sqle;
        }
    }

}
