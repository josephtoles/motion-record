package com.slightlyrobot.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    
    public void exportRawData(View view) {
        Log.d("exportRawData", "Sending output");
        
        RawRecordsDatabaseOpenHelper db = new RawRecordsDatabaseOpenHelper(this);
        String emailBody = "Body of email";
        emailBody = emailBody + db.getAllValues();

        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your raw data");
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        intent.setData(Uri.parse("mailto:default@recipient.com"));
        // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }
    
    // TODO end AccelerometerSamplerService in onDestroy as a matter of form
}
