package com.plymouthuni.dreamteam.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.plymouthuni.dreamteam.weatherapp.Send_JSON_To_Server.HTTPMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class HomeFragment extends Fragment {


    public static String activityName = "HomeFrag";

    private TextView user_welcome = null;
    private TextView mapPinsUploaded = null;
    private TextView accountCreated = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.home_layout, container, false);


        user_welcome        = (TextView) rootView.findViewById(R.id.textviewWelcomeUser);
        mapPinsUploaded     = (TextView) rootView.findViewById(R.id.textview_pinsUploaded);
        accountCreated      = (TextView) rootView.findViewById(R.id.textview_accountCreated);




        try {
            //Start a sharedpreferences object to store necessary data
            SharedPreferences prefs = this.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE );
            int numPins = prefs.getInt("numberPins", 0);

            String username = prefs.getString("username", "N/A");
            user_welcome.setText("Welcome back, "+ username.substring(0,1).toUpperCase() + username.substring(1));
            mapPinsUploaded.setText(String.valueOf(numPins));
            accountCreated.setText(prefs.getString("creationDate", "N/A"));
        }
        catch(Exception ex) {
            Log.i(activityName, "Exception thrown: " + ex.getMessage());
        }


        return rootView;

    }
}
