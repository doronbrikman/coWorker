package com.example.eliavmenachi.simplelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eliavmenachi.simplelist.model.Employee;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginActivity extends Activity {
    // Declare Variables
    Button loginbutton;

    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        // Login Button Click Listener
        loginbutton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    Intent intent = new Intent(
                                            LoginActivity.this,
                                            companyFeedActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged in",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    ParseQuery query = new ParseQuery("Employee");
                                    query.whereEqualTo("name", usernametxt);

                                    try {
                                        List<ParseObject> data = query.find();

                                        if (!data.isEmpty()) {
                                            ParseUser newUser = new ParseUser();
                                            newUser.setUsername(usernametxt);
                                            newUser.setPassword(passwordtxt);
                                            newUser.setEmail(usernametxt + "@asd.com");
                                            newUser.put("companyId",
                                                    data.get(0).get("companyId").toString());
                                            newUser.put("admin", false);

                                            newUser.signUpInBackground(new SignUpCallback() {
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Intent intent = new Intent(
                                                                LoginActivity.this,
                                                                companyFeedActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(getApplicationContext(),
                                                                "Successfully Logged in",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "No such user exist, please signup",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });
    }
}