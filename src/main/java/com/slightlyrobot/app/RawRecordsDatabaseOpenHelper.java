package com.slightlyrobot.app;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
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
            // timestamp format "2010-05-28T15:36:56.200"
            "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
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
    
    // Testing placeholder
    /**
     * Returns a length-3 array of the most recent x, y, z accelerometer samples
     */
    public double[] getLastValues () {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] QUERY_COLUMNS = {"x_raw", "y_raw", "z_raw"};

        Cursor cursor =
            db.query(RAW_RECORDS_TABLE_NAME, // table
                    QUERY_COLUMNS, // column names
                    null, // selections
                    null, // selections args
                    null, // group by
                    null, // having
                    "timestamp DESC", // order by
                    "1"); // limit
        if (cursor != null)
            cursor.moveToFirst(); 

        double[] output = {cursor.getDouble(0),
                    cursor.getDouble(1),
                    cursor.getDouble(2)};

        return output;
    }
    
    // TODO merge this function with getLastValues
    public String getAllValues() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] QUERY_COLUMNS = {"timestamp", "x_raw", "y_raw", "z_raw"};

        Cursor cursor =
            db.query(RAW_RECORDS_TABLE_NAME, // table
                    QUERY_COLUMNS, // column names
                    null, // selections
                    null, // selections args
                    null, // group by
                    null, // having
                    "timestamp DESC", // order by
                    null); // limit
        if (cursor != null)
            cursor.moveToFirst(); 

        String outputTimestamp = cursor.getString(0);
        
        int maxRows = 200;
        
        String csvText = "timestamp,xRaw,yRaw,zRaw";
        int row = 0;
        if (cursor.moveToFirst()) {
           do {
               String timestamp = cursor.getString(0);
               double xRaw = cursor.getDouble(1);
               double yRaw = cursor.getDouble(2);
               double zRaw = cursor.getDouble(3);
 
               // TODO use CSV-handling library instead
               csvText = "\n" + csvText + timestamp + ", " + xRaw + "," + yRaw + "," + zRaw;
           } while (cursor.moveToNext() && ++row < maxRows);
        }

        return "Row count: " + row + "\n" + csvText;
    }
}
