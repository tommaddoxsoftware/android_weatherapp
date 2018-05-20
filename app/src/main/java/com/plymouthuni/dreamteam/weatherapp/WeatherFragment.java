package com.plymouthuni.dreamteam.weatherapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends Fragment implements View.OnClickListener, SensorEventListener {

    private Button upload_weather_button = null;
    private Spinner weather_spinner = null;
    private EditText other_input = null;
    private TextView tempurature_output = null;

    private Weather weather_upload = null;

    private SensorManager sensor_manager;
    private Sensor temp_sensor;
    private float tempurature;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.weather_layout, container, false);

        // Initialise
        upload_weather_button = (Button) rootView.findViewById(R.id.upload_weather_button);
        weather_spinner = (Spinner) rootView.findViewById(R.id.weather_spinner);
        other_input = (EditText) rootView.findViewById(R.id.other_input);
        tempurature_output = (TextView) rootView.findViewById(R.id.temperature_output);

        sensor_manager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);



        if (sensor_manager != null) {
            temp_sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, true);
            sensor_manager.registerListener(this, temp_sensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("SENSOR_MANAGER", "SENSOR MANAGER NOT NULL, TEMP_SENSOR ASSIGNED, SENSOR MANAGER REGISTERED WITH LISTENER");
        }
        else {
            Log.i("SENSOR_MANAGER", "SENSOR MANAGER IS NULL, TEMP_SENSOR NOT ASSIGNED, SENSOR MANAGER NOT REGISTERED WITH LISTENER");
        }

        if ( temp_sensor == null ) {
            Log.i("SENSOR_MANAGER", "TEMP_SENSOR IS NULL");
        }
        else {
            Log.i("SENSOR_MANAGER", "TEMP_SENSOR IS NOT NULL");
        }

        // Set Button Listeners
        upload_weather_button.setOnClickListener(this);

        tempurature_output.setText( String.valueOf(tempurature) );

        return rootView;

    }


    @Override
    public void onClick(View view) {

        tempurature_output.setText( String.valueOf(tempurature) );
        switch (view.getId()) {

            case R.id.upload_weather_button:
                updateWeather();
                sendWeather();
                Log.i("Upload Weather", "Upload Weather Button PRESSED");
                break;

            default:
                break;

        }

    }

    private void updateWeather() {
        float temp = tempurature;
        String weather = String.valueOf(weather_spinner.getPrompt());
        String other_info = String.valueOf(other_input.getText());



        weather_upload = new Weather((int)temp, weather, other_info);
    }


    private void sendWeather() {

    }

    @Override
    public void onResume() {
        super.onResume();


        if ( !sensor_manager.registerListener(this, temp_sensor, SensorManager.SENSOR_DELAY_NORMAL) ) {
            Log.i("SENSOR_MANAGER", "SENSOR MANAGER NOW HAS LISTENER");
            sensor_manager.registerListener(this, temp_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            sensor_manager.registerListener(this, temp_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        sensor_manager.unregisterListener(this);
        Log.i("SENSOR_MANAGER", "LISTENER REMOVED FROM SENSOR MANAGER");
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i("SENSOR_MANGER", "SENSOR HAS CHANGED, UPDATED TEMPERATURE UI");
        tempurature = sensorEvent.values[0];

        tempurature_output.setText( String.valueOf(tempurature) );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i("SENSOR_MANAGER", "SENSOR ACCURACY HAS CHANGED.");
    }
}
