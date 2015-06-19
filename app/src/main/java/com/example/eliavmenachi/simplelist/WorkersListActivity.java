package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class WorkersListActivity extends Activity {
    static final int BACK_FROM_NEW_EMPLOYEE_ACTIVITY = 1;
    ListView myList;
    List<Employee> data = new LinkedList<Employee>();
    Company company;
    CustomAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        Model.getInstance().init(this);

        myList = (ListView) findViewById(R.id.listView);

        adapter = new CustomAdapter();
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "item click " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),EmployeeDetailsActivity.class);
                intent.putExtra("id",data.get(position).getId());
                startActivity(intent);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        Model.getInstance().getAllStudentsAsynch(new Model.GetCompaniesListener() {
            @Override
            public void onResult(List<Company> companies) {
                progressBar.setVisibility(View.GONE);
                data = companies.get(0).getEmployees();
                adapter.notifyDataSetChanged();
            }
        });
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

        switch(id){
            case R.id.action_add:
                Intent intent = new Intent(getApplicationContext(),NewEmployeeActivity.class);
                startActivityForResult(intent,BACK_FROM_NEW_EMPLOYEE_ACTIVITY);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_layout, null);

                CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.checkBox);
                cb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("LISTAPP", "my tag is: " + v.getTag());
                        Employee emp = data.get((Integer) v.getTag());
                        emp.setAtWork(!emp.isAtWork());
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
