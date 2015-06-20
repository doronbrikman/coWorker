package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eliavmenachi.simplelist.model.Model;
import com.example.eliavmenachi.simplelist.model.Employee;
import com.parse.ParseUser;


public class EmployeeDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);
        final TextView name = (TextView) findViewById(R.id.sdName);
        final TextView idtv = (TextView) findViewById(R.id.sdId);
        final TextView phone = (TextView) findViewById(R.id.sdPhone);
        final TextView address = (TextView) findViewById(R.id.sdAddress);
        final TextView department = (TextView) findViewById(R.id.sdDepartment);
        final ImageView image = (ImageView) findViewById(R.id.sdImage);
        final CheckBox check = (CheckBox) findViewById(R.id.sdAtWork);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.sdProgressBar);
        final ProgressBar imageProgressBar = (ProgressBar) findViewById(R.id.sdImageProgressBar);
        Intent intent = getIntent();
        String id = intent.getExtras().getString("id");

        setTitle("Employee details");

        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getStudentById(id,new Model.GetEmployee() {
            @Override
            public void onResult(Employee employee) {
                name.setText(employee.getName());
                idtv.setText(employee.getId());
                address.setText(employee.getAddress());
                phone.setText(employee.getAddress());
                department.setText(employee.getDepartment());
                check.setChecked(employee.isAtWork());
                if(employee.getImageName() != null){
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImage(employee.getImageName(),new Model.LoadImageListener() {
                        @Override
                        public void onResult(Bitmap imageBmp) {
                            if (image != null) {
                                image.setImageBitmap(imageBmp);
                            }
                            imageProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
