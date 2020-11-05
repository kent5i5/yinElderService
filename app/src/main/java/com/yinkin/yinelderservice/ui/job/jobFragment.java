package com.yinkin.yinelderservice.ui.job;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.R;
import com.yinkin.yinelderservice.adapter.MyjobRecyclerViewAdapter;
import com.yinkin.yinelderservice.dummy.DummyContent;
import com.yinkin.yinelderservice.model.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class jobFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Job> mJobs;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public jobFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static jobFragment newInstance(int columnCount) {
        jobFragment fragment = new jobFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_list, container, false);



//        NavController navController = Navigation.findNavController(view);
//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph()).build();
//        Toolbar toolbar = view.findViewById(R.id.toolbar);

       // NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        mJobs = new ArrayList<Job>();
        loadJobs(view);
        return view;
    }

    private void loadJobs(final View view){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("...Loading");
        progressDialog.show();

        // perform an asynchronous operation to fetch users.
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Jobs");
        //query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("createdby",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for ( ParseObject object : objects) {
                    Job job = new Job(object.getObjectId(),
                            object.getCreatedAt(),
                            object.getString("createdby"),
                            object.getString("address"),
                            object.getString("title"));
                    mJobs.add(job);
                }
                if (e == null){
                    Log.i("Data"," found");
    //                View recyclerView = findViewById(R.id.employer_list);
    //                assert recyclerView != null;
    //                setupRecyclerView((RecyclerView) recyclerView);
                    // Set the adapter
                    if (view instanceof RecyclerView) {
                        Context context = view.getContext();


                        RecyclerView recyclerView = (RecyclerView) view;
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

                        }
                        recyclerView.setAdapter(new MyjobRecyclerViewAdapter( DummyContent.ITEMS, mJobs,  true));
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);

                            }
                        });
                    }
                }else {
                    Log.i("", e.getMessage());
                }
                progressDialog.dismiss();
            }
        });


       // Log.i("","Live Data finished loading");
    }
}