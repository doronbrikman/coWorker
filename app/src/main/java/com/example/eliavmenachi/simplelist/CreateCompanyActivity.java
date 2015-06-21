package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
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
import com.example.eliavmenachi.simplelist.model.Post;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

public class CreateCompanyActivity extends Activity {

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    // The minimum distance to change Updates in meters
    private static final long MIN_UPDATE_DISTANCE = 10;
    // The minimum time between updates in milliseconds
    private static final long MIN_UPDATE_TIME = 1000 * 60;

    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        LatLng currLoc = this.getLocation();
        //GeoPoint goe = this.convert(mLastLocation.getLatitude(), mLastLocation.getLongitude());


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
    }

    public LatLng getLocation()
    {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        Location location1 = locationManager.getLastKnownLocation(locationProvider);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();
            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

   /* @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult((Activity)getApplicationContext(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //unable to execute request
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are inadequate, and cannot be fixed here. Dialog not created
                break;
        }
    }*/

  /* public static GeoPoint convert(double latitude, double longitude)
    {
        int lat = (int)(latitude * 1E6);
        int lng = (int)(longitude * 1E6);

        return new GeoPoint(lat, lng);
    }

*/
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
