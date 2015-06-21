package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eliavmenachi.simplelist.model.Company;
import com.example.eliavmenachi.simplelist.model.Post;
import com.example.eliavmenachi.simplelist.model.Model;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;


public class companyFeedActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    ListView myList;
    List<Post> data = new LinkedList<Post>();
    PostAdapter adapter;
    ProgressBar progressBar;
    static final int BACK_FROM_NEW_POST_ACTIVITY = 1;
    //static final int BACK_FROM_EMPLOYEES_VIEW = 2;
    SwipeRefreshLayout swipeLayout;
    Location mLastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_feed);

        progressBar = (ProgressBar) findViewById(R.id.postProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        // Convert currentUser into String
        String   struser = currentUser.getUsername().toString();
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        txtuser.setText("You are logged in as " + struser);

        setTitle("News Feed");

        Model.getInstance().getAllPostsByCompanyAsync(new Model.GetPostsListener() {
            @Override
            public void onResult(List<Post> posts) {
                progressBar.setVisibility(View.GONE);
                data = posts;
                adapter.notifyDataSetChanged();
            }
        });

        myList = (ListView) findViewById(R.id.postslistView);

        adapter = new PostAdapter();
        myList.setAdapter(adapter);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Button arrived = (Button) findViewById(R.id.arrival);
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLastLocation = getLocation();
                try {
                    Model.getInstance().GetCompanyLoc(new Model.GetCompanyLocation() {
                        @Override
                        public void onResult(ParseGeoPoint loc) {
                            ParseGeoPoint p = new ParseGeoPoint();
                            p.setLatitude(mLastLocation.getLatitude ());
                            p.setLongitude(mLastLocation.getLongitude ());

                            if (loc.distanceInKilometersTo(p) < 1)
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Have a nice day.",
                                        Toast.LENGTH_LONG).show();
                                Button arrived = (Button) findViewById(R.id.arrival);
                                arrived.setEnabled(false);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),
                                        "Lier! you are fired.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });


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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == BACK_FROM_NEW_POST_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Model.getInstance().getAllPostsByCompanyAsync(new Model.GetPostsListener() {
                    @Override
                    public void onResult(List<Post> posts) {
                        progressBar.setVisibility(View.GONE);
                        data = posts;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_company_feed, menu);

        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean b = currentUser.getBoolean("admin");

        menu.getItem(0).setVisible(b);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch(id){
            case R.id.action_addPost:
                intent = new Intent(getApplicationContext(),NewPostActivity.class);
                startActivityForResult(intent,BACK_FROM_NEW_POST_ACTIVITY);
                return true;
            case R.id.action_employeesList:
                intent = new Intent(getApplicationContext(),WorkersListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Model.getInstance().getAllPostsByCompanyAsync(new Model.GetPostsListener() {
            @Override
            public void onResult(List<Post> posts) {
                swipeLayout.setRefreshing(false);
                data = posts;
                adapter.notifyDataSetChanged();
            }
        });
    }

    class PostAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.post_layout, null);
            }

            TextView post = (TextView) convertView.findViewById(R.id.postText);
            TextView title = (TextView) convertView.findViewById(R.id.titleText);
            TextView date = (TextView) convertView.findViewById(R.id.postDate);

            Post pt = data.get(position);
            convertView.setTag(position);
            post.setText(pt.getPost());
            title.setText(pt.getTitle());
            date.setText(pt.getDate().toString());

            return convertView;
        }
    }
}
