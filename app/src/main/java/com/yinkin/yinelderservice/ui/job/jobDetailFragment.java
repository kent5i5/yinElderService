package com.yinkin.yinelderservice.ui.job;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.yinkin.yinelderservice.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link jobDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class jobDetailFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView jobTitleTextView;
    private TextView jobDescriptionTextView;
    private TextView jobAddressTextView;
    public jobDetailFragment() {
        // Required empty public constructor
    }

    public void loadData(String objectid){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Jobs");
        query.whereEqualTo("objectId", objectid);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject object : objects){
                    jobTitleTextView.setText(object.getString("title"));
                    jobDescriptionTextView.setText(object.getString("description"));
                    jobAddressTextView.setText(object.getParseGeoPoint("location").toString());
                }
            }
        });
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment jobDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static jobDetailFragment newInstance(String param1, String param2) {
        jobDetailFragment fragment = new jobDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("selectedJobId");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_job_detail, container, false);
        TextView jobIdTextView = rootView.findViewById(R.id.jobIdTextView);

        jobTitleTextView = rootView.findViewById(R.id.jobTitleView);

        jobDescriptionTextView = rootView.findViewById(R.id.jobDescriptionView);

        jobAddressTextView = rootView.findViewById(R.id.jobAddressView);
        loadData(mParam1);
        Button showMapButton = rootView.findViewById(R.id.showJobsite);

        showMapButton.setOnClickListener(this);

        jobIdTextView.setText(mParam1);

        return rootView;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showJobsite:
                Bundle bundle = new Bundle();
                bundle.putString("selectedJobId", mParam1);
                Navigation.findNavController(v).navigate(R.id.joabLocationMapsFragment, bundle);

        }
    }
}