package com.slightlyrobot.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class AccelerometerSamplerService extends IntentService implements SensorEventListener{

    private SensorManager sm;
    //private Sensor mSensor;

    /**
    * A constructor is required, and must call the super IntentService(String)
    * constructor with a name for the worker thread.
    */
    public AccelerometerSamplerService() {
        super("AccelerometerSamplerService");
    }

    /**
    * The IntentService calls this method from the default worker thread with
    * the intent that started the service. When this method returns, IntentService
    * stops the service, as appropriate.
    */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        long endTime = System.currentTimeMillis() + 5*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    System.out.println("Data");

                    sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                    if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
                        Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                        sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
                    } else {
                        // TODO raise exception
                    }

                    System.out.println("Data after");
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) { 
        float mSensorX = event.values[0];
        float mSensorY = event.values[1];
        float mSensorZ = event.values[2];
        System.out.println(mSensorX + ", " + mSensorY + ", " + mSensorZ);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do here.
    }
}
