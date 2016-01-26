package com.slightlyrobot.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slightlyrobot.app.AccelerometerSamplerService;
import com.slightlyrobot.app.RawRecordsDatabaseOpenHelper;

public class AccelerometerReadoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.accelerometer_readout_view, container, false);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        TextView textView = (TextView) getView().findViewById(R.id.readout_text_view);
        textView.setText("updated");
        
        RawRecordsDatabaseOpenHelper db = new RawRecordsDatabaseOpenHelper(this.getActivity());
        double[] latestValues = db.getLastValues();
        textView.setText("" + latestValues[0] + ", " + latestValues[1] + ", " + latestValues[2]);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
