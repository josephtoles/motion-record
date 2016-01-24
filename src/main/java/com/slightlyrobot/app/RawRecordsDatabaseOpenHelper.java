package com.slightlyrobot.app;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO add gyroscopic data

public class RawRecordsDatabaseOpenHelper extends SQLiteOpenHelper {  // TODO shorten name

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "raw_data";
    private static final String RAW_RECORDS_TABLE_NAME = "raw_movement_data";
    
    // TODO extract "x_raw", "y_raw", "z_raw" strings to static final
    
    // Default constructor
    public RawRecordsDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RAW_RECORDS_TABLE = "CREATE TABLE " + RAW_RECORDS_TABLE_NAME + " ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +  // String of format "2010-05-28T15:36:56.200"
            "x_raw NUMERIC, " +
            "y_raw NUMERIC, " +
            "z_raw NUMERIC )";
        db.execSQL(CREATE_RAW_RECORDS_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + RAW_RECORDS_TABLE_NAME);
 
        // create fresh table
        this.onCreate(db);
    }
    
    public void addDatum(double xRaw, double yRaw, double zRaw) {
        Log.d("addDatum", "Recording data (" + xRaw + ", " + yRaw + ", " + zRaw + ")");
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("x_raw", xRaw);
        values.put("y_raw", yRaw);
        values.put("z_raw", zRaw);
        
        db.insert(RAW_RECORDS_TABLE_NAME, // table
                  null, // null column hack -- TODO what is this?
                  values);

        db.close();
    }
}
