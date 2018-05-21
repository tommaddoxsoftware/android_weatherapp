package com.plymouthuni.dreamteam.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFragment extends Fragment implements View.OnClickListener, I_JSON_Response_Listener {


    public static String activityName = "LoginFrag";
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

        View rootView = inflater.inflate(R.layout.login_layout, container, false);

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
          //  if(action.equals("register"))
            password = md5(password);
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
        String date = "";
        int numPins = 0;
        int userID = 0;

        try {
            action = response.getString("action").toLowerCase();
            actionResult =  response.getString("actionResult").toLowerCase();
            reason = response.getString("reason");
            numPins = response.getInt("numberPins");
            date = response.getString("creationDate");

            Log.i(activityName, "Date: " + date);

            username = username_input.getText().toString();
            if(action.equals("login"))
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
                    if(actionResult.equals("fail")) {
                        Toast.makeText(getActivity(), reason, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        //Do login stuff here

                        //Start a sharedpreferences object to store necessary data
                        SharedPreferences prefs = this.getContext().getSharedPreferences("userinfo", Context.MODE_PRIVATE );
                        SharedPreferences.Editor editor = prefs.edit();

                        //Pass username to userinfo
                        editor.putString("username", username);
                        editor.putInt("numberPins", numPins);
                        editor.putString("creationDate", date);

                        editor.commit();
                        


                        //If it's a legitimate user ID, set it to userinfo
                        if(userID != 0)
                            editor.putInt("userid", userID);

                        //Re-enable nav drawer
                        ((MainActivity)this.getActivity()).EnableNavDrawer();

                        //Load the home fragment!
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = null;

                        //LoginFragment login_frag = null;
                        HomeFragment home_frag = null;


                        home_frag = new HomeFragment();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, home_frag);
                        fragmentTransaction.commit();
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

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
