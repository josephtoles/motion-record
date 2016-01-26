package com.slightlyrobot.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.slightlyrobot.app.AccelerometerSamplerService;

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
    
    // TODO end AccelerometerSamplerService in onDestroy as a matter of form
}
