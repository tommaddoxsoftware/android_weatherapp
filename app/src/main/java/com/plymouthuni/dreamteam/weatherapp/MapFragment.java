package com.plymouthuni.dreamteam.weatherapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements I_JSON_Response_Listener, LocationListener, GpsStatus.Listener{

    public static String activityName = "MapFrag";
    private LocationManager locationManager = null;
    private String          bestProvider    = null;
    private int             iGpsStatus      = 0;
    private Location        currLocation    = null;

    MapView map_View;
    private GoogleMap google_map;

    String GetMarkerURL = "https://tommaddoxsoftware.co.uk/android/GetMarkers.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_layout,
                container, false);

        //Initialise map + display as soon as it is created.
        map_View = (MapView) rootView.findViewById(R.id.mapView);
        map_View.onCreate(savedInstanceState);
        map_View.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Get the location service
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        //Enumerate all the location providers
        List<String> providers = locationManager.getAllProviders();
        for(String p: providers) {
            Log.i(activityName, "Location provider found: " + p);
        }

        //Determine best provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        bestProvider = locationManager.getBestProvider(criteria, true);



        map_View.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap g_Map) {
                google_map = g_Map;

                //Request permission for users

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    google_map.setMyLocationEnabled(true);
                    google_map.getUiSettings().setMyLocationButtonEnabled(true);

                } else {

                    Toast.makeText(getContext(), R.string.error_permission_map, Toast.LENGTH_LONG).show();
                }

                // Centre on users current position


                // Load surrounding inputs from server


                //LatLng sydney = new LatLng(-34, 151);
                //google_map.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description") );

                //CameraPosition camera_Position = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //google_map.animateCamera(CameraUpdateFactory.newCameraPosition(camera_Position));

            }
        });

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        map_View.onResume();

        try {
            locationManager.addGpsStatusListener(this);
            locationManager.requestLocationUpdates(bestProvider, 150, 0, this);
            Location location = locationManager.getLastKnownLocation(bestProvider);
        }
        catch (SecurityException secEx)
        {
            Log.e(activityName, "Location security exception");
        }
        catch (Exception ex)	// We will get this exception with
        {					// API numbers older than API23
            Log.i(activityName, "Exception: " + ex.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        map_View.onPause();
        try {
            locationManager.removeGpsStatusListener(this);
            locationManager.removeUpdates(this);
        }
        catch(SecurityException secEx) {
            Log.i(activityName, "Security Exception was thrown");
        }
        catch (Exception ex) {
            Log.i(activityName, "Exception was thrown. Here's the message: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map_View.onDestroy();
    }

    public void GetCurrentMarkers() {
        //Make a request to webservice GetMarkers.php
        //Prepare the JSON object that we're sending
        JSONObject objectToSend = new JSONObject();

        try {
            objectToSend.put("latitude", currLocation.getLatitude());
            objectToSend.put("longitude", currLocation.getLongitude());
        }
        catch(JSONException jsonError) {
            Log.e(activityName, "JSON Exception: " +jsonError.getMessage());
        }

        //Instantiate request to send to server
        Send_JSON_To_Server send = new Send_JSON_To_Server(GetMarkerURL, null, objectToSend, Send_JSON_To_Server.HTTPMode.POST, this);

        //Send the request to the server!
        send.start();

    }

    public void CreateMapMarker(LatLng location, String title, String desc) {

        //Add marker
        google_map.addMarker(new MarkerOptions().position(location).title(title).snippet(desc));


    }

    @Override
    public void onHTTPResponseReceived(JSONObject response) {
        String strResult = getString(R.string.waiting);
        String jsonAction = "";
        String jsonStatus = "";
        JSONObject mapMarkers;

        try {
            jsonStatus = response.getString("status");
            jsonAction = response.getString("action");

            //Serverside code worked
            if(!jsonStatus.equals("Fail")) {
                if (jsonAction.equals("GetMarkers")) {
                    mapMarkers = response.getJSONObject("markers");

                    //Loop through json (I've done the calculations for checking it's within a set distance to the user on the server. There's no point NOT using the external resources to do so.
                    //Also means that the JSON response is smaller - saving data for the win!//Loop through map markers and add them!
                    while(mapMarkers.keys().hasNext()) {
                        //Get location information
                        Double tempLong = mapMarkers.getDouble("longitude");
                        Double tempLat = mapMarkers.getDouble("latitude");
                        String tempTitle = mapMarkers.getString("title");
                        String tempDesc = mapMarkers.getString("description");

                        //Create location of marker
                        LatLng tempLocation;
                        tempLocation = new LatLng(tempLat, tempLong);

                        CreateMapMarker(tempLocation, tempTitle, tempDesc);
                    }
                }
            }
        }
        catch (Exception oops) {
            strResult = getString(R.string.oops);
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

    private String GPSStatusToString(int status)
    {
        String s = "Unknown";
        switch (status)
        {
            case GpsStatus.GPS_EVENT_FIRST_FIX: s = "First position fix";
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS: s = "Satelite status event";
                break;
            case GpsStatus.GPS_EVENT_STARTED: s = "GPS event started";
                break;
            case GpsStatus.GPS_EVENT_STOPPED: s = "GPS Event stopped";
                break;
        }
        return s;
    }
}
