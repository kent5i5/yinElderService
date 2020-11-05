package com.yinkin.yinelderservice.ui.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yinkin.yinelderservice.R;

import java.util.List;

public class JobLocationMapsFragment extends Fragment {
    private String mParam1;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(final GoogleMap googleMap) {
            {

                if (getArguments() != null) {
                    mParam1 = getArguments().getString("selectedJobId");
                    Log.i("object",getArguments().getString("selectedJobId"));

                }

                final ParseGeoPoint[] userParseGeoPoint = {null};
                ParseQuery<ParseObject> query= ParseQuery.getQuery("Jobs");
                query.whereEqualTo("objectId", mParam1);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for ( ParseObject object : objects) {
                            userParseGeoPoint[0] = object.getParseGeoPoint("location");
                            Log.i("user query ", userParseGeoPoint[0].toString());
                            LatLng userLocation = new LatLng(userParseGeoPoint[0].getLatitude(), userParseGeoPoint[0].getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(userLocation).title("Marker of " + object.getString("createdby") + "'s address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        }
                    }
                });
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_job_location_maps, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}