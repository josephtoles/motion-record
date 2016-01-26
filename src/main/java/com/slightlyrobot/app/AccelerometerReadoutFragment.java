package com.slightlyrobot.app;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.Runnable;

import com.slightlyrobot.app.AccelerometerSamplerService;
import com.slightlyrobot.app.RawRecordsDatabaseOpenHelper;

public class AccelerometerReadoutFragment extends Fragment {

    private Handler handler = new Handler();
    private final int interval = 1000;
    private RawRecordsDatabaseOpenHelper db = new RawRecordsDatabaseOpenHelper(this.getActivity());
    private int loopCount = 0;

    private Runnable runnable = new Runnable() {
        public void run() {
            TextView textView = (TextView) getView().findViewById(R.id.readout_text_view);
            double[] latestValues = db.getLastValues();
            textView.setText(latestValues[0] + ", " + 
                             latestValues[1] + ", " + 
                             latestValues[2]);
            handler.postDelayed(runnable, interval);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.accelerometer_readout_view, container, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
