package com.yinkin.yinelderservice;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yinkin.yinelderservice.adapter.EmployerListViewModel;
import com.yinkin.yinelderservice.dummy.DummyContent;
import com.yinkin.yinelderservice.model.Employer;
import com.yinkin.yinelderservice.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Employer detail screen.
 * This fragment is either contained in a {@link EmployerListActivity}
 * in two-pane mode (on tablets) or a {@link EmployerDetailActivity}
 * on handsets.
 */
public class EmployerDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "employer_id";
    //public static final String ARG_ITEM_ADDRESS = "employer_address";
    /**
     * The dummy content this fragment is presenting.
     */
    //private DummyContent.DummyItem mItem;
    List<Employer> mEmployer;
    public EmployerListViewModel employerModelView;
    public String newText;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EmployerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        employerModelView = new ViewModelProvider(getActivity()).get(EmployerListViewModel.class);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_ITEM_ID));
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.employer_detail, container, false);

        final TextView nameTextView = ((TextView) rootView.findViewById(R.id.employer_detail));

        if (employerModelView != null) {
            // Create the observer which updates the UI.
            final Observer<List<Employer>> nameObserver = new Observer<List<Employer>>() {
                @Override
                public void onChanged(@Nullable final List<Employer> newName) {
                    // Update the UI, in this case, a TextView.
                    // Show the dummy content as text in a TextView.

                    nameTextView.setText(newName.get(0).username);

                }
            };
            employerModelView.getEmployersAync().observe(getActivity(), nameObserver);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Employer");
        query.whereFullText("username", "kent");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){

                    if (objects.size() > 0){
                        for (ParseObject object : objects){
                            Log.i("", object.getString("username"));
                            newText = object.getString("address");
                            Log.i("", newText);
                            ((TextView) rootView.findViewById(R.id.employer_detail)).setText(newText);
                        }
                    }
                }
            }
        });

        // Show the dummy content as text in a TextView.
        if (mEmployer != null) {
            ((TextView) rootView.findViewById(R.id.employer_detail)).setText("hello world");
        } else {

            ((TextView) rootView.findViewById(R.id.employer_detail)).setText(newText);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        employerModelView = new ViewModelProvider(getActivity()).get(EmployerListViewModel.class);

        // TODO: Use the ViewModel
        mEmployer = (ArrayList<Employer>)employerModelView.getLiveDataValues();
        //Log.i("", mEmployer .toString());
    }
}