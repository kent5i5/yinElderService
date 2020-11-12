package com.yinkin.yinelderservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.ui.login.LoginViewModel;
import com.yinkin.yinelderservice.ui.login.LoginViewModelFactory;
import com.yinkin.yinelderservice.ui.settings.ObjectSerializer;
import com.yinkin.yinelderservice.ui.settings.SettingsFragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private LoginViewModel loginViewModel;

    public void cacheeUserData(){

        SharedPreferences sharedPreferences = this.getSharedPreferences(" com.yinkin.yinelderservice", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("username", "yin").apply();

        String username = sharedPreferences.getString("username","");

        Log.i("sharePreferences", username);

        ArrayList<String> addresses = new ArrayList<>();

        addresses.add("good");
        addresses.add("great");
        try {
            sharedPreferences.edit().putString("addresses", ObjectSerializer.serialize(addresses)).apply();

        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<String> newAddresses = new ArrayList<>();
        try {
            newAddresses = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("preferences",
                    ObjectSerializer.serialize(new ArrayList<>())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("sharePreferences", newAddresses.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#FF349E80"));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_job,R.id.nav_messages, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel .class);
        if(item.getItemId() == R.id.setting){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.signout){
            loginViewModel.logout();
            ParseUser.logOut();
            SQLiteDatabase yinDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            Cursor sqlcursor = yinDatabase.rawQuery("SELECT * FROM users", null);

            int nameIndex = sqlcursor.getColumnIndex("username");
            sqlcursor.moveToFirst();
            String username = null;
            while (!sqlcursor.isAfterLast()){
                username = sqlcursor.getString(nameIndex);
                sqlcursor.moveToNext();
            }
            yinDatabase.execSQL("DELETE FROM  users  WHERE username='" + username +"'");
            //yinDatabase.execSQL("DROP TABLE IF EXISTS  users");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}