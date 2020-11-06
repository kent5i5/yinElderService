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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hide title bar
        getSupportActionBar().hide();
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
      if(v.getId() == R.id.mode){
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

        }

        return false;
    }
}