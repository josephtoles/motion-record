package com.slightlyrobot.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

import com.slightlyrobot.app.RawRecordsDatabaseOpenHelper;

public class AccelerometerSamplerService extends IntentService implements SensorEventListener{
    private SensorManager sm;
    private RawRecordsDatabaseOpenHelper db;
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
        synchronized (this) {
            try {
                System.out.println("Mark collect data begin");

                sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                //mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


                if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
                    Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
                    sm.registerListener(this,s, SensorManager.SENSOR_DELAY_NORMAL);
                } else {
                    // TODO raise exception
                }

                db = new RawRecordsDatabaseOpenHelper(this);

                System.out.println("Mark collect data end");
            } catch (Exception e) {
                // TODO learn how synchronized works
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) { 
        float mSensorX = event.values[0];
        float mSensorY = event.values[1];
        float mSensorZ = event.values[2];
        System.out.println(mSensorX + ", " + mSensorY + ", " + mSensorZ);

        db.addDatum(mSensorX, mSensorY, mSensorZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do here.
    }
}
