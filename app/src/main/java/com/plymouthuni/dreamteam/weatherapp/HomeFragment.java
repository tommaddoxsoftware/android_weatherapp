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

public class HomeFragment extends Fragment implements View.OnClickListener, I_JSON_Response_Listener {


    public static String activityName = "HomeFrag";
    private Button login_button = null;
    private Button signup_button = null;
    private EditText username_input = null;
    private EditText password_input = null;

    private String loginURL = "https://tommaddoxsoftware.co.uk/android/login.php";
    private String registerURL = "https://tommaddoxsoftware.co.uk/android/register.php";

    private TextView add = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.home_layout, container, false);

        login_button = (Button) rootView.findViewById(R.id.login_button);
        signup_button = (Button) rootView.findViewById(R.id.signup_button);
        add = (TextView) rootView.findViewById(R.id.textView2);
        username_input = (EditText )rootView.findViewById(R.id.username_input);
        password_input = (EditText) rootView.findViewById(R.id.password_input);

        login_button.setOnClickListener(this);
        signup_button.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login_button:
                ContactServer("login");
                Log.i("Login Webservice", "Button Login pressed");
                break;

            case R.id.signup_button:
                ContactServer("register");
                Log.i("Register Webservice", "Button Register pressed");
                break;

            default:
                break;

        }

    }

    public void ContactServer(String action) {
        String username = "";
        String password = "";
        String jsonUrl = "";

        //Try to assign password and username to variable
        try {
            username = username_input.getText().toString();
            password = password_input.getText().toString();
        }
        catch(Exception ex) {
            Toast.makeText(this.getContext(), R.string.unexpected,Toast.LENGTH_SHORT).show();
        }

        //Prepare the JSON object that we're sending
        JSONObject objectToSend = new JSONObject();
        try {
            objectToSend.put("username", username);
            objectToSend.put("password", password);
        }
        catch(JSONException jsonError) {
            Log.e(activityName, "JSON Exception: " +jsonError.getMessage());
        }

        switch(action) {
            case "login":
                    jsonUrl = loginURL;
                break;

            case "register":
                    jsonUrl = registerURL;
                break;
        }

        //Instantiate request to send to server
        Send_JSON_To_Server send = new Send_JSON_To_Server(jsonUrl, null, objectToSend, HTTPMode.POST, this);

        //Send the request to the server!
        send.start();
    }

    @Override
    public void onHTTPResponseReceived(JSONObject response) {
        String strResult = getString(R.string.waiting);
        String action = "";
        String actionResult = "";
        String reason = "";
        String username = "";
        int userID = 0;

        try {
            action = response.getString("action");
            actionResult =  response.getString("actionResult");
            reason = response.getString("reason");

            username = username_input.getText().toString();
            if(action == "login")
                userID = response.getInt("uid");

        }
        catch (Exception oops) {
            strResult = getString(R.string.oops);
        }

        Log.i("action", action);

        Log.i("ActionResult", actionResult);
        Log.i("reason", reason);



        switch(action) {
            case "login":
                    if(actionResult.equals("Fail")) {
                        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        //Do login stuff here

                        //Start a sharedpreferences object to store necessary data
                        SharedPreferences prefs = this.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE );
                        SharedPreferences.Editor editor = prefs.edit();

                        //Pass username to userinfo
                        editor.putString("username", username);

                        //If it's a legitimate user ID, set it to userinfo
                        if(userID != 0)
                            editor.putInt("userid", userID);

                        //Probably load a proper "home" screen

                    }
                break;

            case "register":
                    if( actionResult.equals("fail")) {
                        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Could automatically login, but let's make the user do the hard work
                        Toast.makeText(getActivity(), "Account created successfully! Please proceed to login", Toast.LENGTH_SHORT).show();
                    }

                break;
        }
    }

}
