package com.plymouthuni.dreamteam.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

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

        map_View.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap g_Map) {
                google_map = g_Map;

                //Request permission for users

                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {

                    google_map.setMyLocationEnabled(true);
                    google_map.getUiSettings().setMyLocationButtonEnabled(true);
                } else {

                    //Toast.makeText(this, R.string.error_permission_map, Toast.LENGTH_LONG).show();
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
