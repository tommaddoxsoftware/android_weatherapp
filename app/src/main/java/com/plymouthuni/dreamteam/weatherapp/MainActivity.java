package com.plymouthuni.dreamteam.weatherapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        HomeFragment frag = null;
        Bundle extraData = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_home:
                //Create Extra Data to put into the fragment
                extraData = new Bundle();
                extraData.putString(PlaceholderFragment.TitleKey, getString(R.string.home));

                //Create fragment
                fragment = new PlaceholderFragment();

                frag = new HomeFragment();

                fragment.setArguments(extraData);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, frag);
                fragmentTransaction.commit();
                break;

            case R.id.nav_map:
                //Create Extra Data to put into the fragment
                extraData = new Bundle();
                extraData.putString(PlaceholderFragment.TitleKey, getString(R.string.map));

                //Create fragment
                fragment = new PlaceholderFragment();
                fragment.setArguments(extraData);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.nav_account:
                //Create Extra Data to put into the fragment
                extraData = new Bundle();
                extraData.putString(PlaceholderFragment.TitleKey, getString(R.string.my_account));

                //Create fragment
                fragment = new PlaceholderFragment();
                fragment.setArguments(extraData);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.nav_share:
                //Create Extra Data to put into the fragment
                extraData = new Bundle();
                extraData.putString(PlaceholderFragment.TitleKey, getString(R.string.share));

                //Create fragment
                fragment = new PlaceholderFragment();
                fragment.setArguments(extraData);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.relativeLayoutFragmentContainer, fragment);
                fragmentTransaction.commit();
                break;

            case R.id.nav_send:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
