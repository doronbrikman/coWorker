package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.location.Criteria;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.location.LocationServices;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;

import com.example.eliavmenachi.simplelist.model.Company;
import com.example.eliavmenachi.simplelist.model.Model;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseGeoPoint;

public class CreateCompanyActivity extends Activity {

    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        final EditText adminName = (EditText) findViewById(R.id.txtCompanyAdminName);
        final EditText adminPass = (EditText) findViewById(R.id.txtCompanyAdminPassword);
        final EditText companyName = (EditText) findViewById(R.id.txtCompanyName);

        Button save = (Button) findViewById(R.id.saveNewCompanyButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().addCompany(
                        new Company(companyName.getText().toString()),
                        adminName.getText().toString(),
                        adminPass.getText().toString());
                Intent intent = new Intent(CreateCompanyActivity.this, companyFeedActivity.class);
                startActivity(intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.cancelNewCompanyButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                Intent intent = new Intent(CreateCompanyActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button locationBtn = (Button) findViewById(R.id.btnSetCompanyLocation);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastLocation = getLocation();
                try {
                    ParseGeoPoint p = new ParseGeoPoint();
                    p.setLatitude(mLastLocation.getLatitude ());
                    p.setLongitude(mLastLocation.getLongitude ());
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public Location getLocation()
    {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        /*String locationProvider = LocationManager.NETWORK_PROVIDER;
        mLastLocation = locationManager.getLastKnownLocation(locationProvider);*/
        return locationManager.getLastKnownLocation(bestProvider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_company, menu);
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
}
