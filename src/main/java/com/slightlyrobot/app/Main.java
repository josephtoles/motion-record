package com.slightlyrobot.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.slightlyrobot.app.AccelerometerSamplerService;

public class Main extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // Start the AccelerometerSamplerService
        Intent intent = new Intent(this, AccelerometerSamplerService.class);
        startService(intent);

        // Set up this Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        System.out.println("Activity has been set up");
    }
    
    // TODO end AccelerometerSamplerService on distroy if necessary
}
