package com.plymouthuni.dreamteam.weatherapp;


import android.annotation.TargetApi;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.net.sip.SipAudioCall;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class WeatherFragment extends Fragment implements View.OnClickListener, I_JSON_Response_Listener, LocationListener, GpsStatus.Listener {

    public static   String      activityName            = "Upload Weather";
    private         Button      upload_weather_button   = null;
    private         Spinner     weather_spinner         = null;
    private         EditText    other_input             = null;

    private Location        currLocation    = null;
    private LocationManager locationManager = null;
    private String          bestProvider    = null;
    private int             iGpsStatus      = 0;
    private String          CreateMarkerURL = "https://tommaddoxsoftware.co.uk/android/CreateMarker.php";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.weather_layout, container, false);

        // Initialise
        upload_weather_button = (Button) rootView.findViewById(R.id.upload_button);
        weather_spinner = (Spinner) rootView.findViewById(R.id.weather_spinner);
        other_input = (EditText) rootView.findViewById(R.id.other_input);

        // Set Button Listeners
        upload_weather_button.setOnClickListener(this);

        //Get the location service
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Enumerate all the location providers
                List<String> providers = locationManager.getAllProviders();
                for(String p: providers) {
                    Log.i(activityName, "Location provider found: " + p);
                }

                //Determine best provider
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                bestProvider = locationManager.getBestProvider(criteria, true);
                GetPermission();
            }
        }).start();



        return rootView;


    }

    private void GetPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // No permission granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this.getContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();


            } else {
                // Request permission
                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, 3000);

            }

        } else {
            // Permission already granted :)
            // locationManager.requestLocationUpdates(bestProvider, 5000, 10, this );
            currLocation = locationManager.getLastKnownLocation(bestProvider);
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.upload_button:
                sendWeather();
                Log.i("Upload Weather", "Upload Weather Button PRESSED");
                break;

            default:
                break;

        }

    }



    private void sendWeather() {
        JSONObject objectToSend = new JSONObject();

        try {
            //Get shared preferences
            SharedPreferences prefs = this.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE );
            int userID = prefs.getInt("userid", 0);

            String title = other_input.toString();
            if(other_input.toString().equals("")) {
                title = "Weather pin";
            }

            objectToSend.put("latitude", currLocation.getLatitude());
            objectToSend.put("longitude", currLocation.getLongitude());
            objectToSend.put("title", title );
            objectToSend.put("weather", weather_spinner.getSelectedItem().toString());
            objectToSend.put("uploadedby", userID);

        }
        catch(JSONException jsonError) {
            Log.e(activityName, "JSON Exception: " +jsonError.getMessage());
        }

        //Instantiate request to send to server
        Send_JSON_To_Server send = new Send_JSON_To_Server(CreateMarkerURL, null, objectToSend, Send_JSON_To_Server.HTTPMode.POST, this);

        //Send the request to the server!
        send.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            locationManager.addGpsStatusListener(this);
            locationManager.requestLocationUpdates(bestProvider, 150, 0, this);
            Location location = locationManager.getLastKnownLocation(bestProvider);
        } catch (SecurityException secEx) {
            Log.e(activityName, "Location security exception");
        } catch (Exception ex)    // We will get this exception with
        {                    // API numbers older than API23
            Log.i(activityName, "Exception: " + ex.getMessage());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            locationManager.removeGpsStatusListener(this);
            locationManager.removeUpdates(this);
        } catch (SecurityException secEx) {
            Log.i(activityName, "Security Exception was thrown");
        } catch (Exception ex) {
            Log.i(activityName, "Exception was thrown. Here's the message: " + ex.getMessage());
        }

    }


    public void onHTTPResponseReceived(JSONObject response) {

        String jsonAction = "";
        String jsonStatus = "";

        try {
            Log.i(activityName, "Response: " + response);
            jsonStatus = response.getString("responseStatus");
            jsonAction = response.getString("responseAction");
        } catch (Exception oops) {
            Log.i(activityName, "Exception: " + oops.getMessage());
        }


        //Serverside code worked
        if(jsonAction.equals("CreateMarkers") && jsonStatus.equals("Success")) {
            Toast.makeText(this.getContext(), R.string.uploadSuccess, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this.getContext(), R.string.unexpected, Toast.LENGTH_SHORT).show();
        }

    }


    //Location listener stuff
    @Override
    public void onGpsStatusChanged(int newStatus) {
        iGpsStatus = newStatus;
    }

    @Override
    public void onLocationChanged(Location location) {
        currLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private String GPSStatusToString(int status) {
        String s = "Unknown";
        switch (status) {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                s = "First position fix";
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                s = "Satelite status event";
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                s = "GPS event started";
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                s = "GPS Event stopped";
                break;
        }
        return s;
    }
}
