package com.plymouthuni.dreamteam.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Set home fragment as default
        if(savedInstanceState == null) {
            //Add fragments to the activity
            FragmentManager fragmentManager = getSupportFragmentManager ();
            FragmentTransaction fragmentTransaction = null;

            //LoginFragment login_frag = null;
            HomeFragment login_frag = null;
            HomeFragment home_frag = null;



            Bundle extraData = null;
            try {

                SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);
                if(prefs.contains("username")){
                    //Load default tab
                    if (prefs.getString("username", null).equals("") || prefs.getString("username", null).length() < 0) {
                        //Create fragment
                        home_frag = new HomeFragment();

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, home_frag);
                        fragmentTransaction.commit();
                    }
                }
                else {
                    //Load login screen instead
                    //Create fragment
                    //login_frag = new LoginFragment();
                    login_frag = new HomeFragment();

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, login_frag);
                    fragmentTransaction.commit();
                }
            }
            catch(Exception ex) {
                String error = String.valueOf(ex.getStackTrace()[0].getLineNumber());
                Log.i("Exception", error);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Add fragments to the activity
        FragmentManager fragmentManager = getSupportFragmentManager ();
        FragmentTransaction fragmentTransaction = null;

        PlaceholderFragment fragment = null;

        HomeFragment home_frag = null;
        MapFragment map_frag = null;
        WeatherFragment weather_frag = null;

        Bundle extraData = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_home:
                //Create fragment
                home_frag = new HomeFragment();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, home_frag);
                fragmentTransaction.commit();
                break;

            case R.id.nav_map:
                //Create fragment
                map_frag = new MapFragment();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, map_frag);
                fragmentTransaction.commit();
                break;
            case R.id.nav_weather:
                //Create fragment
                weather_frag = new WeatherFragment();

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, weather_frag);
                fragmentTransaction.commit();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
