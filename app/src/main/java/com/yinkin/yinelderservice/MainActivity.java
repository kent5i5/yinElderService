package com.yinkin.yinelderservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yinkin.yinelderservice.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {
    boolean isEmployer = false;
    boolean isEmployee = true;
    EditText usernameEditText;
    EditText passwordEditText;
    TextView modeTextView;

    public void goToUserListActivity(){
        Intent intent = new Intent(getApplicationContext(), EmployerListActivity.class);
        startActivity(intent);
    }
    public void goToEmployeeListActivity(){
        Intent intent = new Intent(getApplicationContext(), Employee.class);
        startActivity(intent);
    }

    public void Login(View view) {
        Log.i("user and password",usernameEditText.getText().toString()+" "+passwordEditText.getText().toString());
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null) {
                        Log.i("Info", "Logged in " + user.get("level"));
                        if(user.containsKey("level")){
                            if (user.get("level").equals("Employee") && isEmployee){
                                goToEmployeeListActivity();
                            } else if(user.get("level").equals("Employer") && isEmployer) {
                                 goToUserListActivity();
                            } else {
                                Log.i("Info", "Logged fail with incorrect level");
                                Toast.makeText(MainActivity.this, "Please switch to " + user.get("level").toString()+ " mode", Toast.LENGTH_SHORT).show();
                                ParseUser.logOut();
                            }
                        }

                    } else {
                        Log.i("Info", "Logged fail");
                    }
                }
            });

    }
    public void SigUp(View view){

        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(usernameEditText.getText().toString());
        parseUser.setPassword(passwordEditText.getText().toString());
        if(isEmployee){
            parseUser.put("level","Employee");
        } else if(isEmployer){
            parseUser.put("level","Employer");
        }
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("Info", "Signed up");
//                    ParseObject newEmployer = new ParseObject("Employer");
//                    newEmployer.put("username","ken");
//                    newEmployer.put("address","400 clementina street");
//                    newEmployer.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e == null){
//                                Log.i("Parse result:", "new employer added successful");
//                            } else {
//                                Log.i("Parse result:","new employer added fail");
//                            }
//                        }
//                    });
                    if(isEmployee){
                        ParseObject newEmployee = new ParseObject("Employee");
                        newEmployee.put("username", usernameEditText.getText().toString());
                        newEmployee.put("address", "400 clement");
                        newEmployee.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("Parse employee signup", "successful");
                                } else{
                                    Log.i("Parse tweet", "fail");
                                }
                            }
                        });

                        parseUser.put("level","Employee");
                    } else if(isEmployer){
                        ParseObject newEmployer = new ParseObject("Employer");
                        newEmployer.put("username", usernameEditText.getText().toString());
                        newEmployer.put("address", "400 clement");
                        newEmployer.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("Parse employer signup", "successful");
                                } else{
                                    Log.i("Parse tweet", "fail");
                                }
                            }
                        });

                        parseUser.put("level","Employer");
                    }

                    Toast.makeText(MainActivity.this, "hi "+parseUser.getUsername(), Toast.LENGTH_SHORT ).show();
                } else {
                    Log.i("Info", e.getMessage());
                    parseUser.logOut();
                    Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide title bar
        getSupportActionBar().hide();


//        usernameEditText = (EditText) findViewById(R.id.nameEditText);
//
//        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        // Add your initialization code here
        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                // if defined
                .clientKey("myServerKey")
                .server("http://myServerUrl/parse/")
                .build()
        );

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");


        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        //defaultACL.setPublicReadAccess(true);
        //defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ConstraintLayout backgroundLayout = (ConstraintLayout) findViewById(R.id.backgroundLayout);
        backgroundLayout.setOnClickListener(this);
        modeTextView = findViewById(R.id.mode);
        modeTextView.setOnClickListener(this);

    LoadFirstActivity();
}

    public void LoadFirstActivity()
    {
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(mainIntent);
                MainActivity.this.finish();
            }
        }, 1000);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backgroundLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

           inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

        } else if(v.getId() == R.id.mode){
            if (isEmployee ) {
                modeTextView.setText("Employer login");
                isEmployer = true;
                isEmployee = false;
            } else if (isEmployer){
                modeTextView.setText("Employee login");
                isEmployer = false;
                isEmployee = true;
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER &&  event.getAction() == KeyEvent.ACTION_DOWN ){
            goToUserListActivity();
        }

        return false;
    }
}