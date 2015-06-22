package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eliavmenachi.simplelist.model.Company;
import com.example.eliavmenachi.simplelist.model.Employee;
import com.example.eliavmenachi.simplelist.model.Model;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.LinkedList;
import java.util.List;

public class WorkersListActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    static final int BACK_FROM_NEW_EMPLOYEE_ACTIVITY = 1;
    ListView myList;
    List<Employee> data = new LinkedList<Employee>();
    CustomAdapter adapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        Model.getInstance().init(this);

        myList = (ListView) findViewById(R.id.listView);

        setTitle("Employees");

        adapter = new CustomAdapter();
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "item click " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),EmployeeDetailsActivity.class);
                intent.putExtra("id",data.get(position).getId());
                startActivity(intent);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        Model.getInstance().getAllStudentsAsynch(new Model.GetEmployeeListener() {
            @Override
            public void onResult(List<Employee> employees) {
                progressBar.setVisibility(View.GONE);
                data = employees;
                adapter.notifyDataSetChanged();
            }
        });

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == BACK_FROM_NEW_EMPLOYEE_ACTIVITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                data = Model.getInstance().getAllStudents();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_workers_list, menu);

        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean b = currentUser.getBoolean("admin");

        menu.getItem(0).setVisible(b);

        //getMenuInflater().inflate(R.menu.menu_workers_list, menu);
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
            case R.id.action_add:
                intent = new Intent(getApplicationContext(),NewEmployeeActivity.class);
                startActivityForResult(intent,BACK_FROM_NEW_EMPLOYEE_ACTIVITY);
                return true;
            case R.id.action_post:
                intent = new Intent(getApplicationContext(), companyFeedActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Model.getInstance().getAllStudentsAsynch(new Model.GetEmployeeListener() {
            @Override
            public void onResult(List<Employee> employees) {
                swipeLayout.setRefreshing(false);
                data = employees;
                adapter.notifyDataSetChanged();
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_layout, null);

                CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.checkBox);
                cb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox)v;
                        cb.setChecked(true);
                    }
                });

                Button call = (Button) convertView.findViewById(R.id.call);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Employee emp = data.get(position);
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + emp.getPhone()));
                        startActivity(intent);
                    }
                });
            }

            final ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.nameTextView);
            TextView id = (TextView) convertView.findViewById(R.id.idTextView);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            cb.setTag(new Integer(position));
            convertView.setTag(position);

            Employee emp = data.get(position);
            name.setText(emp.getName());
            id.setText(emp.getId());
            cb.setChecked(emp.isAtWork());

            if (emp.getImageName() != null) {
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.rowImageProgressBar);
                progress.setVisibility(View.VISIBLE);
                Model.getInstance().loadImage(emp.getImageName(), new Model.LoadImageListener() {
                    @Override
                    public void onResult(Bitmap imageBmp) {
                        if (imageBmp != null) {
                            image.setImageBitmap(imageBmp);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
            }
            return convertView;
        }
    }
}
