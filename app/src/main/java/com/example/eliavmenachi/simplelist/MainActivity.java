package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import com.example.eliavmenachi.simplelist.model.Company;
import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Employee;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity {
    ListView myList;
    List<Employee> data = new LinkedList<Employee>();
    Company company;
    CustomAdapter adapter;
    ProgressBar progressBar;

    static final int NEW_STUDENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        Model.getInstance().init(this);

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,
                                        LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to Welcome.class
                Intent intent = new Intent(MainActivity.this, WorkersListActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity.class
                Intent intent = new Intent(MainActivity.this,
                                           LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        if (requestCode == NEW_STUDENT_REQUEST) {
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                startActivityForResult(intent,NEW_STUDENT_REQUEST);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CustomAdapter extends BaseAdapter{

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
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row_layout,null);

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

            if (emp.getImageName() != null){
                final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.rowImageProgressBar);
                progress.setVisibility(View.VISIBLE);
                Model.getInstance().loadImage(emp.getImageName(),new Model.LoadImageListener() {
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
