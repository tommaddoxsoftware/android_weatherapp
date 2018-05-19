package com.plymouthuni.dreamteam.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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

import java.net.DatagramPacket;

public class MapFragment extends Fragment {

    MapView map_View;
    private GoogleMap google_map;

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


        getPermissions();


        return rootView;

    }

    private void getPermissions(){
        if ( ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            // No permission granted
            if (ActivityCompat.shouldShowRequestPermissionRationale( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) ) {

                Toast.makeText(this.getContext(), R.string.permission_not_granted, Toast.LENGTH_SHORT).show();


            }
            else {
                // Request permission
                ActivityCompat.requestPermissions( getActivity(), new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION}, 3000);
            }

        }
        else{
            // Permission already granted :)


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map_View.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map_View.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map_View.onDestroy();
    }

}
