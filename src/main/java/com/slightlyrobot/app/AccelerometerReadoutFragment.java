package com.slightlyrobot.app;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.InterruptedException;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.Random; // used for debugging
import java.util.Timer;
import java.util.TimerTask;

import com.slightlyrobot.app.AccelerometerSamplerService;
import com.slightlyrobot.app.RawRecordsDatabaseOpenHelper;

public class AccelerometerReadoutFragment extends Fragment {

    private Handler handler = new Handler();
    private final int interval = 1000;

    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.accelerometer_readout_view, container, false);
    }
    
    public class MyThread implements Runnable {
        private Fragment fragment;

        public MyThread(Fragment inputFragment) {
            this.fragment = inputFragment;
        }

        @Override
        public void run() {
            RawRecordsDatabaseOpenHelper db = new RawRecordsDatabaseOpenHelper(fragment.getActivity());
            TextView textView = (TextView) fragment.getView().findViewById(R.id.readout_text_view);
            double[] latestValues = db.getLastValues();
            textView.setText(latestValues[0] + ", " + 
                            latestValues[1] + ", " + 
                            latestValues[2]);
        }
    }
    
    public class MyTimerTask extends TimerTask {
        private Fragment fragment;

        public MyTimerTask(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void run() {
            MyThread myThread = new MyThread(fragment);
            handler.postDelayed(myThread, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(this), 1000, 1000);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
