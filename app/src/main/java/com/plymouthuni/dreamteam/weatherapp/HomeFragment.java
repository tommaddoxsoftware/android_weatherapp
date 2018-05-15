package com.plymouthuni.dreamteam.weatherapp;

import android.app.Activity;
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

public class HomeFragment extends Fragment implements View.OnClickListener {


    public static String activityName = "HomeFrag";
    private Button login_button = null;
    private Button signup_button = null;
    private EditText username_input = null;
    private EditText password_input = null;

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
                add.setText("YES");
                // Test login
                // Update
                // Enable app
                break;

            case R.id.signup_button:
                add.setText("YES 2");
                // Open SignUp Page
                break;

            default:
                break;

        }

    }

}
