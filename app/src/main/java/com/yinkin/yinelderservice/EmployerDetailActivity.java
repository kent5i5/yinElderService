package com.yinkin.yinelderservice;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.adapter.EmployerListViewModel;
import com.yinkin.yinelderservice.model.Employer;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;

import java.util.List;

/**
 * An activity representing a single Employer detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link EmployerListActivity}.
 */
public class EmployerDetailActivity extends AppCompatActivity {
    //<List<Employer>> mEmployer;
    public EmployerListViewModel employerModelView;

    public void goToMap(String userId){
        Intent intent = new Intent(getApplicationContext(), ServiceMapActivity.class);
       intent.putExtra("theSelectedUserId", userId );
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_detail);

        employerModelView = new ViewModelProvider(EmployerDetailActivity.this).get(EmployerListViewModel.class);
        employerModelView.getEmployersAync();
       // Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);

        Log.i("user id",getIntent().getStringExtra(EmployerDetailFragment.ARG_ITEM_ID));
        final String locationArg = getIntent().getStringExtra(EmployerDetailFragment.ARG_ITEM_ID);
        FloatingActionButton fab = (FloatingActionButton)  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                goToMap(locationArg);
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EmployerDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(EmployerDetailFragment.ARG_ITEM_ID));
            EmployerDetailFragment fragment = new EmployerDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.employer_detail_container, fragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, EmployerListActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}