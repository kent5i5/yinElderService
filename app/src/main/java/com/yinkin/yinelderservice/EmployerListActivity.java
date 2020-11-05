package com.yinkin.yinelderservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yinkin.yinelderservice.adapter.EmployerListViewModel;
import com.yinkin.yinelderservice.adapter.EmployerRecyclerViewAdapter;
import com.yinkin.yinelderservice.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.yinkin.yinelderservice.model.Employer;

/**
 * An activity representing a list of Employers. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EmployerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class EmployerListActivity extends AppCompatActivity {
    EmployerListViewModel employerModelView;
    List<Employer>employerList;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setBackgroundColor(Color.parseColor("#FF58EA10"));
        //getSupportActionBar().setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

//        CollapsingToolbarLayout layout = findViewById(R.id.collapsing_toolbar_layout);
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        //NavController navController = Navigation.findNavController(this, R.id.fragment2);
//
//        NavController navController = navHostFragment.getNavController();
//
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupWithNavController(layout,
//                toolbar, navController, appBarConfiguration);

        employerModelView = new ViewModelProvider(EmployerListActivity.this).get(EmployerListViewModel.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send a message to the employer", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.employer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        loadUsers();
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }



    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
       recyclerView.setAdapter(new EmployerRecyclerViewAdapter(this, employerList , mTwoPane));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
    }

    private void loadUsers()  {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("...Loading");
        progressDialog.show();

        employerList = new ArrayList<>();
        // perform an asynchronous operation to fetch users.
        ParseQuery<ParseUser> query= ParseUser.getQuery();
        //query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("level","Employer");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                for ( ParseObject object : objects) {
                    Employer employer = new Employer(object.getString("objectId"), object.getString("username"), object.getString("address"));
                    employerList.add(employer);
                }
                Log.i("Data"," found");
                View recyclerView = findViewById(R.id.employer_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView);
                progressDialog.dismiss();
            }
        });
        Log.i("","Live Data finished loading");
    }

    public void addEmployer(){
        ParseObject employer = new ParseObject("Employer");
        employer.put("username","ken");
        employer.put("address","400 clementina");
        employer.saveInBackground(new SaveCallback() {
                                      @Override
                                      public void done(ParseException e) {
                                          if (e == null){
                                              Log.i("Parse result", "successful");
                                          } else {
                                              Log.i("Parse result ","fail");
                                          }
                                      }
                                  }
        );

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final EmployerListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(EmployerDetailFragment.ARG_ITEM_ID, item.id);
                    EmployerDetailFragment fragment = new EmployerDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.employer_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EmployerDetailActivity.class);
                    intent.putExtra(EmployerDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(EmployerListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employer_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}