package com.yinkin.yinelderservice.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.R;
import com.yinkin.yinelderservice.ServiceMapActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
          Button searchButton = root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("location:",ParseUser.getCurrentUser().getParseGeoPoint("location").toString());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final String[] result = {"","","","",""};
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("level","Employee");
                query.whereWithinMiles("location",ParseUser.getCurrentUser().getParseGeoPoint("location"),100);
                query.setLimit(5);
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (objects.size() > 0) {
                            int i = 0;
                            for (ParseUser object : objects) {
                                result[i] = object.getUsername();
                                i++;
                            }
                            //builder.setMessage(R.string.dialog_message)
                            builder.setTitle(R.string.dialog_title)
                                    .setItems(result,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getContext(), ServiceMapActivity.class);
                                            intent.putExtra("theSelectedUserId", result[which] );
                                            startActivity(intent);
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else {
                            builder.setMessage("NO NEARBY EMPLOYEE")
                                    .setTitle(R.string.dialog_title);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });

            }
        });

        return root;
    }

}