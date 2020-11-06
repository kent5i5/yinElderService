package com.yinkin.yinelderservice;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ServiceMapActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {

                // Add a marker in Sydney and move the camera
                //LatLng sydney = new LatLng(-34, 151);
                //37.7805618,-122.4064475
//                mMap.clear();
//                LatLng userLocation = new LatLng( location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker of employee location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//                //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
//
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                try {
//                    List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//                    if (listAdresses != null && listAdresses.size() > 0 ) {
//                        Log.i("Place Info",listAdresses.get(0).toString());
//                    }
//                    String address;
//                    if (listAdresses.get(0).getSubAdminArea() != null){
//                        address = listAdresses.get(0).getSubAdminArea();
//                        Toast.makeText(ServiceMapActivity.this, address, Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //Location lastKnownLocation =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //ParseUser user = ParseUser.getCurrentUser();
            //ParseGeoPoint userParseGeoPoint = new ParseGeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            //ParseGeoPoint userParseGeoPoint = null;
            //if (user != null && user.getParseGeoPoint("location") != null){
//                //user.put("location", userParseGeoPoint);
//                //user.saveInBackground();
//                userParseGeoPoint = user.getParseGeoPoint("location");
//            }
            Log.i("object",getIntent().getStringExtra("theSelectedUserId"));
//
            final ParseUser[] user = {null};
            final ParseGeoPoint[] userParseGeoPoint = {null};
            ParseQuery<ParseUser> query= ParseUser.getQuery();
            query.whereEqualTo("username", getIntent().getStringExtra("theSelectedUserId"));
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    for( ParseUser object: objects ) {
                        user[0] = object;
                        ParseGeoPoint currentUserGeopoint = ParseUser.getCurrentUser().getParseGeoPoint("location");
                        LatLng currentUserlocation = new LatLng(currentUserGeopoint.getLatitude(), currentUserGeopoint.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentUserlocation).title("Marker of " + ParseUser.getCurrentUser().getUsername() +"'s address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        userParseGeoPoint[0] = object.getParseGeoPoint("location");
                        Log.i("user query ",userParseGeoPoint[0].toString());
                        LatLng userLocation = new LatLng(userParseGeoPoint[0].getLatitude(), userParseGeoPoint[0].getLongitude());
                        mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker of " + user[0].getUsername() +"'s address").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));
                    }
                }
            });
        }

    }
}