package com.slightlyrobot.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.OutputStreamWriter;

import com.slightlyrobot.app.AccelerometerSamplerService;
import com.slightlyrobot.app.RawRecordsDatabaseOpenHelper;

public class Main extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Set up this Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Start the AccelerometerSamplerService
        Intent intent = new Intent(this, AccelerometerSamplerService.class);
        startService(intent);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, AccelerometerSamplerService.class);
        stopService(intent);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        // TODO create update loop
    }
    
    @Override
    public void onStop() {
        super.onStop();
        // TODO kill update loop
    }
    
    /**
     * @return if file is created successfully
     */
    private String createFileForExport() {
        Log.d("createFileForExport", "creating file");
        RawRecordsDatabaseOpenHelper db = new RawRecordsDatabaseOpenHelper(this);
        try {
            String filePath = this.getExternalCacheDir() + "/output.csv";
            FileOutputStream fos = new FileOutputStream(filePath);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(db.getCSVText());
            out.flush();
            out.close();
            return filePath;
        } catch (Throwable t) {
            Toast.makeText(this, "Could not create file: " + t.toString(),
            Toast.LENGTH_LONG).show();
            return null;
        }
    }
    
    public void exportRawData(View view) {
        String filePath = createFileForExport();
        if(filePath == null)
            return;

        Log.d("exportRawData", "Sending output");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your raw data");
        intent.putExtra(Intent.EXTRA_TEXT, "Email body"); // TODO add stuff here
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
        intent.setData(Uri.parse("mailto:placeholder@gmail.com"));  // TODO change this
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // return to app after sending email
        startActivity(intent);
    }
    
    // TODO end AccelerometerSamplerService in onDestroy as a matter of form
}
